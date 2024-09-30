package kz.bkudaibergenov.aws;

import kz.bkudaibergenov.aws.stack.InfrastructureStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class Application {

    public static void main(final String[] args) {
        App app = new App();

        new InfrastructureStack(app, "InfrastructureStack",
                StackProps.builder()
                        .build());

        app.synth();
    }
}

