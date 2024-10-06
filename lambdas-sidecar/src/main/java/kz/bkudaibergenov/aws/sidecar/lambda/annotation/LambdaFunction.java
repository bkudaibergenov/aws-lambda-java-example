package kz.bkudaibergenov.aws.sidecar.lambda.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LambdaFunction {
    int statusCode() default 200;
    String contentType() default "application/json";
}
