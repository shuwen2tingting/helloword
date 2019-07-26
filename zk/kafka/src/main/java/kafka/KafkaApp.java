package kafka;

import org.apache.kafka.clients.producer.*;

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
        try{
            Properties props = new Properties();
            props.put("bootstrap.servers", "47.106.86.169:9092,47.106.86.169:9093,47.106.86.169:9094");
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1000);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String,String> producer = new KafkaProducer<String, String>(props);

            for(int i = 0; i < 100; i++){
                producer.send(new ProducerRecord<String, String>("ting1", Integer.toString(i), Integer.toString(i)),
                        new Callback() {
                            public void onCompletion(RecordMetadata metadata, Exception e) {
                                System.out.println(metadata+":------");
                                if(e != null)
                                    e.printStackTrace();
                                System.out.println("The offset of the record we just sent is: " + metadata.offset());
                            }
                        });
                System.out.println(i+":------");
            }

            producer.close();
            System.out.println(":------end");
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
