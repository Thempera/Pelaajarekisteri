package fxPelaajat;
    
import Rekisteri.Rekisteri;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class PelaajatMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("PelaajatGUIView.fxml"));
            final Pane root = (Pane)ldr.load(); 
            final PelaajatGUIController rekisteriCtrl = (PelaajatGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("pelaajat.css").toExternalForm());
            primaryStage.setScene(scene);
            
            Rekisteri rekisteri = new Rekisteri();
            rekisteriCtrl.setRekisteri(rekisteri);
            
            
            primaryStage.show();
            
            rekisteriCtrl.lueTiedosto();

            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}
