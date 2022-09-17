package io.github.rovingsea.glancecorrection.core;

import java.util.Map;

/**
 * 订正逻辑 <br>
 *
 * 
 *
 * @author Haixin Wu
 * @since 1.0.1
 */
public abstract class Logic {

    /**
     * <img src='../../../../../../../../../docs/image/执行订正逻辑流程图.png' width=600 height=500 />
     * 设置某一列中的订正逻辑
     * @param sourceDataClass 来源数据
     * @param targetObject 目标表数据
     */
    public abstract void set(Map<Class<?>, ?> sourceDataClass, Object targetObject);

}