package kz.bkudaibergenov.aws.sidecar.lambda.model;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntity<T> {
    private final T body;
    private final Map<String, String> headers;
    private final Integer statusCode;

    private ResponseEntity(Builder<T> builder) {
        this.body = builder.body;
        this.headers = builder.headers != null ? builder.headers : new HashMap<>();
        this.statusCode = builder.statusCode;
    }

    public T getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Integer getStatusCode() {
        return statusCode;
    }


    public static class Builder<T> {
        private T body;
        private Map<String, String> headers = new HashMap<>();
        private Integer statusCode;

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public Builder<T> header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder<T> headers(Map<String, String> headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }
            return this;
        }

        public Builder<T> statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ResponseEntity<T> build() {
            return new ResponseEntity<>(this);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}
