package io.github.rovingsea.glancecorrection.core.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.rovingsea.glancecorrection.core.CorrectionLoaderBuilder;
import io.github.rovingsea.glancecorrection.core.boot.CorrectionBoot;

/**
 * 将由 spring.factories 进行 SPI
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
@Configuration
public class DataCorrectionAutoConfiguration {

    @Bean
    public CorrectionBoot correctionBoot() {
        return new CorrectionBoot();
    }

    @Bean
    public CorrectionLoaderBuilder correctionLoaderBuilder(ConfigurableApplicationContext context) {
        return new CorrectionLoaderBuilder(context);
    }

}
