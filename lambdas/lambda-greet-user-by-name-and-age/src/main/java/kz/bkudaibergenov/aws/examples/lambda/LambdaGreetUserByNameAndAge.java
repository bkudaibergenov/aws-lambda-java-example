package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.PathParam;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.PathTemplate;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

public class LambdaGreetUserByNameAndAge implements LambdaFunctionHandler {

    @LambdaFunction()
    @PathTemplate("/{name}/{age}")
    public ResponseEntity<ExampleResponseDto> greetUserByNameAndAge(@PathParam("name") String name, @PathParam("age") int age) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "! You are " + age + " years old."))
                .build();
    }

}
