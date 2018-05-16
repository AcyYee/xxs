package com.cwy.xxs.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author acy19
 */
public class MongoUtil {

    public static Query getQuery(Object ob) {
        Criteria c = new Criteria();
        List<Criteria> param = new ArrayList<>();
        for (Field f : ob.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            Object value;
            try {
                value = f.get(ob);
                boolean isOk = value != null && (value instanceof Integer || value instanceof String || value instanceof Double
                        || value instanceof String[] || value instanceof Integer[] || value instanceof Boolean);
                if (isOk) {
                    param.add(Criteria.where(f.getName()).is(value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (param.size() > 0) {
            c.andOperator(param.toArray(new Criteria[param.size()]));
        }
        return new Query(c);
    }

    public static Update getUpdate(Object ob) {
        Update update = new Update();
        for (Field f : ob.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            Object value;
            try {
                value = f.get(ob);
                boolean isOk = value != null && (value instanceof Integer || value instanceof String || value instanceof Double
                        || value instanceof String[] || value instanceof Integer[] || value instanceof Boolean);
                if (isOk) {
                    update.set(f.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return update;
    }

}
