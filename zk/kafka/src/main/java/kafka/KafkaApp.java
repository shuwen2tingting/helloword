package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Hello world!
 *
 */
public class KafkaApp
{
    public static void main( String[] args )
    {
        sendKafka();
    }

    public static void sendKafka(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "47.106.86.169:9092,47.106.86.169:9093,47.106.86.169:9094");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer = new KafkaProducer<String, String>(props);

        for(int i = 0; i < 100; i++){
            producer.send(new ProducerRecord<String, String>("haha", Integer.toString(i), Integer.toString(i)));
            System.out.println(i+":------");
        }


        producer.close();
    }

}
