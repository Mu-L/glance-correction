package io.github.rovingsea.glancecorrection.core.boot;

import io.github.rovingsea.glancecorrection.core.Correction;
import io.github.rovingsea.glancecorrection.core.CorrectionConfiguration;
import io.github.rovingsea.glancecorrection.core.CorrectionLoader;
import io.github.rovingsea.glancecorrection.core.Logic;
import io.github.rovingsea.glancecorrection.core.datasource.Connection;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * <p>
 * 订正启动器，它拥有所有的订正加载器 {@link CorrectionLoader}，通过订正加载器获取所有的订正，
 * 得到订正后将通过订正执行器 {@link CorrectionExecutor} 执行逻辑链，初始化一个目标对象后，
 * 随即入库
 * </p>
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class CorrectionBoot implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContextTemp)
            throws BeansException {
        applicationContext = applicationContextTemp;
    }

    /**
     * 所有的订正加载器，只要实现了 {@link CorrectionConfiguration} 就会视为一个订正加载器
     */
    private Map<String, CorrectionLoader> correctionLocatorMap;

    @PostConstruct
    private void initialize() {
        initCorrectionActuator(getApplicationContext());
    }

    private void initCorrectionActuator(ApplicationContext applicationContext) {
        this.correctionLocatorMap =
                applicationContext.getBeansOfType(CorrectionLoader.class);
    }

    /**
     * 开始订正：
     * <ol>
     *     <li> 是否支持处理这些订正加载器
     *     <li> 依次从每个订正加载器中获取每个订正
     * </ol>
     */
    public void boot() {
        if (!supports(correctionLocatorMap)) {
            return;
        }
        CorrectionExecutor correctionExecutor;
        for (String correctionLocatorName : correctionLocatorMap.keySet()) {
            CorrectionLoader correctionLoader = correctionLocatorMap.get(correctionLocatorName);
            List<Correction> corrections = correctionLoader.getCorrections();
            for (Correction correction : corrections) {
                correctionExecutor = newCorrectionExecutor(correction);
                doExecute(correctionExecutor, correction);
            }
        }
    }

    /**
     * <img src='../../../../../../../../../../docs/image/执行订正逻辑流程图.png' width=600 height=500 />
     * @param correctionExecutor 订正执行器
     * @param correction 订正
     */
    private void doExecute(CorrectionExecutor correctionExecutor,
                         Correction correction) {
        LogicChain logicChain = newLogicChain(correction);
        int size = getSourceDataSize(correction);
        for (int i = 0; i < size; i++) {
            Map<Class<?>, Object> curIndexSourceData = new HashMap<>();
            Map<Class<?>, List<?>> sourceData = correction.getSourceData();
            for (Class<?> aClass : sourceData.keySet()) {
                List<?> list = sourceData.get(aClass);
                curIndexSourceData.put(aClass, list.get(i));
            }
            correctionExecutor.setCurIndexSourceData(curIndexSourceData);
            correctionExecutor.execute(logicChain);
        }
    }

    /**
     * 得到数据源的长度
     * @param correction 订正
     * @return 数据源的长度
     */
    private int getSourceDataSize(Correction correction) {
        Collection<List<?>> values = correction.getSourceData().values();
        Iterator<List<?>> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return 0;
        }
        return iterator.next().size();
    }

    private CorrectionExecutor newCorrectionExecutor(Correction correction) {
        Connection conn = correction.getConnectionBuilder().buildConnection();
        Class<?> targetObjectClass = correction.getTargetObjectClass();
        int length = targetObjectClass.getFields().length;
        return new CorrectionExecutor(conn, targetObjectClass, length);
    }

    /**
     * 根据订正对象中的内容，生成一条逻辑链
     * @param correction 订正对象
     * @return 逻辑链
     */
    public LogicChain newLogicChain(Correction correction) {
        Map<String, Logic> columnLogics = correction.getColumnLogics();
        return new LogicChain(columnLogics);
    }

    /**
     * 从所有订正加载器 {@link CorrectionLoader} 获取所有的订正 {@link Correction}<br>
     * <ul>
     *     <li> 判断每个订正中的所有来源数据的数据量是否一样 {@link #sameSize(Map)}
     * </ul>
     * @param correctionLocatorMap 所有订正加载器
     * @return 是否支持订正
     */
    public boolean supports(Map<String, CorrectionLoader> correctionLocatorMap) {
        for (String correctionLocatorName : correctionLocatorMap.keySet()) {
            CorrectionLoader correctionLoader = correctionLocatorMap.get(correctionLocatorName);
            List<Correction> corrections = correctionLoader.getCorrections();
            for (Correction correction : corrections) {
                if (!sameSize(correction.getSourceData())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断一个订正中的所有来源数据的数据量是否一样
     * @param sourceData 来源数据
     * @return 数据量是否一样
     */
    public boolean sameSize(Map<Class<?>, List<?>> sourceData) {
        int size = 0;
        // 获取第一个 List 的长度
        for (Class<?> aClass : sourceData.keySet()) {
            List<?> list = sourceData.get(aClass);
            size = list.size();
            break;
        }
        final int finalSize = size;
        Collection<List<?>> values = sourceData.values();
        // 从第二个开始，要求每个长度都要和第一个相等
        return values.stream().skip(1).allMatch(data -> data.size() == finalSize);
    }

}
