package kz.bkudaibergenov.aws.lambda.model;

import software.amazon.awscdk.services.ec2.Vpc;

public class LambdaRegistration {

    private Vpc vpc;

    public Vpc getVpc() {
        return vpc;
    }

    public void setVpc(Vpc vpc) {
        this.vpc = vpc;
    }

    public static class Builder {
        private Vpc vpc;

        public Builder() {

        }

        public Builder vpc(Vpc vpc) {
            this.vpc = vpc;
            return this;
        }

        public LambdaRegistration build() {
            LambdaRegistration lambdaRegistration = new LambdaRegistration();
            lambdaRegistration.setVpc(this.vpc);
            return lambdaRegistration;
        }
    }
}

