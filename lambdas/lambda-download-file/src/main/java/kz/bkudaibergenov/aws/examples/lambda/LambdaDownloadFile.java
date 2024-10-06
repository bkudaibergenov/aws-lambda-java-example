package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.PathParam;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class LambdaDownloadFile implements LambdaFunctionHandler {

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
}
