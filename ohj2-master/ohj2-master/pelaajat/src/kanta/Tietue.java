package kanta;


/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa ns. attribuutit.
 * 
 * @author Tuuli ja Henna
 * @version 13.5.2020
 * @example
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public interface Tietue {

    
    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import rekisteri.Peli;
     *   Peli peli = new Peli();
     *   peli.getKenttia() === 3;
     * </pre>
     */
    public abstract int getKenttia();


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.getKysymys(2) === "nimi";
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Borderlands 2  |  PS4  ");
     *   peli.anna(0) === "2";   
     *   peli.anna(1) === "10";   
     *   peli.anna(2) === "Borderlands 2";
     *   peli.anna(3) === "PS4";   
     * </pre>
     */
    public abstract String anna(int k);

    
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Bordeslands 2  |  PS4  ");
     *   Object kopio = peli.clone();
     *   kopio.toString() === peli.toString();
     *   peli.parse("   1   |  11  |   Destiny  |  PC  ");
     *   kopio.toString().equals(peli.toString()) === false;
     *   kopio instanceof Peli === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Borderlands 2  |  PS4  ");
     *   peli.toString()    =R= "2\\|10\\|Borderlands 2\\|PS4.*";
     * </pre>
     */
    @Override
    public abstract String toString();

}
