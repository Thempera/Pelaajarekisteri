package Rekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;
/**
 * Rekisteri, joka huolehtii pelaajista.
 * 
 * @author Henna ja Tuuli
 * @version 14.5.2020
 * 
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 * 
 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 *  private Rekisteri rekisteri;
 *  private Pelaaja sasu1;
 *  private Pelaaja sasu2;
 *  
 *  private int jid1;
 *  private int jid2;
 *  
 *  private Peli freddy21;
 *  private Peli freddy11;
 *  private Peli freddy22;
 *  private Peli freddy12; 
 *  private Peli freddy23; 
 *  
 *  public void alustaRekisteri() {
 *    rekisteri = new Rekisteri();
 *    sasu1 = new Pelaaja(); sasu1.taytaTiedoillaSasuToivonen(); sasu1.rekisteroi(); 
 *    sasu2 = new Pelaaja(); sasu2.taytaTiedoillaSasuToivonen(); sasu2.rekisteroi(); 
 *
 *    jid1 = sasu1.getTunnusNro();
 *    jid2 = sasu2.getTunnusNro();
 *    
 *    freddy21 = new Peli(jid2); freddy21.taytaTiedoillaFreddy(jid2);
 *    freddy11 = new Peli(jid1); freddy11.taytaTiedoillaFreddy(jid1);
 *    freddy22 = new Peli(jid2); freddy22.taytaTiedoillaFreddy(jid2);
 *    freddy12 = new Peli(jid1); freddy12.taytaTiedoillaFreddy(jid1); 
 *    freddy23 = new Peli(jid2); freddy23.taytaTiedoillaFreddy(jid2); 
 *    try {
 *    rekisteri.lisaa(sasu1);
 *    rekisteri.lisaa(sasu2);
 *    rekisteri.lisaa(freddy21);
 *    rekisteri.lisaa(freddy11);
 *    rekisteri.lisaa(freddy22);
 *    rekisteri.lisaa(freddy12);
 *    rekisteri.lisaa(freddy23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
 */
public class Rekisteri {
    private  Pelaajat pelaajat = new Pelaajat();
    private  Pelit pelit = new Pelit();
    
