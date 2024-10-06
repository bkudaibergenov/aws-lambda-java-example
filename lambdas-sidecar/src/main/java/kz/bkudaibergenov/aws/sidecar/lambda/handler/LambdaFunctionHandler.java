package kz.bkudaibergenov.aws.sidecar.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.processor.LambdaFunctionProcessor;

import java.util.Map;

public interface LambdaFunctionHandler extends RequestHandler<Object, Map<String, Object>> {

    @Override
    default Map<String, Object> handleRequest(Object input, Context context) {
        return LambdaFunctionProcessor.processResponse(this, input);
    }
}
