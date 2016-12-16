package utils.bfs;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class State implements Cloneable {

    private static final ConcurrentMap<Class<?>, Field[]> fieldsMap = new ConcurrentHashMap<>();
    protected State parent;

    public State(State parent) {
        this.parent = parent;
    }

    public State parent() {
        return parent;
    }

    public int steps() {
        int steps = 0;
        State state = this.parent;
        while (state != null) {
            steps++;
            state = state.parent;
        }
        return steps;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected State clone() {
        try {
            State clone = (State) super.clone();

            // Now we're going to clone any collections/maps on the child...
            for (Field field : getFields()) {
                Object value = field.get(clone);
                if (value instanceof Collection) {
                    Collection<?> newCollection = (Collection<?>) value.getClass().newInstance();
                    newCollection.addAll((Collection) value);
                    value = newCollection;
                }

                else if (value instanceof Map) {
                    Map<?, ?> newMap = (Map<?, ?>) value.getClass().newInstance();
                    newMap.putAll((Map) value);
                    value = newMap;
                }

                field.set(clone, value);
            }

            return clone;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field[] getFields() {
        Field[] fields = fieldsMap.computeIfAbsent(this.getClass(), (classType) -> {
            Field[] fieldArray = classType.getDeclaredFields();
            Field.setAccessible(fieldArray, true);
            return fieldArray;
        });

        return fields;
    }

}
