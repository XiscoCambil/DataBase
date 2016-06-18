/**
 * Created by Xisco on 18/06/2016.
 */
public class TipusCarrer {

    private final String descripcio;
    public final String abrev;
    private final int id_tipus_adreça;

    //Constructor de TipusCarrer
    public TipusCarrer(int id_tipus_adreça, String descripcio, String abrev) {
        this.id_tipus_adreça = id_tipus_adreça;
        this.descripcio = descripcio;
        this.abrev = abrev;
    }
}

