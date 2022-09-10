package io.github.rovingsea.glancecorrection.core.datasource;

import io.github.rovingsea.glancecorrection.core.util.Cast;
import org.apache.commons.dbutils.QueryRunner;
import io.github.rovingsea.glancecorrection.core.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mysql 的数据库连接对象，旨在完成 insert 操作
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class MysqlConnection extends Connection {


    public MysqlConnection(Object conn) {
        super(conn);
    }

    @Override
    public void insert(Object target) {
        DataSource dataSource = Cast.toSubclass(this.conn, DataSource.class);

        Class<?> targetClass = target.getClass();

        String tableName = StringUtils.camelToUnderline(targetClass.getSimpleName());
        String columns = convFieldsToColumns(targetClass);
        int effectiveLength = 0;
        if (columns != null) {
            effectiveLength = columns.split(",").length;
        }
        String matchingStr = getMatchingStr(effectiveLength);

        String sql = String.format("insert into %s (%s) value (%s)", tableName, columns, matchingStr);
        insert(dataSource, target, sql, effectiveLength);
    }

    private void insert(DataSource dataSource, Object target, String sql, int effectiveLength) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        Object[] targetFieldsValues = getTargetFieldsValues(target, effectiveLength);
        try {
            queryRunner.update(sql, targetFieldsValues);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将类中的字段属性转换为用 ',' 隔开的字符串
     *
     * @param targetClass 目标类
     * @return 用 ',' 隔开的字符串
     */
    private String convFieldsToColumns(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        List<String> columnNames = null;
        for (Field field : fields) {
            if (columnNames == null) {
                columnNames = new ArrayList<>();
            }
            String fieldName = field.getName();
            // 省略主键 id
            if ("id".equals(fieldName)) {
                continue;
            }
            columnNames.add(StringUtils.camelToUnderline(fieldName));
        }
        if (columnNames == null) {
            return null;
        }
        return String.join(",", columnNames);
    }

    /**
     * 得到匹配字段，也就是 ?,?,?.....，长度与属性字段的长度保持一致
     *
     * @param effectiveLength 有效列
     * @return 匹配字段
     */
    private String getMatchingStr(int effectiveLength) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < effectiveLength; i++) {
            res.add("?");
        }
        return String.join(",", res);
    }

    /**
     * 得到目标类示例的所有属性值
     *
     * @param target 目标类实例
     * @return 所有属性值
     */
    private Object[] getTargetFieldsValues(Object target, int effectiveLength) {
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        List<Object> values = new ArrayList<>(effectiveLength);
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                // todo 有客户端设置是否忽略
                if ("id".equals(fieldName)) {
                    continue;
                }
                String getValueMethod = "get" + StringUtils.firstToUpperCase(fieldName);
                Method method = targetClass.getMethod(getValueMethod);
                Object value = method.invoke(target);
                values.add(value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return values.toArray();
    }

}
