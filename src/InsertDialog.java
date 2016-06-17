import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class InsertDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nombreFild;
    private JTextField telefonFild;
    private JTextField cifFild;
    private JTextField carrerFild;
    private JTextField nPortalFild;
    private JTextField lletraPortalFild;
    private JTextField PisoLetraFild;
    private JTextField codigoPostalFild;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextPane losCamposConSonTextPane;


    private String nombre = "";
    private String telefon = "";
    private String cif = "";
    private String carrer = "";
    private String nPortal = "";
    private String lletraPortal = "";
    private String PisoYLetra = "";
    private String codigoPostal = "";
    private String localidad = "";
    private String tipus_carrer = "";

    public InsertDialog(JFrame parent) throws SQLException {
        super(parent);
        setLocationRelativeTo(parent);
        setContentPane(contentPane);
        setModal(true);
        CreateComboLocalitat(comboBox1);
        CreateComboTipoCarrer(comboBox2);
        getRootPane().setDefaultButton(buttonOK);
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ObtenerValores();
                if(nombre.isEmpty() || telefon.isEmpty() || cif.isEmpty() || carrer.isEmpty() || localidad.isEmpty() || tipus_carrer.isEmpty() || codigoPostal.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Faltan campos obligatorios por rellenar");
                }else {
                    try {
                        Adreça adreça = new Adreça(carrer,Programa.db.ObtenerIdLocalidad(localidad), codigoPostal, PisoYLetra, lletraPortal, nPortal,Programa.db.ObtenerIdTipoVia(tipus_carrer));
                        Proveidor proveidor = new Proveidor(nombre, telefon, cif, "s", adreça);
                        Programa.db.InsertarProveidor(adreça, proveidor);
                        JOptionPane.showMessageDialog(null, "Proveedor insertado correctamente");
                        dispose();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        //JOptionPane.showMessageDialog(null, "ha habido un error en la inserccion");
                    }
                }
            }
        });
    }

    private void ObtenerValores() {
        nombre = nombreFild.getText();
        telefon = telefonFild.getText();
        cif = cifFild.getText();
        carrer = carrerFild.getText();
        nPortal = nPortalFild.getText();
        lletraPortal = lletraPortalFild.getText();
        PisoYLetra = PisoLetraFild.getText();
        codigoPostal = codigoPostalFild.getText();
        localidad = (String) comboBox1.getSelectedItem();
        tipus_carrer = (String) comboBox2.getSelectedItem();

    }

    private void CreateComboLocalitat(JComboBox comboBox) throws SQLException {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        List<String> localidades = Programa.db.ObtenerNombreLocalidades();
        dcb.addElement("...");
        for (String localidad : localidades) {
            dcb.addElement(localidad);
        }
        comboBox.setModel(dcb);
        comboBox.setVisible(true);
    }

    private void CreateComboTipoCarrer(JComboBox comboBox) throws SQLException {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        List<TipusCarrer> tipos = Programa.db.ObtenerNombreTipusCarrer();
        dcb.addElement("...");
        for (TipusCarrer t : tipos) {
            dcb.addElement(t.abrev);
        }
        comboBox.setModel(dcb);
        comboBox.setVisible(true);
    }
}
