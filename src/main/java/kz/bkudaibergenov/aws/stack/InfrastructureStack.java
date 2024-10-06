package kz.bkudaibergenov.aws.stack;

import kz.bkudaibergenov.aws.lambda.model.LambdaRegistration;
import kz.bkudaibergenov.aws.lambda.register.RegistrationLambda;
import kz.bkudaibergenov.aws.vpc.builder.VpcBuilder;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class InfrastructureStack extends Stack {

    //        Vpc vpc = Vpc.Builder.create(this, "MyVpc")
//                .maxAzs(2)
//                .build();
//
//        String secretKey = "3d38e9577ef44836bc447c4c19ae514f";
//
//        DatabaseInstance rdsInstance = DatabaseInstance.Builder.create(this, "PostgresRDSInstance")
//                .engine(DatabaseInstanceEngine.postgres(
//                        PostgresInstanceEngineProps.builder().version(PostgresEngineVersion.VER_14_12).build()))
//                .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
//                .vpc(vpc)
//                .credentials(Credentials.fromPassword("lambda_user", SecretValue.unsafePlainText(secretKey)))
//                .allocatedStorage(20)
//                .databaseName("mydatabase")
//                .build();

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
