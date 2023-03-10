package com.springbootbasepackage.redis;


import com.springbootbasepackage.base.SntException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SedissonManage {

    @Resource
    private RedissonClient redissonClient;

    private String PREFIX = "RDS_LOCK:BASE" + System.getenv("project.name") + ":";

    /**
     * 锁 不带超时时间 慎用
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(PREFIX + lockKey);
        lock.lock();
        return lock;
    }

    /**
     * @param lockKey
     * @param leaseTime 如果获取到锁 锁的失效时间 防止死锁
     * @return
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(PREFIX + lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * @param lockKey
     * @param leaseTime 如果获取到锁 锁的失效时间 防止死锁
     * @return
     */
    public RLock lock(String lockKey, TimeUnit unit, long leaseTime) {
        RLock lock = redissonClient.getLock(PREFIX + lockKey);
        lock.lock(leaseTime, unit);
        return lock;
    }


    /**
     * 在指定的时间内获取不到锁返回false
     *
     * @param lockKey
     * @param unit
     * @param waitTime  指定的时间
     * @param leaseTime 如果获取到锁 锁的失效时间 防止死锁
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(PREFIX + lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            log.warn("获取锁失败:lockKey{}", lockKey);
            return false;
        }
    }

    /**
     * 在指定的时间内获取不到锁返回false,默认设置过期时间并自动续期，推荐使用
     *
     * @param lockKey
     * @param unit
     * @param waitTime 指定的时间
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime) {
        RLock lock = redissonClient.getLock(PREFIX + lockKey);
        try {
            return lock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            log.warn("获取锁失败:lockKey{}", lockKey);
            return false;
        }
    }


    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(PREFIX + lockKey);
            lock.unlock();
        } catch (Exception e) {
            throw new SntException("release lock fail",e);
        }
    }


    public void unlock(RLock lock) {
        try {
            lock.unlock();
        } catch (Exception e) {
            throw new SntException("release lock fail",e);
        }
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        RBucket<Object> re = redissonClient.getBucket(key);
        re.set(value);
    }

    /**
     * @param key
     * @param value
     * @param expire 时间时间 单位默认SECONDS
     */
    public void set(String key, Object value, long expire) {
        set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @param key
     * @param value
     * @param timeUnit 时间单位
     * @param expire   时间时间
     */
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        RBucket<Object> re = redissonClient.getBucket(key);
        re.set(value, expire, timeUnit);
    }

    /**
     * list集合中添加一个元素
     *
     * @param key
     * @param value
     * @param expire
     */
    public void addElement(String key, Object value, long expire) {
        RList<Object> list = redissonClient.getList(key);
        list.add(value);
        list.expire(expire, TimeUnit.SECONDS);
    }

    /**
     * list集合中添加一个子集合
     *
     * @param key
     * @param value
     * @param expire
     */
    public void addAllElement(String key, Collection value, long expire) {
        RList<Object> list = redissonClient.getList(key);
        list.addAll(value);
        list.expire(expire, TimeUnit.SECONDS);
    }

    /**
     * 获取值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        RBucket<Object> re = redissonClient.getBucket(key);
        Object o = re.get();
        return o == null ? null : (T) o;
    }

    /**
     * 获取list元素
     *
     * @param key
     * @param <V>
     * @return
     */
    public <V> List<V> getList(String key) {
        RList list = redissonClient.getList(key);
        return list == null ? null : list;
    }

    /**
     * 获取list的一个元素
     *
     * @param key
     * @param index 指定下标
     * @param <T>
     * @return
     */
    public <T> T getOneElement(String key, int index) {
        RList list = redissonClient.getList(key);
        return list == null ? null : (T) list.get(index);
    }


    /**
     * 删除缓存 基本数据类型
     *
     * @param k
     */
    public Boolean delete(String k) {
        return redissonClient.getBucket(k).delete();
    }


    /**
     * 删除缓存 集合
     *
     * @param k
     */
    public Boolean deleteList(String k) {
        return redissonClient.getList(k).delete();
    }

    /**
     * 删除集合的某一个元素
     *
     * @param k
     */
    public Boolean removeElement(String k, long expire, Object value) {
        RList list = redissonClient.getList(k);
        boolean b = list.remove(value);
        if (b) {
            list.expire(expire, TimeUnit.SECONDS);
        }
        return b;
    }


    /**
     * 原子增加
     *
     * @param k
     * @param delta
     * @return
     */
    public Long incrby(String k, Long delta) {
        return redissonClient.getAtomicLong(k).addAndGet(delta);
    }


    /**
     * 原子增加
     *
     * @param k
     * @param delta
     * @return
     */
    public Long incrby(String k, Long delta, Long expire, TimeUnit unit) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(k);
        long value = rAtomicLong.addAndGet(delta);
        rAtomicLong.expire(expire, unit);
        return value;
    }


    /**
     * set是否包含某个元素
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean isMemers(String key, String value) {
        return redissonClient.getSet(key).contains(value);
    }

    public Boolean addSet(String key, String value, Long seconds) {
        RSet rSet = redissonClient.getSet(key);
        boolean b = rSet.contains(value);
        if (!b) {
            rSet.add(b);
            rSet.expire(seconds, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }



}
