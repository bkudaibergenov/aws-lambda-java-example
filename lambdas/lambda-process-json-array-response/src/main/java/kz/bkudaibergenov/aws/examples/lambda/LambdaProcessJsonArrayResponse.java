package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.RequestBody;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LambdaProcessJsonArrayResponse implements LambdaFunctionHandler {

    @LambdaFunction()
    public ResponseEntity<List<ExampleResponseDto>> processJsonArrayResponseEntity(@RequestBody List<Map<String, Object>> input) {
        List<ExampleResponseDto> responses = input.stream()
                .map(item -> new ExampleResponseDto("Hello, " + item.get("name") + "!"))
                .collect(Collectors.toList());

        return ResponseEntity.<List<ExampleResponseDto>>builder()
                .body(responses)
                .build();
    }

}
