package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.RequestHeader;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

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
