package fxPelaajat;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;

/**
 * Hallinnoidaan tulostamista 
 * TODO Tulostaminen ei vielä toimi 
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class TulostusController implements ModalControllerInterface<String> {

    @FXML TextArea tulostusAlue;
    
    @FXML
    private Button peruutanappi;

    @FXML 
    private void handleTulostus() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if ( job != null && job.showPrintDialog(null) ) {
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }
    
    @Override
    public String getResult() {
        return null;
    } 

    @Override
    public void setDefault(String oletus) {
        if ( oletus == null ) return;
        tulostusAlue.setText(oletus);
    }

    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        //
    }
    
    /**
     * Näyttää tulostusalueessa tekstin
     * @param tulostus tulostettava teskti
     */
    public static void tulosta(String tulostus) {
        ModalController.showModeless(TulostusController.class.getResource("tulostus.fxml"),
                "Tulostus", tulostus);
    }
    
    @FXML 
    private void handlePeruuta() {
        ModalController.closeStage(peruutanappi);
    }
}
