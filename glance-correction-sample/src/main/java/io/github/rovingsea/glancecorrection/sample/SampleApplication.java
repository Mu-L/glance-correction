package io.github.rovingsea.glancecorrection.sample;

import io.github.rovingsea.glancecorrection.core.boot.CorrectionBoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@SpringBootApplication
public class SampleApplication {
    public static void main(String[] args) {
        CorrectionBoot correctionBoot =
                SpringApplication.run(SampleApplication.class, args).getBean(CorrectionBoot.class);
        correctionBoot.boot();

    }
}

