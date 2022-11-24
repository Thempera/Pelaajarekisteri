package Rekisteri;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;
import java.util.Comparator;
import java.io.*;

/**
 * Pelaaja-luokka, joka huolehtii omista tiedoistaan.
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 * 
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 *
 */
public class Pelaaja implements Cloneable, Tietue {
    private int    tunnusNro;
    private String nimi = "";
    private String oNimi = "";
    private String ika = "";
    private String kieli = "";
    private String katuosoite= "";
    private String postinumero= "";
    private String postiosoite = "";
    private String puhelin = "";
    private String lisatietoja = "";
    
    private static int seuraavaNro = 1;
    
    private String sallitut = "0123456789";
   
    
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    
    /**
     * Hakee pelaajan kentät
     * @return pelaajan kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 10;
    }
    
    
    /**
     * Hakee ensimmäisen kentän
     * @return ekan järkevän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Alustetaan pelaajan merkkijono-attribuuti tyhjiksi jonoiksi
     * ja tunnusnro = 0.
     */
    public Pelaaja() {
        // Tähän ei tarvita vielä mitään
    }
    
    
    /** 
     * Pelaajien vertailija 
     */ 
    public static class Vertailija implements Comparator<Pelaaja> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Pelaaja pelaaja1, Pelaaja pelaaja2) { 
            return pelaaja1.getAvain(k).compareToIgnoreCase(pelaaja2.getAvain(k)); 
        } 
    } 
     
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + nimi.toUpperCase(); 
        case 2: return "" + oNimi;
        case 3: return "" + ika;
        case 4: return "" + kieli;
        case 5: return "" + katuosoite; 
        case 6: return "" + postinumero; 
        case 7: return "" + postiosoite; 
        case 8: return "" + puhelin;   
        case 9: return "" + lisatietoja; 
        default: return "virhe"; 
        } 
    }


    /**
     * Asettaa online -nimen
     * @param s pelaajalle laitettava online-nimi
     * @return virheilmoitus, null jos ok
     */
    public String setoNimi(String s) {
        oNimi = s;
        return null;
    }
    
    
    /**
     * Asettaa pelaajalle puhelinnumeron
     * @param s pelaajalle laitettava puhelinnumero
     * @return virheilmoitus, null jos ok
     */
    public String setPuhelin(String s) {
        if ( !s.matches("[0-9]*") ) 
            return "Puhelinnumeron oltava numeerinen, ja 0 alkuinen";
        puhelin = s;
        return null;
    }
    
    
    /**
     * Palauttaa tietyn kentän tiedot
     * @param k monennen kentän sisältö palautetaan
     * @return merkkijono kentän sisällöstä
     */
    @Override
    public String anna(int k) {
        switch(k) {
        case 0 : return "" + tunnusNro;
        case 1 : return "" + nimi;
        case 2 : return "" + oNimi;
        case 3 : return "" + ika;
        case 4 : return "" + kieli;
        case 5 : return "" + katuosoite;
        case 6 : return "" + postinumero;
        case 7 : return "" + postiosoite;
        case 8 : return "" + puhelin;
        case 9 : return "" + lisatietoja;
        default: return "virhe";
        }
    }
    
    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.aseta(1,"Sasu Toivonen") === null;
     *   pelaaja.aseta(2,"Sasuke") === null;
     *   pelaaja.aseta(3,"kissa") === "Saa olla vain numeroita.";
     *   pelaaja.aseta(3,"19") === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        String virhe;
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            oNimi = tjono;
            return null;
        case 3:
            virhe = tarkista(tjono);
            if ( virhe != null ) return virhe;
            ika = jono;
            return null;
        case 4:
            kieli = tjono;
            return null;
        case 5:
            katuosoite = tjono;
            return null;
        case 6:
            //postinumero = tjono;
            virhe = tarkista(tjono);
            if ( virhe != null ) return virhe;
            postinumero = tjono;
            return null;
        case 7:
            postiosoite = tjono;
            return null;
        case 8:
            puhelin = tjono;
            return null;
        case 9:
            lisatietoja = Mjonot.erota(sb, '§', lisatietoja);
            return null;
        default:
            return "Virhe";
        }
    }
    
    
    /**
     * Palauttaa k:tta pelaaja kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "Nimi";
        case 2: return "Online-nimi";
        case 3: return "Ikä";
        case 4: return "Kieli";
        case 5: return "Katuosoite";
        case 6: return "Postinumero";
        case 7: return "Postiosoite";
        case 8: return "Puhelin";
        case 9: return "Lisätietoja";
        default: return "Äääliö";
        }
    }

    
    /**
     * Palauttaa pelaajan tiedot merkkijonona, joka tallennetaan tiedostoon.
     * @return pelaajan tiedot merkkijonona ja eroteltuna tolpalla.
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   3  |  Toivonen Sasu   ");
     *   pelaaja.toString().startsWith("3|Toivonen Sasu|") === true; // on enemmäkin kuin 2 kenttää, siksi loppu |
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
     * Selvitää pelaaja tiedot merkkijonosta
     * SeuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta pelaajan tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   3  |  Toivonen Sasu   ");
     *   pelaaja.getTunnusNro() === 3;
     *   pelaaja.toString().startsWith("3|Toivonen Sasu|") === true;
     *
     *   pelaaja.rekisteroi();
     *   int n = pelaaja.getTunnusNro();
     *   pelaaja.parse(""+(n+20));       
     *   pelaaja.rekisteroi();           
     *   pelaaja.getTunnusNro() === n+20+1;     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }

    
    @Override
    public boolean equals(Object pelaaja) {
        if ( pelaaja instanceof Pelaaja ) return equals((Pelaaja)pelaaja);
        return false;
    }

    
    /**
     * Hakee tunnusnumeron
     * @return tunnusnumero
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }

    
    /**
     * Rekisteröi pelaajan
     * @return tunnusnumero
     * @example
     * <pre name="test">
     * Pelaaja tommi = new Pelaaja();
     * tommi.getTunnusNro()===0;
     * tommi.rekisteroi();
     * Pelaaja kaisa = new Pelaaja();
     * kaisa.rekisteroi();
     * int t = tommi.getTunnusNro();
     * int k = kaisa.getTunnusNro();
     * t === k-1
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }

    
    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Tulostetaan henkilön tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro)+" " + nimi+" "+oNimi);
        out.println(ika + " vuotias." );
        out.println("Puhuu kieliä: " + kieli);
        out.println(katuosoite+" "+ postinumero +" "+ postiosoite);
        out.println("puhelin: "+puhelin);
        out.println(lisatietoja);
    }
    
    
    /**
     * Kertoo pelaajan nimen
     * @return pelaajan nimi
     * @example
     * <pre name="test">
     *  Pelaaja sasu = new Pelaaja();
     *  sasu.taytaTiedoillaSasuToivonen();
     *  sasu.getNimi() =R= "Sasu Toivonen .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Palauttaa pelaajan tunnusnumeron
     * @return tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Palauttaa pelaajan online-nimen
     * @return online-nimi
     */
    public String getONimi() {
        return oNimi;
    }
    
    
    /**
     * Palauttaa pelaajan iän
     * @return ikä
     */
    public String getIka() {
        return ika;
    }
    
    
    /**
     * Palauttaa pelaajan käyttämät kielet
     * @return kieli
     */
    public String getKieli() {
        return kieli;
    }
    
    
    /**
     * Palauttaa pelaajan postiosoitteen
     * @return postiosoite
     */
    public String getPostiosoite() {
        return postiosoite;
    }
    
    
    /**
     * Palauttaa pelaajan puhelinnumeron
     * @return puhelinnumero
     */
    public String getPuhelin() {
        return puhelin;
    }
    
    
    /**
     * Palauttaa pelaajan lisätiedot
     * @return lisätiedot
     */
    public String getLisatietoja() {
        return lisatietoja;
    }
    
    
    /**
     * Tehdään identtinen klooni pelaajasta
     * @return Object kloonattu pelaaja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   3  |  Toivonen Sasu   | 123");
     *   Pelaaja kopio = pelaaja.clone();
     *   kopio.toString() === pelaaja.toString();
     *   pelaaja.parse("   4  |  Toivonen Saku   | 123");
     *   kopio.toString().equals(pelaaja.toString()) === false;
     * </pre>
     */
    @Override
    public Pelaaja clone() throws CloneNotSupportedException {
        Pelaaja uusi;
        uusi = (Pelaaja) super.clone();
        return uusi;
    }
    
    
    /**
     * Tutkii onko pelaajan tiedot samat kuin parametrina tuodun pelaajan tiedot
     * @param pelaaja pelaaja johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja1 = new Pelaaja();
     *   pelaaja1.parse("   7  |  Toivonen Saku   | sakkerz");
     *   Pelaaja pelaaja2 = new Pelaaja();
     *   pelaaja2.parse("   7  |  Toivonen Saku   | sakkerz");
     *   Pelaaja pelaaja3 = new Pelaaja();
     *   pelaaja3.parse("   7  |  Toivonen Saku   | saccerz");
     *   
     *   pelaaja1.equals(pelaaja2) === true;
     *   pelaaja2.equals(pelaaja1) === true;
     *   pelaaja1.equals(pelaaja3) === false;
     *   pelaaja3.equals(pelaaja2) === false;
     * </pre>
     */
    public boolean equals(Pelaaja pelaaja) {
        if ( pelaaja == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(pelaaja.anna(k)) ) return false;
        return true;
    }
    
    
    /**
     * Apumetodi, jonka avulla täytetään väliaikaisesti pelaajan arvot 
     */
    public void taytaTiedoillaSasuToivonen() {   
        nimi = "Sasu Toivonen " + rand(1000, 9999);
        oNimi = "Sasuke" ;
        ika = Integer.toString(rand(16, 45));
        kieli = "suomi, siansaksa, englanti";
        katuosoite = "Kiveläntie 67";
        postinumero = "40700";
        postiosoite = "TOIVOLA";
        puhelin = "0100100";
        lisatietoja = "Rouskuttaa kovaäänisesti chilipähkinöitä mikrofonin ääressä";
    }
    
    
    /**
     * Antaa satunnaisen luvun tiettyjen rajojen väliltä
     * @param ala alaraja 
     * @param yla yläraja
     * @return satunnainen luku annettujen rajojen väliltä
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
      }
    
    /**
     * Kysyy onko merkkijono sallittu ja palauttaa virheen tarvittaessa
     * @param jono tarkistettava merkkijono
     * @return virheilmoitus jos virheitä on
     */
    public String tarkista(String jono) {
        if ( onkoVain(jono, sallitut) ) return null;
        return "Saa olla vain numeroita.";
    }
    
    /**
     * Tarkistaa onko annettu merkkijono sallittu
     * @param jono tarkastettava
     * @param sallitut sallitut numerot
     * @return onko vai eikö sallittu merkkijono
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i=0; i<jono.length(); i++)
            if ( sallitut.indexOf(jono.charAt(i)) < 0 ) return false;
        return true;
    }
    

    /**
     * Testiohjelma
     * @param args EI käytössä
     */
    public static void main(String[] args) {
        Pelaaja sasu = new Pelaaja(),sasu2 = new Pelaaja();
    
        sasu.rekisteroi();
        sasu2.rekisteroi();
        
        sasu.taytaTiedoillaSasuToivonen();
        sasu2.taytaTiedoillaSasuToivonen();
        
        
        sasu.tulosta(System.out);
        sasu2.tulosta(System.out);

    }
    
}
