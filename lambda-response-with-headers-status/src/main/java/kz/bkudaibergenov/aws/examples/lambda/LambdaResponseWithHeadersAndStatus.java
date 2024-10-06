package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LambdaResponseWithHeadersAndStatus implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> getResponseWithHeadersAndStatusCode() {
        Map<String, String> headers = new HashMap<>();
        headers.put("response-key", UUID.randomUUID().toString());

        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Response with headers and status code"))
                .statusCode(201)
                .headers(headers)
                .build();
    }

}
