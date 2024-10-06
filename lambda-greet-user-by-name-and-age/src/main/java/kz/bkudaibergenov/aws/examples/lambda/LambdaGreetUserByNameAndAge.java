package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.PathParam;
import kz.bkudaibergenov.aws.commons.lambda.annotation.PathTemplate;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

public class LambdaGreetUserByNameAndAge implements LambdaFunctionHandler {

    @LambdaFunction()
    @PathTemplate("/{name}/{age}")
    public ResponseEntity<ExampleResponseDto> greetUserByNameAndAge(@PathParam("name") String name, @PathParam("age") int age) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "! You are " + age + " years old."))
                .build();
    }

}
