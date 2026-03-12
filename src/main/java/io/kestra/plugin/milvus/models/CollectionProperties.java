package io.kestra.plugin.milvus.models;

import io.milvus.v2.common.ConsistencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the properties for a collection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionProperties {
    // Number of shards for the collection, default value at 1
    private Integer numShards = 1;
    // Store various properties in key value mode. Example : Constant.MMAP_ENABLED, "false
    private Map<String, String> properties = new HashMap<>();

    private ConsistencyLevel consistencyLevel = ConsistencyLevel.BOUNDED;
}
