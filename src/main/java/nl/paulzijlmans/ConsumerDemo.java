package nl.paulzijlmans;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerDemo {

  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9093");
    properties.put("security.protocol", "PLAINTEXT");
    properties.put("retries", 3);

    properties.put("group.id", "test-consumer-group");
    properties.put("enable.auto.commit", "false");
    properties.put("auto.offset.reset", "earliest");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties,
        new StringDeserializer(), new StringDeserializer());

    consumer.subscribe(List.of("page-visits"));

    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        processRecords(records);

        consumer.commitAsync();
      }
    } catch (Exception e) {
      consumer.close();
    }
  }

  private static void processRecords(ConsumerRecords<String, String> records) throws IOException {
    for (ConsumerRecord<String, String> record : records) {

      System.out.printf("Partition = %s, offset = %d, key = %s\n",
          record.partition(),
          record.offset(),
          record.key());
      PageView pageView = parse(record.value());
      System.out.println(pageView);
    }
  }

  private static PageView parse(String pageViewStr) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(pageViewStr, PageView.class);
  }
}
