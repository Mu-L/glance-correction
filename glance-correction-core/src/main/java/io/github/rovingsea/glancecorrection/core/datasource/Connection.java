package io.github.rovingsea.glancecorrection.core.datasource;

/**
 * <p>
 * 数据库连接对象，虽然叫做连接，但是其实目的是为了完成 insert 操作
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public abstract class Connection {

    /**
     * 并不一定非得是一个数据库的连接对象，只要是能够完成 insert 操作就行
     */
    protected final Object conn;

    public Connection(Object conn) {
        this.conn = conn;
    }

    /**
     * 插入操作
     * @param target 目标表对应的类
     */
    public abstract void insert(Object target);

}
