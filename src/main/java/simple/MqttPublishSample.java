package simple;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

    public static void main(String[] args) {

        String topic1        = "MQTT Examples";
        String content      = "{\n" +
                " \"datastreams\":[\n" +
                "       { \n" +
                "         \"id\":\"temperature\", \n" +
                "         \"datapoints\":[\n" +
                "                 {\n" +
                "                 \"at\":\"2013-04-22 22:22:22\",\n" +
                "                 \"value\": 36.5\n" +
                "                }\n" +
                "          ]\n" +
                "       }\n" +
                "    ]\n" +
                "} \n";
        int qos             = 0;
        String broker       = "tcp://183.230.40.39:6002";
        String clientId     = "";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(30);
            connOpts.setKeepAliveInterval(120);
            connOpts.setUserName("");
            connOpts.setPassword("".toCharArray());
            System.out.println("Connecting to broker: "+broker);

            System.out.println("Connected");

            sampleClient.setCallback(new PushCallback());
            byte b = 0x70;
            char chars[] = new char[]{0x01};
            StringBuffer sb = new StringBuffer();

//            sb.append((char)0x01);
//            sb.append((char)0x00);
//            sb.append((char)content.length());
//            sb.append(content);
            sb.append(chars);
            sb.append((char)content.length());
            sb.append(content);
            MqttMessage message = new MqttMessage(sb.toString().getBytes());
            System.out.println("Publishing message: ");
            message.setQos(qos);
            sampleClient.connect(connOpts);
            MqttTopic topic = sampleClient.getTopic(topic1);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//遗嘱        options.setWill(topic, "close".getBytes(), 2, true);

            sampleClient.publish("$dp", message);

            System.out.println("Message published");
            //sampleClient.disconnect();
            //System.out.println("Disconnected");
            //System.exit(0);
            while(true){
                Thread.sleep(200);
            }
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
