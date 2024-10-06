package kz.bkudaibergenov.aws.stack;

//import kz.bkudaibergenov.aws.lambda.model.LambdaRegistration;
//import kz.bkudaibergenov.aws.lambda.register.RegistrationLambda;
//import kz.bkudaibergenov.aws.vpc.builder.VpcBuilder;

import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

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

        //Vpc vpc = VpcBuilder.buildVpc(this);


        //LambdaRegistration lambdaRegistration = new LambdaRegistration.Builder()
        //           .vpc(vpc)
        //           .build();



        //RegistrationLambda.register(this, lambdaRegistration);



        // Создание VPC
        Vpc vpc = Vpc.Builder.create(this, "MyVpc")
                .maxAzs(2) // Используем минимальное количество зон доступности
                .build();

        // Упрощенные учетные данные для базы данных
        String dbUser = "lambda_user";
        SecretValue dbPassword = SecretValue.unsafePlainText("lambda_password");




        // Создание RDS PostgreSQL базы данных с минимальными ресурсами
        DatabaseInstance rdsInstance = DatabaseInstance.Builder.create(this, "PostgresRDSInstance")
                .engine(DatabaseInstanceEngine.postgres(PostgresInstanceEngineProps.builder()
                        .version(PostgresEngineVersion.VER_13) // Указание правильной версии PostgreSQL
                        .build()))
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO)) // Самый маленький тип инстанса
                .vpc(vpc)
                .credentials(Credentials.fromPassword(dbUser, dbPassword)) // Простые учетные данные
                .allocatedStorage(5) // Минимальный объем хранилища (5 GB)
                .databaseName("mydatabase") // Имя базы данных
                .build();


        //init lambda
        Function initDBLambda = Function.Builder.create(this, "InitDBLambda")
                .runtime(Runtime.JAVA_17)
                .code(Code.fromAsset("lambda-db-init/build/distributions/lambda-function.zip"))  // путь к zip файлу
                .handler("com.myorg.InitDBHandler::handleRequest")
                .memorySize(128)
                .timeout(software.amazon.awscdk.Duration.seconds(100))
                .vpc(vpc)
                .environment(Map.of(
                        "DB_HOST", rdsInstance.getDbInstanceEndpointAddress(),
                        "DB_PORT", rdsInstance.getDbInstanceEndpointPort(),
                        "DB_NAME", "mydatabase",
                        "DB_USER", dbUser,
                        "DB_PASSWORD", dbPassword.unsafeUnwrap()
                ))
                .build();


        // Добавляем вызов Lambda после создания базы данных
        initDBLambda.getNode().addDependency(rdsInstance);

        // Даем Lambda доступ к RDS
        PolicyStatement initRdsPolicy = PolicyStatement.Builder.create()
                .actions(List.of("rds-data:*"))
                .resources(List.of(rdsInstance.getInstanceArn()))
                .build();
        initDBLambda.addToRolePolicy(initRdsPolicy);



        // Lambda для POST (добавление пользователя в базу данных) с минимальными ресурсами
        Function postLambda = Function.Builder.create(this, "PostUserDBLambda")
                .runtime(Runtime.JAVA_17)
                .code(Code.fromAsset("lambda-db-post/build/distributions/lambda-function.zip"))
                .handler("com.myorg.PostUserHandler::handleRequest")
                .memorySize(128)
                .timeout(software.amazon.awscdk.Duration.seconds(100))
                .vpc(vpc)
                .environment(Map.of(
                        "DB_HOST", rdsInstance.getDbInstanceEndpointAddress(),
                        "DB_PORT", rdsInstance.getDbInstanceEndpointPort(),
                        "DB_NAME", "mydatabase",
                        "DB_USER", dbUser,
                        "DB_PASSWORD", dbPassword.unsafeUnwrap()
                ))
                .build();

        // Lambda для GET (получение пользователей из базы данных) с минимальными ресурсами
        Function getLambda = Function.Builder.create(this, "GetUserDBLambda")
                .runtime(Runtime.JAVA_17)
                .code(Code.fromAsset("lambda-db-get/build/distributions/lambda-function.zip"))
                .handler("com.myorg.GetUserHandler::handleRequest")
                .memorySize(128)
                .timeout(software.amazon.awscdk.Duration.seconds(100))
                .vpc(vpc)
                .environment(Map.of(
                        "DB_HOST", rdsInstance.getDbInstanceEndpointAddress(),
                        "DB_PORT", rdsInstance.getDbInstanceEndpointPort(),
                        "DB_NAME", "mydatabase",
                        "DB_USER", dbUser,
                        "DB_PASSWORD", dbPassword.unsafeUnwrap()
                ))
                .build();

        // Даем Lambda доступ к RDS
        PolicyStatement rdsPolicy = PolicyStatement.Builder.create()
                .actions(List.of("rds-data:*"))
                .resources(List.of(rdsInstance.getInstanceArn()))
                .build();
        postLambda.addToRolePolicy(rdsPolicy);
        getLambda.addToRolePolicy(rdsPolicy);

        // Создание API Gateway для связи с лямбдами
        LambdaRestApi getApi = LambdaRestApi.Builder.create(this, "UserGetApi")
                .restApiName("User Service")
                .handler(getLambda)
                .description("This service handles user operations.")
                .build();

        LambdaRestApi postApi = LambdaRestApi.Builder.create(this, "UserPostApi")
                .restApiName("User Service")
                .handler(postLambda)
                .description("This service handles user operations.")
                .build();

        // Добавляем методы GET и POST в API Gateway
        getApi.getRoot().addMethod("GET", LambdaIntegration.Builder.create(getLambda).build()); // GET запросы
        postApi.getRoot().addMethod("POST", LambdaIntegration.Builder.create(postLambda).build()); // POST запросы
    }
}
