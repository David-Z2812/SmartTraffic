package dispositivo.componentes;

import dispositivo.api.mqtt.FuncionPublisher_APIMQTT;
import dispositivo.interfaces.FuncionStatus;
import dispositivo.interfaces.IFuncion;
import dispositivo.utils.MySimpleLogger;


public class Funcion implements IFuncion {
	
	protected String id = null;
	protected FuncionStatus initialStatus = null;
	protected FuncionStatus status = null;

	private String loggerId = null;
	private FuncionPublisher_APIMQTT publisher; 
	
	public static Funcion build(String id, FuncionPublisher_APIMQTT publisher) {
		return new Funcion(id, FuncionStatus.OFF, publisher);
	}
	
	public static Funcion build(String id, FuncionStatus initialStatus, FuncionPublisher_APIMQTT publisher) {
		return new Funcion(id, initialStatus, publisher);
	}

	protected Funcion(String id, FuncionStatus initialStatus, FuncionPublisher_APIMQTT publisher) {
		this.id = id;
		this.initialStatus = initialStatus;
		this.loggerId = "Funcion " + id;
		this.publisher = publisher;
	}
	
	
	@Override
	public String getId() {
		return this.id;
	}
		
	@Override
	public IFuncion encender() {
		if ( !this.estaHabilitada() ) {
			MySimpleLogger.warn(this.loggerId, "Funcion deshabilitada, no se puede modificar");
			return this;
		}
		MySimpleLogger.info(this.loggerId, "==> Encender");
		this.setStatus(FuncionStatus.ON);
		// TO-DO: Ejercicio 9 - Publicar estado de Funcion (publishStatusChange())
		
		// this.publishStatusChange(); // Publish state change
		return this;
	}

	@Override
	public IFuncion apagar() {
		if ( !this.estaHabilitada() ) {
			MySimpleLogger.warn(this.loggerId, "Funcion deshabilitada, no se puede modificar");
			return this;
		}
		MySimpleLogger.info(this.loggerId, "==> Apagar");
		this.setStatus(FuncionStatus.OFF);
		// TO-DO: Ejercicio 9 - Publicar estado de Funcion (publishStatusChange())
		// this.publishStatusChange();
		return this;
	}

	@Override
	public IFuncion parpadear() {
		if ( !this.estaHabilitada() ) {
			MySimpleLogger.warn(this.loggerId, "Funcion deshabilitada, no se puede modificar");
			return this;
		}
		MySimpleLogger.info(this.loggerId, "==> Parpadear");
		this.setStatus(FuncionStatus.BLINK);
		// TO-DO: Ejercicio 9 - Publicar estado de Funcion (publishStatusChange())
		// this.publishStatusChange();
		return this;
	}
	
	protected IFuncion _putIntoInitialStatus() {
		switch (this.initialStatus) {
		case ON:
			this.encender();
			break;
		case OFF:
			this.apagar();
			break;
		case BLINK:
			this.parpadear();
			break;

		default:
			break;
		}
		
		return this;

	}

	@Override
	public FuncionStatus getStatus() {
		return this.status;
	}
	
	protected IFuncion setStatus(FuncionStatus status) {
		this.status = status;
		return this;
	}
	
	@Override
	public IFuncion iniciar() {
		this._putIntoInitialStatus();
		return this;
	}
	
	@Override
	public IFuncion detener() {
		return this;
	}
		
	public boolean estaHabilitada() {
		return Dispositivo.habilitada; // TO-DO: Ejercicio 4
	}

	protected void publishStatusChange() {
        if (this.publisher != null) {
            this.publisher.publish_status(this.getId(), this.status.name());
        } else {
            MySimpleLogger.warn(this.loggerId, "No publisher available to notify status change");
        }
    }

}
