import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConsultarProveedor extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTextField nombreFild;
    private JTextField cifFild;
    private JTextField telefonFild;
    private JTable table1;
    private JButton consultaButton;
    private JTextField nombreMFild;
    private JTextField cifMFild;
    private JTextField telefonMField;
    private JTextField activoMFild;
    private JTextField nportalMField;
    private JTextField localidadMField;
    private JTextField llypMFild;
    private JTextField calleMFild;
    private JTextField pyllMField;
    private JButton modificarButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField cpostalMField;
    private DefaultTableModel dtm;

    String nombre = "";
    String cif = "";
    String localidad = "";
    String activo = "";
    String telefon = "";

    public ConsultarProveedor() throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        dtm = new DefaultTableModel();
        dtm.addColumn("nom");
        dtm.addColumn("telefon");
        dtm.addColumn("cif");
        dtm.addColumn("localidad");
        dtm.addColumn("activo");
        table1.setModel(dtm);
        CreateComboLocalitat();
        CreateComboActivo();


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        consultaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dtm.setRowCount(0);
                ObtenerValoresConsulta();
                Proveidor p = new Proveidor(nombre,telefon,cif,activo);
                p.setLocalidad(localidad);
                try {
                  ResultSet rs =  Programa.db.ConsultaProveidor(p);
                   if (!rs.next()){
                        JOptionPane.showMessageDialog(null,"No hay ningun registro que coincida");
                   }
                    while (rs.next()){
                        dtm.addRow(new String[]{rs.getString("nom"),rs.getString("telefon"),rs.getString("CIF"),rs.getString("descripcio"),rs.getString("activo")});
                    }
//                    SeleccionProveedor();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void ObtenerValoresConsulta(){
        nombre = nombreFild.getText();
        telefon = telefonFild.getText();
        cif = cifFild.getText();
        localidad = (String) comboBox1.getSelectedItem();
        activo = (String) comboBox2.getSelectedItem();
    }

    private void CreateComboLocalitat() throws SQLException {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        List<String> localidades = Programa.db.ObtenerNombreLocalidades();
        dcb.addElement("...");
        for(String localidad : localidades){
            dcb.addElement(localidad);
        }
        comboBox1.setModel(dcb);
        comboBox1.setVisible(true);
    }

    private void CreateComboActivo(){
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
            dcb.addElement("s");
            dcb.addElement("n");

        comboBox2.setModel(dcb);
        comboBox2.setVisible(true);
    }

//    private void SeleccionProveedor() throws SQLException {
//        int row  = table1.getSelectedRow();
//        Proveidor p = new Proveidor((String)table1.getValueAt(row,0),(String)table1.getValueAt(row,1),(String)table1.getValueAt(row,2),(String)table1.getValueAt(row,4));
//        ResultSet rs = Programa.db.ObtenerValoresModificar(p);
//        rs.next();
//        nombreMFild.setText(rs.getString("p.nom"));
//        telefonMField.setText(rs.getString("p.telefon"));
//            cifMFild.setText(rs.getString("p.CIF"));
//            activoMFild.setText(rs.getString("p.activo"));
//        nportalMField.setText(rs.getString("a.numero"));
//        localidadMField.setText(rs.getString("l.descripcio"));
//        llypMFild.setText(rs.getString("a.porta"));
//        pyllMField.setText(rs.getString("a.pis"));
//        calleMFild.setText(rs.getString("a.descripcio"));
//        cpostalMField.setText(rs.getString("a.codi_postal"));
//    }

}
