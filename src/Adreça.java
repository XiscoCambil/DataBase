/**
 * Created by Xisco on 18/06/2016.
 */
public class Adreça {

    private int id_Adreça;
    private String carrer = "";
    private String nPortal = "";
    private String lletraPortal = "";
    private String PisoYLetra = "";
    private String codigoPostal = "";
    private int tipo_Via;
    private int id_localidad;


    public Adreça(String carrer, int id_localidad, String codigoPostal, String pisoYLetra, String lletraPortal, String nPortal, int Tipo_via) {
        this.carrer = carrer;
        this.id_localidad = id_localidad;
        this.codigoPostal = codigoPostal;
        this.PisoYLetra = pisoYLetra;
        this.lletraPortal = lletraPortal;
        this.nPortal = nPortal;
        this.tipo_Via = Tipo_via;

    }

    public String getCarrer() {
        return carrer;
    }

    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
    }

    public int getId_Adreça() {
        return id_Adreça;
    }

    public void setId_Adreça(int id_Adreça) {
        this.id_Adreça = id_Adreça;
    }

    public String getLletraPortal() {
        return lletraPortal;
    }

    public String getPisoYLetra() {
        return PisoYLetra;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }


    public int getTipo_Via() {
        return tipo_Via;
    }

    public String getnPortal() {
        return nPortal;
    }
}