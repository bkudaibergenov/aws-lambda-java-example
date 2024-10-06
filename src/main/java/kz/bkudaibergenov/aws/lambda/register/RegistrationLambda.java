package kz.bkudaibergenov.aws.lambda.register;

import kz.bkudaibergenov.aws.lambda.builder.LambdaFunctionBuilder;
import kz.bkudaibergenov.aws.lambda.config.LambdaConfigLoader;
import kz.bkudaibergenov.aws.lambda.model.LambdaRegistration;
import kz.bkudaibergenov.aws.lambda.properties.LambdaProperties;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class RegistrationLambda {

    public static void register(final Construct scope, LambdaRegistration lambdaRegistration) {
        LambdaProperties config = LambdaConfigLoader.loadLambdaProperties();

        config.getLambdas().forEach(lambdaDefinition -> {
            Function lambdaFunction = LambdaFunctionBuilder.buildLambdaFunction(scope, lambdaDefinition, lambdaRegistration);

            if (lambdaDefinition.getApi() != null) {
                LambdaFunctionBuilder.configureLambdaApi(scope, lambdaFunction, lambdaDefinition.getApi());
            }
        });
    }
}
