package kz.bkudaibergenov.aws.vpc.properties;

import java.util.List;

public class VpcProperties {

    private VPC vpc;

    public VPC getVpc() {
        return vpc;
    }

    public void setVpc(VPC vpc) {
        this.vpc = vpc;
    }

    public static class VPC {
        private String name;
        private Integer maxAzs;
        private Boolean enableDnsSupport;
        private Boolean enableDnsHostnames;
        private Integer natGateways;
        private Boolean createInternetGateway;
        private List<SubnetConfig> subnetConfiguration;
        private List<EndpointConfig> gatewayEndpoints;
        private List<FlowLogConfig> flowLogs;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMaxAzs() {
            return maxAzs;
        }

        public void setMaxAzs(Integer maxAzs) {
            this.maxAzs = maxAzs;
        }

        public Boolean getEnableDnsSupport() {
            return enableDnsSupport;
        }

        public void setEnableDnsSupport(Boolean enableDnsSupport) {
            this.enableDnsSupport = enableDnsSupport;
        }

        public Boolean getEnableDnsHostnames() {
            return enableDnsHostnames;
        }

        public void setEnableDnsHostnames(Boolean enableDnsHostnames) {
            this.enableDnsHostnames = enableDnsHostnames;
        }

        public Integer getNatGateways() {
            return natGateways;
        }

        public void setNatGateways(Integer natGateways) {
            this.natGateways = natGateways;
        }

        public Boolean getCreateInternetGateway() {
            return createInternetGateway;
        }

        public void setCreateInternetGateway(Boolean createInternetGateway) {
            this.createInternetGateway = createInternetGateway;
        }

        public List<SubnetConfig> getSubnetConfiguration() {
            return subnetConfiguration;
        }

        public void setSubnetConfiguration(List<SubnetConfig> subnetConfiguration) {
            this.subnetConfiguration = subnetConfiguration;
        }

        public List<EndpointConfig> getGatewayEndpoints() {
            return gatewayEndpoints;
        }

        public void setGatewayEndpoints(List<EndpointConfig> gatewayEndpoints) {
            this.gatewayEndpoints = gatewayEndpoints;
        }

        public List<FlowLogConfig> getFlowLogs() {
            return flowLogs;
        }

        public void setFlowLogs(List<FlowLogConfig> flowLogs) {
            this.flowLogs = flowLogs;
        }

        public static class SubnetConfig {
            private String name;
            private String type;
            private Integer cidrMask;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Integer getCidrMask() {
                return cidrMask;
            }

            public void setCidrMask(Integer cidrMask) {
                this.cidrMask = cidrMask;
            }
        }

        public static class EndpointConfig {
            private String service;

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }
        }

        public static class FlowLogConfig {
            private String name;
            private String destination;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDestination() {
                return destination;
            }

            public void setDestination(String destination) {
                this.destination = destination;
            }
        }
    }
}
