package io.github.rovingsea.glancecorrection.sample.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 *
 * 
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class AutoGenerationUtils {


    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 设置数据源
        mpg.setDataSource(new DataSourceConfig()
                .setDriverName("com.mysql.cj.jdbc.Driver")
                // 设置数据库类型
                .setDbType(DbType.MYSQL)
                .setUsername("root")
                .setPassword("123456")
                .setUrl("jdbc:mysql://localhost:3306/glance_correction?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true")
        );

        // 全局配置
        mpg.setGlobalConfig(new GlobalConfig()
                // 是否覆盖
                .setFileOverride(true)
                // 开启AR模式
                .setActiveRecord(true)
                // XML二级缓存
                .setEnableCache(false)
                // 生成ResultMap
                .setBaseResultMap(true)
                // 生成 sql片段
                .setBaseColumnList(true)
                // 自动打开生成后的文件夹
                .setOpen(true)
                // 所有文件的生成者
                .setAuthor("Haixin Wu")
                // 自定义文件命名,%s会自动填充表实体类名字
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
        );

        // 策略配置
        mpg.setStrategy(new StrategyConfig()
                        // 需要生成的表
                        .setInclude("from1", "from2", "to")
//                .setInclude("grade_have_subject")
                        // 实体类使用Lombok
                        .setEntityLombokModel(true)
                        // 表名生成策略,下划线转驼峰
                        .setNaming(NamingStrategy.underline_to_camel)
                        .setColumnNaming(NamingStrategy.underline_to_camel)
        );

        // 包配置
        mpg.setPackageInfo(new PackageConfig()
                .setParent("io.github.glancecorrection.sample")
                // 设置Controller包名
//                .setController("controller")
                // 设置entity包名
                .setEntity("entity")
                // 设置Mapper包名
                .setMapper("mapper")
                // 设置Service包名
                .setService("service")
                // 设置Service实现类包名
                .setServiceImpl("service.impl")
                // 设置Mapper.xml包名
                .setXml("mapper")
        );


        mpg.execute();
    }

}
