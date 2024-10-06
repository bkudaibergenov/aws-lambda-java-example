package kz.bkudaibergenov.aws.sidecar.lambda.processor;


import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.PathParam;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.PathTemplate;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.QueryParam;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.RequestBody;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.RequestHeader;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.sidecar.util.JsonConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LambdaFunctionProcessor {

    private static final Logger logger = Logger.getLogger(LambdaFunctionProcessor.class.getName());

    public static Map<String, Object> processResponse(Object instance, Object input) {
        try {
            Method handlerMethod = findHandlerMethod(instance);
            if (handlerMethod == null) {
                return notFoundResponse();
            }

            LambdaFunction responseBodyAnnotation = handlerMethod.getAnnotation(LambdaFunction.class);
            Map<String, Object> inputMap = castToMap(input);

            Map<String, String> pathParameters = extractPathParameters(handlerMethod, inputMap);
            Map<String, String> queryParameters = extractQueryParameters(inputMap);
            Map<String, String> headers = extractHeaders(inputMap);
            String requestBody = extractRequestBody(inputMap);

            Object[] methodArgs = buildMethodArguments(handlerMethod, queryParameters, pathParameters, headers, requestBody);
            Object responseBodyContent = handlerMethod.invoke(instance, methodArgs);

            return buildResponse(responseBodyContent, responseBodyAnnotation);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing response", e);
            return internalServerErrorResponse();
        }
    }

    private static Object[] buildMethodArguments(Method method,
                                                 Map<String, String> queryParameters,
                                                 Map<String, String> pathParameters,
                                                 Map<String, String> headers,
                                                 String requestBody) {

        Parameter[] parameters = method.getParameters();
        Object[] methodArgs = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(QueryParam.class)) {
                String paramName = parameter.getAnnotation(QueryParam.class).value();
                methodArgs[i] = queryParameters.get(paramName);

            } else if (parameter.isAnnotationPresent(PathParam.class)) {
                String paramName = parameter.getAnnotation(PathParam.class).value();
                String pathValue = pathParameters.get(paramName);
                methodArgs[i] = convertToType(pathValue, parameter.getType());

            } else if (parameter.isAnnotationPresent(RequestHeader.class)) {
                String headerName = parameter.getAnnotation(RequestHeader.class).value();
                String headerValue = headers.get(headerName);
                methodArgs[i] = convertToType(headerValue, parameter.getType());

            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                if (requestBody != null && !requestBody.isEmpty()) {
                    methodArgs[i] = JsonConverter.convertToObject(requestBody, parameter.getParameterizedType());
                } else {
                    methodArgs[i] = null;
                }
            } else {
                methodArgs[i] = null;
            }
        }

        return methodArgs;
    }

    private static Object convertToType(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot convert value '" + value + "' to type " + targetType.getName(), e);
        }
        throw new IllegalArgumentException("Unsupported parameter type: " + targetType.getName());
    }

    public static Map<String, String> parsePathParameters(String template, String actualPath) {
        String[] templateParts = removeLeadingSlash(template).split("/");
        String[] pathParts = removeLeadingSlash(actualPath).split("/");

        if (templateParts.length != pathParts.length) {
            return Collections.emptyMap();
        }

        Map<String, String> pathParams = new HashMap<>();
        for (int i = 0; i < templateParts.length; i++) {
            String templatePart = templateParts[i];
            if (isPathVariable(templatePart)) {
                String paramName = extractVariableName(templatePart);
                pathParams.put(paramName, pathParts[i]);
            }
        }
        return pathParams;
    }

    private static String removeLeadingSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }

    private static boolean isPathVariable(String templatePart) {
        return templatePart.startsWith("{") && templatePart.endsWith("}");
    }

    private static String extractVariableName(String templatePart) {
        return templatePart.substring(1, templatePart.length() - 1);
    }

    public static String extractRequestBody(Map<String, Object> inputMap) {
        return (String) inputMap.get("body");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> extractParameters(Map<String, Object> inputMap, String key) {
        Object value = inputMap.get(key);
        if (value instanceof Map) {
            return (Map<String, String>) value;
        }
        return Collections.emptyMap();
    }

    public static Map<String, String> extractQueryParameters(Map<String, Object> inputMap) {
        return extractParameters(inputMap, "queryStringParameters");
    }

    public static Map<String, String> extractHeaders(Map<String, Object> inputMap) {
        return extractParameters(inputMap, "headers");
    }

    public static Map<String, String> extractPathParameters(Method method, Map<String, Object> inputMap) {
        Map<String, String> pathParameters = extractParameters(inputMap, "pathParameters");

        if (method.isAnnotationPresent(PathTemplate.class) && !pathParameters.isEmpty()) {
            String pathTemplate = method.getAnnotation(PathTemplate.class).value();
            String actualPath = pathParameters.get("proxy");

            if (actualPath != null) {
                return parsePathParameters(pathTemplate, actualPath);
            }
        }

        return pathParameters;
    }

    public static Method findHandlerMethod(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(LambdaFunction.class)) {
                return method;
            }
        }
        return null;
    }

    private static Map<String, Object> buildResponse(Object responseBodyContent, LambdaFunction responseBodyAnnotation) {
        if (responseBodyContent instanceof ResponseEntity<?>) {
            return buildResponseFromEntity((ResponseEntity<?>) responseBodyContent, responseBodyAnnotation);
        } else {
            return buildResponseFromContent(responseBodyContent, responseBodyAnnotation);
        }
    }

    private static Map<String, Object> buildResponseFromEntity(ResponseEntity<?> entityResponse, LambdaFunction responseBodyAnnotation) {
        int statusCode = (entityResponse.getStatusCode() != null && entityResponse.getStatusCode() != 0)
                ? entityResponse.getStatusCode()
                : responseBodyAnnotation.statusCode();

        Map<String, String> headers = new HashMap<>(entityResponse.getHeaders());
        headers.putIfAbsent("Content-Type", responseBodyAnnotation.contentType());

        String body = serializeResponseBody(entityResponse.getBody(), responseBodyAnnotation.contentType());

        return Map.of(
                "statusCode", statusCode,
                "headers", headers,
                "body", body
        );
    }

    private static Map<String, Object> buildResponseFromContent(Object responseBodyContent, LambdaFunction responseBodyAnnotation) {
        int statusCode = responseBodyAnnotation.statusCode();
        Map<String, String> headers = Map.of("Content-Type", responseBodyAnnotation.contentType());
        String body = serializeResponseBody(responseBodyContent, responseBodyAnnotation.contentType());

        return Map.of(
                "statusCode", statusCode,
                "headers", headers,
                "body", body
        );
    }

    public static Map<String, Object> internalServerErrorResponse() {
        String errorBody = JsonConverter.convertToJson(Map.of("error", "Internal Server Error"));
        return buildSimpleResponse(500, errorBody, "application/json");
    }

    public static Map<String, Object> notFoundResponse() {
        String errorBody = JsonConverter.convertToJson(Map.of("error", "Resource not found"));
        return buildSimpleResponse(404, errorBody, "application/json");
    }

    public static String serializeResponseBody(Object content, String contentType) {
        if ("application/json".equalsIgnoreCase(contentType)) {
            return JsonConverter.convertToJson(content);
        }
        return content != null ? content.toString() : "";
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castToMap(Object input) {
        if (input instanceof Map) {
            return (Map<String, Object>) input;
        }
        throw new IllegalArgumentException("Input must be a Map");
    }

    private static Map<String, Object> buildSimpleResponse(int statusCode, String body, String contentType) {
        Map<String, String> headers = Map.of("Content-Type", contentType);
        return Map.of(
                "statusCode", statusCode,
                "headers", headers,
                "body", body
        );
    }
}
