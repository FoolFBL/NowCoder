package com.kong.newcoder;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shijiu
 */
@SpringBootTest
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private KafkaConsumer kafkaConsumer;
    @Test
    public void testKafka()  {
        kafkaProducer.sendMsg("test","您好");
        kafkaProducer.sendMsg("test","在吗");
        try {
            Thread.sleep(1000*10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMsg(String topic , String content){
        kafkaTemplate.send(topic,content);
    }

}

@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handleMsg(ConsumerRecord record){
        System.out.println("==================================================================");
        System.out.println(record.value()+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
    }

}
