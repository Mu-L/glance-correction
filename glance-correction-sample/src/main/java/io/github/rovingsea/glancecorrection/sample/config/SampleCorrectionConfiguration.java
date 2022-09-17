package io.github.rovingsea.glancecorrection.sample.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.rovingsea.glancecorrection.core.CorrectionConfiguration;
import io.github.rovingsea.glancecorrection.core.CorrectionLoader;
import io.github.rovingsea.glancecorrection.core.CorrectionLoaderBuilder;
import io.github.rovingsea.glancecorrection.sample.entity.From1;
import io.github.rovingsea.glancecorrection.sample.entity.From2;
import io.github.rovingsea.glancecorrection.sample.entity.To1;
import io.github.rovingsea.glancecorrection.sample.logic.NameAndAgeLogic;
import io.github.rovingsea.glancecorrection.sample.mapper.From1Mapper;
import io.github.rovingsea.glancecorrection.sample.mapper.From2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Configuration
public class SampleCorrectionConfiguration implements CorrectionConfiguration {


    @Autowired
    private From1Mapper from1Mapper;

    @Autowired
    private From2Mapper from2Mapper;

    @Bean(name = "to1")
    @Override
    public CorrectionLoader setConfiguration(CorrectionLoaderBuilder builder) {
        QueryWrapper<From1> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.last("limit 0,10");
        QueryWrapper<From2> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.last("limit 0,10");

        return builder.corrections()
                .correction(c -> c
                        .mysql()
                        .sourceDataClass(From1.class)
                        .data(from1Mapper.selectList(queryWrapper1))
                        .sourceDataClass(From2.class)
                        .data(from2Mapper.selectList(queryWrapper2))
                        .targetDataClass(To1.class)
                        .column("name_and_age", new NameAndAgeLogic()))
                .build();
    }
}

