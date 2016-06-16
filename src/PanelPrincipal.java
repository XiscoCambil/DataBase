import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by fjcambilr on 13/06/16.
 */
public class PanelPrincipal {
    private JButton insertarProveidorButton;
    private JButton numeroDeProveedoresButton;
    private JButton consultarProveedorButton;
    private JPanel panel;
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
    }
}
