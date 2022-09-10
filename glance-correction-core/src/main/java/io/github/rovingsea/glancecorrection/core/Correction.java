package io.github.rovingsea.glancecorrection.core;

import io.github.rovingsea.glancecorrection.core.datasource.Connection;
import io.github.rovingsea.glancecorrection.core.datasource.ConnectionBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个正确的<b>订正</b>要包含：
 * <ol>
 *     <li>订正逻辑</ui>
 *     <li>来源表数据</ui>
 *     <li>来源表对应的 class 类型，可以有多个</li>
 *     <li>目标表对应的 class 类型，仅有一个</li>
 *     <li>连接数据的对象，这儿称之为连接对象 {@link Connection}，由 {@link #connectionBuilder} 构建</li>
 * </ol>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class Correction {
    /**
     * 目标表的 class
     */
    private final Class<?> targetObjectClass;

    /**
     * 数据库连接对象的构建器
     */
    private final ConnectionBuilder connectionBuilder;

    /**
     * 目标表每一列的转换逻辑
     */
    private final Map<String, Logic> columnLogics = new HashMap<>();

    /**
     * 所有的来源数据，装有每一个来源表对应的来源数据
     */
    private final Map<Class<?>, List<?>> sourceData = new HashMap<>();

    public Correction(Class<?> targetObjectClass, ConnectionBuilder connectionBuilder) {
        this.targetObjectClass = targetObjectClass;
        this.connectionBuilder = connectionBuilder;
    }

    public void addSourceData(Map<Class<?>, List<?>> sourceData) {
        this.sourceData.putAll(sourceData);
    }

    public void addColumLogic(Map<String, Logic> columnLogics) {
        this.columnLogics.putAll(columnLogics);
    }

    public ConnectionBuilder getConnectionBuilder() {
        return connectionBuilder;
    }

    public Map<String, Logic> getColumnLogics() {
        return columnLogics;
    }

    public Class<?> getTargetObjectClass() {
        return targetObjectClass;
    }

    public Map<Class<?>, List<?>> getSourceData() {
        return sourceData;
    }

    public static Builder newBuilder() {
        return new Builder(new SourceData());
    }

    /**
     * 订正构建器
     */
    public static class Builder {

        public final SourceData sourceData;

        private Class<?> targetObjectClass;

        private ConnectionBuilder connectionBuilder;

        private final Map<String, Logic> columnLogics = new HashMap<>();

        public Builder(SourceData sourceData) {
            this.sourceData = sourceData;
        }

        /**
         * 设置目标表的 class
         * @param targetObjectClass 目标表的 class
         */
        public void targetObjectClass(Class<?> targetObjectClass) {
            this.targetObjectClass = targetObjectClass;
        }

        /**
         * 目标表某一列的转换逻辑
         * @param column 列名
         * @param logic 转换逻辑
         * @return 自己
         */
        public Builder column(String column, Logic logic) {
            columnLogics.put(column, logic);
            return this;
        }

        public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
            this.connectionBuilder = connectionBuilder;
        }

        Correction build() {
            Correction correction = new Correction(this.targetObjectClass,
                    this.connectionBuilder);
            correction.addColumLogic(columnLogics);
            correction.addSourceData(this.sourceData.all);
            return correction;
        }

    }


    /**
     * <p>
     * 为订正建造器 {@link Correction.Builder} 用于设置来源数据，
     * 而订正建造器将会借助它来完成来源数据的装配，起承上启下的作用
     * </p>
     */
    public static class SourceData {

        private final Map<Class<?>, List<?>> all = new HashMap<>();

        public void data(List<?> data) {
            if (CollectionUtils.isEmpty(data)) {
                throw new IllegalArgumentException("data cannot be empty");
            }
            Class<?> aClass = data.get(0).getClass();
            this.all.put(aClass, data);
        }

        public void sourceObjectClasses(Class<?>... sourceObjectClasses) {
            for (Class<?> sourceObjectClass : sourceObjectClasses) {
                all.put(sourceObjectClass, null);
            }
        }

    }
}
