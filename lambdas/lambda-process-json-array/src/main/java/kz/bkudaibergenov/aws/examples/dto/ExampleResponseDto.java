package kz.bkudaibergenov.aws.examples.dto;

public class ExampleResponseDto {

    private String message;

    public ExampleResponseDto() {
    }

    public ExampleResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
