package simulator.impl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;

public class Simulator_Publisher implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;
	String clientID = null;

	String BROKER_URL = "tcp://tambori.dsic.upv.es:10083";
	String M2MIO_USERNAME = "<m2m.io username>";
	String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";
	
	public Simulator_Publisher() {
		// setup MQTT Client
        this.clientID = "Simulator.publisher";
	}
	
	protected void _debug(String message) {
		System.out.println(clientID + ": " + message);
	}
	
	@Override
	public void connectionLost(Throwable t) {
		this._debug("Connection lost!");
		// code to reconnect to the broker would go here if desired
	}

	/**
	 * 
	 * deliveryComplete
	 * This callback is invoked when a message published by this client
	 * is successfully received by the broker.
	 * 
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		//System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
	}

	/**
	 * 
	 * messageArrived
	 * This callback is invoked when a message is received on a subscribed topic.
	 * 
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
	}

	

	public void connect() {

		// setup MQTT Client
		String clientID = "Simulator.publisher";
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
		
		this._debug("Notifier Connected to " + BROKER_URL);

	}
	
	
	public void disconnect() {
		// disconnect
		try {
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void publishStep(int index) {

		String myTopic =  "es/upv/pros/tatami/smartcities/traffic/PTPaterna/step";
		MqttTopic topic = myClient.getTopic(myTopic);
		JSONObject json = new JSONObject();
		try {
			JSONObject msg = new JSONObject();
			msg.put("simulator", "PTPaterna");
			msg.put("step", index);
			
			json.put("msg", msg);
			json.put("id", "MSG_" + System.currentTimeMillis());
			json.put("type", "SIMULATOR_STEP");
			json.put("timestamp", System.currentTimeMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String infoMessage = json.toString();
   		int pubQoS = 0;
		MqttMessage message = new MqttMessage(infoMessage.getBytes());
    	message.setQos(pubQoS);
    	message.setRetained(false);

    	// Publish the message
    	this._debug("Publishing to topic \"" + topic + "\" qos " + pubQoS);
    	MqttDeliveryToken token = null;
    	try {
    		// publish message to broker
			token = topic.publish(message);
			this._debug(infoMessage);
	    	// Wait until the message has been delivered to the broker
			token.waitForCompletion();
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    		    	

	}
	
}