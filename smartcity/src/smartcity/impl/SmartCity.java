package smartcity.impl;

import smartcar.impl.SmartCar;

public class SmartCity {
	
	protected SmartCity_RoadIncidentsSubscriber subscriber  = null;
	protected String id = null;
	protected SmartCar ambulance = null;
	
	public SmartCity(String id, SmartCar ambulance) {
		this.setId(id);
		this.ambulance = ambulance;
		this.subscriber = new SmartCity_RoadIncidentsSubscriber(this);
		this.subscriber.connect();
		this.subscriber.subscribe("es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/+/alerts");
		

	}

	 public String getId() {
		return id;
	}
	 
	 public void setId(String id) {
		this.id = id;
	}
	 
	
}
