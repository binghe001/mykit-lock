# 作者简介: 
Adam Lu(刘亚壮)，高级软件架构师，Java编程专家，Spring、MySQL内核专家，开源分布式消息引擎Mysum发起者、首席架构师及开发者，Android开源消息组件Android-MQ独立作者，国内知名开源分布式数据库中间件Mycat核心架构师、开发者，精通Java, C, C++, Python, Hadoop大数据生态体系，熟悉MySQL、Redis内核，Android底层架构。多年来致力于分布式系统架构、微服务、分布式数据库、大数据技术的研究，曾主导过众多分布式系统、微服务及大数据项目的架构设计、研发和实施落地。在高并发、高可用、高可扩展性、高可维护性和大数据等领域拥有丰富的经验。对Hadoop、Spark、Storm等大数据框架源码进行过深度分析并具有丰富的实战经验。

# 作者联系方式
QQ：2711098650

# 框架简述
mykit架构中独立出来的mykit-lock组件，旨在提供高并发架构下分布式系统的分布式锁架构。  
分布式锁是控制分布式系统之间同步访问共享资源的一种方式。在分布式系统中，常常需要协调他们的动作。如果不同的系统或是同一个系统的不同主机之间共享了一个或一组资源，那么访问这些资源的时候，往往需要互斥来防止彼此干扰来保证一致性，在这种情况下，便需要使用到分布式锁。

# 框架结构描述
对高并发下的分布式系统访问共享资源提供分布式锁操作，使用者只需要加入简单的注解便可轻松对共享资源加分布式锁。  
目前主要以Redis的形式实现分布式锁操作，后续扩展其他方式

## mykit-lock-redis
mykit-lock架构下以Redis方式实现分布式锁

### mykit-lock-redis-core
mykit-lock-redis 架构下的核心模块，主要提供通用的注解、自定义异常类和工具类

### mykit-lock-redis-single
mykit-lock-redis 架构下主要以Redis单节点形式实现分布式锁

### mykit-lock-redis-cluster
mykit-lock-redis 架构下主要以Redis集群形式实现分布式锁，待完善

## mykit-lock-test
mykit-lock的测试模块

### mykit-lock-test-redis-single
主要测试以Redis单节点形式实现分布式锁  
测试的入口为：io.mykit.lock.test.redis.single  
  
# 使用说明
1、引用mykit-lock-redis-single说明  
1)在pom.xml中添加如下配置：
```
<dependency>
    <groupId>io.mykit.lock</groupId>
    <artifactId>mykit-lock-redis-single</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2)在项目的classpath目录下配置redis.properties  
在项目的classpath目录下创建redis.properties(注意：配置文件的名称必须为redis.properties)，文件中的配置项的Key必须包含以下内容：
```
redis.maxIdle=100
redis.minIdle=1
redis.maxTotal=1000
redis.host=127.0.0.1
redis.port=6379
```
3)定义需要分布式锁支持的接口
```
package io.mykit.lock.test.redis.single.service;

import io.mykit.lock.redis.annotation.CacheLock;
import io.mykit.lock.redis.annotation.LockedObject;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 23:36
 * @description 商品秒杀Service
 */
public interface SeckillService {

    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void secKill(String arg1,@LockedObject Long arg2);
}

```
4)实现需要分布式锁支持接口的类
```
package io.mykit.lock.test.redis.single.service.impl;

import io.mykit.lock.test.redis.single.service.SeckillService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 23:38
 * @description 商品秒杀实现
 */
public class SeckillServiceImpl implements SeckillService {
    public static Map<Long, Long> inventory ;
    static{
        inventory = new HashMap<>();
        inventory.put(10000001L, 10000l);
        inventory.put(10000002L, 10000l);
    }
    @Override
    public void secKill(String arg1, Long arg2) {
        reduceInventory(arg2);
    }
    //模拟秒杀操作，姑且认为一个秒杀就是将库存减一，实际情景要复杂的多
    public Long reduceInventory(Long commodityId){
        inventory.put(commodityId,inventory.get(commodityId) - 1);
        return inventory.get(commodityId);
    }
}

