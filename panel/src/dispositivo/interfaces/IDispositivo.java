package dispositivo.interfaces;

import java.util.Collection;

import dispositivo.api.mqtt.FuncionPublisher_APIMQTT;

public interface IDispositivo {

	public String getId();
	
	public IDispositivo iniciar();
	public IDispositivo detener();
	
	public IDispositivo addFuncion(IFuncion f);
	public IFuncion getFuncion(String funcionId);
	public Collection<IFuncion> getFunciones();

	// TO-DO: Ejercicio 5 - Funciones habilitar/deshabilitar
	public IDispositivo habilitar();
	public IDispositivo deshabilitar();

	public FuncionPublisher_APIMQTT getFuncionPublisher();

	public void processInfoMessage(String payload);

	public void processTrafficMessage(String payload);
}