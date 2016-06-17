import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
    private static JFrame frame = new JFrame("Proveedor");

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
                    cp = new ConsultarProveedor(frame);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cp.pack();
                cp.setVisible(true);
            }
        });
        exportarBaseDeDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    List<Proveidor> proveedores = Programa.db.ObtenerDump();
                   ExportarXML(proveedores);
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
        importarBaseDeDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                int code = jfc.showOpenDialog(frame);
                jfc.getFileFilter();
                if(code == JFileChooser.APPROVE_OPTION) {
                    try {
                        SimpleXML importXml = new SimpleXML(new FileInputStream("src/xml/config.xml"));
                        Document doc = importXml.getDoc();
                        Element proveidor = doc.getDocumentElement();


                    } catch (ParserConfigurationException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (SAXException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
    }

    private static void XMLproveidor(Document doc, Element proveidor, Proveidor p){

        Element nom = doc.createElement("nom");
        proveidor.appendChild(nom);
        nom.setTextContent(p.getNombre());

        Element id_adreca = doc.createElement("id_adreca");
        proveidor.appendChild(id_adreca);
        id_adreca.setTextContent(String.valueOf(p.adreça.getId_Adreça()));

        Element telefon = doc.createElement("telefon");
        proveidor.appendChild(telefon);
        telefon.setTextContent(p.getTelefon());

        Element cif = doc.createElement("cif");
        proveidor.appendChild(cif);
        cif.setTextContent(p.getCif());

        Element activo = doc.createElement("activo");
        proveidor.appendChild(activo);
        activo.setTextContent(p.getActivo());
    }

    private static void XMLadreca(Document doc, Element adreca, Proveidor p){

        Element id_tipus_adreca = doc.createElement("id_tipus_adreca");
        adreca.appendChild(id_tipus_adreca);
        id_tipus_adreca.setTextContent(p.adreça.getTipo_de_via());

        Element id_localitat = doc.createElement("id_localitat");
        adreca.appendChild(id_localitat);
        id_localitat.setTextContent(String.valueOf(p.adreça.getId_localidad()));

        Element adescripcio = doc.createElement("carrer");
        adreca.appendChild(adescripcio);
        adescripcio.setTextContent(p.adreça.getCarrer());

        Element numero = doc.createElement("numero_portal");
        adreca.appendChild(numero);
        numero.setTextContent(p.adreça.getnPortal());

        Element portal = doc.createElement("lletra_portal");
        adreca.appendChild(portal);
        portal.setTextContent(p.adreça.getLletraPortal());

        Element pis = doc.createElement("piso");
        adreca.appendChild(pis);
        pis.setTextContent(p.adreça.getPisoYLetra());

        Element codiPostal = doc.createElement("codi_postal");
        adreca.appendChild(codiPostal);
        codiPostal.setTextContent(p.adreça.getCodigoPostal());
    }

    public static void ExportarXML(List<Proveidor> proveedores) throws ParserConfigurationException, SQLException, FileNotFoundException, TransformerException {
        JFileChooser jfc = new JFileChooser();
        int code = jfc.showSaveDialog(frame);
        if(code == JFileChooser.APPROVE_OPTION) {

        SimpleXML dumpXml = new SimpleXML();
        Document doc = dumpXml.getDoc();
        Element arrel = doc.createElement("data");
        doc.appendChild(arrel);

        for (Proveidor p : proveedores) {

            Element proveidor = doc.createElement("proveidor");
            arrel.appendChild(proveidor);

            XMLproveidor(doc, proveidor, p);

            Element adreca = doc.createElement("adreça");
            proveidor.appendChild(adreca);

            XMLadreca(doc, adreca, p);
        }
            String path = jfc.getSelectedFile().getPath();
            if(!path.endsWith(".xml") ) {
                dumpXml.write(new FileOutputStream(jfc.getSelectedFile().getPath()+".xml"));
            }else{
                dumpXml.write(new FileOutputStream(jfc.getSelectedFile().getPath()));
            }

        JOptionPane.showMessageDialog(null, "Exportacion realizada con exito");
    }
    }
}
