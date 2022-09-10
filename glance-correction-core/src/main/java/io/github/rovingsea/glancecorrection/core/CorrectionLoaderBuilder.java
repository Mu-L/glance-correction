package io.github.rovingsea.glancecorrection.core;

import io.github.rovingsea.glancecorrection.core.datasource.Connection;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 顾名思义，它的作用就是构建出一个订正加载器 {@link CorrectionLoader}，它提供了设置订正集
 * 的方法 {@link #corrections()}
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class CorrectionLoaderBuilder {

    private final ConfigurableApplicationContext context;

    public CorrectionLoaderBuilder(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public Builder corrections() {
        return new Builder(context);
    }

    /**
     * 作为内部的构建器，它提供了设置订正的方法 {@link #correction(Consumer)}
     */
    public static class Builder {

        private final ConfigurableApplicationContext context;

        public Builder(ConfigurableApplicationContext context) {
            this.context = context;
        }

        ConfigurableApplicationContext getContext() {
            return this.context;
        }

        /**
         * 所有的订正构建器，它们将会被统一执行构建方法，生成一套订正集
         */
        private final List<Correction.Builder> correctionBuilders = new ArrayList<>();

        /**
         * 一个正确的<b>订正</b>要包含：
         * <ol>
         *     <li>订正逻辑</ui>
         *     <li>来源表数据</ui>
         *     <li>来源表对应的 class 类型，可以有多个</li>
         *     <li>目标表对应的 class 类型，仅有一个</li>
         *     <li>连接数据的对象，这儿称之为连接对象 {@link Connection}</li>
         * </ol>
         * 所以将会在一套订正标准下完成一个订正构建器的初始化，最后加入到订正构建器链表中
         *
         * @param consumer 函数对象，通过 lambda 方式表达出来，效果更加直观
         * @return 返回它将可以重复使用构建订正的方法
         */
        public Builder correction(Consumer<CorrectionSpec> consumer) {
            Correction.Builder correctionBuilder = Correction.newBuilder();
            consumer.accept(new CorrectionSpec(correctionBuilder, getContext()));
            add(correctionBuilder);
            return this;
        }

        /**
         * 将生成的订正构建器加入到链表中，届时统一执行构建方法
         * @param correctionBuilder 订正构建器
         */
        private void add(Correction.Builder correctionBuilder) {
            correctionBuilders.add(correctionBuilder);
        }

        /**
         * 使所有的订正构建器进行<i>构建</i>，最后生成一套订正集
         *
         * @return 订正加载器
         */
        public CorrectionLoader build() {
            return () -> correctionBuilders.stream()
                    .map(Correction.Builder::build)
                    .collect(Collectors.toList());
        }

    }

}
