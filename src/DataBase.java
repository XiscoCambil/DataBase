import java.sql.*;
import java.util.Properties;

/**
 * Created by fjcambilr on 13/06/16.
 */
public class DataBase {
    private String username;
    private String password;
    private String server;
    private Connection c;

    public DataBase(String server, String username, String password) throws Exception {
        this.username = username;
        this.password = password;
        this.server = server;
        ConexionBBDD();
        PanelPrincipal pp = new PanelPrincipal();
    }

    private String dbClassName = "com.mysql.jdbc.Driver";

    private void ConexionBBDD() throws ClassNotFoundException, SQLException {
        try {
            Class.forName(dbClassName);
            // Properties for user and password. Here the user and password are both 'paulr'
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);
            String CONNECTION = "jdbc:mysql://" + server + "/gestio";
            // Now try to connect
            c = DriverManager.getConnection(CONNECTION, p);
            PanelPrincipal pp = new PanelPrincipal();

        } catch (Exception e) {
            System.out.println("Error en la conexion");

        }
    }

    public void InsertarProveidor(Adreça adreça, Proveidor proveidor) throws SQLException {
        int id_adreça = ObtenerIdAdreca();
        int id_localidad = ObtenerIdLocalidad(adreça.getLocalidad());
        adreça.setId_Adreça(id_adreça);
        adreça.setId_localidad(id_localidad);
        proveidor.setId_adreça(id_adreça);
        InsertAdr(adreça);
        InsertPro(proveidor);
    }



    private int ObtenerIdAdreca() throws SQLException {
        //Obtenemos los datos para insertar en la tabala ADRECA
        String ConsultaMaxAdreca = "SELECT MAX(id_adreca)+1 as adreca FROM ADRECA";
        PreparedStatement prs3 = c.prepareStatement(ConsultaMaxAdreca);
        ResultSet rs3 = prs3.executeQuery();
        rs3.next();
        return rs3.getInt("adreca");
    }

    private int ObtenerIdLocalidad(String localidad) throws SQLException {
        //Obtenemos el idetificador de la localidad.
        String consultaidLocalida = "SELECT id_localitat FROM LOCALITAT WHERE descripcio=?";
        PreparedStatement ps2 = c.prepareStatement(consultaidLocalida);
        ps2.setString(1, localidad);
        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        return rs2.getInt("id_localitat");

    }

    private void InsertAdr(Adreça adreça) {
        try {
            //Hacemos el insert dentro de ADRECA.
            String InsertAdreca = "INSERT INTO ADRECA VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement inAdreca = c.prepareStatement(InsertAdreca);
            inAdreca.setInt(1, adreça.getId_Adreça());
            inAdreca.setInt(2, adreça.getTipo_Via());
            inAdreca.setInt(3, adreça.getId_localidad());
            inAdreca.setString(4, adreça.getCarrer());
            inAdreca.setString(5, adreça.getnPortal());
            inAdreca.setString(6, adreça.getLletraPortal());
            inAdreca.setString(7, adreça.getPisoYLetra());
            inAdreca.setString(8, adreça.getCodigoPostal());
            inAdreca.execute();
        } catch (Exception e) {
            System.out.println("Error en la inserccion en la direccion");
        }
    }

    private void InsertPro(Proveidor proveidor) {
        //Hacemos el insert dentro de ADRECA.
        try {
            String InsertProveedor = "INSERT INTO PROVEIDOR (nom,id_adreça,telefon,CIF,activo) VALUES(?,?,?,?,?)";
            PreparedStatement inProveidor = c.prepareStatement(InsertProveedor);
            inProveidor.setString(1, proveidor.getNombre());
            inProveidor.setInt(2, proveidor.getId_adreça());
            inProveidor.setString(3, proveidor.getTelefon());
            inProveidor.setString(4, proveidor.getCif());
            inProveidor.setString(5, "s");
            inProveidor.execute();
        } catch (Exception e) {
            System.out.println("Error en la inserccion de proveedor");
        }
    }

    public int maxCodiProveidor() throws ClassNotFoundException, SQLException {
        String sql = "select max(id_proveidor) as max from PROVEIDOR";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int max = rs.getInt("max");
        return max;

    }



}

class Proveidor {

    public int id_adreça;
    public String nombre;
    public String telefon;
    public String cif;
    public String activo;
    public Adreça adreça;

    public int getId_adreça() {
        return id_adreça;
    }

    public void setId_adreça(int id_adreça) {
        this.id_adreça = id_adreça;
    }

    public Proveidor(String nombre,  String telefon, String cif, String activo, Adreça adreça) {
        this.nombre = nombre;
        this.telefon = telefon;
        this.cif = cif;
        this.activo = activo;
        this.adreça = adreça;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public Adreça getAdreça() {
        return adreça;
    }

    public void setAdreça(Adreça adreça) {
        this.adreça = adreça;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

class Programa{
    static DataBase db;
    public static void main(String[] args) throws Exception {
        db = new DataBase("172.16.10.156","root","terremoto11");

    }

}

class Adreça{

    public int id_Adreça;
    public String carrer = "";
    public String nPortal = "";
    public String lletraPortal = "";
    public String PisoYLetra = "";
    public String codigoPostal = "";
    public String localidad = "";
    public int tipo_Via = 1;

    public String getCarrer() {
        return carrer;
    }

    public void setCarrer(String carrer) {
        this.carrer = carrer;
    }

    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
    }

    public int id_localidad;

    public int getId_Adreça() {
        return id_Adreça;
    }

    public void setId_Adreça(int id_Adreça) {
        this.id_Adreça = id_Adreça;
    }


    public String getLletraPortal() {
        return lletraPortal;
    }

    public void setLletraPortal(String lletraPortal) {
        this.lletraPortal = lletraPortal;
    }

    public String getPisoYLetra() {
        return PisoYLetra;
    }

    public void setPisoYLetra(String pisoYLetra) {
        PisoYLetra = pisoYLetra;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getTipo_Via() {
        return tipo_Via;
    }

    public void setTipo_Via(int tipo_Via) {
        this.tipo_Via = tipo_Via;
    }

    public String getnPortal() {
        return nPortal;
    }

    public void setnPortal(String nPortal) {
        this.nPortal = nPortal;
    }


    public Adreça(String carrer, String localidad, String codigoPostal, String pisoYLetra, String lletraPortal, String nPortal) {
        this.carrer = carrer;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.PisoYLetra = pisoYLetra;
        this.lletraPortal = lletraPortal;
        this.nPortal = nPortal;

    }


}
