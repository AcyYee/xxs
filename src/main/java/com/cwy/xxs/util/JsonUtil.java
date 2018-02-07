package com.cwy.xxs.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Acy
 */
public class JsonUtil {

    public final static ObjectMapper MAPPER = new ObjectMapper();

    public static String objectToJson(Object object){
        try {
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
