import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by fjcambilr on 13/06/16.
 */
public class PanelPrincipal {
    private JButton insertarProveidorButton;
    private JButton numeroDeProveedoresButton;
    private JButton consultarProveedorButton;
    private JPanel panel;
    private JButton exportarBaseDeDatosButton;
    private JButton importarBaseDeDatosButton;
    private JFrame frame = new JFrame("Proveedor");

    public PanelPrincipal() {
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        insertarProveidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InsertDialog id = null;
                try {
                    id = new InsertDialog(frame);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                id.pack();
                id.setVisible(true);
            }
        });
        numeroDeProveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JOptionPane.showMessageDialog(null, "Numero de proveedores: " + Programa.db.maxCodiProveidor());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        consultarProveedorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConsultarProveedor cp = null;
                try {
                    cp = new ConsultarProveedor();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cp.pack();
                ;
                cp.setVisible(true);
            }
        });
        exportarBaseDeDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SimpleXML dumpXml = new SimpleXML();
                    Document doc = dumpXml.getDoc();
                    Element arrel = doc.createElement("data");
                    doc.appendChild(arrel);
                    List<Proveidor> proveedores = Programa.db.ObtenerDump();
                    for(Proveidor p : proveedores){

                        Element proveidor =  doc.createElement("proveidor");
                        arrel.appendChild(proveidor);

                        Element nom = doc.createElement("nom");
                        proveidor.appendChild(nom);
                        nom.setTextContent(p.getNombre());

                        Element id_adreca = doc.createElement("id_adreca");
                        proveidor.appendChild(id_adreca);
                        id_adreca.setTextContent(String.valueOf(p.adreça.getId_Adreça()));

                        Element telefon = doc.createElement("telefon");
                        proveidor.appendChild(telefon);
                        telefon.setTextContent(p.getNombre());

                        Element cif = doc.createElement("cif");
                        proveidor.appendChild(cif);
                        cif.setTextContent(p.getCif());

                        Element activo = doc.createElement("activo");
                        proveidor.appendChild(activo);
                        activo.setTextContent(p.getActivo());

                        Element adreca = doc.createElement("adreça");
                        proveidor.appendChild(adreca);

                    }
                    dumpXml.write(new FileOutputStream("src/xml/dump.xml"));
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
