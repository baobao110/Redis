import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
    private static  JedisPool pool=null;
    //单例模型：懒汉模式，这里注意懒汉和饿汉的一点区别,懒汉不能用final，饿汉可以，因为final定义的变量一旦赋值就不能改变
    public  static   JedisPool getBean(){
        if(null==pool){
            synchronized (JedisPool.class){
                if(null==pool){
                    JedisPoolConfig config=new JedisPoolConfig();
                    config.setMaxWaitMillis(100);
                    config.setMaxTotal(32);
                 pool=new JedisPool();
                }
            }
        }
        return pool;
    }
}
