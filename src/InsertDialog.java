import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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
    private JTextField localidadFild;




    String nombre = "";
    String telefon = "";
    String cif = "";
    String carrer = "";
    String nPortal = "";
    String lletraPortal = "";
    String PisoYLetra = "";
    String codigoPostal = "";
    String localidad = "";



    public InsertDialog(JFrame parent) {
        super(parent);
        setLocationRelativeTo(parent);
        setContentPane(contentPane);
        setModal(true);
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
                Adreça adreça = new Adreça(carrer,localidad,codigoPostal,PisoYLetra,lletraPortal,nPortal);
                Proveidor proveidor = new Proveidor(nombre,telefon,cif,"s", adreça);
                try {
                    Programa.db.InsertarProveidor(adreça,proveidor);
                    JOptionPane.showMessageDialog(null, "Proveedor insertado correctamente");
                    dispose();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ObtenerValores(){
        nombre = nombreFild.getText();
        telefon = telefonFild.getText();
        cif = cifFild.getText();
        carrer = carrerFild.getText();
        nPortal = nPortalFild.getText();
        lletraPortal = lletraPortalFild.getText();
        PisoYLetra = PisoLetraFild.getText();
        codigoPostal = codigoPostalFild.getText();
        localidad = localidadFild.getText().toUpperCase();
    }



}
