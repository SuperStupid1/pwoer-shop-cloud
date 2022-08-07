package com.powernode.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.powernode.constant.AuthConstant;
import com.powernode.domain.SysLoginUser;
import com.powernode.domain.WxUser;
import com.powernode.mapper.SysLoginUserMapper;
import com.powernode.mapper.WxUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/22 20:48
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysLoginUserMapper sysLoginUserMapper;

    @Autowired
    private WxUserMapper wxUserMapper;


    @Value("${wx.appid}")
    private String appId;

    @Value("${wx.appsecret}")
    private String appSecret;

    @Value("${wx.tokenurl}")
    private String tokenUrl;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 因为有后台管理系统用户和前端站点用户，所以需要对登录用户进行判断
        // 获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取判断标识
        String loginType = request.getHeader(AuthConstant.LOGIN_TYPE);
        if (StringUtils.isEmpty(loginType)){
            return null;
        }
        // 判断标识类型
        switch (loginType){
            case AuthConstant.SYS_USER:
                // 后台管理系统用户
                SysLoginUser sysLoginUser = sysLoginUserMapper.getByUsername(username);
                if (!ObjectUtils.isEmpty(sysLoginUser)){
                    List<String> auths = sysLoginUserMapper.selectAuthById(sysLoginUser.getUserId());
                    sysLoginUser.setAuths(auths);
                }
                return sysLoginUser;
            case AuthConstant.USER:
                // 前台用户
                // 将url的占位符替换
                String wxTokenUrl = String.format(tokenUrl, appId, appSecret, username);
                // 发送get请求获取openid
                String result = HttpUtil.get(wxTokenUrl);
                JSONObject jsonObject = JSON.parseObject(result);
                String openid = jsonObject.getString("openid");
                if (StringUtils.hasText(openid)){
                    // 说明微信获取用户身份成功了  我们再查询自己的数据库 如果没有这个用户在我们的商城中 我们帮他注册一个
                    WxUser wxUser = wxUserMapper.selectById(openid);
                    if (ObjectUtils.isEmpty(wxUser)){
                        wxUser = createUser(openid);
                    }
                    return wxUser;
                }
                break;
            default:
                return null;
        }
        return null;

    }

    /**
     * 创建一个商城的小程序用户
     *
     * @param openId
     * @return
     */
    private WxUser createUser(String openId) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();
        WxUser wxUser = new WxUser();
        wxUser.setUserId(openId);
        wxUser.setUserRegtime(LocalDateTime.now());
        wxUser.setUserLasttime(LocalDateTime.now());
        wxUser.setModifyTime(LocalDateTime.now());
        wxUser.setUserLastip(ip);
        wxUser.setUserRegip(ip);
        wxUser.setStatus(1);
        // 新增
        wxUserMapper.insert(wxUser);
        return wxUser;
    }
}

