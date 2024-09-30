package kz.bkudaibergenov.aws.lambda.util;

import kz.bkudaibergenov.aws.constant.InfrastructureStackConstants;
import kz.bkudaibergenov.aws.lambda.model.LambdaConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class LambdaConfigLoader {

    public static LambdaConfig loadLambdaConfig() {
        LoaderOptions loaderOptions = new LoaderOptions();

        Yaml yaml = new Yaml(new Constructor(LambdaConfig.class, loaderOptions));

        try (InputStream inputStream = LambdaConfigLoader.class.getClassLoader()
                .getResourceAsStream(InfrastructureStackConstants.LAMBDA_CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Configuration file not found: " +
                        InfrastructureStackConstants.LAMBDA_CONFIG_FILE);
            }

            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Configuration loading error", e);
        }
    }
}
