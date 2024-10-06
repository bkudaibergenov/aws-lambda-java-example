package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.RequestHeader;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

public class LambdaHandleRequestHeaders implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> handleRequestHeaders(@RequestHeader("Authorization") String authToken) {
        if (authToken != null && authToken.equals("Bearer abc123")) {
            return ResponseEntity.<ExampleResponseDto>builder()
                    .body(new ExampleResponseDto("Token is valid"))
                    .build();
        } else {
            return ResponseEntity.<ExampleResponseDto>builder()
                    .statusCode(401)
                    .body(new ExampleResponseDto("Unauthorized"))
                    .build();
        }
    }

}
