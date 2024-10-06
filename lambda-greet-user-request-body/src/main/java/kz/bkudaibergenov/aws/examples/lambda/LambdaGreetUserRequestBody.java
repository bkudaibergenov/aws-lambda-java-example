package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.RequestBody;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

import java.util.Map;

public class LambdaGreetUserRequestBody implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> greetUserFromRequestBody(@RequestBody Map<String, Object> input) {
        String name = (String) input.get("name");
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "!"))
                .build();
    }

}
