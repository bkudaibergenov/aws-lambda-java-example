package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;

public class LambdaPlainTextCreated implements LambdaFunctionHandler {

    @LambdaFunction(statusCode = 201, contentType = "text/plain")
    public String getPlainTextCreatedExample() {
        return "Plain text created example";
    }

}
