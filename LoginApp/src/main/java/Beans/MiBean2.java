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
public class MiBean2 implements Serializable {
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
            conn = DriverManager.getConnection(this.DB_URL, this.USER, this.PASS);
            System.out.println("Conexion exitosa a base de datos...");
            
            int existe = 0;
            String query = "SELECT COUNT(*) AS existe FROM USUARIOS WHERE NOMBRE=? and CONTRASENNA=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, mysqlRealScapeString(this.usuario));
            ps.setString(2, mysqlRealScapeString(this.contrasenna));
            
            ResultSet rs = ps.executeQuery(); 
            while ( rs.next() ) {
              existe = rs.getInt("existe");
            }
            rs.close();
            ps.close();
            
            
            return existe > 0 ? "principal.xhtml" : "";
        } catch(Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
    
    public String mysqlRealScapeString(String str) {
        String data = null;
        if (str != null && str.length() > 0) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;
    }
}
