package io.github.rovingsea.glancecorrection.core.boot;


import io.github.rovingsea.glancecorrection.core.datasource.Connection;

import java.util.Map;

/**
 * <p>
 * 提供重写执行时执行前、执行、执行后三个操作 <br>
 * </p>
 *
 * @author Haixin Wu
 */
public interface ExecutionAroundProcessor {

    /**
     * 执行插入前的前置操作
     * @param curIndexSourceData 当前下标的来源表数据
     * @param targetObjectClass 目标表的类
     * @param conn 数据库操作对象
     * @param instance 目标表的类实例
     */
    default void postProcessBeforeExecution(Map<Class<?>, Object> curIndexSourceData,
                                            Class<?> targetObjectClass, Connection conn,
                                            Object instance) {
    }

    /**
     * 执行插入操作
     * @param curIndexSourceData 当前下标的来源表数据
     * @param targetObjectClass 目标表的类
     * @param conn 数据库操作对象
     * @param instance 目标表的类实例
     */
    default void processExecution(Map<Class<?>, Object> curIndexSourceData,
                                  Class<?> targetObjectClass, Connection conn,
                                  Object instance) {
        conn.insert(instance);
    }

    /**
     * 执行插入后的后置操作
     * @param curIndexSourceData 当前下标的来源表数据
     * @param targetObjectClass 目标表的类
     * @param conn 数据库操作对象
     * @param instance 目标表的类实例
     */
    default void postProcessAfterExecution(Map<Class<?>, Object> curIndexSourceData,
                                           Class<?> targetObjectClass, Connection conn,
                                           Object instance) {

    }

}
