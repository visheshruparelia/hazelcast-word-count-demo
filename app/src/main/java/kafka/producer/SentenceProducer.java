package kafka.producer;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SentenceProducer implements Runnable {
    private static final Faker faker = new Faker();
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public SentenceProducer(KafkaProducer kafkaProducer, String topic) {
        this.producer = kafkaProducer;
        this.topic = topic;
    }

    @Override
    public void run() {
        System.out.println("producing");
        while (true) {
            try {
                producer.send(new ProducerRecord<>(topic, faker.shakespeare().asYouLikeItQuote()));
            } catch (Exception e) {
                System.out.println("Exception occurred " + e.getStackTrace());
            }
        }
    }



}
