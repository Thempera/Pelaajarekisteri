package fxPelaajat;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import Rekisteri.Rekisteri;

/**
 * Kertoo rekisterin tilastoja
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class RekisterinTiedotController implements Initializable, ModalControllerInterface<Rekisteri> {

    @FXML private TextField pelaajatLkm;
    @FXML private TextField pelitLkm;
    @FXML private TextField pelitKeskiarvo;
    @FXML private TextField ikaKeskiarvo;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //setTiedot();        
    }    

    @FXML private void handleCancel() {
        Platform.exit();
    }
    
    //===================================================================
    
    private Rekisteri rekisteri;
    
    /**
     * Pyytää tietoja rekisterin yleistietoja varten
     */
    public void setTiedot(){
        int pelaajat = rekisteri.getPelaajia();
        int pelit = rekisteri.getPelit();
        double keskiarvoPelit = (pelit*1.0)/pelaajat;        
        double keskiarvoIka = (rekisteri.getSummaIka()*1.0)/pelaajat;
 
        pelaajatLkm.setText(Integer.toString(pelaajat));
        pelitLkm.setText(Integer.toString(pelit));
        //pelitKeskiarvo.setText(Double.toString(keskiarvoPelit));
        pelitKeskiarvo.setText(String.format("%.1f", keskiarvoPelit));
        //ikaKeskiarvo.setText(Double.toString((int)Math.round(keskiarvoIka)));
        ikaKeskiarvo.setText(String.format("%.0f", keskiarvoIka));
        
    }

    @Override
    public Rekisteri getResult() {
        return rekisteri;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(Rekisteri oletus) {
        this.rekisteri = oletus;
        setTiedot();
        
    }
    

    
}
