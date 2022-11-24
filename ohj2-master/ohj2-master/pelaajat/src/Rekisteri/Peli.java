package Rekisteri;

import java.io.*;
import kanta.Tietue;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Peli-luokka, joka huolehtii omista tiedoistaan 
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 * 
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class Peli implements Cloneable, Tietue {
    private int tunnusNro;
    private int jasenNro;
    private String nimi = "";
    private String pelialusta = "";

    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan peli
     */
    public Peli() {
        // vielä tyhjä
    }
    

    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                jasenNro = Mjonot.erota(sb, '$', jasenNro);
                return null;
            case 2:
                nimi = st;
                return null;
            case 3:
                pelialusta = st;
                return null;
            default:
                return "Väärä kentän indeksi";
        }
    }


    /**
     * Tehdään identtinen klooni pelistä
     * @return Object kloonattu peli
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Borderlands 2  | PS4  ");
     *   Peli kopio = peli.clone();
     *   kopio.toString() === peli.toString();
     *   peli.parse("   1   |  11  |   Destiny  | PC  ");
     *   kopio.toString().equals(peli.toString()) === false;
     * </pre>
     */
    @Override
    public Peli clone() throws CloneNotSupportedException { 
        return (Peli)super.clone();
    }

    
    /**
     * Tietyn pelaajan pelin alustaminen
     * @param jasenNro pelaajan viitenumero
     */
    public Peli(int jasenNro) {
        this.jasenNro = jasenNro;
    }
    
    
    /**
     * Kertoo kenttien määrän
     * @return pelin kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 4;
    }

    
    /**
     * Ensimmäinen kenttä, johon pelaaja voi syöttää tietoja
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    
    
    /**
     * Kysyy kenttään haluttavan tiedon
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "jäsenId";
            case 2:
                return "Peli";
            case 3:
                return "Pelialusta";
            default:
                return "virhe";
        }
    }
    

    /**
     * Kertoo valitun kentän tiedot
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Minecraft  | PC  ");
     *   peli.anna(0) === "2";   
     *   peli.anna(1) === "10";   
     *   peli.anna(2) === "Minecraft";   
     *   peli.anna(3) === "PC";    
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
                return "" + jasenNro;
            case 2:
                return "" + nimi;
            case 3:
                return "" + pelialusta;
            default:
                return "";
        }
    }
    
    
    /**
     * Asettaa tunnusnumeron
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    
    
    /**
     * Erottelee pelien tiedot
     * @param rivi josta saadaan pelin tiedot
     * @example
     * <pre name="test">
     *  Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Minecraft  | PC  ");
     *   peli.getJasenNro() === 10;
     *   peli.toString()    === "2|10|Minecraft|PC";
     *   
     *   peli.rekisteroi();
     *   int n = peli.getTunnusNro();
     *   peli.parse(""+(n+20));
     *   peli.rekisteroi();
     *   peli.getTunnusNro() === n+20+1;
     *   peli.toString()     === "" + (n+20+1) + "|10||";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    

    /**
     * Palauttaa pelin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return peli tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   2   |  10  |   Borderlands 2  | PS4  ");
     *   peli.toString()    === "2|10|Borderlands 2|PS4";
     * </pre>
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
     }

    /**
     * Pelin kloonia
     */
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }

    
    /**
     * Palauttaa tunnusnumeron
     * @return tunnusnumero
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }
    
    
    /**
     * tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Tulostetaan pelin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(nimi + " " + pelialusta);
    }

    
    /**
     * Apumetodi, jolla täytetään väliaikaisesti arvoja pelille.
     * @param nro pelin pelaajan viite
     */
    public void taytaTiedoillaFreddy(int nro) {
        jasenNro = nro;
        nimi = "Five nights at Freddy's" ;
        pelialusta = "PC" ;
    }
    
    
    /**
     * Antaa pelille seuraavan rekisterinumeron.
     * @return pelin uusi tunnusNro
     * @example
     * <pre name="test">
     *   Peli freddy1 = new Peli();
     *   freddy1.getTunnusNro() === 0;
     *   freddy1.rekisteroi();
     *   Peli freddy2 = new Peli();
     *   freddy2.rekisteroi();
     *   int n1 = freddy1.getTunnusNro();
     *   int n2 = freddy2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * Palauttaa pelin tunnusnumeron(id)
     * @return pelin tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Palauttaa mille pelaajalle peli kuuluu
     * @return Pelaajan jäsennumero
     */
    public int getJasenNro() {
        return jasenNro;
    }

    
    /**
     * Testiohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Peli peli = new Peli();
        peli.taytaTiedoillaFreddy(2);
        peli.tulosta(System.out);

    }

}
