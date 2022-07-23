import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/26 16:51
 */
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        System.out.println(stringRedisTemplate);
    }
}
