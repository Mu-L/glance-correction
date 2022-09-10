package io.github.rovingsea.glancecorrection.core;

import java.util.Map;

/**
 * 订正逻辑 <br>
 *
 * 
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public abstract class Logic implements Runnable {

    /**
     * 设置某一列中的订正逻辑
     * @param sourceDataClass
     * @param targetObject
     */
    public abstract void set(Map<Class<?>, ?> sourceDataClass, Object targetObject);

    @Override
    public void run() {

    }
}
