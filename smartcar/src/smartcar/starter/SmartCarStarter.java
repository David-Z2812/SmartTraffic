package smartcar.starter;

import smartcar.impl.SmartCar;
import smartcar.types.ESmartcarRole;
import smartcar.types.ESmartcarTypes;
import smartcity.impl.SmartCity;
import ina.vehicle.navigation.components.Route;
import ina.vehicle.navigation.interfaces.IRouteFragment;
import ina.vehicle.navigation.utils.MySimpleLogger;
import smartroad.types.ESmartroadSegments;
import java.io.IOException;
import smartroad.impl.SmartRoad;
import dispositivo.componentes.PanelInformativo;
import simulator.impl.Simulator;;



public class SmartCarStarter {

	public static void main(String[] args) {

		int SIM_STEP_MS = 3000; //simulation step time in milliseconds

		Route route1 = new Route();
		// route1.addRouteFragment(ESmartroadSegments.R1s1.toString(), ESmartroadSegments.R1s1.getPoint(0), ESmartroadSegments.R1s1.getPoint(5));
		// route1.addRouteFragment(ESmartroadSegments.R1s2a.toString(), ESmartroadSegments.R1s2a.getPoint(0), ESmartroadSegments.R1s2a.getPoint(2));
		// route1.addRouteFragment(ESmartroadSegments.R5s1.toString(), ESmartroadSegments.R5s1.getPoint(0), ESmartroadSegments.R5s1.getPoint(2));
		// route1.addRouteFragment(ESmartroadSegments.R1s3.toString(), ESmartroadSegments.R1s3.getPoint(0), ESmartroadSegments.R1s3.getPoint(6));
		// route1.addRouteFragment(ESmartroadSegments.R1s4a.toString(), ESmartroadSegments.R1s4a.getPoint(0), ESmartroadSegments.R1s4a.getPoint(2));
		// route1.addRouteFragment(ESmartroadSegments.R1s5.toString(), ESmartroadSegments.R1s5.getPoint(0), ESmartroadSegments.R1s5.getPoint(6));
		route1.addRouteFragment(ESmartroadSegments.R1s6a.toString(), ESmartroadSegments.R1s6a.getPoint(0), ESmartroadSegments.R1s6a.getPoint(3));
		route1.addRouteFragment(ESmartroadSegments.R1s7.toString(), ESmartroadSegments.R1s7.getPoint(0), ESmartroadSegments.R1s7.getPoint(6));
		route1.addRouteFragment(ESmartroadSegments.R1s8a.toString(), ESmartroadSegments.R1s8a.getPoint(0), ESmartroadSegments.R1s8a.getPoint(2));
		route1.addRouteFragment(ESmartroadSegments.R1s8d.toString(), ESmartroadSegments.R1s8d.getPoint(0), ESmartroadSegments.R1s8d.getPoint(1));

		Route route2 = new Route();
		route2.addRouteFragment(ESmartroadSegments.R2s1.toString(), ESmartroadSegments.R2s1.getPoint(0), ESmartroadSegments.R2s1.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R2s2.toString(), ESmartroadSegments.R2s2.getPoint(0), ESmartroadSegments.R2s2.getPoint(1));
		route2.addRouteFragment(ESmartroadSegments.R1s2a.toString(), ESmartroadSegments.R1s2a.getPoint(0), ESmartroadSegments.R1s2a.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R1s3.toString(), ESmartroadSegments.R1s3.getPoint(0), ESmartroadSegments.R1s3.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s4a.toString(), ESmartroadSegments.R1s4a.getPoint(0), ESmartroadSegments.R1s4a.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R1s5.toString(), ESmartroadSegments.R1s5.getPoint(0), ESmartroadSegments.R1s5.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s6a.toString(), ESmartroadSegments.R1s6a.getPoint(0), ESmartroadSegments.R1s6a.getPoint(3));
		route2.addRouteFragment(ESmartroadSegments.R1s7.toString(), ESmartroadSegments.R1s7.getPoint(0), ESmartroadSegments.R1s7.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s8a.toString(), ESmartroadSegments.R1s8a.getPoint(0), ESmartroadSegments.R1s8a.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R1s8d.toString(), ESmartroadSegments.R1s8d.getPoint(0), ESmartroadSegments.R1s8d.getPoint(1));

		Route route3 = new Route();
		route3.addRouteFragment(ESmartroadSegments.R3s1.toString(), ESmartroadSegments.R3s1.getPoint(0), ESmartroadSegments.R3s1.getPoint(2));
		route3.addRouteFragment(ESmartroadSegments.R1s2d.toString(), ESmartroadSegments.R1s2d.getPoint(0), ESmartroadSegments.R1s2d.getPoint(1));
		route3.addRouteFragment(ESmartroadSegments.R1s3.toString(), ESmartroadSegments.R1s3.getPoint(0), ESmartroadSegments.R1s3.getPoint(6));
		route3.addRouteFragment(ESmartroadSegments.R1s4a.toString(), ESmartroadSegments.R1s4a.getPoint(0), ESmartroadSegments.R1s4a.getPoint(2));
		route3.addRouteFragment(ESmartroadSegments.R1s5.toString(), ESmartroadSegments.R1s5.getPoint(0), ESmartroadSegments.R1s5.getPoint(6));
		route3.addRouteFragment(ESmartroadSegments.R1s6a.toString(), ESmartroadSegments.R1s6a.getPoint(0), ESmartroadSegments.R1s6a.getPoint(3));
		route3.addRouteFragment(ESmartroadSegments.R1s7.toString(), ESmartroadSegments.R1s7.getPoint(0), ESmartroadSegments.R1s7.getPoint(6));
		route3.addRouteFragment(ESmartroadSegments.R1s8a.toString(), ESmartroadSegments.R1s8a.getPoint(0), ESmartroadSegments.R1s8a.getPoint(2));
		route3.addRouteFragment(ESmartroadSegments.R1s8d.toString(), ESmartroadSegments.R1s8d.getPoint(0), ESmartroadSegments.R1s8d.getPoint(1));
		route3.addRouteFragment(ESmartroadSegments.R1s1.toString(), ESmartroadSegments.R1s1.getPoint(0), ESmartroadSegments.R1s1.getPoint(5));

		Simulator simulator = new Simulator();

		SmartCity scity1 = new SmartCity("VLC.net");

		SmartCar sc1 = new SmartCar("Car001", ESmartcarTypes.Automobile, ESmartcarRole.PrivateUsage, 100, route1);
		// SmartCar sc2 = new SmartCar("Car002", ESmartroadSegments.R2s1.toString(), 20, ESmartcarTypes.Bus, ESmartcarRole.PublicTransport, 40, route2);
		// SmartCar sc3 = new SmartCar("Car003", ESmartroadSegments.R3s1.toString(), 30, ESmartcarTypes.Motorcycle, ESmartcarRole.PrivateUsage, 60, route3);
		
		String segment = null;	
		for (IRouteFragment fragment : route1.getAllRouteFragments()) {
			segment = fragment.getStartPoint().getRoadSegment();
			new SmartRoad(segment);
			String panelId = "panel-" + segment; // Unique panel identifier
			new PanelInformativo(panelId, segment);
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}



		int step_cnt = 0;

		while(true) {
			try {
				simulator.Step(step_cnt);
				Thread.sleep(SIM_STEP_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt(); // Restore interrupted status
			}
			if(step_cnt >= 2) sc1.notifyIncident("accident");
			step_cnt++;
		}

	}

}
