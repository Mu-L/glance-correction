package io.github.rovingsea.glancecorrection.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * 
 * @since 1.0.2
 * @author Haixin Wu
 * @since 1.0.0
 */
public abstract class Cast {

    /**
     * 将一个 {@link Object} 类型的类转换为 {@link Map}
     * @param obj 目标类
     * @param keyClass key.class
     * @param valueClass value.class
     * @return 结果
     * @param <K> key 的类型
     * @param <V> value 的类型
     */
    public static <K, V> Map<K, V> toMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
        HashMap<K, V> map = new HashMap();
        if (obj instanceof Map<?, ?>) {
            Set keys = ((Map<?, ?>) obj).keySet();
            for (Object key : keys) {
                map.put(keyClass.cast(key), valueClass.cast(((Map<?, ?>) obj).get(key)));
            }
            return map;
        }
        return null;
    }

    /**
     * {@link Object} 类型转换为 {@link String}，常用于基础数据类型和包装基础类型类 <br>
     * 目的是：
     * <ol>
     *     <li>避免业务代码过程中需要使用强转
     *     <li>避免空指针异常，如 obj 已经为 null 却仍然继续使用 toString 方法
     * </ol>
     * @param obj 目标对象
     * @return 转换为 {@link String} 结果
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    /**
     * 将一个父类类型转换为子类类型
     * @param parent 父类类型的对象
     * @param subClass 子类类型类
     * @return 子类类型的对象
     * @param <P> 父类类型
     * @param <S> 子类类型
     */
    public static <P, S> S toSubclass(P parent, Class<S> subClass) {
        return subClass.cast(parent);
    }

}
