package smartcar.impl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import org.json.JSONException;

public class SmartCar_RoadInfoSubscriber implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER_URL = "tcp://tambori.dsic.upv.es:10083";
//	static final String M2MIO_USERNAME = "<m2m.io username>";
//	static final String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";

	SmartCar smartcar;
	
	public SmartCar_RoadInfoSubscriber(SmartCar smartcar) {
		this.smartcar = smartcar;
	}
	
	protected void _debug(String message) {
		System.out.println("(SmartCar: " + this.smartcar.getSmartCarID() + ") " + message);
	}

	
	@Override
	public void connectionLost(Throwable t) {
		this._debug("Connection lost!");
		// code to reconnect to the broker would go here if desired
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		//System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
	}

	@Override
public void messageArrived(String topic, MqttMessage message){
    String payload = new String(message.getPayload());
 
    // Debug information
    this._debug("Message arrived on topic: " + topic);
    this._debug("Payload: " + payload);
 
    // Check if the topic matches the signal topic pattern
    if (topic.matches("es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.smartcar.navigator.getCurrentPosition().getRoadSegment() + "/signals")) {
			try {
				JSONObject jsonPayload = new JSONObject(payload);
 
				// Extract the "value" field
				if (jsonPayload.has("value")) {
					int value = jsonPayload.getInt("value");
					this._debug("Extracted value: " + value);
					this.smartcar.setSpeedLimit(value);
				} else {
					this._debug("The payload does not contain the 'value' key.");
				}
			} catch (JSONException e) {
				this._debug("Failed to parse JSON payload: " + e.getMessage());
			}
   
    }
 
    }

	/**
	 * 
	 * runClient
	 * The main functionality of this simple example.
	 * Create a MQTT client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void connect() {
		// setup MQTT Client
		String clientID = this.smartcar.getSmartCarID() + ".subscriber";
		connOpt = new MqttConnectOptions();
		
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
//			connOpt.setUserName(M2MIO_USERNAME);
//			connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());
		
		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, clientID);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		this._debug("Subscriber Connected to " + BROKER_URL);

	}
	
	
	public void disconnect() {
		
		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			Thread.sleep(120000);

			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	
	public void subscribe(String myTopic) {
		
		// subscribe to topic
		try {
			int subQoS = 0;
			myClient.subscribe(myTopic, subQoS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public void unsubscribe(String myTopic) {
		
		// unsubscribe to topic
		try {
			int subQoS = 0;
			myClient.unsubscribe(myTopic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
