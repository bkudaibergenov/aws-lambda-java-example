package kz.bkudaibergenov.aws.stack;

import kz.bkudaibergenov.aws.lambda.model.LambdaConfig;
import kz.bkudaibergenov.aws.lambda.util.LambdaConfigLoader;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        LambdaConfig config = LambdaConfigLoader.loadLambdaConfig();

        for (LambdaConfig.LambdaDefinition lambdaDefinition : config.getLambdas()) {
            Function lambdaFunction = Function.Builder.create(this, lambdaDefinition.getName())
                    .runtime(Runtime.JAVA_17)
                    .handler(lambdaDefinition.getHandler())
                    .code(Code.fromAsset(lambdaDefinition.getCodePath()))
                    .build();

            if (lambdaDefinition.getApi() != null) {
                LambdaRestApi api = LambdaRestApi.Builder.create(this, lambdaDefinition.getApi().getName())
                        .handler(lambdaFunction)
                        .build();

                Resource resource = api.getRoot().addResource(lambdaDefinition.getApi().getPath().substring(1)); // удаляем начальный "/"
                for (String method : lambdaDefinition.getApi().getMethods()) {
                    resource.addMethod(method, new LambdaIntegration(lambdaFunction));
                }
            }
        }
    }
}
