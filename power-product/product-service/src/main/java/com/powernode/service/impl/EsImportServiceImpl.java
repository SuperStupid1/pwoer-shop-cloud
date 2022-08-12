package com.powernode.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.constant.QueueConstant;
import com.powernode.dao.ProdEsDao;
import com.powernode.domain.EsChange;
import com.powernode.domain.Prod;
import com.powernode.domain.ProdEs;
import com.powernode.service.EsImportService;
import com.powernode.service.ProdService;
import com.powernode.util.ProductThreadPool;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.Escape;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 导入到es
 * 全量导入 （业务场景：项目一启动就要导入，商品有500w条    代码怎么实现？ 自己分页   自定义导入批量大小  300-500条 ）
 * 增量导入 （业务场景：新增商品    代码实现： 有一个时间窗口  30分钟  updateTime 区间的 就导入  16.05 --- 16.45 ）
 * 快速导入   （立马修改es的数据  下单 买商品 减库存的时候吗 怎么实现 （MQ）
 *
 * @author DuBo
 * @createDate 2022/7/30 13:10
 */
@Service
@Slf4j
public class EsImportServiceImpl implements EsImportService, ApplicationRunner {


    /**
     * 配置文件中设置的每次导入的条数
     */
    @Value("${esimport.size}")
    private Integer size;

    @Autowired
    private ProdService prodService;

    @Autowired
    private ProdEsDao prodEsDao;

    /**
     * 导入范围的开始时间
     */
    private LocalDateTime start;

    /**
     * 全量导入
     * 项目一起动就触发
     * 自定义分页  current size
     * 500
     * 1. 查询总条数 totalCount
     * 2. 计算总页数 totalCount % size ==0?totalCount / size :  ((totalCount / size)+1)
     * 3. 查询数据库
     * 4. 填充prodEs对象
     * 5. saveAll
     */
    @Override
    public void importAll() {
        log.info("全量导入开始");
        // 查询总条数
        Integer totalCount = prodService.getTotalCount(this.start, null);
        if (totalCount <= 0) {
            log.info("总条数为空");
            return;
        }
        // 计算总页数
        int pages = totalCount % size == 0 ? totalCount / size : (totalCount / size) + 1;
        // CountDownLatch 线程计数器     CyclicBarrier 线程栅栏   Semaphore 信号量
        CountDownLatch downLatch = new CountDownLatch(pages);
        for (int i = 1; i <= pages; i++) {
            final int current = i;
            // 多线程执行
            ProductThreadPool.poolExecutor.execute(() -> {
                import2Es(current, size, null, null);
                // 计数器--
                downLatch.countDown();
            });
        }
        // 如果使用了多线程 这个时间点肯定不是最终的时间点  因为是异步执行的
        // 记录当前的时间点
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start = LocalDateTime.now();

    }

    /**
     * 查询并且导入的方法
     *
     * @param current
     * @param size
     * @param start
     * @param end
     */
    private void import2Es(int current, Integer size, LocalDateTime start, LocalDateTime end) {
        // 组装一个page对象
        Page<Prod> page = new Page<>(current, size, false);
        List<Prod> prodList = prodService.findProdPage2Es(page, start, end);
        // 把prod的集合 转成prodEs的集合
        List<ProdEs> prodEsList = new ArrayList<>(prodList.size());
        prodList.forEach(prod -> {
            ProdEs prodEs = new ProdEs();
            // 对象拷贝   BeanUtil  orika
            BeanUtil.copyProperties(prod, prodEs, true);
            prodEsList.add(prodEs);
        });
        // saveAll 保存到es
        prodEsDao.saveAll(prodEsList);
    }


    /**
     * 增量导入 有一个时间范围区间   10 --- 10.30     1000条
     * 我们每隔一段时间就看一下哪些数据是没有更新到es的
     * 我们每次更新商品 把他的updateTime修改一下
     * 10 --- 10.30   10.20上了一个新的商品  updateTime 10.20
     * 2分钟
     * 第一个t1
     */
    @Override
    @Scheduled(initialDelay = 120 * 1000, fixedRate = 120 * 1000)
    public void updateImport() {
        log.info("增量导入开始");
        LocalDateTime end = LocalDateTime.now();
        System.out.println(start + "----" + end);
        // 查询总条数
        Integer totalCount = prodService.getTotalCount(start, end);
        if (totalCount <= 0) {
            start = end;
            log.info("总条数为空");
            return;
        }
        // 计算总页数
        int page = totalCount % size == 0 ? totalCount / size : ((totalCount / size) + 1);
        // 循环
        for (int i = 1; i <= page; i++) {
            import2Es(i, size, start, end);
        }
        // 把end给start 缩小查询范围  优化
        start = end;
    }


    @Override
    public void quickImport() {

    }

    /**
     * 使用mq监听的方式去完成快速导入
     * 用户下单 es中库存的扣减
     * 我们和生产者约定好 一个参数
     * prodId 18 count + - 2   使用正负号来复用方法
     * 用户没有支付？ 库存要加回来
     * Map<prodId,count>
     * 18  -2
     * 19  -1
     * 20  -2
     * java强语言类型 尽量使用对象来接受和传递参数
     * List<EsChange>
     *
     * @param message
     * @param channel
     */

    @RabbitListener(queues = QueueConstant.ES_CHANGE_QUEUE, concurrency = "3-5")
    public void handlerProduceEs(Message message, Channel channel) {
        // 获取消息
        String megStr = new String(message.getBody());
        List<EsChange> esChanges = JSON.parseArray(megStr, EsChange.class);
        // 获取商品id列表
        List<Long> ids = esChanges.stream()
                .map(EsChange::getProdId)
                .collect(Collectors.toList());
        // 统一查询es
        Iterable<ProdEs> prodEsIterable = prodEsDao.findAllById(ids);
        // 循环修改库存
        prodEsIterable.forEach(prodEs -> {
            EsChange esChange1 = esChanges.stream()
                    .filter(esChange -> esChange.getProdId().equals(prodEs.getProdId()))
                    .collect(Collectors.toList())
                    .get(0);
            long finalCount = prodEs.getTotalStocks() + esChange1.getCount();
            if (finalCount < 0) {
                log.error("商品{}的库存不足", prodEs.getProdName());
                throw new IllegalArgumentException("商品库存不足");
            }
            // 修改销量和库存
            prodEs.setTotalStocks(finalCount);
            prodEs.setSoldNum(prodEs.getSoldNum() - esChange1.getCount());
        });

        // 统一保存修改
        prodEsDao.saveAll(prodEsIterable);

        // 签收消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 当springApplication启动以后
     * 就会执行这个run方法
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        importAll();
    }
}
