package io.github.rovingsea.glancecorrection.core;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 仅仅只是为了得到某个配置下的订正集
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
@FunctionalInterface
public interface CorrectionLoader {

    /**
     * <p>
     * 设置订正集，个数取决于 {@link CorrectionLoaderBuilder#corrections()}
     * 使用了多少次 {@link CorrectionLoaderBuilder.Builder#correction(Consumer)}
     * </p>
     * @return 订正集
     */
    List<Correction> getCorrections();

}
