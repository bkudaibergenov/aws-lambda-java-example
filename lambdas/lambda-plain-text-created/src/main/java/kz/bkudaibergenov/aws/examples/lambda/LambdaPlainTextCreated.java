package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;

public class LambdaPlainTextCreated implements LambdaFunctionHandler {

    @LambdaFunction(statusCode = 201, contentType = "text/plain")
    public String getPlainTextCreatedExample() {
        return "Plain text created example";
    }

}
