package kz.bkudaibergenov.aws.lambda.util;

import kz.bkudaibergenov.aws.vpc.config.VpcConfigLoader;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class YamlConfigurationUtils {

    public static <T> T loadConfiguration(@NotNull String filename, @NotNull Class<T> clazz) {
        LoaderOptions loaderOptions = new LoaderOptions();

        Yaml yaml = new Yaml(new Constructor(clazz, loaderOptions));

        try (InputStream inputStream = VpcConfigLoader.class.getClassLoader()
                .getResourceAsStream(filename)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Configuration file not found: " +
                        filename);
            }

            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Configuration loading error", e);
        }
    }
}
