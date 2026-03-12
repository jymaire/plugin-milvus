package io.kestra.plugin.milvus;

import io.kestra.core.models.tasks.Task;

public abstract class AbstractMilvusTask extends Task {
    public static final String CLUSTER_ENDPOINT = "http://localhost:19530";
}
