package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;

public class LambdaJsonExample implements LambdaFunctionHandler {

    @LambdaFunction()
    public ExampleResponseDto getJsonExample() {
        return new ExampleResponseDto("Json example");
    }

}