    /** 
     * Korvaa pelaajan tietorakenteessa.  Ottaa pelaajan omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva pelaaja.  Jos ei löydy, 
     * niin lisätään uutena pelaajana. 
     * @param pelaaja lisättävän pelaajan viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.etsi("*",0).size() === 2;
     *  rekisteri.korvaaTaiLisaa(sasu1);
     *  rekisteri.etsi("*",0).size() === 2;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Pelaaja pelaaja) throws SailoException { 
        pelaajat.korvaaTaiLisaa(pelaaja); 
    }
    
    /** 
     * Korvaa pelin tietorakenteessa.  Ottaa pelin omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva peli.  Jos ei löydy, 
     * niin lisätään uutena pelinä. 
     * @param peli lisättävän pelin viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Peli peli) throws SailoException { 
        pelit.korvaaTaiLisaa(peli); 
    } 
    
    /**
     * Hakee ja palauttaa pelaajien yhteenlasketun iän
     * @return pelaajien yhteenlaskettu ikä
     */
    public int getSummaIka() {
        return pelaajat.getSummaIka();
    }

    
    /**
     * Kertoo pelaajien määrän rekisterissä
     * @return jäsenmäärä
     */
    public int getPelaajia() {
        return pelaajat.getLkm();
    }
    
    
    /**
     * Kertoo pelin pelaajien lukumäärän
     * @return pelaajamäärä
     */
    public int getPelit() {
        return pelit.getLkm();
    }
    
    
    /**
     * Lisää rekisteriin uuden pelaajan
     * @param pelaaja lisättävä pelaaja
     * @throws SailoException jos lisäystä ei voida tehdä
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.etsi("*",0).size() === 2;
     *  rekisteri.lisaa(sasu1);
     *  rekisteri.etsi("*",0).size() === 3;
     *  </pre>
     */
    public void lisaa(Pelaaja pelaaja) throws SailoException{
        pelaajat.lisaa(pelaaja);
    }
    
    
    /**
     * Lisätään uusi peli rekisteriin
     * @param peli peli joka lisätään pelaajalle
     * @throws SailoException jos lisäystä ei voida tehdä
     */
    public void lisaa(Peli peli) throws SailoException{
        pelit.lisaa(peli);
    }    
    
    
    /**
     * Antaa pelaajan indeksin
     * @param index kuinka mones pelaaja
     * @return viite pelaajaan 
     * @throws IndexOutOfBoundsException jos index ei haettavissa
     */
    public Pelaaja annaPelaaja(int index) throws IndexOutOfBoundsException{
        return pelaajat.anna(index);
    }
    
    
    /**
     * Antaa pelaajan pelit
     * @param pelaaja Pelaaja jolle pelejä haetaan
     * @return tietorakenne jossa viitteet peleihin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  alustaRekisteri();
     *  Pelaaja sasu3 = new Pelaaja();
     *  sasu3.rekisteroi();
     *  rekisteri.lisaa(sasu3);
     *  
     *  List<Peli> loytyneet;
     *  loytyneet = rekisteri.annaPelit(sasu3);
     *  loytyneet.size() === 0; 
     *  loytyneet = rekisteri.annaPelit(sasu1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == freddy11 === true;
     *  loytyneet.get(1) == freddy12 === true;
     *  loytyneet = rekisteri.annaPelit(sasu2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == freddy21 === true;
     * </pre>
     */
    public List<Peli> annaPelit(Pelaaja pelaaja) throws SailoException {
        return pelit.annaPelit(pelaaja.getTunnusNro());
    }

    
    /**
     * Tallentaa annetut tiedot
     * @throws SailoException jos tallentaminen ei onnistu tai on ongelmia
     */
    public void tallenna() throws SailoException{
        String virhe = "";
        try {
            pelaajat.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        
        try {
            pelit.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        if (!"".equals(virhe)) throw new SailoException(virhe);
    }
    
    
    /**
    * Poistaa pelaajista ja peleistä pelaajan tiedot 
    * @param pelaaja pelaaja jokapoistetaan
    * @return montako pelaajaa poistettiin
    * @example
    * <pre name="test">
    * #THROWS Exception
    *   alustaRekisteri();
    *   rekisteri.etsi("*",0).size() === 2;
    *   rekisteri.annaPelit(sasu1).size() === 2;
    *   rekisteri.poista(sasu1) === 1;
    *   rekisteri.etsi("*",0).size() === 1;
    *   rekisteri.annaPelit(sasu1).size() === 0;
    *   rekisteri.annaPelit(sasu2).size() === 3;
    * </pre>
    */
   public int poista(Pelaaja pelaaja) {
       if ( pelaaja == null ) return 0;
       int ret = pelaajat.poista(pelaaja.getTunnusNro()); 
       pelit.poistaPelaajanPelit(pelaaja.getTunnusNro()); 
       return ret; 
   }


   /** 
    * Poistaa tämän pelin 
    * @param peli poistettava peli 
    * @example
    * <pre name="test">
    * #THROWS Exception
    *   alustaRekisteri();
    *   rekisteri.annaPelit(sasu1).size() === 2;
    *   rekisteri.poistaPeli(freddy11);
    *   rekisteri.annaPelit(sasu1).size() === 1;
    */ 
   public void poistaPeli(Peli peli) { 
       pelit.poista(peli); 
   }
    
    
    /**
     * Lukee tiedot tiedostosta
     * @param nimi luettava
     * @throws SailoException jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     *  
     *  String hakemisto = "testi";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/pelaajat.dat");
     *  File fhtied = new File(hakemisto+"/pelit.dat");
     *  
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  
     *  rekisteri = new Rekisteri(); // tiedostoja ei ole, tulee poikkeus
     *  rekisteri.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaRekisteri();
     *  rekisteri.setTiedosto(hakemisto); // nimi annettava koska uusi poisti sen
     *  rekisteri.tallenna();  
     *  rekisteri = new Rekisteri();
     *  rekisteri.lueTiedostosta(hakemisto);
     *  
     *  Collection<Pelaaja> kaikki = rekisteri.etsi("",-1); 
     *  Iterator<Pelaaja> it = kaikki.iterator();
     *  it.next() === sasu1;
     *  it.next() === sasu2;
     *  it.hasNext() === false;
     *  List<Peli> loytyneet = rekisteri.annaPelit(sasu1);
     *  Iterator<Peli> ih = loytyneet.iterator();
     *  ih.next() === freddy11;
     *  ih.next() === freddy12;
     *  ih.hasNext() === false;
     *  
     *  loytyneet = rekisteri.annaPelit(sasu2);
     *  ih = loytyneet.iterator();
     *  ih.next() === freddy21;
     *  ih.next() === freddy22;
     *  ih.next() === freddy23;
     *  ih.hasNext() === false;
     *  rekisteri.lisaa(sasu2);
     *  rekisteri.lisaa(freddy23);
     *  
     *  rekisteri.tallenna(); // tekee molemmista .bak
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/pelaajat.bak");
     *  File fhbak = new File(hakemisto+"/pelit.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException{
        
        pelaajat = new Pelaajat();
        pelit = new Pelit();

        setTiedosto(nimi);
        pelaajat.lueTiedostosta();
        pelit.lueTiedostosta();        
    }
    
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien pelaajien viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä jäsenistä 
     * @throws SailoException Jos jotakin menee väärin
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaRekisteri();
     *   Pelaaja pelaaja3 = new Pelaaja(); pelaaja3.rekisteroi();
     *   pelaaja3.aseta(1,"Haavisto Kullervo");
     *   rekisteri.lisaa(pelaaja3);
     *   Collection<Pelaaja> loytyneet = rekisteri.etsi("*Haavisto*",1);
     *   loytyneet.size() === 1;
     *   Iterator<Pelaaja> it = loytyneet.iterator();
     *   it.next() == pelaaja3 === true;
     * </pre>
     */ 
    public Collection<Pelaaja> etsi(String hakuehto, int k) throws SailoException { 
        return pelaajat.etsi(hakuehto, k); 
    }

    
    /**
     * Asettaa tiedoston nimen
     * @param nimi tiedoston nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        pelaajat.setTiedostonPerusNimi(hakemistonNimi + "pelaajat");
        pelit.setTiedostonPerusNimi(hakemistonNimi + "pelit");
    }

    
    /**
     * testiohjelmaa
     * @param args EI käytössä
     */
    public static void main(String[] args) {
        Rekisteri rekisteri = new Rekisteri();
        
        try {
            Pelaaja sasu1 = new Pelaaja();
            Pelaaja sasu2 = new Pelaaja();
            Pelaaja sasu3 = new Pelaaja();
            
            sasu1.rekisteroi();
            sasu2.rekisteroi();
            sasu3.rekisteroi();
            
            sasu1.taytaTiedoillaSasuToivonen();
            sasu2.taytaTiedoillaSasuToivonen();
            sasu3.taytaTiedoillaSasuToivonen();
            
            rekisteri.lisaa(sasu1);
            rekisteri.lisaa(sasu2);
            rekisteri.lisaa(sasu3);
            
            int a = sasu1.getTunnusNro();
            int b = sasu2.getTunnusNro();
            int c = sasu3.getTunnusNro();
            
            Peli freddy = new Peli(a);
            freddy.taytaTiedoillaFreddy(a);
            rekisteri.lisaa(freddy);
            
            Peli bonnie = new Peli(b);
            bonnie.taytaTiedoillaFreddy(b);
            rekisteri.lisaa(bonnie);
            
            Peli chica = new Peli(b);
            chica.taytaTiedoillaFreddy(b);
            rekisteri.lisaa(chica);
            
            Peli foxy = new Peli(c);
            foxy.taytaTiedoillaFreddy(c);
            rekisteri.lisaa(foxy);
            
            Peli puppet = new Peli(c);
            puppet.taytaTiedoillaFreddy(c);
            rekisteri.lisaa(puppet);
            
            Peli mangle = new Peli(c);
            mangle.taytaTiedoillaFreddy(c);
            rekisteri.lisaa(mangle);
            
            System.out.println("========== Kerhon testi ==============");
            
            for (int i = 0;i<rekisteri.getPelaajia();i++) {
                Pelaaja pelaaja = rekisteri.annaPelaaja(i);
                System.out.println("Pelaaja ideksissä: " + i);
                pelaaja.tulosta(System.out);
                
                List<Peli> loydetyt = rekisteri.annaPelit(pelaaja);
                for (Peli peli : loydetyt)
                    peli.tulosta(System.out);               
            } 
            
        }catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
  
    }

}
