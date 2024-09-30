package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.annotation.PathParam;
import kz.bkudaibergenov.aws.commons.lambda.annotation.PathTemplate;
import kz.bkudaibergenov.aws.commons.lambda.annotation.QueryParam;
import kz.bkudaibergenov.aws.commons.lambda.annotation.RequestBody;
import kz.bkudaibergenov.aws.commons.lambda.annotation.RequestHeader;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.commons.lambda.model.ResponseEntity;
import kz.bkudaibergenov.aws.examples.dto.ExampleResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExampleLambda implements LambdaFunctionHandler {

    @LambdaFunction(contentType = "text/plain")
    public String getPlainText() {
        return "Text example";
    }

    @LambdaFunction(statusCode = 201, contentType = "text/plain")
    public String getPlainTextCreatedExample() {
        return "Example 2";
    }

    @LambdaFunction()
    @PathTemplate("/{name}/{age}")
    public ResponseEntity<ExampleResponseDto> greetUserByNameAndAge(@PathParam("name") String name, @PathParam("age") int age) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "! You are " + age + " years old."))
                .build();
    }

    @LambdaFunction()
    public ExampleResponseDto getJsonExample() {
        return new ExampleResponseDto("Example json");
    }

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> getResponseWithHeadersAndStatusCode() {
        Map<String, String> headers = new HashMap<>();
        headers.put("response-key", UUID.randomUUID().toString());

        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Example 5"))
                .statusCode(201)
                .headers(headers)
                .build();
    }

    @LambdaFunction()
    public ResponseEntity<?> redirectToUrl() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "https://google.com/");

        return ResponseEntity.builder()
                .statusCode(302)
                .headers(headers)
                .build();
    }

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> greetUserByNameQuery(@QueryParam("name") String name) {
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "!"))
                .build();
    }

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> greetUserFromRequestBody(@RequestBody Map<String, Object> input) {
        String name = (String) input.get("name");
        return ResponseEntity.<ExampleResponseDto> builder()
                .body(new ExampleResponseDto("Hello, " + name + "!"))
                .build();
    }

    @LambdaFunction(contentType = "application/octet-stream")
    public ResponseEntity<byte[]> downloadFile(@PathParam("fileId") String fileId) {
        byte[] fileContent = "text".getBytes();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Disposition", "attachment; filename=\"" + fileId + ".txt\"");

        return ResponseEntity.<byte[]>builder()
                .body(fileContent)
                .headers(headers)
                .build();
    }

    @LambdaFunction()
    public ResponseEntity<ExampleResponseDto> handleRequestHeaders(@RequestHeader("Authorization") String authToken) {
        if (authToken != null && authToken.equals("Bearer abc123")) {
            return ResponseEntity.<ExampleResponseDto>builder()
                    .body(new ExampleResponseDto("Token is valid"))
                    .build();
        } else {
            return ResponseEntity.<ExampleResponseDto>builder()
                    .statusCode(401)
                    .body(new ExampleResponseDto("Unauthorized"))
                    .build();
        }
    }

    @LambdaFunction()
    public ResponseEntity<List<ExampleResponseDto>> processJsonArrayResponseEntity(@RequestBody List<Map<String, Object>> input) {
        List<ExampleResponseDto> responses = input.stream()
                .map(item -> new ExampleResponseDto("Hello, " + item.get("name") + "!"))
                .collect(Collectors.toList());

        return ResponseEntity.<List<ExampleResponseDto>>builder()
                .body(responses)
                .build();
    }

    @LambdaFunction()
    public List<ExampleResponseDto> processJsonArray(@RequestBody List<Map<String, Object>> input) {
        return input.stream()
                .map(item -> new ExampleResponseDto("Hello, " + item.get("name") + "!"))
                .collect(Collectors.toList());
    }

}