```

# 分布式锁概念补充
## 业务场景  
所谓秒杀，从业务角度看，是短时间内多个用户“争抢”资源，这里的资源在大部分秒杀场景里是商品；将业务抽象，技术角度看，秒杀就是多个线程对资源进行操作，所以实现秒杀，就必须控制线程对资源的争抢，既要保证高效并发，也要保证操作的正确。
一些可能的实现  
  
刚才提到过，实现秒杀的关键点是控制线程对资源的争抢，根据基本的线程知识，可以不加思索的想到下面的一些方法： 
   
1、秒杀在技术层面的抽象应该就是一个方法，在这个方法里可能的操作是将商品库存-1，将商品加入用户的购物车等等，在不考虑缓存的情况下应该是要操作数据库的。那么最简单直接的实现就是在这个方法上加上synchronized关键字，通俗的讲就是锁住整个方法；  
2、锁住整个方法这个策略简单方便，但是似乎有点粗暴。可以稍微优化一下，只锁住秒杀的代码块，比如写数据库的部分；  
3、既然有并发问题，那我就让他“不并发”，将所有的线程用一个队列管理起来，使之变成串行操作，自然不会有并发问题。  
  
上面所述的方法都是有效的，但是都不好。为什么？第一和第二种方法本质上是“加锁”，但是锁粒度依然比较高。什么意思？试想一下，如果两个线程同时执行秒杀方法，这两个线程操作的是不同的商品,从业务上讲应该是可以同时进行的，但是如果采用第一二种方法，这两个线程也会去争抢同一个锁，这其实是不必要的。第三种方法也没有解决上面说的问题。
那么如何将锁控制在更细的粒度上呢？可以考虑为每个商品设置一个互斥锁，以和商品ID相关的字符串为唯一标识，这样就可以做到只有争抢同一件商品的线程互斥，不会导致所有的线程互斥。分布式锁恰好可以帮助我们解决这个问题。  
## 何为分布式锁  
分布式锁是控制分布式系统之间同步访问共享资源的一种方式。在分布式系统中，常常需要协调他们的动作。如果不同的系统或是同一个系统的不同主机之间共享了一个或一组资源，那么访问这些资源的时候，往往需要互斥来防止彼此干扰来保证一致性，在这种情况下，便需要使用到分布式锁。  
  
我们来假设一个最简单的秒杀场景：数据库里有一张表，column分别是商品ID，和商品ID对应的库存量，秒杀成功就将此商品库存量-1。现在假设有1000个线程来秒杀两件商品，500个线程秒杀第一个商品，500个线程秒杀第二个商品。我们来根据这个简单的业务场景来解释一下分布式锁。  
  
通常具有秒杀场景的业务系统都比较复杂，承载的业务量非常巨大，并发量也很高。这样的系统往往采用分布式的架构来均衡负载。那么这1000个并发就会是从不同的地方过来，商品库存就是共享的资源，也是这1000个并发争抢的资源，这个时候我们需要将并发互斥管理起来。这就是分布式锁的应用。  
而key-value存储系统，如redis，因为其一些特性，是实现分布式锁的重要工具。  
## 具体的实现
先来看看一些redis的基本命令：  
```
SETNX key value  
```
如果key不存在，就设置key对应字符串value。在这种情况下，该命令和SET一样。当key已经存在时，就不做任何操作。SETNX是”SET if Not eXists”。  
```
expire KEY seconds
```  
设置key的过期时间。如果key已过期，将会被自动删除。  
```
del KEY 
``` 
删除key  

## 需要考虑的问题
1、用什么操作redis？幸亏redis已经提供了jedis客户端用于java应用程序，直接调用jedis API即可。  
2、怎么实现加锁？“锁”其实是一个抽象的概念，将这个抽象概念变为具体的东西，就是一个存储在redis里的key-value对，key是于商品ID相关的字符串来唯一标识，value其实并不重要，因为只要这个唯一的key-value存在，就表示这个商品已经上锁。  
3、如何释放锁？既然key-value对存在就表示上锁，那么释放锁就自然是在redis里删除key-value对。  
4、阻塞还是非阻塞？笔者采用了阻塞式的实现，若线程发现已经上锁，会在特定时间内轮询锁。  
5、如何处理异常情况？比如一个线程把一个商品上了锁，但是由于各种原因，没有完成操作（在上面的业务场景里就是没有将库存-1写入数据库），自然没有释放锁，这个情况笔者加入了锁超时机制，利用redis的expire命令为key设置超时时长，过了超时时间redis就会将这个key自动删除，即强制释放锁（可以认为超时释放锁是一个异步操作，由redis完成，应用程序只需要根据系统特点设置超时时间即可）  