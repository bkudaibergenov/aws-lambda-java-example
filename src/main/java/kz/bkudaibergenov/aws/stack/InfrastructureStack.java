package kz.bkudaibergenov.aws.stack;

import kz.bkudaibergenov.aws.lambda.model.LambdaRegistration;
import kz.bkudaibergenov.aws.lambda.register.RegistrationLambda;
import kz.bkudaibergenov.aws.vpc.builder.VpcBuilder;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class InfrastructureStack extends Stack {

    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Vpc vpc = VpcBuilder.buildVpc(this);

        LambdaRegistration lambdaRegistration = new LambdaRegistration.Builder()
                .vpc(vpc)
                .build();

        RegistrationLambda.register(this, lambdaRegistration);
    }
}
