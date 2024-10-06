package kz.bkudaibergenov.aws.lambda.model;

import software.amazon.awscdk.services.ec2.Vpc;

public class LambdaRegistration {

    private Long timeout;
    private Vpc vpc;

    public Vpc getVpc() {
        return vpc;
    }

    public void setVpc(Vpc vpc) {
        this.vpc = vpc;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public static class Builder {
        private Long timeout;
        private Vpc vpc;

        public Builder() {

        }

        public Builder vpc(Vpc vpc) {
            this.vpc = vpc;
            return this;
        }

        public Builder timeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public LambdaRegistration build() {
            LambdaRegistration lambdaRegistration = new LambdaRegistration();
            lambdaRegistration.setVpc(this.vpc);
            lambdaRegistration.setTimeout(this.timeout);
            return lambdaRegistration;
        }
    }
}

