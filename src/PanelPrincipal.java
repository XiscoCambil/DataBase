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
    private JFrame frame = new JFrame("Proveedor");

    public PanelPrincipal(){
        frame.add(insertarProveidorButton);
        frame.add(numeroDeProveedoresButton);
        frame.add(consultarProveedorButton);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        insertarProveidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InsertDialog id = new InsertDialog(frame);
                id.pack();
                id.setVisible(true);
            }
        });
        numeroDeProveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JOptionPane.showMessageDialog(null,"Numero de proveedores: "+Programa.db.maxCodiProveidor());
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
                ConsultarProveedor cp = new ConsultarProveedor();
                cp.pack();;
                cp.setVisible(true);
            }
        });
    }
}
