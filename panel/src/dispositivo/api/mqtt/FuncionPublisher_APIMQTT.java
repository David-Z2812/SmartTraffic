package dispositivo.api.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import dispositivo.interfaces.IDispositivo;
import dispositivo.utils.MySimpleLogger;

import java.util.UUID;

public class FuncionPublisher_APIMQTT implements MqttCallback {
    private MqttClient myClient;
    private MqttConnectOptions connOpt;
    private final String loggerId;
    private final IDispositivo dispositivo;
    private String mqttBroker;
    private String mqttUser;
    private String mqttPassword;

    public static FuncionPublisher_APIMQTT build(IDispositivo dispositivo, String brokerURL) {
        FuncionPublisher_APIMQTT api = new FuncionPublisher_APIMQTT(dispositivo);
        api.setBroker(brokerURL);
        return api;
    }

    private FuncionPublisher_APIMQTT(IDispositivo dispositivo) {
        this.dispositivo = dispositivo;
        this.loggerId = dispositivo.getId() + "-apiMQTT";
    }

    public void setBroker(String mqttBrokerURL) {
        this.mqttBroker = mqttBrokerURL;
    }

    public void setBrokerCredentials(String user, String password) {
        this.mqttUser = user;
        this.mqttPassword = password;
    }

    @Override
    public void connectionLost(Throwable cause) {
        MySimpleLogger.error(loggerId, "Connection lost! Attempting to reconnect...");
        try {
            connect();
        } catch (Exception e) {
            MySimpleLogger.error(loggerId, "Reconnection attempt failed: " + e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        MySimpleLogger.info(loggerId, "Message delivered to topic: " + token.getTopics()[0]);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            String payload = new String(message.getPayload()); // Convert payload to string
            MySimpleLogger.debug(loggerId, "Message arrived on topic: " + topic + ", payload: " + payload);

            // Route to the appropriate processing method based on the topic
            if (topic.contains("/info")) {
                MySimpleLogger.debug(loggerId, "Processing info message.");
                dispositivo.processInfoMessage(payload);
            } else if (topic.contains("/traffic")) {
                MySimpleLogger.debug(loggerId, "Processing traffic message.");
                dispositivo.processTrafficMessage(payload);
            } else {
                MySimpleLogger.warn(loggerId, "Unknown topic: " + topic + ". Payload: " + payload);
            }
        } catch (Exception e) {
            MySimpleLogger.error(loggerId, "Error handling message on topic: " + topic + ". Exception: " + e.getMessage());
        }
    }

    public void connect() {
        try {
            if (myClient != null && myClient.isConnected()) {
                MySimpleLogger.info(loggerId, "Client is already connected.");
                return;
            }

            connOpt = new MqttConnectOptions();
            connOpt.setCleanSession(true);
            connOpt.setKeepAliveInterval(120);

            if (mqttUser != null && mqttPassword != null) {
                connOpt.setUserName(mqttUser);
                connOpt.setPassword(mqttPassword.toCharArray());
            }

            myClient = new MqttClient(mqttBroker, dispositivo.getId() + UUID.randomUUID(), new MqttDefaultFilePersistence("/tmp"));
            myClient.setCallback(this);
            myClient.setTimeToWait(1000); // Set timeToWait to 1 second (1000 milliseconds)

            myClient.connect(connOpt);

            MySimpleLogger.info(loggerId, "Connected to broker: " + mqttBroker);

            // Re-subscribe to topics after connecting
            subscribeToTopics();

        } catch (MqttException e) {
            MySimpleLogger.error(loggerId, "Error connecting to broker: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (myClient != null && myClient.isConnected()) {
            try {
                myClient.disconnect();
                MySimpleLogger.info(loggerId, "Disconnected from broker.");
            } catch (MqttException e) {
                MySimpleLogger.error(loggerId, "Error disconnecting from broker: " + e.getMessage());
            }
        }
    }

    public void iniciar() {
        MySimpleLogger.info(loggerId, "Starting FuncionPublisher_APIMQTT...");
        connect();
    }

    public void detener() {
        MySimpleLogger.info(loggerId, "Stopping FuncionPublisher_APIMQTT...");
        disconnect();
    }

    public void publish_status(String topic, String message) {
        try {
            if (myClient == null || !myClient.isConnected()) {
                MySimpleLogger.warn(loggerId, "Client is not connected. Attempting to reconnect...");
                connect(); // Ensure the client is connected
            }

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1); // Set QoS level

            myClient.publish(topic, mqttMessage);

            MySimpleLogger.info(loggerId, "Message published to topic: " + topic + " with message: " + message);

        } catch (MqttException e) {
            MySimpleLogger.error(loggerId, "Error publishing message to topic: " + topic + ". Exception: " + e.getMessage());
            // Attempt to reconnect if the client is disconnected
            if (!myClient.isConnected()) {
                MySimpleLogger.warn(loggerId, "Client disconnected. Attempting to reconnect...");
                connect();
            }
        }
    }

    public void subscribe(String topic) {
        try {
            if (myClient == null || !myClient.isConnected()) {
                connect();
            }

            myClient.subscribe(topic, 1);
            MySimpleLogger.info(loggerId, "Subscribed to topic: " + topic);

        } catch (MqttException e) {
            MySimpleLogger.error(loggerId, "Error subscribing to topic: " + e.getMessage());
        }
    }

    private void subscribeToTopics() {
        // Add your topics here
        String infoTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + dispositivo.getId() + "/info";
        String trafficTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + dispositivo.getId() + "/traffic";

        subscribe(infoTopic);
        subscribe(trafficTopic);
    }
}
