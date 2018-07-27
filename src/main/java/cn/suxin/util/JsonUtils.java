package cn.suxin.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.*;

/**
 * json工具类.
 * 
 * @author wangfeng
 * 
 */
public final class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    // public static JsonConfig profileJsonConfig=new JsonConfig();
    //
    // static {
    // profileJsonConfig.setExcludes(new String[]{"outOfDate"});
    // }

    private JsonUtils() {
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * 
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * 
     * @see #fromJson(String, com.fasterxml.jackson.databind.JavaType)
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public static <T> T fromJsonThrows(String jsonString, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonString, clazz);

    }

    /**
     * 返回复杂类型
     * 
     * @param jsonString
     * @param reference
     * @return
     */
    public static <T> T fromJson(String jsonString, TypeReference<T> reference) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonString, reference);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public static String writeJsonValue(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("write json string error", e);
        }
        return null;
    }

    public static String readNodeValue(String nodeName, String content) {
        try {
            JsonNode node = mapper.readTree(content);
            JsonNode nameNode = node.path(nodeName);
            if (nameNode != null) {
                return nameNode.textValue();
            }
        } catch (IOException e) {
            logger.error("read json tree error", e);
        }

        return null;
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * 参数名字母顺序排序
     * 
     * @param paraMap 参数map
     * @param containParaName 是否包含参数
     * @return
     * @throws SDKRuntimeException
     */
    public static String mapToString(Map<String, String> paraMap, boolean containParaName) {

        return mapToString(paraMap, containParaName, true);
    }

    /**
     * 参数名字母顺序排序
     * 
     * @param paraMap 参数map
     * @param containParaName 是否包含参数
     * @return
     * @throws SDKRuntimeException
     */
    public static String mapToString(Map<String, String> paraMap, boolean containParaName, boolean toUpperCase) {

        StringBuffer buff = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : paraMap.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (containParaName) {
                    if (toUpperCase) {
                        buff.append(key.toUpperCase());
                    } else {
                        buff.append(key);
                    }
                    buff.append("=");
                }
                buff.append(val);
                buff.append("&");
            }

            if (buff.length() > 0) {
                buff.delete(buff.length() - 1, buff.length()); // 去掉最后的&
            }
        } catch (Exception e) {
            throw e;
        }
        return buff.toString();
    }

    /**
     * jsonString -> map
     * 
     * @param jsonStr
     * @return
     */
    public static Map<String, String> jsonStringToMap(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            return mapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("parse json string error:" + jsonStr, e);
            return null;
        }

    }

    /**
     * jsonString -> map
     * 
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> jsonStringToMap2(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            return mapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("parse json string error:" + jsonStr, e);
            return null;
        }

    }

   


    public static void main(String[] args) {
        int aaa = 1;
        String tt = JsonUtils.fromJson("true", String.class);
        System.out.println(tt);
    }

}
