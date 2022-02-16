package com.springbootbasepackage.redis;

import com.springbootbasepackage.exception.SntException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SedisManage {

    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_REDIS = "redis";
    private static final String KEY_NODE = "node";
    private static final String KEY_TYPE = "type";
    private static final String KEY_HOST = "host";
    private static final String KEY_PORT = "port";
    private static final String KEY_PASSWORD = "password";
    private static final String SENTINEL = "sentinel";

    private static final String CHARSET = "UTF-8";

    private static final Long LOCK_EXPIRE = 5000L;          //分布式锁默认有效时间 5秒
    private static final Long TRY_LOCK = 5000L;             //默认尝试获取分布式锁时间 5秒
    private static final String LOCK_PREFIX = "RDS_LOCK_";  //分布式锁key前缀

    private static final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    private Integer maxIdle = 100;
    private Integer minIdle =0;
    private Integer maxTotal =300;
    private Long maxWaitMillis = 1000L;

    public SedisManage(String yamlPath){
        Yaml yaml = new Yaml();
        HashMap<String, Object> config;
        try {
            config = yaml.loadAs(new ClassPathResource(yamlPath).getInputStream(), HashMap.class);
        }catch (IOException e){
            throw new SntException("读取配置文件失败");
        }

        List<HashMap> nodesConfig = (List<HashMap>)((Map)config.get(KEY_REDIS)).get(KEY_NODE);
        String type = (String) ((Map)config.get(KEY_REDIS)).get(KEY_TYPE);
        List<Node> nodes = new ArrayList<>(nodesConfig.size());
        nodesConfig.forEach(nodeConfig -> {
            Node node = new Node();
            node.host = (String) nodeConfig.get(KEY_HOST);
            node.port = (Integer) nodeConfig.get(KEY_PORT);
            node.password = (String) nodeConfig.get(KEY_PASSWORD);
            nodes.add(node);
        });

        redisTemplate = SENTINEL.equals(type)
                ? createTemplate(createConnectFactory(nodes))
                : createTemplate(createConnectFactory(nodes.get(0)));
        redisTemplate.afterPropertiesSet();
    }


    public SedisManage(String yamlPath,Integer maxIdle,Integer minIdle,Integer maxTotal,Long maxWaitMillis){
        this.maxIdle = maxIdle;
        this.minIdle = minIdle;
        this.maxTotal=maxTotal;
        this.maxWaitMillis=maxWaitMillis;
        Yaml yaml = new Yaml();
        HashMap<String, Object> config;
        try {
            config = yaml.loadAs(new ClassPathResource(yamlPath).getInputStream(), HashMap.class);
        }catch (IOException e){
            throw new SntException("读取配置文件失败");
        }

        List<HashMap> nodesConfig = (List<HashMap>)((Map)config.get(KEY_REDIS)).get(KEY_NODE);
        String type = (String) ((Map)config.get(KEY_REDIS)).get(KEY_TYPE);
        List<Node> nodes = new ArrayList<>(nodesConfig.size());
        nodesConfig.forEach(nodeConfig -> {
            Node node = new Node();
            node.host = (String) nodeConfig.get(KEY_HOST);
            node.port = (Integer) nodeConfig.get(KEY_PORT);
            node.password = (String) nodeConfig.get(KEY_PASSWORD);
            nodes.add(node);
        });

        redisTemplate = SENTINEL.equals(type)
                ? createTemplate(createConnectFactory(nodes))
                : createTemplate(createConnectFactoryPool(nodes.get(0)));
        redisTemplate.afterPropertiesSet();
    }


    private static class Node {
        String host;
        Integer port;
        String password;
    }

    /**
     * 连接池
     */
    private JedisClientConfiguration jedisClientConfiguration(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        return JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig).build();
    }

    /**
     * 创建连接工厂 单机配置
     */
    private RedisConnectionFactory createConnectFactory(Node node){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(node.host);
        configuration.setPort(node.port);
        configuration.setPassword(RedisPassword.of(node.password));
        return new JedisConnectionFactory(configuration, jedisClientConfiguration());
    }

    /**
     * 创建连接工厂 单机配置
     */
    private RedisConnectionFactory createConnectFactoryPool(Node node){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(node.host);
        configuration.setPort(node.port);
        configuration.setPassword(RedisPassword.of(node.password));
        JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jedisClientConfiguration());
        factory.afterPropertiesSet();
        return factory;
    }


    /**
     * 创建连接工厂 集群配置
     */
    private LettuceConnectionFactory createConnectFactory(List<Node> nodes){
        if (nodes == null || nodes.size() < 6) {
            throw new SntException("cluster nodes must be more than 6");
        }

        List<RedisNode> redisNodes = nodes.stream()
                .map(node -> new RedisClusterNode(node.host, node.port))
                .collect(Collectors.toList());

        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        configuration.setClusterNodes(redisNodes);

        //RedisSentinelConfiguration configuration = new RedisSentinelConfiguration().master("sedis-master");
        //nodes.forEach(node -> configuration.sentinel(node.host, node.port).setPassword(RedisPassword.of(node.password)));
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * 创建模版
     */
    private RedisTemplate<String, Object> createTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jdkSerializationRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jdkSerializationRedisSerializer);
        template.setEnableTransactionSupport(false);
        return template;
    }



    /**
     * 获取缓存
     * @param k
     * @param <T>
     * @return
     */
    public <T> T get(String k){

        return k == null ? null : (T)redisTemplate.opsForValue().get(k);
    }

    /**
     * 设置缓存 没有失效时间
     * @param k
     * @param v
     */
    public void set(String k, Object v){
        redisTemplate.opsForValue().set(k, v);
    }


    public void batchSet(List<String> keys, List<Object> values){
//        Assert.notEmpty(keys);
//        Assert.notEmpty(values);
//        Assert.isTrue(keys.size() == values.size(), "键值长度不配对");

        redisTemplate.executePipelined((RedisCallback<Object>)(conn) -> {
            StringRedisSerializer keySerializer = (StringRedisSerializer)redisTemplate.getKeySerializer();
            JdkSerializationRedisSerializer valueSerializer = (JdkSerializationRedisSerializer)redisTemplate.getValueSerializer();
            for (int i = 0; i < keys.size(); i++) {
                try {
                    conn.set(keySerializer.serialize(keys.get(i)), valueSerializer.serialize(values.get(i)));
                } catch (Exception e) {
                }
            }
            return null;
        });
    }

    /**
     * 设置缓存
     * @param k 健
     * @param v 值
     * @param expire 失效时间
     * @param unit  失效时间单位
     */
    public void set(String k, Object v, long expire, TimeUnit unit){
        redisTemplate.opsForValue().set(k, v, expire, unit);
    }

    /**
     * 设置缓存 失效时间单位默认毫秒
     * @param k
     * @param v
     * @param expire
     */
    public void set(String k, Object v, long expire){
        set(k, v, expire, TimeUnit.MILLISECONDS);
    }

    /**
     * 原子增加
     * @param k
     * @param delta
     * @return
     */
    public Long incrby(String k, Long delta) {

        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(k, redisTemplate.getConnectionFactory());
        return redisAtomicLong.addAndGet(delta);

        //return redisTemplate.opsForValue().increment(k, delta);
    }

    /**
     * 原子增加
     * @param k
     * @param delta
     * @return
     */
    public Long incrby(String k, Long delta, Long expire, TimeUnit unit){
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(k, redisTemplate.getConnectionFactory());
        redisAtomicLong.expire(expire, unit);
        return redisAtomicLong.addAndGet(delta);
    }

    /**
     * @param k
     * @param v
     * @param expire
     * @param unit
     * @return
     */
    public Boolean setNX(String k, Object v, Long expire, TimeUnit unit){
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        JdkSerializationRedisSerializer valueSerializer = (JdkSerializationRedisSerializer) redisTemplate.getValueSerializer();
        Expiration expiration = Expiration.from(expire, unit);
        RedisStringCommands.SetOption option = RedisStringCommands.SetOption.ifAbsent();
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            try {
                return conn.set(keySerializer.serialize(k), valueSerializer.serialize(v), expiration, option);
            } catch (Exception e){
                return false;
            }
        });
    }

    /**
     *
     * @param k
     * @param v
     * @return
     */
    public Boolean setNX(String k, Object v){
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        JdkSerializationRedisSerializer valueSerializer = (JdkSerializationRedisSerializer) redisTemplate.getValueSerializer();
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            try {
                return conn.setNX(keySerializer.serialize(k), valueSerializer.serialize(v));
            } catch (Exception e){
                return false;
            }
        });
    }

    /**
     * 删除缓存
     * @param k
     */
    public Boolean remove(String k){

        return redisTemplate.delete(k);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 尝试获取分布式锁
     * @param key
     * @param tryMillis 尝试时间 毫秒
     * @param expireMillis 分布式锁有效时间 毫秒
     * @return lockValue 用于解锁时判断是否为获取锁的线程解锁
     */
    public String tryLock(String key, Long tryMillis, Long expireMillis){
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
        byte[] lockKey = keySerializer.serialize(LOCK_PREFIX.concat(key));
        byte[] lockValue = valueSerializer.serialize(UUID.randomUUID().toString());
        Expiration expiration = Expiration.from(expireMillis, TimeUnit.MILLISECONDS);
        RedisStringCommands.SetOption option = RedisStringCommands.SetOption.ifAbsent();

        Long startMillis = System.currentTimeMillis();      //开始尝试枷锁时间
        boolean isLock;
        int tryCount = 0;

        //在规定时间内 获取分布式锁
        do {
            //防止自旋cpu消耗过高
            if (tryCount ++ > 0){
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e){
                }
            }

            isLock = redisTemplate.execute((RedisCallback<Boolean>) conn -> {
                try {
                    return conn.set(lockKey, lockValue, expiration, option);
                } catch (Exception e) {
                    throw new SntException("try lock fail, exec set error", e);
                }
            });
        } while (!isLock && startMillis + tryMillis > System.currentTimeMillis());

        if (isLock){
            return valueSerializer.deserialize(lockValue);
        }

        throw new SntException("try lock fail, timeout");
    }

    /**
     * 尝试获取分布式锁 尝试时间5秒
     * @param key
     * @return
     */
    public String tryLock(String key){
        return tryLock(key, TRY_LOCK, LOCK_EXPIRE);
    }

    /**
     * 释放分布式锁
     * 通过key查询lock 如果lock的值与value一至 通过value确定是否可以释放（防止其他线程释放）
     * 由于上述步骤需要原子执行，所以采用Lua脚本执行
     * @param key
     * @param value
     */
    public void releaseLock(String key, String value){
        key = LOCK_PREFIX.concat(key);
        RedisScript<Long> script = RedisScript.of(RELEASE_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(key), value);
        if (1 != result){
            throw new SntException("release lock fail");
        }
    }

    public Boolean isMemers(String key,String value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public Boolean addSet(String key,String value,Long seconds){
        boolean b= isMemers(key,value);
        if(!b){
            redisTemplate.opsForSet().add(key,value);
            redisTemplate.expire(key,seconds,TimeUnit.SECONDS);
            return true;
        }
        return  false;
    }

    protected RedisTemplate<String, Object> getTemplate(){
        return this.redisTemplate;
    }
}
