package CRUD;
/**
 * @author Prof Matias Garcia.
 * <p> Copyright (C) 2017 para <a href = "https://www.profmatiasgarcia.com.ar/"> www.profmatiasgarcia.com.ar </a>
 * - con licencia GNU GPL3.
 * <p> Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo los términos de la
 * Licencia Pública General de GNU según es publicada por la Free Software Foundation, 
 * bien con la versión 3 de dicha Licencia o bien (según su elección) con cualquier versión posterior. 
 * Este programa se distribuye con la esperanza de que sea útil, pero SIN NINGUNA GARANTÍA, 
 * incluso sin la garantía MERCANTIL implícita o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO
 * PARTICULAR. Véase la Licencia Pública General de GNU para más detalles.
 * Debería haber recibido una copia de la Licencia Pública General junto con este programa. 
 * Si no ha sido así ingrese a <a href = "http://www.gnu.org/licenses/"> GNU org </a>
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ConsultasPersona {

    //static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
    static final String CONTROLADOR = "org.mariadb.jdbc.Driver";
    //static final String URL_BASEDATOS = "jdbc:mysql://localhost/Empleados";
    static final String URL_BASEDATOS = "jdbc:mariadb://localhost/Empleados";
    private Connection conexion = null; // maneja la conexión
    private PreparedStatement seleccionarTodasLasPersonas = null;
    private PreparedStatement seleccionarPersonasPoridPersona = null;
    private PreparedStatement insertarNuevaPersona = null;
    private PreparedStatement borraridPersona = null;
    private PreparedStatement modificarDatosPersona = null;
    //Para poder realizar utilizando Stored Procedures
    private CallableStatement mostrarTodos = null;
    private CallableStatement filtrarXId = null;
    private CallableStatement insertarNuevo = null;

    // constructor
    public ConsultasPersona() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(CONTROLADOR);
            conexion = DriverManager.getConnection(URL_BASEDATOS, "root", "");

            // crea una consulta que selecciona todos los registros en la tabla DatosContacto
//            seleccionarTodasLasPersonas = conexion.prepareStatement("SELECT * FROM DatosContacto");
            //lo mismo pero usando Stored Procedures
            mostrarTodos = conexion.prepareCall("{call mostrarContactos()}");

            // crea una consulta que selecciona las entradas con un id específico
//            seleccionarPersonasPoridPersona = conexion.prepareStatement("SELECT * FROM DatosContacto WHERE idPersona = ?");
            filtrarXId = conexion.prepareCall("{call contactosXId(?)}");

            // crea instrucción insert para agregar una nueva entrada en la Base de Datos
//            String insertar = "INSERT INTO `DatosContacto` (`idPersona`, `Nombre`, `Apellido`, `Email`, `Telefono`) VALUES (NULL, ?, ?, ?, ?)";
//            insertarNuevaPersona = conexion.prepareStatement(insertar);
            insertarNuevo = conexion.prepareCall("{call insertarContacto(?,?,?,?)}");

            // crea instrucción delete para eliminar un registro en la Base de Datos a partir del id
            String borrar = "DELETE FROM `DatosContacto` WHERE `idPersona` = ?";
            this.borraridPersona = conexion.prepareStatement(borrar);

            // crea instrucción update para modificar un registro en la Base de
            // Datos a partir del id
            String modificar = "UPDATE `DatosContacto` SET `Nombre` = ? ,`Apellido` = ? ,`Email` = ?,`Telefono` = ? WHERE `idPersona` = ?";
            this.modificarDatosPersona = conexion.prepareStatement(modificar);
        } catch (SQLException excepcionSql) {
            // excepcionSql.printStackTrace();
            System.exit(1);
        }
    } // fin del constructor

    // Metodo que selecciona todos los registros de la tabla DatosContacto
    public List<Persona> obtenerTodasLasPersonas() {
        List<Persona> resultados = null;
        ResultSet conjuntoResultados = null;
        try {
            // executeQuery devuelve ResultSet que contiene las entradas que coinciden
//            conjuntoResultados = seleccionarTodasLasPersonas.executeQuery();
            //lo mismo pero usando un Stored Procedure
            conjuntoResultados = mostrarTodos.executeQuery();

            resultados = new ArrayList<Persona>();

            while (conjuntoResultados.next()) {
                resultados.add(new Persona(conjuntoResultados.getInt(1), conjuntoResultados.getString(2),
                        conjuntoResultados.getString(3), conjuntoResultados.getString(4),
                        conjuntoResultados.getString(5)));

            }
            if (!conjuntoResultados.first()) {
                resultados = null;
            }
        } catch (SQLException excepcionSql) {
            JOptionPane.showMessageDialog(null, "ERROR al intentar seleccionar todas las personas");
            // excepcionSql.printStackTrace();
        } finally {
            try {
                conjuntoResultados.close();
            } catch (SQLException excepcionSql) {
                excepcionSql.printStackTrace();
                close();
            }
        }

        return resultados;
    } // fin del método obtenerTodasLasPersonas

    // Metodo que selecciona una persona por su Id
    public List<Persona> obtenerPersonasPoridPersona(int id) {
        List<Persona> resultados = null;
        ResultSet conjuntoResultados = null;

        try {
//            seleccionarPersonasPoridPersona.setInt(1, id);
            filtrarXId.setInt(1, id);
            // executeQuery devuelve ResultSet que contiene las entradas que coinciden
//            conjuntoResultados = seleccionarPersonasPoridPersona.executeQuery();
            conjuntoResultados = filtrarXId.executeQuery();
            resultados = new ArrayList<Persona>();

            while (conjuntoResultados.next()) {
                resultados.add(new Persona(conjuntoResultados.getInt(1), conjuntoResultados.getString(2),
                        conjuntoResultados.getString(3), conjuntoResultados.getString(4),
                        conjuntoResultados.getString(5)));
            }
        } catch (SQLException excepcionSql) {
            // excepcionSql.printStackTrace();
        } finally {
            try {
                conjuntoResultados.close();
            } catch (SQLException excepcionSql) {
                // excepcionSql.printStackTrace();
                close();
            }
        }

        return resultados;
    } // fin del método obtenerPersonasPoridPersona

    // Metodo para agregar un registro
    public int agregarPersona(String pnombre, String papellido, String pemail, String ptelefono) {
        int resultado = 0;

        // establece los parámetros, después ejecuta insertarNuevaPersona
        try {
//            insertarNuevaPersona.setString(1, pnombre);
//            insertarNuevaPersona.setString(2, papellido);
//            insertarNuevaPersona.setString(3, pemail);
//            insertarNuevaPersona.setString(4, ptelefono);

            insertarNuevo.setString(1, pnombre);
            insertarNuevo.setString(2, papellido);
            insertarNuevo.setString(3, pemail);
            insertarNuevo.setString(4, ptelefono);

            // inserta el nuevo registro; devuelve cant de filas actualizadas
//            resultado = insertarNuevaPersona.executeUpdate();
            resultado = insertarNuevo.executeUpdate();

        } catch (SQLException excepcionSql) {
            JOptionPane.showMessageDialog(null, "ERROR No pudo insertar nuevo registro");
            excepcionSql.printStackTrace();
            // close();
        }

        return resultado;
    } // fin del método agregarPersona

    // Metodo que cierra la conexión a la Base de Datos
    public void close() {
        try {
            conexion.close();
        } catch (SQLException excepcionSql) {
            excepcionSql.printStackTrace();
        }
    } // fin del método close

    // Metodo para eliminar un registro
    public int borrarPersona(int id) {
        int resultado = 0;

        // establece los parámetros, después ejecuta insertarNuevaPersona
        try {
            borraridPersona.setInt(1, id);

            // elimina el registro; devuelve cant de filas actualizadas
            resultado = borraridPersona.executeUpdate();
        } catch (SQLException excepcionSql) {
            JOptionPane.showMessageDialog(null, "ERROR No pudo borrar registro");
            excepcionSql.printStackTrace();
            // close();
        }

        return resultado;
    } // fin del método borrarPersona

    // Metodo para modificar un registro
    public int modificarPersona(int pid, String pnombre, String papellido, String pemail, String ptelefono) {
        int resultado = 0;

        // establece los parámetros, después ejecuta insertarNuevaPersona
        try {
            modificarDatosPersona.setInt(5, pid);
            modificarDatosPersona.setString(1, pnombre);
            modificarDatosPersona.setString(2, papellido);
            modificarDatosPersona.setString(3, pemail);
            modificarDatosPersona.setString(4, ptelefono);

            // ejecuta el update de modificasion
            resultado = modificarDatosPersona.executeUpdate();
        } catch (SQLException excepcionSql) {
            JOptionPane.showMessageDialog(null, "ERROR No se pudo modificar el registro");
            excepcionSql.printStackTrace();
            // close();
        }

        return resultado;
    } // fin del método modificarPersona
} // fin de la interfaz ConsultasPersona
