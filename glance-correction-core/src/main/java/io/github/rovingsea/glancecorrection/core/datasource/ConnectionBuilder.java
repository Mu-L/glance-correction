package io.github.rovingsea.glancecorrection.core.datasource;

/**
 * 数据库连接对象的构建器
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public interface ConnectionBuilder {

    /**
     * 构建出数据库连接对象
     * @return 数据库连接对象
     */
    Connection buildConnection();

}
