package io.kestra.plugin.milvus.models;

import io.milvus.v2.common.IndexParam;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a parameter for an index
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexParameter {
    @NotNull
    private String fieldName;
    private IndexParam.IndexType indexType;
    private IndexParam.MetricType metricType;

}
