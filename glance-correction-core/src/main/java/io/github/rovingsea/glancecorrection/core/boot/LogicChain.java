package io.github.rovingsea.glancecorrection.core.boot;

import io.github.rovingsea.glancecorrection.core.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public class LogicChain {

    private final List<SubItem> chain = new ArrayList<>();

    public LogicChain(Map<String, Logic> columnLogics) {
        columnLogics.forEach((name, logic) ->
                chain.add(new SubItem(name, logic)));
    }

    public List<SubItem> getChain() {
        return chain;
    }

    public static class SubItem {

        private final String name;

        private final Logic logic;

        public SubItem(String name, Logic logic) {
            this.name = name;
            this.logic = logic;
        }

        public String getName() {
            return name;
        }

        public Logic getLogic() {
            return logic;
        }
    }

}
