package com.pp.server.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerTest {
    private KafkaConsumer<Integer, String> consumer;

    private String topic = "test";

    public KafkaConsumerTest(String kafaServer)
    {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafaServer);
        properties.put("group.id", "test2");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        //properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", IntegerDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<Integer, String>(properties);
    }

    public void consumer() throws Exception {
        if (null == consumer)
            throw new Exception("consumer is null");

        consumer.subscribe(Arrays.asList(topic));

        while(true)
        {
            ConsumerRecords<Integer, String> records = consumer.poll(10);
            for (ConsumerRecord<Integer, String> record:records) {
                System.out.printf("offset %d key=%s value=%s", record.offset(), record.key(), record.value());
                System.out.println();
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String args[]) throws Exception {
        KafkaConsumerTest consumerTest = new KafkaConsumerTest("127.0.0.1:9092");
        consumerTest.consumer();
    }
}
