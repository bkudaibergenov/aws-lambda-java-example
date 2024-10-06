package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.commons.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.commons.lambda.handler.LambdaFunctionHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LambdaPlainText implements LambdaFunctionHandler {

//    @LambdaFunction(contentType = "text/plain")
//    public String getPlainText() {
//        return "Plain text example";
//    }

    private static final HttpClient httpClient = HttpClient.newHttpClient();


    @LambdaFunction(contentType = "text/plain")
    public String fetchJsonFromUrl() throws Exception {
        try {
            String url = "https://earth.gov/ghgcenter/custom-interfaces/noaa-gggrn-ghg-concentrations/data/processed/co2/flask/surface/co2_wlg_surface-flask_1_ccgg_event.json";

            System.out.println("Fetching data from " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            System.out.println("Sending request to " + url);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response received from " + url);

            return response.body();
        } catch (Exception e) {
            System.out.println("Error occurred while fetching data from URL: " + e.getMessage());
            throw e;
        }
    }

}
