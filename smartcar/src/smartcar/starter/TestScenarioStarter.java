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
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import simulator.impl.Simulator;

public class TestScenarioStarter {

	public static void main(String[] args) {

		int SIM_STEP_MS = 3000; // Simulation step time in milliseconds

		// Sportscar Route
		Route route1 = new Route();
		route1.addRouteFragment(ESmartroadSegments.R1s1.toString(), ESmartroadSegments.R1s1.getPoint(0), ESmartroadSegments.R1s1.getPoint(5));
		route1.addRouteFragment(ESmartroadSegments.R1s2a.toString(), ESmartroadSegments.R1s2a.getPoint(0), ESmartroadSegments.R1s2a.getPoint(2));
		

		// Schoolbus Route
		Route route2 = new Route();
		route2.addRouteFragment(ESmartroadSegments.R1s3.toString(), ESmartroadSegments.R1s3.getPoint(0), ESmartroadSegments.R1s3.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s4a.toString(), ESmartroadSegments.R1s4a.getPoint(0), ESmartroadSegments.R1s4a.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R1s5.toString(), ESmartroadSegments.R1s5.getPoint(0), ESmartroadSegments.R1s5.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s6a.toString(), ESmartroadSegments.R1s6a.getPoint(0), ESmartroadSegments.R1s6a.getPoint(3));
		route2.addRouteFragment(ESmartroadSegments.R1s7.toString(), ESmartroadSegments.R1s7.getPoint(0), ESmartroadSegments.R1s7.getPoint(6));
		route2.addRouteFragment(ESmartroadSegments.R1s8a.toString(), ESmartroadSegments.R1s8a.getPoint(0), ESmartroadSegments.R1s8a.getPoint(2));
		route2.addRouteFragment(ESmartroadSegments.R1s8d.toString(), ESmartroadSegments.R1s8d.getPoint(0), ESmartroadSegments.R1s8d.getPoint(1));

		// Ambulance Route: starts at base (R1s6a,960) and goes to accident (R1s2a,286) 
		Route route3 = new Route();
		route3.addRouteFragment(ESmartroadSegments.R1s6a.toString(), ESmartroadSegments.R1s6a.getPoint(2), ESmartroadSegments.R1s6a.getPoint(3));
		route3.addRouteFragment(ESmartroadSegments.R1s5.toString(), ESmartroadSegments.R1s5.getPoint(0), ESmartroadSegments.R1s5.getPoint(6));
		route3.addRouteFragment(ESmartroadSegments.R1s4a.toString(), ESmartroadSegments.R1s4a.getPoint(0), ESmartroadSegments.R1s4a.getPoint(2));
		route3.addRouteFragment(ESmartroadSegments.R1s3.toString(), ESmartroadSegments.R1s3.getPoint(0), ESmartroadSegments.R1s3.getPoint(6));
		route3.addRouteFragment(ESmartroadSegments.R1s2a.toString(), ESmartroadSegments.R1s2a.getPoint(0), ESmartroadSegments.R1s2a.getPoint(1));
		route3.addRouteFragment(ESmartroadSegments.R1s2a.toString(), ESmartroadSegments.R1s2a.getPoint(1), 286);

		Simulator simulator = new Simulator();

		// Create SmartCar instances
		SmartCar sportscar = new SmartCar("Aston Martin", ESmartcarTypes.Automobile, ESmartcarRole.PrivateUsage, 100, route1);
		SmartCar schoolbus = new SmartCar("Schoolbus", ESmartcarTypes.Bus, ESmartcarRole.PublicTransport, 80, route2);
		SmartCar ambulance = new SmartCar("Ambulance", ESmartcarTypes.Van, ESmartcarRole.Ambulance, 100, route3);

		// Create a SmartCity instance
		SmartCity scity1 = new SmartCity("VLC.net", ambulance);

		// Create road segments and informational panels
		Set<String> createdSegments = new HashSet<>();
		List<Route> routes = Arrays.asList(route1, route2, route3);

		for (Route route : routes) {
			for (IRouteFragment fragment : route.getAllRouteFragments()) {
				String segment = fragment.getStartPoint().getRoadSegment();
				if (!createdSegments.contains(segment)) {
					createdSegments.add(segment);
					new SmartRoad(segment);
					String panelId = "panel-" + segment; // Unique panel identifier
					new PanelInformativo(panelId, segment);
				}
			}
		}

		// Initial delay before starting the simulation
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// Handle the exception
		}

		int step_cnt = 0;

		// Start driving the sportscar and schoolbus
		sportscar.startDriving();
		schoolbus.startDriving();

		// Simulation loop
		while(true) {
			try {
				simulator.Step(step_cnt);
				Thread.sleep(SIM_STEP_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt(); // Restore interrupted status
			}
			if(step_cnt == 6) {
				sportscar.notifyIncident("accident"); // Simulate an accident at R1s2a,286 
				sportscar.StopDriving();
			}
			step_cnt++;
		}

	}

}
