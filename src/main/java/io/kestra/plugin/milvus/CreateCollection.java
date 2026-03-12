package io.kestra.plugin.milvus;


import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.runners.RunContext;
import io.kestra.plugin.milvus.models.Field;
import io.kestra.plugin.milvus.models.IndexParameter;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Schema(
    title = "Create a new collection in Milvus"

)
@Builder
@ToString
public class CreateCollection implements RunnableTask<CreateCollection.Output> {

    @Schema(title = "Fields of the collection")
    @NotNull
    private Property<List<Field>> fields;

    @Schema(title = "Parameters of the index")
    private Property<List<IndexParameter>> indexParameters;

    @Schema(title = "Name of the collection to create")
    @NotNull
    private Property<String> collectionName;

    @NotNull
    private Property<ConnectConfig> connectConfig;

    @Override
    public Output run(RunContext runContext) throws Exception {
        ConnectConfig rConnectConfig = runContext.render(this.connectConfig).as(ConnectConfig.class).orElseThrow(() -> new IllegalArgumentException("Connection configuration to Milvus is required"));
        MilvusClientV2 client = new MilvusClientV2(rConnectConfig);

        List<Field> rFields = runContext.render(this.fields).asList(Field.class);
        List<IndexParameter> rIndexParameters = runContext.render(this.indexParameters).asList(IndexParameter.class);
        String rCollectionName = runContext.render(this.collectionName).as(String.class).orElseThrow(() -> new IllegalArgumentException("Collection name is required"));

        // Schema creation
        CreateCollectionReq.CollectionSchema schema = MilvusClientV2.CreateSchema();

        rFields.forEach(field ->
            schema.addField(AddFieldReq.builder()
                .fieldName(field.getName())
                .dataType(field.getDataType())
                .isPrimaryKey(field.isPrimaryKey())
                .autoID(field.isAutoId())
                .build())
        );


        // Index parameters definition
        List<IndexParam> indexParams = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rIndexParameters)) {
            rIndexParameters.forEach(param -> indexParams.add(IndexParam.builder()
                .fieldName(param.getFieldName())
                .indexType(param.getIndexType())
                .metricType(param.getMetricType())
                .build()));
        }

        // collection creation
        CreateCollectionReq.CreateCollectionReqBuilder collectionToCreateBuilder = CreateCollectionReq.builder()
            .collectionName(rCollectionName)
            .collectionSchema(schema)
            .indexParams(indexParams);


        CreateCollectionReq collectionToCreate = collectionToCreateBuilder
            .build();

        client.createCollection(collectionToCreate);

        // Check whether collection is loaded or not
        GetLoadStateReq getLoadStateReq = GetLoadStateReq.builder()
            .collectionName(rCollectionName)
            .build();

        return new Output(client.getLoadState(getLoadStateReq));
    }


    @Builder
    @Getter
    public static class Output implements io.kestra.core.models.tasks.Output {
        private boolean isCollectionLoaded;
    }
}
