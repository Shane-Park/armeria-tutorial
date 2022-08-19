package com.tistory.shanepark.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;
import com.tistory.shanepark.domain.Board;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicLong;

public class BoardPostRequestConverter implements RequestConverterFunction {

    private static final ObjectMapper mapper = new ObjectMapper();
    private AtomicLong idGenerator = new AtomicLong();

    static String stringValue(JsonNode jsonNode, String field) {
        JsonNode value = jsonNode.get(field);
        if (value == null) {
            throw new IllegalArgumentException(field + " is missing!");
        }
        return value.textValue();
    }

    @Override
    public @Nullable Object convertRequest(
            ServiceRequestContext ctx
            , AggregatedHttpRequest request
            , Class<?> expectedResultType
            , @Nullable ParameterizedType expectedParameterizedResultType)
            throws Exception {
        if (expectedResultType == Board.class) {
            JsonNode jsonNode = mapper.readTree(request.contentUtf8());
            long id = idGenerator.getAndIncrement();
            String title = stringValue(jsonNode, "title");
            String content = stringValue(jsonNode, "content");
            return new Board(id, title, content);
        }
        return RequestConverterFunction.fallthrough();
    }
}
