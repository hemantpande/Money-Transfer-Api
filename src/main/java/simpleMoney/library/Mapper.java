package simpleMoney.library;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import simpleMoney.library.exceptions.MapperException;

import java.io.IOException;

import static simpleMoney.library.ResponseCode.PARSER_ERROR;

public final class Mapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Mapper() {}

    public static <T> String toJson(T obj) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            String message = "Can't parse to JSON";
            throw new MapperException(PARSER_ERROR, message, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            String message = "Cannot parse request to JSON";
            throw new MapperException(PARSER_ERROR, message, e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return OBJECT_MAPPER.readValue(json, typeRef);
        } catch (IOException e) {
            String message = "Cannot parse request to JSON";
            throw new MapperException(PARSER_ERROR, message, e);
        }
    }
}
