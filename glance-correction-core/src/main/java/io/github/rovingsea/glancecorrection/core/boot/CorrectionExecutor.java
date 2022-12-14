package io.github.rovingsea.glancecorrection.core.boot;

import io.github.rovingsea.glancecorrection.core.datasource.Connection;

import java.util.List;
import java.util.Map;

/**
 * 订正执行器，最后将会借助数据库连接对象 {@link Connection} 来完成 insert 操作
 *
 * @author Haixin Wu
 * @since 1.0.1
 */
public class CorrectionExecutor {
    /**
     * 数据库连接对象
     */
    private final Connection conn;
    /**
     * 目标表的 class
     */
    private final Class<?> targetObjectClass;

    private final ExecutionAroundProcessor executionAroundProcessor;

    public CorrectionExecutor(Connection conn, Class<?> targetObjectClass,
                              ExecutionAroundProcessor executionAroundProcessor) {
        this.conn = conn;
        this.targetObjectClass = targetObjectClass;
        this.executionAroundProcessor = executionAroundProcessor;
    }

    private Map<Class<?>, Object> curIndexSourceData;

    public void execute(LogicChain logicChain) {
        execute(logicChain.getChain());
    }

    private void execute(List<LogicChain.SubItem> chain) {
        int size = chain.size();
        if (size == 0) {
            return;
        }

        Object instance;

        try {
            instance = targetObjectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (LogicChain.SubItem subItem : chain) {
            executeLogic(subItem, instance);
        }

        if (executionAroundProcessor == null) {
            this.conn.insert(instance);
        } else {
            this.executionAroundProcessor.postProcessBeforeExecution(curIndexSourceData,
                    targetObjectClass, conn, instance);
            this.executionAroundProcessor.processExecution(curIndexSourceData,
                    targetObjectClass, conn, instance);
            this.executionAroundProcessor.postProcessAfterExecution(curIndexSourceData,
                    targetObjectClass, conn, instance);
        }
    }

    /**
     * 执行逻辑链
     *
     * @param subItem  逻辑
     * @param instance 目标表的实例，或者说某一行
     */
    private void executeLogic(LogicChain.SubItem subItem, Object instance) {
        subItem.getLogic().set(curIndexSourceData, instance);
    }

    public void setCurIndexSourceData(Map<Class<?>, Object> curIndexSourceData) {
        this.curIndexSourceData = curIndexSourceData;
    }

}
