package io.kestra.milvus;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.models.property.Property;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.plugin.milvus.CreateCollection;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import jakarta.inject.Inject;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;

@KestraTest
class CreateCollectionTest {
    @Inject
    private RunContextFactory runContextFactory;

    private MockWebServer mockWebServer;

    @Mock
    private MilvusClientV2 milvusClientV2;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void pluginWithoutConfiguration_shouldThrowException() {

        CreateCollection task = CreateCollection.builder().build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            task.run(runContextFactory.of(Map.of()));

        });
    }

    // @Test // Test not done, error during mocking of Milvius server
    // Other solution : mount a Milvus container with TestContainers for example
    void pluginShouldCreateCollection() throws Exception {
        RunContext runContext = runContextFactory.of();

        CreateCollection task = CreateCollection.builder().connectConfig(Property.<ConnectConfig>builder()
                .value(ConnectConfig.builder().uri("http://host.com").build())
            .build()).build();
        task.run(runContext);
    }
}
