package kz.bkudaibergenov.aws.vpc.builder;

import kz.bkudaibergenov.aws.vpc.config.VpcConfigLoader;
import kz.bkudaibergenov.aws.vpc.properties.VpcProperties;
import software.amazon.awscdk.services.ec2.GatewayVpcEndpointAwsService;
import software.amazon.awscdk.services.ec2.GatewayVpcEndpointOptions;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

import java.util.stream.Collectors;

public class VpcBuilder {

    public static Vpc buildVpc(final Construct scope) {
        VpcProperties vpcProperties = VpcConfigLoader.loadVpcProperties();

        Vpc.Builder vpcBuilder = Vpc.Builder.create(scope, vpcProperties.getVpc().getName())
                .maxAzs(vpcProperties.getVpc().getMaxAzs())
                .natGateways(vpcProperties.getVpc().getNatGateways());

        if (vpcProperties.getVpc().getEnableDnsSupport() != null) {
            vpcBuilder.enableDnsSupport(vpcProperties.getVpc().getEnableDnsSupport());
        }

        if (vpcProperties.getVpc().getEnableDnsHostnames() != null) {
            vpcBuilder.enableDnsHostnames(vpcProperties.getVpc().getEnableDnsHostnames());
        }

        if (vpcProperties.getVpc().getCreateInternetGateway() != null) {
            vpcBuilder.createInternetGateway(vpcProperties.getVpc().getCreateInternetGateway());
        }

        if (vpcProperties.getVpc().getSubnetConfiguration() != null) {
            vpcBuilder.subnetConfiguration(vpcProperties.getVpc().getSubnetConfiguration().stream()
                    .map(subnet -> SubnetConfiguration.builder()
                            .name(subnet.getName())
                            .subnetType(SubnetType.valueOf(subnet.getType()))
                            .cidrMask(subnet.getCidrMask())
                            .build())
                    .collect(Collectors.toList()));
        }

        Vpc vpc = vpcBuilder.build();

        for (VpcProperties.VPC.EndpointConfig endpoint : vpcProperties.getVpc().getGatewayEndpoints()) {
            GatewayVpcEndpointAwsService service = VpcBuilder.map(endpoint.getService());
            vpc.addGatewayEndpoint(endpoint.getService() + "Endpoint",
                    GatewayVpcEndpointOptions.builder()
                            .service(service)
                            .build());
        }

        return vpc;
    }

    private static GatewayVpcEndpointAwsService map(String serviceName) {
        return switch (serviceName.toUpperCase()) {
            case "S3" -> GatewayVpcEndpointAwsService.S3;
            case "DYNAMODB" -> GatewayVpcEndpointAwsService.DYNAMODB;
            case "S3_EXPRESS" -> GatewayVpcEndpointAwsService.S3_EXPRESS;
            default -> throw new IllegalArgumentException("Unsupported service: " + serviceName);
        };
    }
}
