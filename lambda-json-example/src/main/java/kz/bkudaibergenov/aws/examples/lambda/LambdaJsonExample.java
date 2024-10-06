package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

public class LambdaJsonExample implements LambdaFunctionHandler {

    @LambdaFunction()
    public ExampleResponseDto getJsonExample() {
        return new ExampleResponseDto("Json example");
    }

}
