/**
 * Created by Xisco on 18/06/2016.
 */
//Clase proveidro
public class Proveidor {

    private int id_proveidor;
    private String nombre;
    private String telefon;
    private String cif;
    private String activo;
    private String localidad;
    public Adreça adreça;

    //Constructor
    public Proveidor(String nombre, String telefon, String cif, String activo, Adreça adreça) {
        this.nombre = nombre;
        this.telefon = telefon;
        this.cif = cif;
        this.activo = activo;
        this.adreça = adreça;
    }

    public Proveidor(String nombre, String telefon, String cif, String activo) {
        this.nombre = nombre;
        this.telefon = telefon;
        this.cif = cif;
        this.activo = activo;
    }

    public Proveidor(String cif) {
        this.cif = cif;
    }

    public int getId_proveidor() {
        return id_proveidor;
    }

    public void setId_proveidor(int id_proveidor) {
        this.id_proveidor = id_proveidor;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCif() {
        return cif;
    }

    public String getActivo() {
        return activo;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getNombre() {
        return nombre;
    }

}
