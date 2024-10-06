
# AWS Lambda Java Examples

This project demonstrates the creation of AWS Lambda functions in Java, showcasing various examples using custom annotations to define Lambda handlers.

## Project Structure

- **lambda-commons**: Contains common utilities and configurations shared across Lambda functions.
- **lambda-examples**: Contains example implementations of AWS Lambda functions using Java and custom annotations.

## Requirements

- **Java 17**
- **Gradle**
- **AWS CLI**
- **AWS CDK**

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd my-aws-infra
```

### Build the Project

```bash
./gradlew clean build
```

### Set Up AWS CLI

Install AWS CLI using Homebrew:

```bash
brew install awscli
```

Configure AWS credentials:

```bash
aws configure --profile your-account
```

### Deploy with AWS CDK

Ensure that AWS CDK is installed:

```bash
npm install -g aws-cdk
```

Bootstrap your environment:

```bash
cdk bootstrap --profile your-account
```

Synthesize and deploy the CDK stack:

```bash
cdk synth --profile your-account
cdk deploy --profile your-account
```

## Example Usage

### Custom Annotations

The project uses custom annotations to define the Lambda handlers, simplifying the process of creating and managing Lambda functions. Here are some of the examples:

```java
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
```

### Lambda Configuration

The configuration for Lambda functions is stored in `resources/lambdas-config.yaml`:

```yaml
lambdas:
  - name: "ExampleFunction"
    handler: "kz.bkudaibergenov.aws.examples.lambda.LambdaPlainText::handleRequest"
    codePath: "./lambda-examples/build/libs/example-lambda.jar"
    api:
      name: "ExampleApi"
      path: "/example"
      methods: ["GET", "POST"]
```

## Examples Included

1. **Plain Text Response**
    - Simple Lambda function returning plain text.

2. **JSON Response**
    - Lambda function returning a JSON response using `ExampleResponseDto`.

3. **Path Parameters**
    - Example demonstrating handling of path parameters using `@PathTemplate`.

4. **Request Headers**
    - Example demonstrating validation of authorization tokens using `@RequestHeader`.

5. **Redirects**
    - Example returning a 302 redirect with a `Location` header.

6. **File Download**
    - Example for downloading a file with proper `Content-Disposition`.

7. **Processing JSON Arrays**
    - Lambda function that processes JSON arrays and returns a list of DTOs.

## How to Run

You can run the application locally for testing:

```bash
./gradlew run
```
