package Beans;

import java.io.Serializable;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.annotation.PostConstruct;
import java.sql.*;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class MiBean implements Serializable {
    private String usuario = "";
    private String contrasenna = "";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://127.0.0.1/pruebas";

    //  Database credentials
    static final String USER = "miusuario";
    static final String PASS = "micontrasenna";
    
    @PostConstruct
    public void init() {
        System.out.println("Iniciando variables");
    }
    
    public String loguearse() {
        try {
            System.out.println("Logueandose");
            System.out.println(this.usuario);
            System.out.println(this.contrasenna);
            
            //STEP 2: Register JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = null;
            Statement stmt = null;
            
            //STEP 3: Open a connection
            System.out.println("Conectando base de datos...");
            //conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1/pruebas", "alex", "12345");
            conn = DriverManager.getConnection(this.DB_URL, this.USER, this.PASS);
            System.out.println("Conexion exitosa a base de datos...");
                        
            //STEP 4: Execute a query
            System.out.println("Consultando base de datos...");
            stmt = conn.createStatement();
            
            // our SQL SELECT query. 
            String query = "SELECT COUNT(*) AS existe FROM USUARIOS WHERE NOMBRE='"+this.usuario+"' and CONTRASENNA='"+this.contrasenna+"';";
            System.out.println("**********CONSULTA**********");
            System.out.println(query);
            // create the java statement
            stmt = conn.createStatement();

            int existe = 0;
            // execute the query, and get a java resultset
            ResultSet rs = stmt.executeQuery(query);
            // iterate through the java resultset
            while (rs.next()) {
              existe = rs.getInt("existe");
            }
            
            stmt.close();
            
            System.out.println("**********RESULTADO**********");
            System.out.println(existe);

            
            return existe > 0 ? "principal.xhtml" : "";
        } catch(Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
}
