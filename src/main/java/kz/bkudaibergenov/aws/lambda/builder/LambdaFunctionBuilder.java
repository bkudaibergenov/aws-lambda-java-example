package kz.bkudaibergenov.aws.lambda.builder;

import kz.bkudaibergenov.aws.lambda.model.LambdaRegistration;
import kz.bkudaibergenov.aws.lambda.properties.LambdaProperties;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.Optional;

public class LambdaFunctionBuilder {

    public static Function buildLambdaFunction(Construct scope, LambdaProperties.LambdaDefinition lambdaDefinition, LambdaRegistration lambdaRegistration) {
        Function.Builder lambdaFunctionBuilder = Function.Builder.create(scope, lambdaDefinition.getName())
                .runtime(Runtime.JAVA_17)
                .handler(lambdaDefinition.getHandler())
                .code(Code.fromAsset(lambdaDefinition.getCodePath()))
                .timeout(Optional.ofNullable(lambdaDefinition.getTimeout())
                        .map(Duration::seconds).orElse(null));

        if (Boolean.TRUE.equals(lambdaDefinition.getUseVpc())) {
            if (lambdaRegistration.getVpc() == null) {
                throw new IllegalArgumentException("VPC is required for lambda function with VPC");
            }

            lambdaFunctionBuilder
                    .vpc(lambdaRegistration.getVpc())
                    .vpcSubnets(SubnetSelection.builder()
                            .subnetType(SubnetType.PRIVATE_WITH_EGRESS)
                            .build());
        }

        return lambdaFunctionBuilder.build();
    }

    public static void configureLambdaApi(Construct scope, Function lambdaFunction, LambdaProperties.ApiDefinition apiDefinition) {
        LambdaRestApi api = LambdaRestApi.Builder.create(scope, apiDefinition.getName())
                .handler(lambdaFunction)
                .build();

        Resource resource = api.getRoot()
                .addResource(apiDefinition.getPath().substring(1));

        apiDefinition.getMethods().forEach(method -> resource.addMethod(method, new LambdaIntegration(lambdaFunction)));
    }
}
