package fxPelaajat;

import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static fxPelaajat.TietueDialogController.getFieldId;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import Rekisteri.Pelaaja;
import Rekisteri.Rekisteri;
import Rekisteri.Peli;
import Rekisteri.SailoException;

/**
 * Rekisterin käyttöliittymän toimintoja varten luotu luokka
 * 
 * @author Henna ja Tuuli
 * @version 15.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class PelaajatGUIController implements Initializable {
	
    @FXML private TextField hakuehto; 
    @FXML private ComboBoxChooser<String> cbKentat; 
    @FXML private Label labelVirhe;
    
    @FXML private ScrollPane panelPelaaja;
    @FXML private GridPane gridPelaaja;
    @FXML private ListChooser<Pelaaja> chooserPelaajat;
    @FXML private StringGrid<Peli> tablePelit;
    
    @FXML private TextField pelaajia;
    @FXML private TextField peleja;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();    
    }
    
    @FXML private void handleHakuehto() {
        hae(0); 
    }
    
    @FXML private void handlePoistaPeli() {
        poistaPeli();
    }
    
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    @FXML private void handleSulje() {
        tallenna();
        Platform.exit();
    }
    
    @FXML private void handlePeruuta() {
        Platform.exit();
    }
    
    @FXML private void handleRekisterinTietoja() {
        rekisterinTietoja();
        ModalController.showModal(RekisterinTiedotController.class.getResource("rekisterinTiedotView.fxml"),"Rekisterin tietoja", null, rekisteri);
    }
    
    @FXML private void handleTietoja() {
        ModalController.showModal(PelaajatGUIController.class.getResource("tietoja.fxml"),"Tietoja", null,"");
    }
    
    @FXML private void handlePoistaPelaaja() {
        poistaPelaaja();
    }
    
    @FXML private void handleUusiPelaaja() {
        uusiPelaaja();
    }
    
    @FXML private void handleUusiPeli() {
        uusiPeli();
    }
    
    @FXML private void handleMuokkaaPelaajaa() {
        muokkaa(kentta);
    }
    
    @FXML private void handleTulosta() {
        TulostusController.tulosta(null);
    }

    
    // ===============================================================
    
    
    
    private String rekisterinnimi = "pelaajat";
    private Rekisteri rekisteri;
    private Pelaaja pelaajaKohdalla;
    private TextField[] edits;
    private int kentta = 0;
    private static Pelaaja apupelaaja = new Pelaaja(); 
    private static Peli apupeli = new Peli();

    
    /**
     * Tallentaminen
     * @return tulostus
     */
    private String tallenna() {
        try {
            rekisteri.tallenna();
            return null;
            
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia. " + ex.getMessage());
            return ex.getMessage();
        }
    }  
    
    
    /**
     * @return Alustaa rekisterin
     */
    protected String lueTiedosto() {
        //setTitle("Kerho - " + rekisterinnimi);
        try {
            rekisteri.lueTiedostosta(rekisterinnimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }

    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa pelaajien tiedot.
     * Alustetaan myös pelaajalistan kuuntelija 
     */
    protected void alusta() {       
        chooserPelaajat.clear();
        chooserPelaajat.addSelectionListener(e -> naytaPelaaja());
        
        cbKentat.clear(); 
        for (int k = apupelaaja.ekaKentta(); k < apupelaaja.getKenttia(); k++) 
            cbKentat.add(apupelaaja.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0); 
        
        edits = TietueDialogController.luoKentat(gridPelaaja, apupelaaja);
        for (TextField edit: edits)  
            if ( edit != null ) {  
                edit.setEditable(false);  
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);});  
            }
        
        // alustetaan pelitaulukon otsikot 
        int eka = apupeli.ekaKentta(); 
        int lkm = apupeli.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apupeli.getKysymys(k); 
        tablePelit.initTable(headings); 
        tablePelit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tablePelit.setEditable(false); 
        tablePelit.setPlaceholder(new Label("Ei vielä pelejä")); 
         
        // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
        tablePelit.setColumnSortOrderNumber(1); 
        tablePelit.setColumnSortOrderNumber(2); 
        tablePelit.setColumnWidth(1, 60); 
        tablePelit.setColumnWidth(2, 60); 
        
        //tablePelit.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaPeliä(); } );
        //tablePelit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaPeliä();}); 
    }
        

    /**
     * Uusi jäsen
     */
    protected void uusiPelaaja() {
        try {
            Pelaaja uusi = new Pelaaja();
            uusi = TietueDialogController.kysyTietue(null, uusi, 1);
            if (uusi == null) return;
            uusi.rekisteroi();
            rekisteri.lisaa(uusi);
            rekisterinTietoja();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        //hae(uusi.getTunnusNro());
    }
    
    
    /**
     * Poistetaan pelitaulukosta valitulla kohdalla oleva peli. 
     */
    private void poistaPeli() {
        int rivi = tablePelit.getRowNr();
        if ( rivi < 0 ) return;
        Peli peli = tablePelit.getObject();
        if ( peli == null ) return;
        rekisteri.poistaPeli(peli);
        rekisterinTietoja();
        naytaPelit(pelaajaKohdalla);
        int peleja = tablePelit.getItems().size(); 
        if ( rivi >= peleja ) rivi = peleja -1;
        tablePelit.getFocusModel().focus(rivi);
        tablePelit.getSelectionModel().select(rivi);
    }


    /**
     * Poistetaan listalta valittu pelaaja
     */
    private void poistaPelaaja() {
        Pelaaja pelaaja = pelaajaKohdalla;
        if ( pelaaja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko pelaaja: " + pelaaja.getNimi(), "Kyllä", "Ei") )
            return;
        rekisteri.poista(pelaaja);
        rekisterinTietoja();
        int index = chooserPelaajat.getSelectedIndex();
        hae(0);
        chooserPelaajat.setSelectedIndex(index);
    }
    
    
    /**
     * Jäsenhaku
     * @param jnr jäsennumero
     */
    protected void hae(int jnr) {
        int jnro = jnr; // jnro pelaajan numero, joka aktivoidaan haun jälkeen 
        if ( jnro <= 0 ) { 
            Pelaaja kohdalla = pelaajaKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apupelaaja.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        
        chooserPelaajat.clear();

        int index = 0;
        Collection<Pelaaja> pelaajat;
        try {
            pelaajat = rekisteri.etsi(ehto, k);
            int i = 0;
            for (Pelaaja pelaaja:pelaajat) {
                if (pelaaja.getTunnusNro() == jnro) index = i;
                chooserPelaajat.add(pelaaja.getNimi(), pelaaja);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserPelaajat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
    }

    
    /**
     * Tekee uuden tyhjän pelin
     */
    private void uusiPeli() {
        if ( pelaajaKohdalla == null ) return;
        try {
            Peli uusi = new Peli(pelaajaKohdalla.getTunnusNro());
            uusi = TietueDialogController.kysyTietue(null, uusi, 0);
            if ( uusi == null ) return;
            uusi.rekisteroi();
            rekisteri.lisaa(uusi);
            rekisterinTietoja();
            naytaPelit(pelaajaKohdalla); 
            tablePelit.selectRow(1000);  // järjestetään viimeinen rivi valituksi
        } catch (SailoException e) {         
            Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
        }
    }
    
    
    /**
     * Muokkaa peliä
     * @param peli
     */
    @SuppressWarnings("unused")
    private void muokkaaPelia() {
        int r = tablePelit.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Peli peli = tablePelit.getObject();
        if ( peli == null ) return;
        int k = tablePelit.getColumnNr()+peli.ekaKentta();
        try {
            peli = TietueDialogController.kysyTietue(null, peli.clone(), k);
            if ( peli == null ) return;
            rekisteri.korvaaTaiLisaa(peli); 
            naytaPelit(pelaajaKohdalla); 
            tablePelit.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }

    
    /**
     * @param rekisteri rekisteri jota käsitellään
     */
    public void setRekisteri(Rekisteri rekisteri) {
       this.rekisteri = rekisteri;
       naytaPelaaja();    
    }
    
    /**
     * Muokkaaminen
     * @param k kentän numero
     */
    private void muokkaa(int k) { 
        if ( pelaajaKohdalla == null ) return; 
        try { 
            Pelaaja pelaaja; 
            pelaaja = TietueDialogController.kysyTietue(null, pelaajaKohdalla.clone(), k);  
            if ( pelaaja == null ) return; 
            rekisteri.korvaaTaiLisaa(pelaaja); 
            hae(pelaaja.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }     
    
    /**
     * Näyttää tietyn pelaajan pelit
     * @param pelaaja pelaaja, jolla halutaan pelit
     */
    private void naytaPelit(Pelaaja pelaaja) {
        tablePelit.clear();
        if ( pelaaja == null ) return;
        
        try {
            List<Peli> pelit = rekisteri.annaPelit(pelaaja);
            if ( pelit.size() == 0 ) return;
            for (Peli peli: pelit)
                naytaPeli(peli);       
        } catch (SailoException e) {
          naytaVirhe(e.getMessage());
        }        
    }
    
    
    /**
     * Näyttää mikäli on tullut virhe
     * @param virhe
     */
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
     * Näyttää tietyn pelin
     * @param peli peli, joka halutaan
     */
    private void naytaPeli(Peli peli) {
        int kenttia = peli.getKenttia(); 
        String[] rivi = new String[kenttia-peli.ekaKentta()]; 
        for (int i=0, k=peli.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = peli.anna(k); 
        tablePelit.add(peli,rivi);
    }

    
    /**
     * Näyttää pelaajan tiedot
     */
    private void naytaPelaaja() {
        rekisterinTietoja();
        pelaajaKohdalla = chooserPelaajat.getSelectedObject();
        
        if (pelaajaKohdalla == null) return;
        
        TietueDialogController.naytaTietue(edits, pelaajaKohdalla);
        naytaPelit(pelaajaKohdalla);
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     * @param pelaaja kyseinen pelaaja
     */
    public void tulosta(PrintStream os, final Pelaaja pelaaja) {
        os.println("----------------------------------------------");
        pelaaja.tulosta(os);
        os.println("----------------------------------------------");
        try {
            List<Peli> pelit = rekisteri.annaPelit(pelaaja);   
            for (Peli peli:pelit)
                peli.tulosta(os); 
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Pelien hakemisessa ongelmia! " + ex.getMessage());
        }
    }
    
    /**
     * Hakee ja asettaa rekisterin tietoja esille
     */
    public void rekisterinTietoja(){
        int pelaajiaLkm = rekisteri.getPelaajia();
        int pelitLkm = rekisteri.getPelit();
        pelaajia.setText(Integer.toString(pelaajiaLkm));
        peleja.setText(Integer.toString(pelitLkm));
        
    }
    
}
    