package dispositivo.componentes;

import dispositivo.api.mqtt.FuncionPublisher_APIMQTT;
import dispositivo.interfaces.FuncionStatus;
import dispositivo.interfaces.IFuncion;
import dispositivo.utils.MySimpleLogger;
import org.json.JSONObject;

public class PanelInformativo extends Dispositivo {
    private final String roadSegment;
    String mqttBrokerURL = "tcp://tambori.dsic.upv.es:10083"; // Replace with your broker URL

    public PanelInformativo(String deviceId, String roadSegment) {
        super(deviceId);
        this.roadSegment = roadSegment;
        this.initialize(this.mqttBrokerURL);
        // this.getFuncion("f1").apagar(); // Default to OFF
        // this.getFuncion("f2").apagar(); // Default to OFF
        // this.getFuncion("f3").apagar(); // Default to OFF
        // this.publishFunctionState("f1");
        // this.publishFunctionState("f2");
        // this.publishFunctionState("f3");
    }

    public void initialize(String mqttBrokerURL) {
        this.apiPublisherMQTT = FuncionPublisher_APIMQTT.build(this, mqttBrokerURL);
        this.apiPublisherMQTT.connect();

        this.subscribeToTopics();

        this.addFuncion(Funcion.build("f1", FuncionStatus.OFF, apiPublisherMQTT));
        this.addFuncion(Funcion.build("f2", FuncionStatus.OFF, apiPublisherMQTT));
        this.addFuncion(Funcion.build("f3", FuncionStatus.OFF, apiPublisherMQTT));
       

        MySimpleLogger.info("PanelInformativo-"+deviceId, "Initialized and subscribed.");
    }

    private void subscribeToTopics() {
        String infoTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.roadSegment + "/info";
        String trafficTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.roadSegment + "/traffic";

        apiPublisherMQTT.subscribe(infoTopic);
        apiPublisherMQTT.subscribe(trafficTopic);

        MySimpleLogger.info("PanelInformativo-"+deviceId, "Subscribed to topics: " + infoTopic + ", " + trafficTopic);
    }

    @Override
    public void processInfoMessage(String payload) {
    try {
        JSONObject data = new JSONObject(payload);

        String status = data.optString("status", "");
        String event = data.optString("event", "");

        // Handle accident alert (f2)
        if ("accident".equals(event)) {
            this.getFuncion("f2").parpadear();
            this.publishFunctionState("f2"); // Publish updated state
        } else {
            this.getFuncion("f2").apagar();
            this.publishFunctionState("f2"); // Publish updated state
        }

        // Handle congestion status (f1)
        if ("Free_Flow".equals(status) || "Mostly_Free_Flow".equals(status)) {
            this.getFuncion("f1").apagar();
            this.publishFunctionState("f1"); // Publish updated state
        } else if ("Limited_Manouvers".equals(status)) {
            this.getFuncion("f1").parpadear();
            this.publishFunctionState("f1"); // Publish updated state
        } else if ("No_Manouvers".equals(status) || "Collapsed".equals(status)) {
            this.getFuncion("f1").encender();
            this.publishFunctionState("f1"); // Publish updated state
        }

       

    } catch (Exception e) {
        MySimpleLogger.error("PanelInformativo-"+deviceId, "Error processing info message: " + e.getMessage());
    }
    }


    @Override
    public void processTrafficMessage(String payload) {
    try {
        JSONObject data = new JSONObject(payload).getJSONObject("msg");

        String vehicleRole = data.optString("vehicle-role", "");
        String action = data.optString("action", "");
        int position = data.optInt("position", 0);

        MySimpleLogger.debug("PanelInformativo-"+deviceId, "Vehicle action: " + action + ", role: " + vehicleRole + ", position: " + position);

        // Handle f3 based on special vehicle action and position
        if ("VEHICLE_IN".equals(action) && ("Ambulance".equals(vehicleRole) || "Police".equals(vehicleRole))) {
            if (Math.abs(position) < 200) {
                this.getFuncion("f3").parpadear(); // BLINK if vehicle is within 200m
                MySimpleLogger.info("PanelInformativo-"+deviceId, "f3 updated to BLINK for special vehicle within 200m.");
            } else {
                this.getFuncion("f3").encender(); // Turn ON if vehicle is beyond 200m
                MySimpleLogger.info("PanelInformativo-"+deviceId, "f3 updated to ON for special vehicle beyond 200m.");
            }
        } else if ("VEHICLE_OUT".equals(action)) {
            this.getFuncion("f3").apagar(); // Turn OFF when vehicle exits
            MySimpleLogger.info("PanelInformativo-"+deviceId, "f3 updated to OFF as vehicle exited.");
        } else {
            // Handle other cases or default behavior
            this.getFuncion("f3").apagar(); // Default to OFF
            MySimpleLogger.info("PanelInformativo-"+deviceId, "f3 defaulted to OFF for unhandled action or role.");
        }

        // Publish updated state for f3
        this.publishFunctionState("f3");

        } catch (Exception e) {
            MySimpleLogger.error("PanelInformativo-"+deviceId, "Error processing traffic message: " + e.getMessage());
        }
    }
    
    

    private void publishFunctionState(String functionId) {
    try {
        IFuncion funcion = this.getFuncion(functionId);
        if (funcion != null) {
            String estado = funcion.getStatus().name(); // Get the current status
            String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.roadSegment + "/panel/" + functionId;

            this.apiPublisherMQTT.publish_status(topic, estado); // Publish the status
            MySimpleLogger.info("PanelInformativo-"+deviceId, "Published state for " + functionId + " to topic " + topic + ": " + estado);
        } else {
            MySimpleLogger.warn("PanelInformativo-"+deviceId, "Function " + functionId + " does not exist.");
        }
    } catch (Exception e) {
        MySimpleLogger.error("PanelInformativo-"+deviceId, "Error publishing state for " + functionId + ": " + e.getMessage());
    }

    
    }
}