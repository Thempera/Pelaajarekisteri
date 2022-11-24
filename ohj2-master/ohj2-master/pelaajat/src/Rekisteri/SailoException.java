package Rekisteri;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville ongelmille
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Poikkeuksen muodostaja
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }

}
