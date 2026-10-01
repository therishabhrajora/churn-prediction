package com.churn.kafka;
import org.springframework.beans.factory.annotation.Value; import org.springframework.kafka.core.KafkaTemplate; import org.springframework.stereotype.Component;
@Component public class KafkaProducer { private final KafkaTemplate<String,Object> template; private final String topic; public KafkaProducer(KafkaTemplate<String,Object> t,@Value("${app.kafka.prediction-request-topic}") String topic){this.template=t;this.topic=topic;} public void publish(PredictionRequestedEvent e){template.send(topic,String.valueOf(e.customerId()),e);} }
