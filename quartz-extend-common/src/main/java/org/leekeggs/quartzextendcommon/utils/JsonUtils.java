package org.leekeggs.quartzextendcommon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.leekeggs.quartzextendcommon.exception.JacksonApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * JSON工具类，支持：
 * <ul>
 *     <li>将map转成json字符串</li>
 *     <li>将Java Bean转换成json字符串</li>
 *     <li>将json字符串转换成Bean</li>
 * </ul>
 *
 * @author redcoder54
 * @since 1.0.0
 */
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
    public static String mapToJsonString(Map map) {
        return mapToJsonString(map, null);
    }

    /**
     * 将map转成json字符串
     *
     * @param map        要转换的map
     * @param optionalJM 用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @return json字符串
     */
    @SuppressWarnings("rawtypes")
    public static String mapToJsonString(Map map, @Nullable JsonMapper optionalJM) {
        try {
            if (optionalJM == null) {
                return jsonMapper.writeValueAsString(map);
            }
            return optionalJM.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("json serialization exception", e);
            throw new JacksonApiException("map对象转换成json字符串失败");
        }
    }

    /**
     * 将Java Bean转换成json字符串
     *
     * @param obj 待转换的对象Bean
     * @return json字符串
     */
    public static String beanToJsonString(Object obj) {
        return beanToJsonString(obj, null);
    }

    /**
     * 将Java Bean转换成json字符串
     *
     * @param obj        待转换的对象Bean
     * @param optionalJM 用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @return json字符串
     */
    public static String beanToJsonString(Object obj, @Nullable JsonMapper optionalJM) {
        try {
            if (optionalJM == null) {
                return jsonMapper.writeValueAsString(obj);
            }
            return optionalJM.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json serialization exception", e);
            throw new JacksonApiException("bean对象转换成json字符串失败");
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
    public static <T> T jsonStringToBean(String jsonStr, Class<T> clazz) {
        return jsonStringToBean(jsonStr, clazz, null);
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr    json字符串
     * @param clazz      待转换的bean class对象
     * @param optionalJM 用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @param <T>        待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T jsonStringToBean(String jsonStr, Class<T> clazz, @Nullable JsonMapper optionalJM) {
        try {
            if (optionalJM == null) {
                return jsonMapper.readValue(jsonStr, clazz);
            }
            return optionalJM.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error("json deserialization exception", e);
            throw new JacksonApiException("json字符串转换成bean对象失败");
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
    public static <T> T jsonStringToBean(String jsonStr, TypeReference<T> typeReference) {
        return jsonStringToBean(jsonStr, typeReference, null);
    }

    /**
     * 将json字符串转换成Bean
     *
     * @param jsonStr       json字符串
     * @param typeReference {@link TypeReference}
     * @param optionalJM    用于处理数据转换的JsonMapper，如果为空，使用默认的JsonMapper
     * @param <T>           待转换的bean类型
     * @return 转换后的Bean对象
     */
    public static <T> T jsonStringToBean(String jsonStr, TypeReference<T> typeReference, @Nullable JsonMapper optionalJM) {
        try {
            if (optionalJM == null) {
                return jsonMapper.readValue(jsonStr, typeReference);
            }
            return optionalJM.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json deserialization exception", e);
            throw new JacksonApiException("json字符串转换成bean对象失败");
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
