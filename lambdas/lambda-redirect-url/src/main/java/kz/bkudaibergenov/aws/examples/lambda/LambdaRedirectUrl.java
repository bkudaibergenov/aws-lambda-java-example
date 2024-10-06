package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class LambdaRedirectUrl implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<?> redirectToUrl() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "https://google.com/");

        return ResponseEntity.builder()
                .statusCode(302)
                .headers(headers)
                .build();
    }

}
