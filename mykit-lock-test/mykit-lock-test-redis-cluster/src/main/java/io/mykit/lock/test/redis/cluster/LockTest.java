package io.mykit.lock.test.redis.cluster;

import io.mykit.lock.redis.cluster.factory.RedisFactory;
import io.mykit.lock.redis.cluster.interceptor.CacheLockInterceptor;
import io.mykit.lock.test.redis.cluster.service.SeckillService;
import io.mykit.lock.test.redis.cluster.service.impl.SeckillServiceImpl;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/8 19:46
 * @description 测试分布式锁
 */
public class LockTest {
    private static Long commidityId1 = 10000001L;
    private static Long commidityId2 = 10000002L;

    @Test
    public void testGetJedisCluster() throws Exception{
        while (true){
            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            System.out.println("打开成功" + System.currentTimeMillis());
            jedisCluster.close();
        }
    }

    @Test
    public void testSecKill(){
        int threadCount = 1000;
        int splitPoint = 500;
        final CountDownLatch endCount = new CountDownLatch(threadCount);
        final CountDownLatch beginCount = new CountDownLatch(1);
        final SeckillServiceImpl testClass = new SeckillServiceImpl();

        Thread[] threads = new Thread[threadCount];
        //起500个线程，秒杀第一个商品
        for(int i= 0;i < splitPoint;i++){
            threads[i] = new Thread(new  Runnable() {
                public void run() {
                    try {
                        //等待在一个信号量上，挂起
                        beginCount.await();
                        //用动态代理的方式调用secKill方法
                        SeckillService proxy = (SeckillService) Proxy.newProxyInstance(SeckillService.class.getClassLoader(),
                                new Class[]{SeckillService.class}, new CacheLockInterceptor(testClass));
                        proxy.secKill("test", commidityId1);
                        endCount.countDown();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();

        }

        for(int i= splitPoint;i < threadCount;i++){
            threads[i] = new Thread(new  Runnable() {
                public void run() {
                    try {
                        //等待在一个信号量上，挂起
                        beginCount.await();
                        //用动态代理的方式调用secKill方法
                        beginCount.await();
                        SeckillService proxy = (SeckillService) Proxy.newProxyInstance(SeckillService.class.getClassLoader(),
                                new Class[]{SeckillService.class}, new CacheLockInterceptor(testClass));
                        proxy.secKill("test", commidityId2);
                        //testClass.testFunc("test", 10000001L);
                        endCount.countDown();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();

        }


        long startTime = System.currentTimeMillis();
        //主线程释放开始信号量，并等待结束信号量
        beginCount.countDown();

        try {
            //主线程等待结束信号量
            endCount.await();
            //观察秒杀结果是否正确
            System.out.println(SeckillServiceImpl.inventory.get(commidityId1));
            System.out.println(SeckillServiceImpl.inventory.get(commidityId2));
            System.out.println("error count" + CacheLockInterceptor.ERROR_COUNT);
            System.out.println("total cost " + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
