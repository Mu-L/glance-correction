package io.github.rovingsea.glancecorrection.sample.logic;

import io.github.rovingsea.glancecorrection.core.Logic;
import io.github.rovingsea.glancecorrection.sample.entity.From1;
import io.github.rovingsea.glancecorrection.sample.entity.From2;
import io.github.rovingsea.glancecorrection.sample.entity.To1;

import java.util.Map;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class NameAndAgeLogic extends Logic {
    @Override
    public void set(Map<Class<?>, ?> sourceDataClass, Object targetObject) {
        From1 from1 = (From1) sourceDataClass.get(From1.class);
        From2 from2 = (From2) sourceDataClass.get(From2.class);
        To1 to1 = (To1) targetObject;

        to1.setNameAndAge(from1.getName() + from2.getName() + " " + from1.getAge() + from2.getAge());
    }
}

