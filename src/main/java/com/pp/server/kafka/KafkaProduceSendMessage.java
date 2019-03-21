package com.pp.server.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class KafkaProduceSendMessage {
    Properties properties = new Properties();
    KafkaProducer<Integer, String> producer = null;
    private String topic = "";

    public KafkaProduceSendMessage(String connStr, String topic)
    {
        properties.put("bootstrap.servers", connStr);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<Integer, String>(properties);
        this.topic = topic;
    }

    public ProducerRecord<Integer, String> assemableRecord(Integer key, String value)
    {
        ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(topic, key, value);
        return record;
    }

    public RecordMetadata syncSndMessage(ProducerRecord<Integer, String> record) throws ExecutionException, InterruptedException {
        RecordMetadata metadata = null;
        if (null == record)
            throw new RuntimeException("param record is null");

        if (null == producer)
            throw  new RuntimeException("Kafka Producer is null");

        try {
            metadata = producer.send(record).get();
        } catch (InterruptedException xe) {
            xe.printStackTrace();
            throw  xe;
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw  e;
        }
        return  metadata;
    }

    public static void main(String args[])
    {
        KafkaProduceSendMessage kafkaProduceSendMessage = new KafkaProduceSendMessage("127.0.0.1:9092", "test");
        AtomicInteger keyValue = new AtomicInteger(0);
        while(true){
            StringBuilder messageValue = new StringBuilder();
            messageValue.append("producer");
            messageValue.append(keyValue.intValue());
            ProducerRecord<Integer, String> record = kafkaProduceSendMessage.assemableRecord(keyValue.incrementAndGet(), messageValue.toString());
            try {
                RecordMetadata metadata = kafkaProduceSendMessage.syncSndMessage(record);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("key:%d value:%s", keyValue.intValue(), messageValue.toString());
            System.out.println("");
        }
    }
}
