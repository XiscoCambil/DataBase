import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by fjcambilr on 13/06/16.
 */
public class DataBase {
    private final String username;
    private final String password;
    private final String server;
    private Connection c;

    //Constructor DataBase
    public DataBase(String server, String username, String password) throws Exception {
        this.username = username;
        this.password = password;
        this.server = server;
        ConexionBBDD();
        PanelPrincipal pp = new PanelPrincipal();
    }

    //Metodos Conexion a la Base de Datos
    private void ConexionBBDD() {
        try {
            String dbClassName = "com.mysql.jdbc.Driver";
            Class.forName(dbClassName);
            // Properties for user and password. Here the user and password are both 'paulr'
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);
            String CONNECTION = "jdbc:mysql://" + server + "/gestio";
            // Now try to connect
            c = DriverManager.getConnection(CONNECTION, p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexion");
        }
    }

    //Metodo InsertarProveedor llamado una vez que se aprieta el botn de insertar y se rellenan los datos
    public void InsertarProveidor(Adreça adreça, Proveidor proveidor) throws SQLException {
        int id_adreça = ObtenerIdAdreca();
        adreça.setId_Adreça(id_adreça);
        InsertAdr(adreça);
        InsertPro(proveidor);
    }

    //Metodo para obtener la nueva id_adreca
    private int ObtenerIdAdreca() throws SQLException {
        //Obtenemos los datos para insertar en la tabala ADRECA
        String ConsultaMaxAdreca = "SELECT MAX(id_adreca)+1 as adreca FROM ADRECA";
        PreparedStatement prs3 = c.prepareStatement(ConsultaMaxAdreca);
        ResultSet rs3 = prs3.executeQuery();
        rs3.next();
        return rs3.getInt("adreca");
    }


    //Metodo para la inserccion de la direccion del proveedor
    private void InsertAdr(Adreça adreça) {
        try {
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
            JOptionPane.showMessageDialog(null, "Error en la inserccion en la direccion");
        }
    }

    //Metodo para la insercion de los datos del proveedor
    private void InsertPro(Proveidor proveidor) {
        try {
            String InsertProveedor = "INSERT INTO PROVEIDOR (nom,id_adreça,telefon,CIF,activo) VALUES(?,?,?,?,?)";
            PreparedStatement inProveidor = c.prepareStatement(InsertProveedor);
            inProveidor.setString(1, proveidor.getNombre());
            inProveidor.setInt(2, proveidor.adreça.getId_Adreça());
            inProveidor.setString(3, proveidor.getTelefon());
            inProveidor.setString(4, proveidor.getCif());
            inProveidor.setString(5, "s");
            inProveidor.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la inserccion de proveedor");
        }
    }


    //Metodo utilizado para obtener la id de la locadiad a partir de su nombre
    public int ObtenerIdLocalidad(String localidad) throws SQLException {
        String consultaidLocalida = "SELECT id_localitat FROM LOCALITAT WHERE descripcio=?";
        PreparedStatement ps2 = c.prepareStatement(consultaidLocalida);
        ps2.setString(1, localidad);
        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        return rs2.getInt("id_localitat");

    }

    //Metodo utilizado para obtener la id del tipo_de_via a partir de la abreviatura.
    public int ObtenerIdTipoVia(String abreviatura) throws SQLException {
        //Obtenemos el idetificador de la localidad.
        String consultaAbreviatura = "SELECT id_tipus_adreca FROM TIPUS_ADRECA WHERE abreviatura=?";
        PreparedStatement ps2 = c.prepareStatement(consultaAbreviatura);
        ps2.setString(1, abreviatura);
        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        return rs2.getInt("id_tipus_adreca");

    }


    //Metodo para obtener el numero total del proveedores.
    public int maxCodiProveidor() throws ClassNotFoundException, SQLException {
        String sql = "select count(*) as count from PROVEIDOR";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt("count");

    }

