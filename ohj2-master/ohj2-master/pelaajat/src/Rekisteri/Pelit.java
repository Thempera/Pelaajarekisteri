package Rekisteri;

import java.io.*;
import java.util.*;

/**
 * Pelit-luokka, joka lisää uusia pelejä
 * 
 * @author Henna ja Tuuli
 * @version 13.5.2020
 *
 * Sähköpostit
 * Tuuli: manttszw@student.jyu.fi 
 * Henna: henna.m.sillanpaa@student.jyu.fi
 */
public class Pelit implements Iterable<Peli> {
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    /**
     * Taulukko peleistä
     */
    private final List<Peli> alkiot = new ArrayList<Peli>();

    
    /**
     * Pelien alustaminen
     */
    public Pelit() {
        // Ei vielä tarpeellinen
    }
    
    
    /**
     * Korvaa pelin tietorakenteessa.  Ottaa pelin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva peli.  Jos ei löydy,
     * niin lisätään uutena pelinä.
     * @param peli lisättävän pelin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Pelit pelit = new Pelit();
     * Peli peli1 = new Peli(), peli2 = new Peli();
     * peli1.rekisteroi(); peli2.rekisteroi();
     * pelit.getLkm() === 0;
     * pelit.korvaaTaiLisaa(peli1); pelit.getLkm() === 1;
     * pelit.korvaaTaiLisaa(peli2); pelit.getLkm() === 2;
     * Peli peli3 = peli1.clone();
     * peli3.aseta(2,"kkk");
     * Iterator<Peli> i2=pelit.iterator();
     * i2.next() === peli1;
     * pelit.korvaaTaiLisaa(peli3); pelit.getLkm() === 2;
     * i2=pelit.iterator();
     * Peli p = i2.next();
     * p === peli3;
     * p == peli3 === true;
     * p == peli1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Peli peli) throws SailoException {
        int id = peli.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, peli);
                muutettu = true;
                return;
            }
        }
        lisaa(peli);
    }

    
    /**
     * Lisätään peli pelaajalle
     * @param peli peli joka lisätään
     */
    public void lisaa(Peli peli) {
        alkiot.add(peli);
        muutettu = true;
    }
    
    
    /**
     * Tallentaa pelaajien tiedostoon
     * @throws SailoException jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); 
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Peli peli : this) {
                fo.println(peli.toString());
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }

    
    
    /**
     * Lukee pelejä tiedostosta
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException Jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Pelit videopelit = new Pelit();
     *  Peli freddy21 = new Peli(); freddy21.taytaTiedoillaFreddy(2);
     *  Peli freddy22 = new Peli(); freddy22.taytaTiedoillaFreddy(1);
     *  Peli freddy23 = new Peli(); freddy23.taytaTiedoillaFreddy(2); 
     *  Peli freddy24 = new Peli(); freddy24.taytaTiedoillaFreddy(1); 
     *  String tiedNimi = "testirekisteri";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  videopelit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  videopelit.lisaa(freddy21);
     *  videopelit.lisaa(freddy22);
     *  videopelit.lisaa(freddy23);
     *  videopelit.lisaa(freddy24);
     *  videopelit.tallenna();
     *  videopelit = new Pelit();
     *  videopelit.lueTiedostosta(tiedNimi);
     *  Iterator<Peli> i = videopelit.iterator();
     *  i.next().toString() === freddy21.toString();
     *  i.next().toString() === freddy22.toString();
     *  i.next().toString() === freddy23.toString();
     *  i.next().toString() === freddy24.toString();
     *  i.hasNext() === false;
     *  videopelit.lisaa(freddy23);
     *  videopelit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Peli peli = new Peli();
                peli.parse(rivi); 
                lisaa(peli);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    
    /**
     * Lukee tietyn tiedoston
     * @throws SailoException jos ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    
    /**
     * Iteraattori kaikkien pelien läpikäymiseen
     * @return peli-iteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Pelit videopelit = new Pelit();
     * 
     * Peli freddy21 = new Peli(1); videopelit.lisaa(freddy21);
     * Peli freddy22 = new Peli(1); videopelit.lisaa(freddy22);
     * Peli freddy23 = new Peli(1); videopelit.lisaa(freddy23);
     * Peli freddy24 = new Peli(2); videopelit.lisaa(freddy24);
     * 
     * Iterator<Peli> iter = videopelit.iterator();
     * iter.next() === freddy21;
     * iter.next() === freddy22;
     * iter.next() === freddy23;
     * iter.next() === freddy24;
     * iter.next() === freddy21;  #THROWS NoSuchElementException  
     * 
     *  int n = 0;
     *  int nrot[] = {1,1,1,2};
     *  
     *  for ( Peli peli:videopelit ) { 
     *    peli.getJasenNro() === nrot[n]; n++;  
     *  } 
     *  n === 4;
     * </pre>
     */
    @Override
    public Iterator<Peli> iterator() {
        return alkiot.iterator();
    }
    
    
    /**
     * Palauttaa rekisterin kaikkien pelien lukumäärän 
     * @return alkioiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();   
    }
    
    
    /**
     * Asettaa tiedoston perusnimen
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tallennukseen käytettävän tiedoston nimen 
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     *Palauttaa tallennukseen käytettävän tiedoston nimen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Kertoo pelaajan pelit
     * @param tunnutNro pelaajan tunnusnumero jolle haetaan pelit
     * @return tietorakenne jossa viitteet peleihin
     * 
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     * Pelit videopelit = new Pelit();
     * 
     * Peli freddy21 = new Peli(1); videopelit.lisaa(freddy21);
     * Peli freddy22 = new Peli(1); videopelit.lisaa(freddy22);
     * Peli freddy23 = new Peli(1); videopelit.lisaa(freddy23);
     * Peli freddy24 = new Peli(2); videopelit.lisaa(freddy24);
     * 
     * List<Peli> loydetyt;
     * 
     * loydetyt = videopelit.annaPelit(3);
     * loydetyt.size() === 0; 
     * 
     * loydetyt = videopelit.annaPelit(1);
     * loydetyt.size() === 3; 
     * loydetyt.get(0) == freddy21 === true;
     * loydetyt.get(1) == freddy22 === true;
     * loydetyt.get(2) == freddy23 === true;
     * loydetyt.get(2) == freddy24 === false;
     * 
     * loydetyt = videopelit.annaPelit(2);
     * loydetyt.size() === 1; 
     * loydetyt.get(0) == freddy24 === true;
     * loydetyt.get(0) == freddy21 === false;
     * 
     * </pre>
     */
    public List<Peli> annaPelit(int tunnutNro){
        List<Peli> loydetyt = new ArrayList<Peli>();
        for (Peli peli : alkiot)
            if (peli.getJasenNro() == tunnutNro)loydetyt.add(peli);
        return loydetyt;
    }
    
    
    /**
     * Poistaa valitun pelin
     * @param peli poistettava peli
     * @return true jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Pelit pelit = new Pelit();
     *  Peli freddy021 = new Peli(); freddy021.taytaTiedoillaFreddy(2);
     *  Peli freddy011 = new Peli(); freddy011.taytaTiedoillaFreddy(1);
     *  Peli freddy022 = new Peli(); freddy022.taytaTiedoillaFreddy(2); 
     *  Peli freddy012 = new Peli(); freddy012.taytaTiedoillaFreddy(1); 
     *  Peli freddy023 = new Peli();
     *  pelit.lisaa(freddy021);
     *  pelit.lisaa(freddy011);
     *  pelit.lisaa(freddy022);
     *  pelit.lisaa(freddy012);
     *  pelit.poista(freddy023) === false; pelit.getLkm() === 4;
     *  pelit.poista(freddy011) === true;  pelit.getLkm() === 3;
     *  List<Peli> p = pelit.annaPelit(1);
     *  p.size() === 1; 
     *  p.get(0) === freddy012;
     * </pre>
     */
    public boolean poista(Peli peli) {
        boolean ret = alkiot.remove(peli);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn jäsenen harrastukset
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Pelit videopelit = new Pelit();
     *  Peli freddy21 = new Peli(); freddy21.taytaTiedoillaFreddy(2);
     *  Peli freddy11 = new Peli(); freddy11.taytaTiedoillaFreddy(1);
     *  Peli freddy22 = new Peli(); freddy22.taytaTiedoillaFreddy(2); 
     *  Peli freddy12 = new Peli(); freddy12.taytaTiedoillaFreddy(1); 
     *  Peli freddy23 = new Peli(); freddy23.taytaTiedoillaFreddy(2); 
     *  videopelit.lisaa(freddy21);
     *  videopelit.lisaa(freddy11);
     *  videopelit.lisaa(freddy22);
     *  videopelit.lisaa(freddy12);
     *  videopelit.lisaa(freddy23);
     *  videopelit.poistaPelaajanPelit(2) === 3;  videopelit.getLkm() === 2;
     *  videopelit.poistaPelaajanPelit(3) === 0;  videopelit.getLkm() === 2;
     *  List<Peli> p = videopelit.annaPelit(2);
     *  p.size() === 0; 
     *  p = videopelit.annaPelit(1);
     *  p.get(0) === freddy11;
     *  p.get(1) === freddy12;
     * </pre>
     */
    public int poistaPelaajanPelit(int tunnusNro) {
        int n = 0;
        for (Iterator<Peli> it = alkiot.iterator(); it.hasNext();) {
            Peli peli = it.next();
            if ( peli.getJasenNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    
    /**
     * Testiohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Pelit videopelit = new Pelit();
        
        Peli freddy = new Peli();
        Peli bonnie = new Peli();
        Peli chica = new Peli();
        Peli foxy = new Peli();
        
        freddy.taytaTiedoillaFreddy(1);
        bonnie.taytaTiedoillaFreddy(1);
        chica.taytaTiedoillaFreddy(1);
        foxy.taytaTiedoillaFreddy(2);
        
        videopelit.lisaa(freddy);
        videopelit.lisaa(freddy);
        videopelit.lisaa(bonnie);
        videopelit.lisaa(chica);
        videopelit.lisaa(foxy);
        
        System.out.println("========== Pelit testi ==============");
        
        List<Peli> peleja = videopelit.annaPelit(1);
        
        for (Peli peli : peleja) {
            System.out.print(peli.getJasenNro()+ " ");
            peli.tulosta(System.out);
        }
        
        List<Peli> peleja2 = videopelit.annaPelit(2);
        
        for (Peli peli : peleja2) {
            System.out.print(peli.getJasenNro()+ " ");
            peli.tulosta(System.out);
        }        
    }
    

}
