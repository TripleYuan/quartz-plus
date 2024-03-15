package redcoder.quartzplus.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import redcoder.quartzplus.common.exception.JacksonApiException;

import java.util.Map;

public class JsonUtils {

    private static final JsonMapper jsonMapper;
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    static {
        jsonMapper = new JsonMapper();
        jsonMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * @return 返回复制的JsonMapper对象
     */
    public static JsonMapper getJsonMapper() {
        return copyJsonMapper();
    }

    /**
     * 拷贝一个 <code>jsonMapper</code>对象
     *
     * @return 新拷贝的<code>jsonMapper</code>对象
     */
    public static JsonMapper copyJsonMapper() {
        return jsonMapper.copy();
    }

    /**
     * 将map转成json字符串
     *
     * @param map 要转换的map
     * @return json字符串
     */
    @SuppressWarnings("rawtypes")
    public static String toJsonString(Map map) {
        return toJsonString(map, null);
    }

    /**
     * 将map转成json字符串
     *
     * @param map  要转换的map
     * @param spec 用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @return json字符串
     */
    @SuppressWarnings("rawtypes")
    public static String toJsonString(Map map, @Nullable JsonMapper spec) {
        try {
            if (spec == null) {
                return jsonMapper.writeValueAsString(map);
            }
            return spec.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new JacksonApiException("map对象转换成json字符串失败", e);
        }
    }

    /**
     * 将Java Bean转换成json字符串
     *
     * @param obj 待转换的对象Bean
     * @return json字符串
     */
    public static String toJsonString(Object obj) {
        return toJsonString(obj, null);
    }

    /**
     * 将Java Bean转换成json字符串
     *
     * @param obj  待转换的对象Bean
     * @param spec 用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @return json字符串
     */
    public static String toJsonString(Object obj, @Nullable JsonMapper spec) {
        try {
            if (spec == null) {
                return jsonMapper.writeValueAsString(obj);
            }
            return spec.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JacksonApiException("bean对象转换成json字符串失败", e);
        }
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr json字符串
     * @param clazz   待转换的bean class对象
     * @param <T>     待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        return toBean(jsonStr, clazz, null);
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr json字符串
     * @param clazz   待转换的bean class对象
     * @param spec    用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @param <T>     待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz, @Nullable JsonMapper spec) {
        try {
            if (spec == null) {
                return jsonMapper.readValue(jsonStr, clazz);
            }
            return spec.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new JacksonApiException("json字符串转换成bean对象失败", e);
        }
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr       json字符串
     * @param typeReference {@link TypeReference}
     * @param <T>           待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        return toBean(jsonStr, typeReference, null);
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr       json字符串
     * @param typeReference {@link TypeReference}
     * @param spec          用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @param <T>           待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference, @Nullable JsonMapper spec) {
        try {
            if (spec == null) {
                return jsonMapper.readValue(jsonStr, typeReference);
            }
            return spec.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json deserialization exception", e);
            throw new JacksonApiException("json字符串转换成bean对象失败", e);
        }
    }

    /**
     * 开启SerializationFeature
     *
     * @param feature 要开启的SerializationFeature
     * @return 返回一个新的JsonMapper对象，不会修改内部持有的JsonMapper对象
     */
    public static JsonMapper enableFeature(SerializationFeature feature) {
        JsonMapper jsonMapper = copyJsonMapper();
        return (JsonMapper) jsonMapper.enable(feature);
    }

    /**
     * 启用DeserializationFeature
     *
     * @param feature 要开启的DeserializationFeature
     * @return 返回一个新的JsonMapper对象，不会修改内部持有的JsonMapper对象
     */
    public static JsonMapper enableFeature(DeserializationFeature feature) {
        JsonMapper jsonMapper = copyJsonMapper();
        return (JsonMapper) jsonMapper.enable(feature);
    }

    /**
     * 禁用SerializationFeature
     *
     * @param feature 要禁用的SerializationFeature
     * @return 返回一个新的JsonMapper对象，不会修改内部持有的JsonMapper对象
     */
    public static JsonMapper disableFeature(SerializationFeature feature) {
        JsonMapper jsonMapper = copyJsonMapper();
        return (JsonMapper) jsonMapper.disable(feature);
    }

    /**
     * 禁用DeserializationFeature
     *
     * @param feature 要禁用的DeserializationFeature
     * @return 返回一个新的JsonMapper对象，不会修改内部持有的JsonMapper对象
     */
    public static JsonMapper disableFeature(DeserializationFeature feature) {
        JsonMapper jsonMapper = copyJsonMapper();
        return (JsonMapper) jsonMapper.disable(feature);
    }
}
