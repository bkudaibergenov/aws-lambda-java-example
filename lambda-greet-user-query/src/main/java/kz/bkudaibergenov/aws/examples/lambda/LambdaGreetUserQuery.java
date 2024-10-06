package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.QueryParam;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

public class LambdaGreetUserQuery implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> greetUserByNameQuery(@QueryParam("name") String name) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "!"))
                .build();
    }

}