    //Metodo para Obtener los nombres de las localidades para los combobox
    public List<String> ObtenerNombreLocalidades() throws SQLException {
        List<String> localidades = new ArrayList<>();
        String sql = "select * from LOCALITAT";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            localidades.add(rs.getString("descripcio"));
        }
        return localidades;
    }

    //Metodo para obtener las abreviaturas los tipos de carreteras para los combobox
    public List<TipusCarrer> ObtenerNombreTipusCarrer() throws SQLException {
        List<TipusCarrer> tipusCarrers = new ArrayList<>();
        String sql = "select * from TIPUS_ADRECA";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        // execute select SQL stetement
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            tipusCarrers.add(new TipusCarrer(rs.getInt("id_tipus_adreca"), rs.getString("descripcio"), rs.getString("abreviatura")));
        }
        return tipusCarrers;
    }

    //Metodo empleado para crear la consulta de todos los registros de la tablas PROVEDOR Y ADRECA
    public List<Proveidor> ObtenerDump() throws SQLException {
        String sql = "SELECT * FROM ((ADRECA as a INNER JOIN PROVEIDOR as p ON p.id_adreça = a.id_adreca) INNER JOIN LOCALITAT as l ON l.id_localitat = a.id_localitat)INNER JOIN TIPUS_ADRECA as t ON t.id_tipus_adreca = a.id_tipus_adreca ";
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Proveidor> proveedores = new ArrayList<>();
        while (rs.next()) {
            Adreça a = new Adreça(rs.getString("a.descripcio"), Programa.db.ObtenerIdLocalidad(rs.getString("l.descripcio")), rs.getString("a.codi_postal"), rs.getString("a.pis"), rs.getString("a.porta"), rs.getString("a.numero"), Programa.db.ObtenerIdTipoVia(rs.getString("t.abreviatura")));
            a.setId_Adreça(rs.getInt("a.id_adreca"));
            Proveidor p = new Proveidor(rs.getString("p.nom"), rs.getString("p.telefon"), rs.getString("p.CIF"), rs.getString("p.activo"), a);
            proveedores.add(p);
        }
        return proveedores;
    }

    //Metodo empleado para obtener la consulta de la cual se quiere hacer un XML
    public List<Proveidor> ObtenerXmlConsulta(List<String> nombres) throws SQLException {
        String sql = "SELECT * FROM ((ADRECA as a INNER JOIN PROVEIDOR as p ON p.id_adreça = a.id_adreca) INNER JOIN LOCALITAT as l ON l.id_localitat = a.id_localitat)INNER JOIN TIPUS_ADRECA as t ON t.id_tipus_adreca = a.id_tipus_adreca WHERE ";
        for (int i = 0; i < nombres.size(); i++) {
            if (i == nombres.size() - 1) {
                sql += "nom=? ";
            } else {
                sql += "nom=? and ";
            }
        }
        PreparedStatement ps = c.prepareStatement(sql);
        for (int i = 0, x = 1; i < nombres.size(); i++, x++) {
            ps.setString(x, nombres.get(i));
        }
        ResultSet rs = ps.executeQuery();
        List<Proveidor> proveedores = new ArrayList<>();
        while (rs.next()) {
            Adreça a = new Adreça(rs.getString("a.descripcio"), Programa.db.ObtenerIdLocalidad(rs.getString("l.descripcio")), rs.getString("a.codi_postal"),
                    rs.getString("a.pis"), rs.getString("a.porta"), rs.getString("a.numero"), Programa.db.ObtenerIdTipoVia(rs.getString("t.abreviatura")));
            a.setId_Adreça(rs.getInt("a.id_adreca"));
            Proveidor p = new Proveidor(rs.getString("p.nom"), rs.getString("p.telefon"), rs.getString("p.CIF"), rs.getString("p.activo"), a);
            proveedores.add(p);
        }
        return proveedores;
    }

    //Metodo utilizado para obtener la consulta de los proveedores a partir de los datos introducidos por el usuario
    public ResultSet ConsultaProveidor(Proveidor p) throws SQLException {
        List<String> valores = new ArrayList<>();
        String sql = "";
        if (!p.getNombre().isEmpty()) {
            valores.add("p.nom");
            valores.add(p.getNombre());
        }
        if (!p.getTelefon().isEmpty()) {
            valores.add("p.telefon");
            valores.add(p.getTelefon());
        }
        if (!p.getCif().isEmpty()) {
            valores.add("p.CIF");
            valores.add(p.getCif());
        }
        if (p.getActivo() != "...") {
            valores.add("p.activo");
            valores.add(p.getActivo());
        }
        if (p.getLocalidad() != "...") {
            valores.add("l.descripcio");
            valores.add(p.getLocalidad());
        }
        if (valores.size() == 0) {
            sql += "SELECT p.nom,p.telefon,p.cif,l.descripcio,p.activo FROM( PROVEIDOR as p INNER JOIN ADRECA as a ON p.id_adreça = a.id_adreca) INNER JOIN LOCALITAT as l ON l.id_localitat = a.id_localitat";
        } else {
            sql += "SELECT p.nom,p.telefon,p.cif,l.descripcio,p.activo FROM( PROVEIDOR as p INNER JOIN ADRECA as a ON p.id_adreça = a.id_adreca) INNER JOIN LOCALITAT as l ON l.id_localitat = a.id_localitat WHERE ";
        }
        for (int i = 0; i < valores.size(); i += 2) {
            if (i == valores.size() - 2) {
                sql += valores.get(i) + "=?";
            } else {
                sql += valores.get(i) + "=? && ";
            }
        }
        PreparedStatement ps = c.prepareStatement(sql);
        for (int i = 1, x = 1; i < valores.size(); i += 2, x++) {
            ps.setString(x, valores.get(i));
        }
        return ps.executeQuery();
    }

    //Metodo para obtener los valores que con los que se llenaran los campos de modificar del registro seleccionado
    public ResultSet ObtenerValoresRellenarCampos(Proveidor p) throws SQLException {
        String sql = "SELECT p.id_proveidor,p.nom,p.telefon,p.CIF,p.activo,a.id_adreca,a.descripcio,a.numero,a.porta,a.pis,a.codi_postal,l.descripcio,t.abreviatura FROM((PROVEIDOR as p INNER JOIN ADRECA as a ON p.id_adreça = a.id_adreca)INNER JOIN LOCALITAT as l ON a.id_localitat = l.id_localitat)INNER JOIN TIPUS_ADRECA AS t ON t.id_tipus_adreca = a.id_tipus_adreca  WHERE CIF=?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, p.getCif());
        return ps.executeQuery();
    }

    //Metodo llamado una vez se aprieta el boton de modificar del dilogo de ConsultaProveedor
    public void ModificarProveedor(Proveidor p) throws SQLException {
        UpdateAdreca(p);
        UpdateProveidor(p);
    }

    //Metodo llamada una vez se aprieta el boton de eliminar del dialogo de ConsultaProveedor
    public void EliminarProveedor(Proveidor p) throws SQLException {
        String sql = "UPDATE PROVEIDOR SET activo='n' WHERE CIF=?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, p.getCif());
        ps.execute();
    }

    //Metodo utilizado para la actualizacion de los datos de la direccion del proveedor
    private void UpdateAdreca(Proveidor p) throws SQLException {
        String sql = "UPDATE ADRECA SET id_localitat=?,descripcio=?,numero=?,porta=?,pis=?,codi_postal=?,id_tipus_adreca=? WHERE id_adreca=?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, p.adreça.getId_localidad());
        ps.setString(2, p.adreça.getCarrer());
        ps.setString(3, p.adreça.getnPortal());
        ps.setString(4, p.adreça.getLletraPortal());
        ps.setString(5, p.adreça.getPisoYLetra());
        ps.setString(6, p.adreça.getCodigoPostal());
        ps.setInt(7, p.adreça.getTipo_Via());
        ps.setInt(8, p.adreça.getId_Adreça());
        ps.execute();
    }

    //Metodo utilizado para la actualizacion de los datos del proveedor
    private void UpdateProveidor(Proveidor p) throws SQLException {
        String sql = "UPDATE PROVEIDOR SET nom=?,telefon=?,CIF=?,activo=? WHERE id_proveidor=?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getTelefon());
        ps.setString(3, p.getCif());
        ps.setString(4, p.getActivo());
        ps.setInt(5, p.getId_proveidor());
        ps.execute();
    }

    //Metodo llamado cuando se aprieta el boton de InsertarXML
    public void InsertarXML(String sql, String sql2, List<Adreça> direcciones, List<Proveidor> proveidors) throws SQLException {
        for (int i = 0; i < direcciones.size(); i++) {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, direcciones.get(i).getId_Adreça());
            ps.setInt(2, direcciones.get(i).getTipo_Via());
            ps.setInt(3, direcciones.get(i).getId_localidad());
            ps.setString(4, direcciones.get(i).getCarrer());
            ps.setString(5, direcciones.get(i).getnPortal());
            ps.setString(6, direcciones.get(i).getLletraPortal());
            ps.setString(7, direcciones.get(i).getPisoYLetra());
            ps.setString(8, direcciones.get(i).getCodigoPostal());
            ps.execute();
        }
        for (int i = 0; i < proveidors.size(); i++) {
            PreparedStatement ps2 = c.prepareStatement(sql2);
            ps2.setString(1, proveidors.get(i).getNombre());
            ps2.setInt(2, proveidors.get(i).adreça.getId_Adreça());
            ps2.setString(3, proveidors.get(i).getTelefon());
            ps2.setString(4, proveidors.get(i).getCif());
            ps2.setString(5, proveidors.get(i).getActivo());
            ps2.execute();
        }
    }

}







