package io.kestra.plugin.milvus.client;

import io.kestra.core.runners.RunContext;
import io.milvus.v2.client.ConnectConfig;
import org.slf4j.Logger;

import static io.kestra.plugin.milvus.AbstractMilvusTask.CLUSTER_ENDPOINT;

public class MilvusClient {


    public static final String DEFAULT_CLUSTER_ENDPOINT = "http://localhost:19530";

    private final String apiToken;
    private final Logger logger;

    public MilvusClient(RunContext runContext, String apiToken, String baseUrl) {
        this.apiToken = apiToken;
        this.logger = runContext.logger();

        String resolvedBaseUrl = (baseUrl == null || baseUrl.isBlank())
            ? CLUSTER_ENDPOINT
            : baseUrl;

        ConnectConfig.builder()
            .uri(resolvedBaseUrl)
            .token(apiToken)
            .build();
    }
}
