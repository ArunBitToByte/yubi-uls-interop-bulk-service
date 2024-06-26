package com.yubi.uls.bulk.core.utility.helper;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HelperUtility {

    public static Map<String, Object> convertJsonNodeToMap(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>(){});
        return map;
    }

    public static JsonNode  convertMapToJsonNode(Map<String, String> map) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(map, JsonNode.class);
        return jsonNode;
    }

    public static String checkAndSetNullValue(String value) {
        if(StringUtils.isEmpty(value)){
            return "";
        }
        return value;
    }

    public static Long checkAndSetNullValue(Long value) {
        if(value == null){
            return 0L;
        }
        return value;
    }

}
