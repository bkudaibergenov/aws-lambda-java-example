package kz.bkudaibergenov.aws.lambda.model;

import java.util.List;

public class LambdaConfig {

    private List<LambdaDefinition> lambdas;

    public List<LambdaDefinition> getLambdas() {
        return lambdas;
    }

    public void setLambdas(List<LambdaDefinition> lambdas) {
        this.lambdas = lambdas;
    }

    public static class LambdaDefinition {

        private String name;
        private String handler;
        private String codePath;
        private ApiDefinition api;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public String getCodePath() {
            return codePath;
        }

        public void setCodePath(String codePath) {
            this.codePath = codePath;
        }

        public ApiDefinition getApi() {
            return api;
        }

        public void setApi(ApiDefinition api) {
            this.api = api;
        }
    }

    public static class ApiDefinition {

        private String name;
        private String path;
        private List<String> methods;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<String> getMethods() {
            return methods;
        }

        public void setMethods(List<String> methods) {
            this.methods = methods;
        }
    }
}

