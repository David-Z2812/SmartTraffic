package smartcar.impl;

import smartcar.types.ESmartcarTypes;
import smartcar.types.ESmartcarRole;
import ina.vehicle.navigation.components.Navigator;
import ina.vehicle.navigation.components.RoadPoint;
import ina.vehicle.navigation.interfaces.INavigator;
import ina.vehicle.navigation.interfaces.IRoadPoint;
import ina.vehicle.navigation.interfaces.IRoute;
import ina.vehicle.navigation.utils.MySimpleLogger;
import smartroad.types.ESmartroadSegments;
import ina.vehicle.navigation.types.ENavigatorStatus;;

public class SmartCar {

	protected String smartCarID = null;
	protected RoadPlace rp = null;	// simula la ubicación actual del vehículo
	protected ESmartcarTypes type = null;
	protected ESmartcarRole role = null;
	protected SmartCar_RoadInfoSubscriber subscriber = null;
	protected SmartCar_MQTTPublisher publisher = null;
	protected int maxCarSpeed = 0;
	protected int maxRoadSpeed = 0;
	protected int current_vehicle_speed = 0;
	protected INavigator navigator = null;
	protected IRoute route = null;
	protected IRoadPoint roadpoint = null;
	protected boolean IsDriving = false;
	protected SmartCar_StepSubscriber stepSubscriber = null;
	protected int SpeedLimit = 100;		//maxima velocidad permitida en la carretera
	
	public SmartCar(String id, ESmartcarTypes type, ESmartcarRole role, int maxcarspeed, IRoute route) {
		
		this.navigator = new Navigator(id);

		this.route = route;
		MySimpleLogger.info("main", "Setting Route " + this.route);
		this.navigator.setRoute(this.route);

		this.setSmartCarID(id);
		this.type = type;
		this.role = role;
		this.maxCarSpeed = maxcarspeed;
		this.current_vehicle_speed = maxcarspeed;

		this.roadpoint = navigator.getCurrentPosition();
		MySimpleLogger.info("main", "Current Position: " + this.roadpoint);
		MySimpleLogger.info("main", "Remaining Route: " + this.navigator.getRoute());

		
		
		this.subscriber = new SmartCar_RoadInfoSubscriber(this);
		this.subscriber.connect();
		this.stepSubscriber = new SmartCar_StepSubscriber(this);
		this.stepSubscriber.connect();
		String Step_topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/step";
		this.stepSubscriber.subscribe(Step_topic);
		this.publisher = new SmartCar_MQTTPublisher(id);
		this.publisher.connect();

		// initial road
		this.rp = new RoadPlace(this.roadpoint.getRoadSegment(), this.roadpoint.getPosition());
		this.moveCar(this.roadpoint.getRoadSegment(), this.roadpoint.getPosition());

	}
	

	public void startDriving() {
		this.navigator.startRouting();
		MySimpleLogger.info("main", "Navigator Status: " + this.navigator.getNavigatorStatus().getName());
		this.IsDriving = true;
	}
	
	public void setSmartCarID(String smartCarID) {
		this.smartCarID = smartCarID;
	}
	
	public String getSmartCarID() {
		return this.smartCarID;
	}

	public RoadPlace getCurrentPlace() {
		return this.rp;
	}

	public void changeKm(int km) {
		this.getCurrentPlace().setKm(km);
	}
	
	public void moveCar(String road, int km) {
		this.rp.setRoad(road);
		this.rp.setKm(km);
		String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + road + "/info";
		this.subscriber.subscribe(topic);
		this.publisher.trafficUpdate(this.getSmartCarID(), this.role, this.rp, "VEHICLE_IN");
		MySimpleLogger.info("main", "traffic updates done");

		this.maxRoadSpeed = ESmartroadSegments.valueOf(this.rp.getRoad()).getMaxSpeed();
		if (this.role == ESmartcarRole.Ambulance || this.role == ESmartcarRole.Police || this.role == ESmartcarRole.Firetruck || this.role == ESmartcarRole.Military) {
			// no speed limit for emergency vehicles
		}
		else {
		int newSpeed = Math.min(Math.min(this.maxRoadSpeed, this.maxCarSpeed),this.SpeedLimit);	// calcula la velocidad máxima permitida
		if (newSpeed != this.current_vehicle_speed) {
			MySimpleLogger.info("main", "Speed changed from " + this.current_vehicle_speed + " to " + newSpeed);
			this.changeSpeed(newSpeed);
		}
		}
		
	}
	
	public void notifyIncident(String incidentType) {
		if ( this.publisher == null )
			return;
		this.publisher.alert(this.getSmartCarID(), incidentType, this.getCurrentPlace());
	}

	public void changeSpeed(int speed) {
		this.current_vehicle_speed = speed;
	}

	public void Drive(int step) {
		if (this.navigator.getNavigatorStatus() == ENavigatorStatus.ROUTING) {
			this.navigator.move(step, this.current_vehicle_speed);
			// check if the vehicle has changed road
			if (navigator.getCurrentPosition().getRoadSegment() != this.rp.getRoad()) {
				MySimpleLogger.info("main", "Vehicle changed road from " + this.rp.getRoad() + " to " + navigator.getCurrentPosition().getRoadSegment());
				this.publisher.trafficUpdate(this.getSmartCarID(), this.role, this.rp, "VEHICLE_OUT");
			}
			this.moveCar(navigator.getCurrentPosition().getRoadSegment(), navigator.getCurrentPosition().getPosition());
			// MySimpleLogger.trace("time", " <- t"+ time_cnt);
			MySimpleLogger.info("main", "Current Position: " + navigator.getCurrentPosition());
			MySimpleLogger.info("main", " Remaining Route: " + navigator.getRoute());
		} else if (this.navigator.getNavigatorStatus() == ENavigatorStatus.REACHED_DESTINATION) {
			MySimpleLogger.info("main", "Route finished.");
			this.StopDriving();
			this.publisher.trafficUpdate(this.getSmartCarID(), this.role, this.rp, "VEHICLE_IN");
		} else{
			MySimpleLogger.info("main", "Navigator Status: " + navigator.getNavigatorStatus().getName());
			this.publisher.trafficUpdate(this.getSmartCarID(), this.role, this.rp, "VEHICLE_IN");
		}
	}

	public void StopDriving() {
		this.navigator.stopRouting();
		MySimpleLogger.info("main", "Navigator Status: " + navigator.getNavigatorStatus().getName());
		this.IsDriving = false;
	}

	public boolean isDriving() {
		return this.IsDriving;
	}

	public void setSpeedLimit(int speedLimit) {
		this.SpeedLimit = speedLimit;
        // Implementation for setting the speed limit

    }

}
