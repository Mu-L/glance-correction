package io.github.rovingsea.glancecorrection.core;

import io.github.rovingsea.glancecorrection.core.datasource.MysqlConnectionBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * <p>
 * 订正标准，旨在设置数据库类型、来源表的 class、来源数据、目标表
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class CorrectionSpec {

    private final Correction.Builder correctionBuilder;

    private final ConfigurableApplicationContext context;

    public CorrectionSpec(Correction.Builder correctionBuilder, ConfigurableApplicationContext context) {
        this.correctionBuilder = correctionBuilder;
        this.context = context;
    }

    /**
     * 设置来源表的 class 类型
     * @param sourceDataClass 来源表
     * @param <S> 来源表的 class 类型
     * @return 数据对象 {@link Data}
     */
    public <S> Data<S> sourceDataClass(Class<S> sourceDataClass) {
        this.correctionBuilder.sourceData.sourceObjectClasses(sourceDataClass);
        return new Data<>(this.correctionBuilder, this);
    }

    /**
     * 在 mysql 的基础上进行数据订正
     * @return 返回自己
     */
    public CorrectionSpec mysql() {
        this.correctionBuilder.setConnectionBuilder(
                new MysqlConnectionBuilder(this.context));
        return this;
    }

    /**
     * 设置目标表的 class 类型
     * @param targetDataClass 目标表
     * @return 返回订正构建器，进而下一步设置目标表每一列的订正逻辑，或者构建订正
     */
    public Correction.Builder targetDataClass(Class<?> targetDataClass) {
        this.correctionBuilder.targetObjectClass(targetDataClass);
        return this.correctionBuilder;
    }

    /**
     * 用于设置来源数据
     * @param <S> 来源表的 class
     */
    public static class Data<S> {

        private final Correction.Builder correctionBuilder;

        private final CorrectionSpec correctionSpec;

        public Data(Correction.Builder correctionBuilder,
                    CorrectionSpec correctionSpec) {
            this.correctionBuilder = correctionBuilder;
            this.correctionSpec = correctionSpec;
        }

        public CorrectionSpec data(List<S> data) {
            this.correctionBuilder.sourceData.data(data);
            return this.correctionSpec;
        }

    }


}
