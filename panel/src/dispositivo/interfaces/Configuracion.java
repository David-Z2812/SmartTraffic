package dispositivo.interfaces;

public interface Configuracion {

    public static final String M2MIO_USERNAME = "<m2m.io username>";
    public static final String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";

	public static final String TOPIC_BASE = "es/upv/pros/tatami/smartcities/traffic/PTPaterna";

	public static final String TOPIC_REGISTRO = TOPIC_BASE + "/road/%s/dispositivo/%s";
	public static final String TOPIC_INFO = TOPIC_BASE + "/road/%s/info";
	public static final String TOPIC_TRAFFIC = TOPIC_BASE + "road/%s/traffic";
	public static final String TOPIC_SIGNALS = TOPIC_BASE + "/road/%s/signals";

	}
	
