package kz.bkudaibergenov.aws.lambda.config;

import kz.bkudaibergenov.aws.constant.InfrastructureStackConstants;
import kz.bkudaibergenov.aws.lambda.properties.LambdaProperties;
import kz.bkudaibergenov.aws.lambda.util.YamlConfigurationUtils;

public class LambdaConfigLoader {

    public static LambdaProperties loadLambdaProperties() {
        return YamlConfigurationUtils.loadConfiguration(InfrastructureStackConstants.LAMBDA_CONFIG_FILE,
                LambdaProperties.class);
    }
}
