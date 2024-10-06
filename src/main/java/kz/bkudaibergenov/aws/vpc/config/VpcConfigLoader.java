package kz.bkudaibergenov.aws.vpc.config;

import kz.bkudaibergenov.aws.constant.InfrastructureStackConstants;
import kz.bkudaibergenov.aws.lambda.util.YamlConfigurationUtils;
import kz.bkudaibergenov.aws.vpc.properties.VpcProperties;

public class VpcConfigLoader {

    public static VpcProperties loadVpcProperties() {
        return YamlConfigurationUtils.loadConfiguration(InfrastructureStackConstants.VPC_CONFIG_FILE, VpcProperties.class);
    }
}
