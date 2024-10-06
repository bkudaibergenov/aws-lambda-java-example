package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.RequestBody;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

import java.util.List;
import java.util.Map;

public class LambdaProcessJsonArray implements LambdaFunctionHandler {

    @LambdaFunction()
    public List<ExampleResponseDto> processJsonArray(@RequestBody List<Map<String, Object>> input) {
        return input.stream()
                .map(item -> new ExampleResponseDto("Hello, " + item.get("name") + "!"))
                .toList();
    }

}
