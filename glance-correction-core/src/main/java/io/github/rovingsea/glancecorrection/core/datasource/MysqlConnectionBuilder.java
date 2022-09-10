package io.github.rovingsea.glancecorrection.core.datasource;

import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * @author Haixin Wu
 * @since 1.0.0
 */
public class MysqlConnectionBuilder implements ConnectionBuilder {

    private final ApplicationContext context;

    public MysqlConnectionBuilder(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public io.github.rovingsea.glancecorrection.core.datasource.Connection buildConnection() {
        DataSource dataSource = this.context.getBean(DataSource.class);
        return new MysqlConnection(dataSource);
    }

}
