package com.ivan.geotruck.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ivan.geotruck.activities.UserActivity;
import com.ivan.geotruck.adapter.BusinessListArrayAdapter;
import com.ivan.geotruck.adapter.EmployeeListArrayAdapter;
import com.ivan.geotruck.password_encrypter.PasswordEncrypter;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Inicia una nueva instancia para poder acceder a los métodos que establecen la conexión con la base de datos de GeoTruck, alojada en Parse.
 */
public class DatabaseConnect {

    //region Consultas
    private ParseQuery<ParseObject> businessQuery = ParseQuery.getQuery("Empresa");
    private ParseQuery<ParseObject> employeeQuery = ParseQuery.getQuery("Empleado");
    private ParseQuery<ParseObject> bossQuery = ParseQuery.getQuery("Empleado");
    //endregion

    //region Variables usadas para el logeo
    //Variable que se usa para iniciar sesión en la aplicación
    private static ParseObject currentUser;
    //Se usará para iniciar la sesión con un tipo de usuario u otro.
    private Intent logIn = new Intent();
    //endregion

    //region Parte de conexión con la BBDD

    /**
     * Método usado para conectar la aplicación a la BBDD
     *
     * @param context = Contexto de la aplicación
     */
    public static void connectToDatabase(Context context) {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "BpQcU0xldkiPDjDFp6YOls9o0TmN5LVIy0FdrccG",
                "IADD4vD9sTX1CaFAIANWivqFfZFZuh2d4Y8SSY5v");
    }
    //endregion conexión

    //region Parte registro de usuarios
    //region Métodos de registro de usuarios

    /**
     * Método que comprueba si el nombre de usuario existe en la BBDD.
     *
     * @param userName Nombre de usuario.
     * @return Devuelve true si el usuario existe.
     */
    public boolean checkUserName(String userName) {
        try {
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (ParseObject employee : listOfEmployees) {
                String currentUserName = employee.get("Usuario").toString();
                if (currentUserName.equalsIgnoreCase(userName)) {
                    exist = true;
                }
            }
            //Si no coincide, el empleado no existe
            return !exist;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Método que comprueba si el email existe en la BBDD.
     *
     * @param email El email del usuario a registrar.
     * @return Devuelve true si el email existe.
     */
    public boolean checkUserEmail(String email) {
        try {
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (ParseObject employee : listOfEmployees) {
                String currentUserEmail = employee.get("Email").toString();
                if (currentUserEmail.equalsIgnoreCase(email)) {
                    exist = true;
                }
            }
            //Si no coincide, el email no existe
            return !exist;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método que guarda al empleado/jefe en la BBDD.
     *
     * @param name     Nombre del usuario.
     * @param phone    Teléfono del usuario.
     * @param userName Nick de inicio de sesión del usuario.
     * @param password Contraseña del usuario.
     * @param email    Email del usuario.
     * @param business Empresa a la que pertenece el usuario.
     * @param type     Tipo del usuario. Se debe pasar un 0 para los jefes y un 1 para los empleados.
     * @param context  Contexto del Activity desde donde se llama al método.
     * @return Devuelve true si el registro se ha realizado correctamente.
     */
    public boolean createEmployee(String name, String phone, String userName, String password, String email, final String business, int type, Context context) {

        try {
            ParseObject newEmployee = new ParseObject("Empleado");
            switch (type) {
                //Para el jefe...
                case 0:
                    newEmployee.put("Jefe", true);
                    break;
                //Para el empleado...
                case 1:
                    newEmployee.put("Jefe", false);
                    break;
                default:
                    break;
            }
            newEmployee.put("Conectado", false);
            //Poner la primera letra en mayúscula
            String newName = name.substring(0, 1).toUpperCase() + name.substring(1);
            newEmployee.put("Nombre", newName);
            newEmployee.put("Telefono", phone);
            newEmployee.put("Password", PasswordEncrypter.EncryptText(password));
            newEmployee.put("Email", email);
            newEmployee.put("Usuario", userName);
            //Doy una localización inicial
            ParseGeoPoint newPosition = new ParseGeoPoint();
            newPosition.setLatitude(0.0);
            newPosition.setLongitude(0.0);
            newEmployee.put("Localizacion", newPosition);
            //Comprobamos la empresa del usuario
            String businessUpperCase = business.toUpperCase();
            businessQuery.whereEqualTo("Nombre", businessUpperCase);
            ParseObject currentBusiness = businessQuery.getFirst();
            newEmployee.put("Empresa", currentBusiness);
            newEmployee.save();
            ((Activity) context).finish();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    //endregion
    //region Métodos para el registro de empresas

    /**
     * Método que guarda la empresa en la BBDD, en el registro del JEFE.
     *
     * @param business Nombre de la empresa a registrar.
     * @return Devuelve true si el nombre de la empresa ya está registrado.
     */
    public boolean checkBusinessName(final String business) {
        try {
            List<ParseObject> listOfBusiness = businessQuery.find();
            boolean exist = false;
            for (ParseObject parseObject : listOfBusiness) {
                String currentBusiness = parseObject.get("Nombre").toString();
                if (business.equalsIgnoreCase(currentBusiness)) {
                    exist = true;
                }
            }
            //Si no coincide, la empresa no existe
            return !exist;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método que crea una empresa y la guarda en la BBDD.
     *
     * @param business Nombre de la empresa que se va a registrar.
     * @return Devuelve true si la empresa se ha registrado correctamente.
     */
    public boolean createBusiness(String business) {
        //El constructor permite elegir la clase(tabla) en la que queremos guardar los datos
        try {
            ParseObject newBusiness = new ParseObject("Empresa");
            newBusiness.put("Nombre", business.toUpperCase());
            newBusiness.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion

    /**
     * Método que obtiene las empresas registradas.Recibe un ArrayList de tipo String y lo llena con los nombres de las empresas guardadas, devolviendo un Adapter para la lista de selección.
     *
     * @param context Contexto del Activity desde donde se llama al método.
     * @return Devuelve un ArrayAdapter del tipo especificado con las empresas existentes en la base de datos.
     */
    public BusinessListArrayAdapter catchBusiness(Context context) {
        ArrayList<String> finalBusiness = new ArrayList<String>();
        try {
            List<ParseObject> empresas = businessQuery.find();
            for (ParseObject empresa : empresas) {
                finalBusiness.add(empresa.get("Nombre").toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.sort(finalBusiness);
        return new BusinessListArrayAdapter
                (context, android.R.layout.simple_list_item_1, finalBusiness);
    }
    //endregion

    //region Logeo de usuarios

    /**
     * Método que conecta al usuario a la aplicación.
     *
     * @param sessionId Nick de inicio de sesión del usuario.
     * @param password  Contraseña de inicio de sesión del usuario.
     * @param context   Contexto del Activity desde donde se llama al método.
     * @return Devuelve true si la conexión se realiza correctamente.
     */
    public boolean connectToGeoTruck(final String sessionId, final String password, final Context context) {
        try {
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (int i = 0; i < listOfEmployees.size(); i++) {
                String currentUserName = ((ParseObject) listOfEmployees.get(i)).get("Usuario").toString();
                String currentUserPassword = ((ParseObject) listOfEmployees.get(i)).get("Password").toString();
                if (sessionId.equals(currentUserName) && PasswordEncrypter.EncryptText(password).equals(currentUserPassword)) {
                    exist = true;
                    currentUser = listOfEmployees.get(i);
                }
            }
            if (exist) {
                //Si el usuario existe, inicio sesión
                currentUser.put("Conectado", true);
                currentUser.saveInBackground();
                if (Boolean.parseBoolean(String.valueOf(currentUser.get("Jefe")))) {
                    //Se conecta como jefe
                    logIn.putExtra("Tipo", 0);
                } else {
                    //Se conecta como conductor
                    logIn.putExtra("Tipo", 1);
                }
                logIn.putExtra("Usuario", sessionId);
                logIn.setClass(context, UserActivity.class);
                context.startActivity(logIn);
                ((Activity) context).finish();
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método que carga una lista con los empleados conectados de la empresa del jefe que se está conectando.
     *
     * @param context  Contexto del Activity desde donde se llama al metodo.
     * @param bossName Nombre del jefe que está conectado en el momento.
     * @return Devuelve un ArrayAdapter del tipo especificado, el cual contiene los empleados conectados al sistema que pertenecen a la misma empresa que el jefe que se encuentra conectado.
     */
    public EmployeeListArrayAdapter catchEmployees(Context context, String bossName) {
        ArrayList<ParseObject> employeeList = new ArrayList<ParseObject>();
        try {
            //Recuperamos el jefe que se está conectando al sistema para comprobar que los empleados que tenemos que cargar son los de su empresa
            ParseObject boss = getBoss(bossName);
            if (boss != null) {
                //Que sea de su empresa
                employeeQuery.whereEqualTo("Empresa", boss.get("Empresa"));
                //Que no sean el jefe (Que no coincida su nombre de usuario)
                employeeQuery.whereNotEqualTo("Usuario", bossName);
                //Que no sean jefes, porque estos no son transportistas
                employeeQuery.whereNotEqualTo("Jefe", boss.get("Jefe"));
                //Que estén conectados
                employeeQuery.whereEqualTo("Conectado", true);
                List<ParseObject> employees = employeeQuery.find();
                for (ParseObject employee : employees) {
                    employeeList.add(employee);
                }
                if (employeeList.size() > 0) {
                    Collections.sort(employeeList, new Comparator<ParseObject>() {
                        @Override
                        public int compare(ParseObject employee1, ParseObject employee2) {
                            //Cojo el nombre del primer empleado a comparar y su primer cáracter lo paso a mayúsculas
                            String name1 = employee1.get("Nombre").toString();
                            String nameUpperCase1 = name1.substring(0, 1).toUpperCase() + name1.substring(1);

                            //Cojo el nombre del segundo empleado a comparar y su primer cáracter lo paso a mayúsculas
                            String name2 = employee2.get("Nombre").toString();
                            String nameUpperCase2 = name2.substring(0, 1).toUpperCase() + name2.substring(1);
                            return nameUpperCase1.compareTo(nameUpperCase2);
                        }
                    });
                    return new EmployeeListArrayAdapter
                            (context, android.R.layout.simple_list_item_1, employeeList);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Método que recupera el jefe de la BBDD en función del usuario introducido en la zona de login.
     *
     * @param bossUserName Nombre de usuario perteneciente al jefe.
     * @return Devuelve el objeto correspondiente al nombre pasado como parámetro.
     */
    private ParseObject getBoss(String bossUserName) {
        try {
            bossQuery.whereEqualTo("Usuario", bossUserName);
            return bossQuery.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que coge los datos del conductor seleccionado para mostrarlos en la vista del detalle.
     *
     * @param employeeName Nombre del empleado del cual se van a recuperar datos.
     * @param bossName     Nombre del jefe que tiene al empleado que se va a buscar en su empresa.
     * @return Devuelve el objeto que coincide con los datos pasados como parámetros.
     */
    public ParseObject getEmployeeData(String employeeName, String bossName) {
        ParseObject selectEmployee = null;
        try {
            ParseObject boss = getBoss(bossName);
            if (boss != null) {
                employeeQuery.whereEqualTo("Nombre", employeeName);
                selectEmployee = employeeQuery.getFirst();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return selectEmployee;
    }

    /**
     * Método que desconecta al usuario de la aplicación.
     *
     * @param user Nick del usuario conectado.
     * @return Devuelve true si se consigue desconectar satisfactoriamente al usuario.
     */
    public boolean logOut(String user) {
        employeeQuery.whereEqualTo("Usuario", user);
        try {
            ParseObject logoutEmployee = employeeQuery.getFirst();
            logoutEmployee.put("Conectado", false);
            logoutEmployee.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    //endregion

    //region Guardar y coger posición del usuario

    /**
     * Método que guarda la posición del empleado en la BBDD.
     *
     * @param lat       Latitud de la posición actual del empleado.
     * @param longitude Longitud de la posición actual del empleado.
     * @return Devuelve true si se consigue guardar correctamente la posición del usuario.
     */
    public boolean saveEmployeePosition(double lat, double longitude, String currentUser) {
        employeeQuery.whereEqualTo("Usuario", currentUser);
        try {
            ParseObject currentEmployee = employeeQuery.getFirst();
            ParseGeoPoint geoPoint = (ParseGeoPoint) currentEmployee.get("Localizacion");
            geoPoint.setLatitude(lat);
            geoPoint.setLongitude(longitude);
            currentEmployee.put("Localizacion", geoPoint);
            currentEmployee.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Método que recupera la posición del empleado de la BBDD.
     *
     * @param currentUser Usuario seleccionado del cual se va a recuperar la posición.
     * @return Devuelve un Geopoint con la posición del usuario.
     */
    public ParseGeoPoint getCurrentEmployeePosition(String currentUser) {
        ParseGeoPoint currentPosition = null;
        employeeQuery.whereEqualTo("Nombre", currentUser);
        try {
            ParseObject employee = employeeQuery.getFirst();
            currentPosition = (ParseGeoPoint) employee.get("Localizacion");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentPosition;
    }
    //endregion

    //region Parte de ajustes

    /**
     * Método que recupera la información del usuario actual buscando por su nombre de usuario.
     *
     * @param userName Nombre de usuario del cual se va a recuperar la información.
     * @return Devuelve un objeto que contiene la información del usuario pasado como parámetro.
     */
    public ParseObject catchEmployeeInfo(String userName) {
        ParseObject currentEmployee = null;
        employeeQuery.whereEqualTo("Usuario", userName);
        try {
            currentEmployee = employeeQuery.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return currentEmployee;
    }

    /**
     * Método que recupera la empresa del empleado actual.
     *
     * @param business Empresa del empleado seleccionado.
     * @return Devuelve el nombre de la empresa a la que pertenece el usuario.
     */
    public String catchEmployeeBusiness(ParseObject business) {
        String currentBusiness = null;
        try {
            currentBusiness = business.fetchIfNeeded().getString("Nombre");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentBusiness;
    }

    /**
     * Método que comprueba si la contraseña que se introduce en el Fragment de confirmación de contraseña corresponde con la de usuario actual.
     *
     * @param userName Nombre de usuario.
     * @param password Contraseña del usuario.
     * @param context  Contexto del Activity desde el que método es llamado.
     * @return Devuelve true si la contraseña introducida es la del usuario actual.
     */
    public boolean checkEmployeePassword(final String userName, final String password, final Context context) {
        try {
            String encryptedPassword = PasswordEncrypter.EncryptText(password);
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (int i = 0; i < listOfEmployees.size(); i++) {
                String currentUserName = ((ParseObject) listOfEmployees.get(i)).get("Usuario").toString();
                String currentUserPassword = ((ParseObject) listOfEmployees.get(i)).get("Password").toString();
                if (userName.equals(currentUserName) && encryptedPassword.equals(currentUserPassword)) {
                    exist = true;
                    currentUser = listOfEmployees.get(i);
                }
            }
            return exist;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método que cambia el nombre de usuario del usuario actual.
     *
     * @param newUsername Nuevo nombre de usuario.
     * @param oldUsername Antiguo nombre de usuario
     * @return Devuelve true si se consigue cambiar correctamente el nombre de usuario.
     */
    public boolean changeUsername(String newUsername, String oldUsername) {
        try {
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (ParseObject employee : listOfEmployees) {
                String currentUserName = employee.get("Usuario").toString();
                if (currentUserName.equalsIgnoreCase(newUsername)) {
                    exist = true;
                }
            }
            //Si el usuario introducido no existe
            if (!exist) {
                employeeQuery.whereEqualTo("Usuario", oldUsername);
                ParseObject currentUser = null;
                currentUser = employeeQuery.getFirst();
                currentUser.put("Usuario", newUsername);
                currentUser.save();
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método que cambia la contraseña del usuario actual.
     *
     * @param userName    Nombre de usuario.
     * @param newPassword Nueva contraseña del usuario.
     * @return Devuelve true si se cambia correctamente la contraseña.
     */
    public boolean changeUserPassword(String userName, String newPassword) {
        employeeQuery.whereEqualTo("Usuario", userName);
        ParseObject currentUser = null;
        try {
            currentUser = employeeQuery.getFirst();
            currentUser.put("Password", PasswordEncrypter.EncryptText(newPassword));
            currentUser.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método que cambia el email del usuario actual.
     *
     * @param userName Nombre de usuario.
     * @param newEmail Nuevo E-mail del usuario.
     * @return Devuelve true si se cambia correctamente el E-mail.
     */
    public boolean changeUserEmail(String userName, String newEmail) {
        try {
            List<ParseObject> listOfEmployees = employeeQuery.find();
            boolean exist = false;
            for (ParseObject employee : listOfEmployees) {
                String currentUserEmail = employee.get("Email").toString();
                if (currentUserEmail.equalsIgnoreCase(newEmail)) {
                    exist = true;
                }
            }
            //Si el email introducido no existe
            if (!exist) {
                employeeQuery.whereEqualTo("Usuario", userName);
                ParseObject currentUser = null;
                currentUser = employeeQuery.getFirst();
                currentUser.put("Email", newEmail);
                currentUser.save();
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return false;
    }
    //endregion
}
