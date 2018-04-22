import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

//redis 事务机制
public class TxTest {
    public static void main(String[] args) {
        Jedis jedis =new Jedis("192.168.111.128",6379);
        Transaction transaction=jedis.multi();
        transaction.set("key4","1");
        transaction.set("key5","2222");
        transaction.set("key6","33333");
        //transaction.exec();
        transaction.discard();
    }

    public boolean check() throws  Exception{
        Jedis jedis =new Jedis("192.168.111.128",6379);
        jedis.set("total","1000");
        jedis.set("pay","0");
        int total;
        int pay=200;
        //监视total
        jedis.watch("total");
        total=Integer.parseInt(jedis.get("total"));
        //此时尝试在redis客户端修改total，就可以知道Watch 用处
        Thread.sleep(5000);
        if(total<pay){
            //取消监视
            jedis.unwatch();
            System.out.println("账户已经被修改");
            return false;
        }
        else{
            Transaction transaction=jedis.multi();
            transaction.incrBy("pay",pay);
            transaction.decrBy("total",pay);
            transaction.exec();
            System.out.println(jedis.get("pay"));
            return true;
        }


    }
}
/**
 * discard()的作用是取消事务，感觉就是事务的回滚
 * exec():执行事务块内的所有命令
 * multi():标记事务块的开始
 * unwatch():取消WATCH命令对所有Key的监视
 * watch(key):监视一个或者多个key，如果事务执行之前key被其它命令所改动，
 * 那么事务被打断,就是当watch某个变量后，如果其它人对该变量进行修改,
 * 你这边开启事务对该变量事务不会成功,这里相当于事务中的锁
 */