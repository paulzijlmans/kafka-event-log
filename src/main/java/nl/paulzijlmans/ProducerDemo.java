package nl.paulzijlmans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo {

  public static void main(String[] args) throws Exception {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9093");
    properties.put("security.protocol", "PLAINTEXT");
    properties.put("retries", 3);

    Producer<String, String> producer = new KafkaProducer<>(properties, new StringSerializer(),
        new StringSerializer());

    for (int i = 0; i < 100; i++) {
      PageView pageView = generateRecord();
      String pageViewStr = toJsonString(pageView);
      System.out.println(pageView);
      sleep(500);

      RecordMetadata metadata = producer
          .send(new ProducerRecord<>("page-visits", pageView.getUsername(), pageViewStr))
          .get();

      System.out.printf("Key = %s; partition = %s; offset = %s%n",
          pageView.getUsername(),
          metadata.partition(),
          metadata.offset());
      System.out.println();
    }

    producer.flush();
    producer.close();
  }

  private static String toJsonString(PageView pageView) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(pageView);
  }

  private static PageView generateRecord() {
    Faker faker = new Faker();

    PageView pageView = new PageView();
    pageView.setUsername(randomName());
    pageView.setBrowser(faker.internet().userAgentAny());
    pageView.setPage(randomPage());
    pageView.setViewDate(new Date());

    return pageView;
  }

  private static String randomName() {
    return randomSelect(
        new String[]{"robbin", "joe", "daisy", "lisa", "laurette", "raphael", "elda", "eric"});
  }

  private static String randomPage() {
    return randomSelect(new String[]{"/home", "/user/profile", "/orders", "/search", "/purchase"});
  }

  private static String randomSelect(String[] arr) {
    int random = new Random().nextInt(arr.length);
    return arr[random];
  }

  private static void sleep(int ms) throws InterruptedException {
    Thread.sleep(ms);
  }
}
