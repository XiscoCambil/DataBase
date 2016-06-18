import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileInputStream;

/**
 * Created by Xisco on 18/06/2016.
 */
public class Programa {

    static DataBase db;

    public static void main(String[] args) throws Exception {
        String username;
        String password;
        String server;
        SimpleXML configXml = new SimpleXML(new FileInputStream("src/config.xml"));
        Document doc = configXml.getDoc();
        Element raiz = doc.getDocumentElement();
        username = configXml.getElement(raiz, "usuario").getTextContent();
        password = configXml.getElement(raiz, "password").getTextContent();
        server = configXml.getElement(raiz, "server").getTextContent();
        db = new DataBase(server, username, password);

    }
}

