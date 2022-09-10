package kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hazelcast.word.count.Constants.kafkaBrokerUrl;
import static hazelcast.word.count.Constants.topicName;

public class ProducerTrigger {
    private static final String BOOTSTRAP_SERVERS = kafkaBrokerUrl;
    private static final String AUTO_OFFSET_RESET = "earliest";
    private static final String TOPIC = topicName;
    private final ExecutorService executorService;
    private final int producerCount;
    public ProducerTrigger(int producerCount) {
        this.executorService = Executors.newFixedThreadPool(producerCount);
        this.producerCount = producerCount;
    }

    public static void main(String[] args) {
        int producerCount = args.length > 0 ? Integer.parseInt(args[0]) : 2;
        ProducerTrigger producerTrigger =
                new ProducerTrigger(producerCount);
        producerTrigger.startProducing();
    }

    private void startProducing() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.setProperty("auto.offset.reset", AUTO_OFFSET_RESET);
        System.out.println("Total producer count: " + producerCount);
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties, new StringSerializer(), new StringSerializer());
        for (int i = 0; i < producerCount; i++) {
            executorService.submit(new SentenceProducer(producer, TOPIC));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executorService.shutdownNow()));
    }
}
