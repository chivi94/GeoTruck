package com.ivan.geotruck.shared_preferences;

/**
 * Clase que guarda las constantes de las SharedPreferences de GeoTruck, principalmente, para mantener la sesión iniciada.
 */
public class SharedPreferencesConstants {
    //Nombres de las claves de SharedPreferences
    /**
     * Constante que almacena el nombre de usuario.
     */
    public static final String USERNAME = "com.ivan.geotruck.username";
    /**
     * Constante que almacena la contreña del usuario
     */
    public static final String PASSWORD = "com.ivan.geotruck.user_password";
    /**
     * Constante que indica si la sesión se debe mantener o no guardada
     */
    public static final String HOLD_SESSION = "com.ivan.geotruck.hold_session";
    /**
     * Constante que almacena el nombre de las SharedPreferences correspondientes a GeoTruck
     */
    public static final String PREFERENCES_NAME = "com.ivan.geotruck.preferences";
    public static boolean autoLogin = false;
}
