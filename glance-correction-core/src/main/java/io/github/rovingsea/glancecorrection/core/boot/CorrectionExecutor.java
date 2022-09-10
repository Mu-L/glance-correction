package io.github.rovingsea.glancecorrection.core.boot;

import io.github.rovingsea.glancecorrection.core.datasource.Connection;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 订正执行器，最后将会借助数据库连接对象 {@link Connection} 来完成 insert 操作
 *
 * @author Haixin Wu
 * @since 1.0.0
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

    /**
     * 由于使用多线程初始化目标表对象，它将会等待最后一个线程结束后才允许执行 insert
     */
    private final CountDownLatch countDownLatch;

    public CorrectionExecutor(Connection conn, Class<?> targetObjectClass, int targetTableColumnSize) {
        this.conn = conn;
        this.targetObjectClass = targetObjectClass;
        this.countDownLatch = new CountDownLatch(targetTableColumnSize);
    }

    private Map<Class<?>, Object> curIndexSourceData;

    private final ExecutorService executorService = new ThreadPoolExecutor(
            1,
            30,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(30),
            new CustomizableThreadFactory("correction-prefix-"));


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
            execute(subItem, instance);
            this.countDownLatch.countDown();
        }

        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.conn.insert(instance);
    }

    /**
     * 使用线程执行某一个逻辑
     * @param subItem 逻辑
     * @param instance 目标表的实例，或者说某一行
     */
    private void execute(LogicChain.SubItem subItem, Object instance) {
        subItem.getLogic().set(this.curIndexSourceData, instance);
        this.executorService.execute(subItem.getLogic());
    }

    public void setCurIndexSourceData(Map<Class<?>, Object> curIndexSourceData) {
        this.curIndexSourceData = curIndexSourceData;
    }

}
