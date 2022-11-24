package Rekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Rekisterin pelaajat, jotka osaavat esim. lisätä pelaajan.
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */ 
public class Pelaajat implements Iterable<Pelaaja> {
    private static final int MAX_PELAAJIA = 15;
    private boolean muutettu = false;
    private int lkm = 0;
    private String tiedostonPerusNimi = "nimet";
    private Pelaaja alkiot[] = new Pelaaja[MAX_PELAAJIA];

    
    /**
     * Oletusmuodostaja
     */
    public Pelaajat() {
        // Oletusmuodostaja tarpeeton, koska attribuuttien oma alustus riittää.
    }
    
    /**
     * Käy läpi kaikki pelaajat ja palauttaa pelaajien ikien summan
     * @return kaikkien rekisteröityjen pelaajien ikien summa
     */
    public int getSummaIka() {
        int summa = 0;
        String ika;
        for (Pelaaja pelaaja : this) {
            if (pelaaja.getIka()!= null || !pelaaja.getIka().equals("")) {
                ika = pelaaja.getIka();
                summa += Integer.parseInt(ika);
            }
        }
        return summa;
    }
    
    /**
     * Palauttaa rekisteröityjen pelaajien lukumäärän
     * @return pelaajien lukumäärä
     */
    public int getPelaajia() {
        return lkm;
    }
    
    
    /**
     * @param pelaaja Lisättävä pelaaja
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Pelaajat pelaajat = new Pelaajat();
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja();
     * 
     * pelaajat.getLkm() === 0;
     * pelaajat.lisaa(sasu1); 
     * pelaajat.getLkm() === 1;
     * pelaajat.lisaa(sasu2); 
     * pelaajat.getLkm() === 2;
     * pelaajat.lisaa(sasu1); 
     * pelaajat.getLkm() === 3;
     * Iterator<Pelaaja> it = pelaajat.iterator(); 
     * it.next() === sasu1;
     * it.next() === sasu2; 
     * it.next() === sasu1; 
     * pelaajat.lisaa(sasu1); 
     * pelaajat.getLkm() === 4;
     * pelaajat.lisaa(sasu1); 
     * pelaajat.getLkm() === 5;
     * </pre>  
     */
    public void lisaa(Pelaaja pelaaja) {
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, alkiot.length+10);
        alkiot[lkm] = pelaaja;
        lkm++;
        muutettu = true;
    }
    
    
    /**
     * Korvaa pelaajan tietorakenteessa.  Ottaa pelaajan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva pelaaja.  Jos ei löydy,
     * niin lisätään uutena pelaajana.
     * @param pelaaja lisättävän pelaajan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Pelaajat pelaajat = new Pelaajat();
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja();
     * sasu1.rekisteroi(); sasu2.rekisteroi();
     * pelaajat.getLkm() === 0;
     * pelaajat.korvaaTaiLisaa(sasu1); pelaajat.getLkm() === 1;
     * pelaajat.korvaaTaiLisaa(sasu2); pelaajat.getLkm() === 2;
     * Pelaaja sasu3 = sasu1.clone();
     * Iterator<Pelaaja> it = pelaajat.iterator();
     * it.next() == sasu1 === true;
     * pelaajat.korvaaTaiLisaa(sasu3); pelaajat.getLkm() === 2;
     * it = pelaajat.iterator();
     * Pelaaja p0 = it.next();
     * p0 === sasu3;
     * p0 == sasu3 === true;
     * p0 == sasu1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Pelaaja pelaaja) throws SailoException {
        int id = pelaaja.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = pelaaja;
                muutettu = true;
                return;
            }
        }
        lisaa(pelaaja);
    }
    
    
    /**
     * Palauttaa viitteen i:teen pelaajaan.
     * @param i Monesko jäsen
     * @return Jäsenen, jonka indeksi on i, viite
     * @throws IndexOutOfBoundsException  Jos i ei ole sallitulla alueella  
     */
    public Pelaaja anna(int i) throws IndexOutOfBoundsException {
        if ( i < 0 || lkm <= i ) 
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /** 
     * Poistaa pelaajan jolla on valittu tunnusnumero  
     * @param id poistettavan pelaajan tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja(), sasu3 = new Pelaaja(); 
     * sasu1.rekisteroi(); sasu2.rekisteroi(); sasu3.rekisteroi(); 
     * pelaajat.lisaa(sasu1); pelaajat.lisaa(sasu2); pelaajat.lisaa(sasu3); 
     * int id1 = sasu1.getTunnusNro(); 
     * pelaajat.poista(id1+1) === 1; 
     * pelaajat.annaId(id1+1) === null; pelaajat.getLkm() === 2; 
     * pelaajat.poista(id1) === 1; pelaajat.getLkm() === 1; 
     * pelaajat.poista(id1+3) === 0; pelaajat.getLkm() === 1; 
     * </pre> 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Lukee rekisterin pelaajat tiedostosta
     * @param tied tiedosto
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Pelaajat pelaajat = new Pelaajat();
     *  Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja();
     *  sasu1.taytaTiedoillaSasuToivonen();
     *  sasu2.taytaTiedoillaSasuToivonen();
     *  String hakemisto = "testirekisteri";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  
     *  pelaajat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  pelaajat.lisaa(sasu1);
     *  pelaajat.lisaa(sasu2);
     *  pelaajat.tallenna();
     *  pelaajat = new Pelaajat();            // Poistetaan vanhat luomalla uusi
     *  pelaajat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Pelaaja> i = pelaajat.iterator();
     *  i.next() === sasu1;
     *  i.next() === sasu2;
     *  i.hasNext() === false;
     *  pelaajat.lisaa(sasu2);
     *  pelaajat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
           
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Pelaaja pelaaja = new Pelaaja();
                pelaaja.parse(rivi); 
                lisaa(pelaaja);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
   }
    
    
    /**
    * Luetaan aikaisemmin annetun nimisestä tiedostosta
    * @throws SailoException jos tulee poikkeus
    */
   public void lueTiedostosta() throws SailoException {
       lueTiedostosta(getTiedostonPerusNimi());
   }

    
    /**
     * Tallentaa pelaajat tiedostoon
     * @throws SailoException jos tallentaminen epäonnistuu
     */
   public void tallenna() throws SailoException {
       if ( !muutettu ) return;

       File fbak = new File(getBakNimi());
       File ftied = new File(getTiedostonNimi());
       fbak.delete(); 
       ftied.renameTo(fbak); 

       try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
           fo.println(alkiot.length);
           for (Pelaaja pelaaja : this) {
               fo.println(pelaaja.toString());
           }
       } catch ( FileNotFoundException ex ) {
           throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
       } catch ( IOException ex ) {
           throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
       }

       muutettu = false;
   }
    
    
    /**
     * Palauttaa rekisterin pelaajien lukumäärän
     * @return Pelaajien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }

    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Luokka pelaajien iteroimiseksi.
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Pelaajat pelaajat = new Pelaajat();
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja();
     * sasu1.rekisteroi(); sasu2.rekisteroi();
     *
     * pelaajat.lisaa(sasu1); 
     * pelaajat.lisaa(sasu2); 
     * pelaajat.lisaa(sasu1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Pelaaja pelaaja:pelaajat)   // for-silmukan toimivuus
     *   ids.append(" "+pelaaja.getTunnusNro());           
     * 
     * String tulos = " " + sasu1.getTunnusNro() + " " + sasu2.getTunnusNro() + " " + sasu1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Pelaaja>  i=pelaajat.iterator(); i.hasNext(); ) { // iteraattorin toimivuus
     *   Pelaaja pelaaja = i.next();
     *   ids.append(" "+pelaaja.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Pelaaja>  i=pelaajat.iterator();
     * i.next() == sasu1  === true;
     * i.next() == sasu2  === true;
     * i.next() == sasu1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class PelaajatIterator implements Iterator<Pelaaja> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa pelaajaa
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä pelaajia
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava pelaaja
         * @return seuraava pelaaja
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Pelaaja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori pelaajistaan.
     * @return pelaaja iteraattori
     */
    @Override
    public Iterator<Pelaaja> iterator() {
        return new PelaajatIterator();
    }
    

    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien pelaajien viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä pelaajista 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Pelaajat Pelaajat = new Pelaajat(); 
     *   Pelaaja pelaaja1 = new Pelaaja(); pelaaja1.parse("1|Sasu Toivonen|Sasuke"); 
     *   Pelaaja pelaaja2 = new Pelaaja(); pelaaja2.parse("2|Liisa Saaristo|Lisa"); 
     *   Pelaaja pelaaja3 = new Pelaaja(); pelaaja3.parse("3|Topi Mäki|Topi"); 
     *   Pelaaja pelaaja4 = new Pelaaja(); pelaaja4.parse("4|Linda Toivo|Lindi"); 
     *   Pelaaja pelaaja5 = new Pelaaja(); pelaaja5.parse("5|Kati Lohja|Kati"); 
     *   Pelaajat.lisaa(pelaaja1); Pelaajat.lisaa(pelaaja2); Pelaajat.lisaa(pelaaja3); Pelaajat.lisaa(pelaaja4); Pelaajat.lisaa(pelaaja5);
     *   List<Pelaaja> loytyneet;  
     *   loytyneet = (List<Pelaaja>)Pelaajat.etsi("*s*",1);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == pelaaja2 === true;  
     *   loytyneet.get(1) == pelaaja1 === true;  
     *     
     *   loytyneet = (List<Pelaaja>)Pelaajat.etsi("*l*",2);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == pelaaja4 === true;  
     *   loytyneet.get(1) == pelaaja2 === true; 
     *     
     *   loytyneet = (List<Pelaaja>)Pelaajat.etsi(null,-1);  
     *   loytyneet.size() === 5; 
     * </pre> 
     */ 
    public Collection<Pelaaja> etsi(String hakuehto, int k) {
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 0; // jotta etsii id:n mukaan
        List<Pelaaja> loytyneet = new ArrayList<Pelaaja>(); 
        for (Pelaaja pelaaja : this) { 
            if (WildChars.onkoSamat(pelaaja.anna(hk), ehto)) loytyneet.add(pelaaja);  
        }
        Collections.sort(loytyneet, new Pelaaja.Vertailija(hk));
        return loytyneet; 
    }
    
    
    /** 
     * Etsii pelaajan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return pelaaja jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja(), sasu3 = new Pelaaja(); 
     * sasu1.rekisteroi(); sasu2.rekisteroi(); sasu3.rekisteroi(); 
     * int id1 = sasu1.getTunnusNro(); 
     * pelaajat.lisaa(sasu1); pelaajat.lisaa(sasu2); pelaajat.lisaa(sasu3); 
     * pelaajat.annaId(id1  ) == sasu1 === true; 
     * pelaajat.annaId(id1+1) == sasu2 === true; 
     * pelaajat.annaId(id1+2) == sasu3 === true; 
     * </pre> 
     */ 
    public Pelaaja annaId(int id) { 
        for (Pelaaja pelaaja : this) { 
            if (id == pelaaja.getTunnusNro()) return pelaaja; 
        } 
        return null; 
    } 


    /** 
     * Etsii pelaajan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen pelaajan indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja sasu1 = new Pelaaja(), sasu2 = new Pelaaja(), sasu3 = new Pelaaja(); 
     * sasu1.rekisteroi(); sasu2.rekisteroi(); sasu3.rekisteroi(); 
     * int id1 = sasu1.getTunnusNro(); 
     * pelaajat.lisaa(sasu1); pelaajat.lisaa(sasu2); pelaajat.lisaa(sasu3); 
     * pelaajat.etsiId(id1+1) === 1; 
     * pelaajat.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    }


    /**
     * Testiohjelma pelaajille
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        Pelaajat pelaajat = new Pelaajat();
        
        Pelaaja sasu = new Pelaaja();
        Pelaaja toinenSasu = new Pelaaja();
        sasu.rekisteroi();
        toinenSasu.rekisteroi();
        
        sasu.taytaTiedoillaSasuToivonen();
        toinenSasu.taytaTiedoillaSasuToivonen();
        
        
            pelaajat.lisaa(sasu);
            pelaajat.lisaa(toinenSasu);

            System.out.println("========= Pelaajat testi ==============");

            int i = 0;
            for (Pelaaja pelaaja: pelaajat) { 
                System.out.println("Pelaaja nro: " + i++);
                pelaaja.tulosta(System.out);
                }

    }
    
    
}
