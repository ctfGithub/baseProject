package com.springbootbasepackage.exception;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

/**
 * 断言类
 *
 * @author  bangzheng.peng
 */
@UtilityClass
public class Assert {
    
    /**
     * 断言一个对象等于空
     *
     * @param object  待检查的对象
     * @param message 消息
     * @throws BusinessException object != null 抛异常
     */
    public static void isNull(Object object, String message) throws BusinessException {
        if (Objects.nonNull(object)) {
            throw new BusinessException(message);
        }
    }
    

    /**
     * 断言一个对象!=null
     *
     * @param object  待检查的对象
     * @param message 消息
     * @throws BusinessException object == null 时抛异常
     */
    public static void nonNull(Object object, String message) throws BusinessException {
        if (object == null) {
            throw new BusinessException(message);
        }
    }
    
    /**
     * 断言Collection.nonEmpty
     *
     * @param coll    待检查的对象
     * @param message 消息
     * @throws BusinessException coll.isEmpty 抛异常
     */
    public static <T,P extends Collection<T>> P requireNonEmpty(P coll, String message) throws BusinessException {
        return Assert.requireNonEmpty(coll, message);
    }
    
    /**
     * 预期对象不能为空,并且返回该对象
     *
     * @param <T>     the type parameter
     * @param object  待检查的对象
     * @param message 消息
     * @return 返回预期的对象 t
     * @throws BusinessException object == null 时抛异常
     */
    public static <T> T requireNonNull(T object, String message) throws BusinessException {
        return Assert.requireNonNull(object, message);
    }

    
    /**
     * 断言预期布尔表达式
     *
     * @param expression 预期结果
     * @param message    消息
     * @throws BusinessException 未达成预期结果,抛出异常
     */
    public static void state(boolean expression, String message) throws BusinessException {
        if (!expression) {
            throw new BusinessException(message);
        }
    }
    

}
