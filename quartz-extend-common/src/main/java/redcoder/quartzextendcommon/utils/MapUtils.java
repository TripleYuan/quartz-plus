package redcoder.quartzextendcommon.utils;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendcommon.exception.BeanToMapException;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 *
 * @author redcoder54
 * @since 1.0.0
 */
@Slf4j
public class MapUtils {

    private MapUtils() {
    }

    /**
     * 构建一个包含一对key-value映射的map对象
     *
     * @param key   key
     * @param value value
     * @param <K>   key类型
     * @param <V>   value类型
     * @return map对象
     */
    public static <K, V> Map<K, V> buildMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 构建一个包含两对key-value映射的map对象
     *
     * @param k1  key
     * @param v1  value
     * @param k2  key
     * @param v2  value
     * @param <K> key类型
     * @param <V> value类型
     * @return map对象
     */
    public static <K, V> Map<K, V> buildMap(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    /**
     * 构建一个包含两对key-value映射的map对象
     *
     * @param k1  key
     * @param v1  value
     * @param k2  key
     * @param v2  value
     * @param k3  key
     * @param v3  value
     * @param <K> key类型
     * @param <V> value类型
     * @return map对象
     */
    public static <K, V> Map<K, V> buildMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    /**
     * 将bean转换成Map，key：属性名，value：属性值。
     * 注意：转换后的Map对象，不会包含bean父类中的属性！
     *
     * @param bean           要转换的bean
     * @param beanClass      bean类型
     * @param filterNullProp 如果为true，bean中的null值属性不会被处理
     * @return 包含bean属性值的map对象，key：属性名，value：属性值
     * @throws BeanToMapException bean转map失败
     */
    public static Map<String, Object> beanToMap(Object bean, Class<?> beanClass, boolean filterNullProp) {
        Assert.notNull(bean, "parameter 'bean' must not be null");

        try {
            Field[] fields = beanClass.getDeclaredFields();
            if (fields.length == 0) {
                return Collections.emptyMap();
            }
            Map<String, Object> map = new HashMap<>(fields.length);
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(bean);
                if (value == null && filterNullProp) {
                    continue;
                }
                map.put(name, value);
            }
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BeanToMapException(e.getMessage(), e);
        }
    }
}
