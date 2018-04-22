import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

;import java.util.Set;

public class JedisTest {

    //方法1
    public  void test(){
        //1 设置服务器Ip地址和端口号
        Jedis jedis =new Jedis("192.168.111.128",6379);
        //2 保存数据
        jedis.set("name","Tom");
        //获取数据
        System.out.println(jedis.get("name"));
        //关闭资源
        jedis.close();
    }

    //方法2
    public void test2(){
        //1 获取连接池配置对象
        JedisPoolConfig config=new JedisPoolConfig();
        //2 设置最大连接数
        config.setMaxTotal(20);
        //3 最大空闲连接数
        config.setMaxIdle(10);
        //4 获取连接池
        JedisPool pool=new JedisPool(config,"192.168.111.128",6379);
        //4 获取对象
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            jedis.set("name","Jack");
            System.out.println(jedis.get("name"));
        }catch (Exception e){
           e.printStackTrace();
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
            if(pool!=null) {
                pool.close();
            }
            }
    }

    //获取所有的数据信息
    public  void key(){
        Jedis jedis =new Jedis("192.168.111.128",6379);
       Set<String> keys= jedis.keys("*");
        System.out.println(keys);
    }
}
//redis连接不上的解决方案：https://blog.csdn.net/a532672728/article/details/78035559

/**
 *redis的数据存储在内存上的，数据备份在硬盘 ，常用的数据库存在硬盘上，所以它吃内存,redis为非关系型数据库
 *  redis是远程的,Redis为非关系型数据库，非数据型数据库用于高并发 可以快速查询的特点，
 *  缺点是缺少数据结构Redis应用场景: 缓存 队列： push pop 数据存储
 *  Redis的数据类型：String:存储字符串 整形 浮点型    List:存储序列集合(注意左右的区分)
 *  Set:存储不同元素,无序存储 Hash:以键值队的形式存储，StringKey为字符串 ，StringValue为元素的map容器  Sort Set:存储带分数的score-value有序集合,score为浮点 value为元素,有序集合，默认为升序
 * redis的持久化使用方式：RDB持久化：默认每隔一段时间将数据写入到硬盘，文件小，效率高 ，缺点是宕机数据丢失  AOF持久化：与RDB相反  无持久化：作为缓存使用不存储数据
 * */

/**
 * aof方式：

 *优势：1.带来更高的数据安全性。有三种同步策略。每秒同步、每修改同步、不同步。

* 2.AOF 文件是一个只进行追加操作的日志文件，因此在写入过程中即使出现宕机现象也不影响之前已经存在的内容。

 *3.如果日志过大，redis可以启动重写机制。在重写过程中产生的对数据库操作记录会保存在一个新文件中，等到重写完成后再追加到现有的文件中。

 *4.AOF 文件有序地保存了对数据库执行的所有写入操作

 *劣势：1.对于相同数量的数据集而言，文件比rdb方式要大。

 *2.效率比rdb低
 */

/*rdb方式：

*优势：只有一个文件，时间间隔的数据，可以归档为一个文件，方便压缩转移（就一个文件）

*劣势：如果宕机，数据损失比较大，因为它是没一个时间段进行持久化操作的。
* 也就是积攒的数据比较多，一旦懵逼，就彻底懵逼了*/