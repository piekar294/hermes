package pl.allegro.tech.hermes.tracker.elasticsearch;

import com.codahale.metrics.Timer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import pl.allegro.tech.hermes.tracker.QueueCommitter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ElasticsearchQueueCommitter extends QueueCommitter<XContentBuilder> {

    private final IndexFactory indexFactory;
    private final Client client;
    private final String typeName;

    public ElasticsearchQueueCommitter(BlockingQueue<XContentBuilder> queue,
                                       Timer timer,
                                       IndexFactory indexFactory,
                                       String typeName,
                                       Client client) {
        super(queue, timer);
        this.indexFactory = indexFactory;
        this.typeName = typeName;
        this.client = client;
    }

    @Override
    protected void processBatch(List<XContentBuilder> batch) throws ExecutionException, InterruptedException {
        BulkRequestBuilder bulk = client.prepareBulk();
        batch.forEach(entry -> bulk.add(client.prepareIndex(indexFactory.createIndex(), typeName).setSource(entry)));
        bulk.execute().get();
    }

    public static void scheduleCommitAtFixedRate(BlockingQueue<XContentBuilder> queue, IndexFactory indexFactory, String typeName, Client client,
                                                 Timer timer, int interval) {
        ElasticsearchQueueCommitter committer = new ElasticsearchQueueCommitter(queue, timer, indexFactory, typeName, client);
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("elasticsearch-queue-committer-%d").build();
        newSingleThreadScheduledExecutor(factory).scheduleAtFixedRate(committer, interval, interval, MILLISECONDS);
    }
}
