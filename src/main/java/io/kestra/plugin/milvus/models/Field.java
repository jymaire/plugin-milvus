package io.kestra.plugin.milvus.models;

import io.milvus.v2.common.DataType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Field in Milvus
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @NotNull
    private String name;
    private DataType dataType;
    private boolean primaryKey;
    private boolean autoId;

    //TODO : complete with all necessary fields https://milvus.io/api-reference/java/v2.4.x/v2/Collections/createCollection.md

}
