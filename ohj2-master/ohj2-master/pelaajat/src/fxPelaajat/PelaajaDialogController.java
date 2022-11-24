package fxPelaajat;

import java.net.URL;
import java.util.ResourceBundle;

import Rekisteri.Pelaaja;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Luodaan dialogi, jolla kysytään pelaajan tiedot
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 * 
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 *
 */
public class PelaajaDialogController  implements ModalControllerInterface<Pelaaja>,Initializable {

    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelPelaaja;
    @FXML private GridPane gridPelaaja;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
    
    @FXML private void handleOK() {
        if ( pelaajaKohdalla != null && pelaajaKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
        pelaajaKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
//========================================================

    private Pelaaja pelaajaKohdalla;
    private static Pelaaja apupelaaja = new Pelaaja(); // Pelaaja jolta voidaan kysellä tietoja.
    private TextField[] edits;
    private int kentta = 0;
    

    /**
     * Luodaan GridPaneen jäsenen tiedot
     * @param gridPelaaja mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridPelaaja) {
        gridPelaaja.getChildren().clear();
        TextField[] edits = new TextField[apupelaaja.getKenttia()];
        
        for (int i=0, k = apupelaaja.ekaKentta(); k < apupelaaja.getKenttia(); k++, i++) {
            Label label = new Label(apupelaaja.getKysymys(k));
            gridPelaaja.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridPelaaja.add(edit, 1, i);
        }
        return edits;
    }
    
    
    /**
    * Palautetaan komponentin id:stä saatava luku
    * @param obj tutkittava komponentti
    * @param oletus mikä arvo jos id ei ole kunnollinen
    * @return komponentin id lukuna 
    */
   public static int getFieldId(Object obj, int oletus) {
       if ( !( obj instanceof Node)) return oletus;
       Node node = (Node)obj;
       return Mjonot.erotaInt(node.getId().substring(1),oletus);
   }
   
    
   /**
    * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
    * yksi iso tekstikenttä, johon voidaan tulostaa pelaajien tiedot.
    */
    protected void alusta() {       
        edits = luoKentat(gridPelaaja);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosPelaajaan((TextField)(e.getSource())));
        panelPelaaja.setFitToHeight(true);
    
    }
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    /**
     * Käsitellään pelaajaan tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosPelaajaan(TextField edit) {
        if (pelaajaKohdalla == null) return;
        int k = getFieldId(edit,apupelaaja.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = pelaajaKohdalla.aseta(k,s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Tyhjentään pelaajan tekstikentät
     * @param edits tyhjennettävät kentät 
     */
    public static void tyhjenna(TextField[] edits) {        
        for (TextField edit: edits) 
            if ( edit != null ) edit.setText(""); 
    }
    
    @Override
    public Pelaaja getResult() {
        return pelaajaKohdalla;
    }
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apupelaaja.ekaKentta(), Math.min(kentta, apupelaaja.getKenttia()-1));
        edits[kentta].requestFocus(); 
    }

    @Override
    public void setDefault(Pelaaja oletus) {
        pelaajaKohdalla = oletus;
        naytaPelaaja(pelaajaKohdalla);
    }
    

    /**
     * Määrittää pelaajan tekstikenttien tiedot
     * @param edits kentät
     * @param pelaaja pelaaja jonka tiedot haetaan ja annetaan
     */
    public static void naytaPelaaja(TextField[] edits,Pelaaja pelaaja) {
        if (pelaaja == null) return;
        for (int k = pelaaja.ekaKentta(); k < pelaaja.getKenttia(); k++) {
            edits[k].setText(pelaaja.anna(k));
        }
        
    }
    
    
    /**
     * Määrittää pelaajan tekstikenttien tiedot
     * @param pelaaja pelaaja jonka tiedot haetaan ja annetaan
     */
    public void naytaPelaaja(Pelaaja pelaaja) {
        naytaPelaaja(edits,pelaaja);          
    }
    
    
    /**
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan cancel, muuten täytetty tietue
     */
    public static Pelaaja kysyPelaaja(Stage modalityStage, Pelaaja oletus, int kentta) {
        return ModalController.<Pelaaja, PelaajaDialogController>showModal(
                PelaajaDialogController.class.getResource("UusiPelaaja.fxml"),
                "Rekisteri",
                modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta)
                );
    }
       
}
