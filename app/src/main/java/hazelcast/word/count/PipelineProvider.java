package hazelcast.word.count;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.jet.core.ProcessorMetaSupplier;
import com.hazelcast.jet.kafka.impl.StreamKafkaP;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.StreamSource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

import static com.hazelcast.jet.pipeline.Sources.streamFromProcessorWithWatermarks;
import static hazelcast.word.count.Constants.kafkaBrokerUrl;
import static hazelcast.word.count.Constants.topicName;

public class PipelineProvider {
    private PipelineProvider() {}

    public static Pipeline getWordCountPipeline() {
        Pipeline pipeline = Pipeline.create();
        // Create a kafka stream source.
        StreamSource<String> kafkaSource =
                streamFromProcessorWithWatermarks(
                        "KafkaSource",
                        true,
                        w -> ProcessorMetaSupplier.of(2,
                                StreamKafkaP.processorSupplier(getKafkaConsumerProps(), Arrays.asList(topicName),
                                        (FunctionEx<ConsumerRecord<String, String>, String>) r -> r.value(), w)));
        pipeline.readFrom(kafkaSource) // We read from a Kafka topic
                .withoutTimestamps()
                // We use map transform and pass a function which counts the number of words.
                .map(sentence -> sentence.split(" ").length).setLocalParallelism(2)
                // The output of the resulting map will be logged.
                .writeTo(Sinks.logger());

        return pipeline;
    }

    private static Properties getKafkaConsumerProps() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", kafkaBrokerUrl);
        props.setProperty("key.deserializer", StringDeserializer.class.getCanonicalName());
        props.setProperty("value.deserializer", StringDeserializer.class.getCanonicalName());
        props.setProperty("auto.offset.reset", "earliest");
        return props;
    }
}
