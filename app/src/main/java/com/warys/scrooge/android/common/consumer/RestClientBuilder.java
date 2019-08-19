package com.warys.scrooge.android.common.consumer;

public class RestClientBuilder {
    private String baseUrl;

    public RestClientBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RestClient build() {
        return new RestClient(baseUrl);
    }

    public RestClient buildInitalized() {
        final RestClient restClient = build();
        restClient.init();
        return restClient;
    }
}
