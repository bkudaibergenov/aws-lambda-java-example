package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.QueryParam;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

public class LambdaGreetUserQuery implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> greetUserByNameQuery(@QueryParam("name") String name) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "!"))
                .build();
    }

}
