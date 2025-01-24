package smartcar.impl;

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

import smartcar.types.ESmartcarRole;

public class SmartCar_MQTTPublisher implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	String BROKER_URL = "tcp://tambori.dsic.upv.es:10083";
	String M2MIO_USERNAME = "<m2m.io username>";
	String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";

	String id = null;
	
	public SmartCar_MQTTPublisher(String smartCarID) {
		this.id = smartCarID;
	}
	
	protected void _debug(String message) {
		System.out.println("(SmartCar: " + this.id + ") " + message);
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
		String clientID = this.id;
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
	
	
	public void alert(String smartCarID, String notificationType, RoadPlace place) {

		String myTopic =  "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + place.getRoad() + "/alerts";
		MqttTopic topic = myClient.getTopic(myTopic);


		// publish incident
		JSONObject pubMsg = new JSONObject();
		try {
			pubMsg.put("vehicle", smartCarID);
			pubMsg.put("event", notificationType);
			pubMsg.put("road", place.getRoad());
			pubMsg.put("kp", place.getKm());
	   		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
   		int pubQoS = 0;
		MqttMessage message = new MqttMessage(pubMsg.toString().getBytes());
    	message.setQos(pubQoS);
    	message.setRetained(false);

    	// Publish the message
    	this._debug("Publishing to topic \"" + topic + "\" qos " + pubQoS);
    	MqttDeliveryToken token = null;
    	try {
    		// publish message to broker
			token = topic.publish(message);
			this._debug(pubMsg.toString());
	    	// Wait until the message has been delivered to the broker
			token.waitForCompletion();
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    		    	

	}
	
	public void trafficUpdate(String smartCarID, ESmartcarRole vehicleRole, RoadPlace place, String vehicleState) {
		String myTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + place.getRoad() + "/traffic";
		MqttTopic topic = myClient.getTopic(myTopic);

		JSONObject pubMsg = new JSONObject();
		JSONObject msg = new JSONObject();
		try {
			msg.put("action", vehicleState);
			msg.put("vehicle-role", vehicleRole);
			msg.put("vehicle-id", smartCarID);
			msg.put("road-segment", place.getRoad());
			msg.put("position", place.getKm());
			
			pubMsg.put("msg", msg);
			pubMsg.put("id", "MSG_" + System.currentTimeMillis());
			pubMsg.put("type", "TRAFFIC");
			pubMsg.put("timestamp", System.currentTimeMillis());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		int pubQoS = 0;
		MqttMessage message = new MqttMessage(pubMsg.toString().getBytes());
		message.setQos(pubQoS);
		message.setRetained(false);

		this._debug("Publishing to topic \"" + topic + "\" qos " + pubQoS);
		MqttDeliveryToken token = null;
		try {
			token = topic.publish(message);
			this._debug(pubMsg.toString());
			token.waitForCompletion();
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}