import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private JTextField nportalMField;
    private JTextField llypMFild;
    private JTextField calleMFild;
    private JTextField pyllMField;
    private JButton modificarButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField cpostalMField;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JButton eliminarButton;
    private JButton exportarConsultaButton;
    private final DefaultTableModel dtm;

    private final List<String> localidades = Programa.db.ObtenerNombreLocalidades();
    private List<String> nombres;

    private String nombreN = "";
    private String cifN = "";
    private String localidadN = "";
    private String activoN = "";
    private String telefonN = "";
    private String calleN = "";
    private String nportalN = "";
    private String llportalN = "";
    private String plletraN = "";
    private String codigo_postalN = "";
    private String tipus_adreçaN = "";

    private int id_proveidor;
    private int id_adreca;
    private String nombre = "";
    private String cif = "";
    private String localidad = "";
    private String activo = "";
    private String telefon = "";



    public ConsultarProveedor(JFrame parent) throws SQLException {
        super(parent);
        setLocationRelativeTo(parent);
        setContentPane(contentPane);
        setTitle("Consultas,Modificaciones y Eliminaciones");
        setModal(true);
        exportarConsultaButton.setEnabled(false);
        dtm = new DefaultTableModel();
        dtm.addColumn("nom");
        dtm.addColumn("telefon");
        dtm.addColumn("cif");
        dtm.addColumn("localidad");
        dtm.addColumn("activo");
        table1.setModel(dtm);
        CreateComboLocalitat(comboBox1);
        CreateComboActivo(comboBox2);
        CreateComboLocalitat(comboBox3);
        CreateComboActivo(comboBox4);
        CreateComboTipoCarrer(comboBox5);
        ResetearCampos();

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        consultaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dtm.setRowCount(0);
                ResetearCampos();
                ObtenerValoresConsulta();
                Proveidor p = new Proveidor(nombre, telefon, cif, activo);
                p.setLocalidad(localidad);
                try {
                   CargarRegistros(p);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                try {
                    if (table1.getSelectedRow() >= 0) {
                        SeleccionProveedor();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (table1.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(null, "No hay registro seleccionado");
                } else {
                    ObtenerValoresNuevosCamposModificar();
                    if (Objects.equals(tipus_adreçaN, "...") || Objects.equals(localidadN, "...") || Objects.equals(activoN, "...")) {
                        JOptionPane.showMessageDialog(null, "Seleccione un valor en todos los desplegables");
                    } else {
                        Adreça a;
                        try {
                            a = new Adreça(calleN, Programa.db.ObtenerIdLocalidad(localidadN), codigo_postalN, plletraN, llportalN, nportalN, Programa.db.ObtenerIdTipoVia(tipus_adreçaN));
                             a.setId_Adreça(id_adreca);
                            a.setId_localidad(Programa.db.ObtenerIdLocalidad(localidadN));
                             Proveidor p = new Proveidor(nombreN, telefonN, cifN, activoN, a);
                              p.setId_proveidor(id_proveidor);
                            Programa.db.ModificarProveedor(p);
                            JOptionPane.showMessageDialog(null, "Modificacion realizada con exito");
                            Proveidor p2 = new Proveidor(nombre,telefon,cif,activo);
                            p2.setLocalidad(localidad);
                            dtm.setRowCount(0);
                            CargarRegistros(p2);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error en modificacion, se ha cancelado la accion");
                        }
                    }
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (table1.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(null, "No hay registro seleccionado");
                } else {
                    Proveidor p = new Proveidor(cifN);
                    try {
                        Programa.db.EliminarProveedor(p);
                        JOptionPane.showMessageDialog(null,"Proveedor dado de baja");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        exportarConsultaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                  PanelPrincipal.ExportarXML(Programa.db.ObtenerXmlConsulta(nombres));
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (TransformerException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void ObtenerValoresConsulta() {
        nombre = nombreFild.getText();
        telefon = telefonFild.getText();
        cif = cifFild.getText();
        localidad = (String) comboBox1.getSelectedItem();
        activo = (String) comboBox2.getSelectedItem();
    }

    private void CreateComboLocalitat(JComboBox comboBox) {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        dcb.addElement("...");
        for (String localidad : localidades) {
            dcb.addElement(localidad);
        }
        comboBox.setModel(dcb);
        comboBox.setVisible(true);
    }

    private void CreateComboActivo(JComboBox comboBox) {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        dcb.addElement("...");
        dcb.addElement("s");
        dcb.addElement("n");
        comboBox.setModel(dcb);
        comboBox.setVisible(true);
    }

    private void SeleccionProveedor() throws SQLException {
        int row = table1.getSelectedRow();
        Proveidor p = new Proveidor((String) table1.getValueAt(row, 0), (String) table1.getValueAt(row, 1), (String) table1.getValueAt(row, 2), (String) table1.getValueAt(row, 4));
        ResultSet rs = Programa.db.ObtenerValoresRellenarCampos(p);
        rs.next();
        RellenarCampos(rs);
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

    private void CargarRegistros(Proveidor p) throws SQLException {
        ResultSet rs = Programa.db.ConsultaProveidor(p);
        if (rs.next()) {
            nombres = new ArrayList<String>();
            nombres.add(rs.getString("nom"));
            dtm.addRow(new String[]{rs.getString("nom"), rs.getString("telefon"), rs.getString("CIF"), rs.getString("descripcio"), rs.getString("activo")});
            while (rs.next()) {
                dtm.addRow(new String[]{rs.getString("nom"), rs.getString("telefon"), rs.getString("CIF"), rs.getString("descripcio"), rs.getString("activo")});
            }
            exportarConsultaButton.setEnabled(true);

        }else {
            JOptionPane.showMessageDialog(null, "No hay ningun registro que coincida");
            exportarConsultaButton.setEnabled(false);
        }

    }

    private void RellenarCampos(ResultSet rs) throws SQLException {
        cifN = rs.getString("p.CIF");
        id_proveidor = rs.getInt("p.id_proveidor");
        id_adreca = rs.getInt("a.id_adreca");
        nombreMFild.setText(rs.getString("p.nom"));
        nombreMFild.setEnabled(true);
        telefonMField.setText(rs.getString("p.telefon"));
        telefonMField.setEnabled(true);
        cifMFild.setText(rs.getString("p.CIF"));
        cifMFild.setEnabled(true);
        comboBox3.setSelectedItem(rs.getString("l.descripcio"));
        comboBox3.setEnabled(true);
        nportalMField.setText(rs.getString("a.numero"));
        nportalMField.setEnabled(true);
        comboBox4.setSelectedItem(rs.getString("p.activo"));
        comboBox4.setEnabled(true);
        llypMFild.setText(rs.getString("a.porta"));
        llypMFild.setEnabled(true);
        pyllMField.setText(rs.getString("a.pis"));
        pyllMField.setEnabled(true);
        calleMFild.setText(rs.getString("a.descripcio"));
        calleMFild.setEnabled(true);
        cpostalMField.setText(rs.getString("a.codi_postal"));
        cpostalMField.setEnabled(true);
        comboBox5.setSelectedItem(rs.getString("t.abreviatura"));
        comboBox5.setEnabled(true);
    }

    private void ResetearCampos() {
        nombreMFild.setText("");
        nombreMFild.setEnabled(false);
        telefonMField.setText("");
        telefonMField.setEnabled(false);
        cifMFild.setText("");
        cifMFild.setEnabled(false);
        comboBox3.setSelectedItem( "...");
        comboBox3.setEnabled(false);
        nportalMField.setText("");
        nportalMField.setEnabled(false);
        comboBox4.setSelectedItem("...");
        comboBox4.setEnabled(false);
        llypMFild.setText("");
        llypMFild.setEnabled(false);
        pyllMField.setText("");
        pyllMField.setEnabled(false);
        calleMFild.setText("");
        calleMFild.setEnabled(false);
        cpostalMField.setText("");
        cpostalMField.setEnabled(false);
        comboBox5.setSelectedItem("...");
        comboBox5.setEnabled(false);
    }

    private void ObtenerValoresNuevosCamposModificar(){
        tipus_adreçaN = (String) comboBox5.getSelectedItem();
        nombreN = nombreMFild.getText();
        cifN = cifMFild.getText();
        localidadN = (String) comboBox3.getSelectedItem();
        activoN =  (String) comboBox4.getSelectedItem();
        telefonN = telefonMField.getText();
        calleN = calleMFild.getText();
        nportalN = nportalMField.getText();
        llportalN = llypMFild.getText();
        plletraN = pyllMField.getText();
        codigo_postalN = cpostalMField.getText();
    }


}
