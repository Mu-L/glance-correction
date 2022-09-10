package io.github.rovingsea.glancecorrection.core;

import io.github.rovingsea.glancecorrection.core.boot.CorrectionBoot;
import io.github.rovingsea.glancecorrection.core.boot.CorrectionExecutor;

/**
 * <p>
 * 通过订正加载器构建器 {@link CorrectionLoaderBuilder} 设置订正的来源表、目标表、订正逻辑，之后将会生成一个
 * 订正加载器 {@link CorrectionLoader}，这个加载器包含了在这个配置下所有的订正 {@link Correction}，
 * 视其为这个配置的订正集。这个订正集将会被订正启动器 {@link CorrectionBoot} 进行依次处理订正逻辑，最后由
 * 订正执行器 {@link CorrectionExecutor} 执行入库。
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public interface CorrectionConfiguration {

    /**
     * 设置配置，实现之后还需要添加 Bean 注解
     * @param builder 订正执行器建造器
     * @return 返回一个订正执行器
     */
    CorrectionLoader setConfiguration(CorrectionLoaderBuilder builder);

}
