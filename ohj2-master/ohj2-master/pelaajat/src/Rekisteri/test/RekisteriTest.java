package Rekisteri.test;
// Generated by ComTest BEGIN
// Generated by ComTest END
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.*;
import Rekisteri.*;

/**
 * Test class made by ComTest
 * @version 2020.05.16 21:24:24 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class RekisteriTest {


  // Generated by ComTest BEGIN  // Rekisteri: 19
   private Rekisteri rekisteri; 
   private Pelaaja sasu1; 
   private Pelaaja sasu2; 

   private int jid1; 
   private int jid2; 

   private Peli freddy21; 
   private Peli freddy11; 
   private Peli freddy22; 
   private Peli freddy12; 
   private Peli freddy23; 

   public void alustaRekisteri() {
     rekisteri = new Rekisteri(); 
     sasu1 = new Pelaaja(); sasu1.taytaTiedoillaSasuToivonen(); sasu1.rekisteroi(); 
     sasu2 = new Pelaaja(); sasu2.taytaTiedoillaSasuToivonen(); sasu2.rekisteroi(); 

     jid1 = sasu1.getTunnusNro(); 
     jid2 = sasu2.getTunnusNro(); 

     freddy21 = new Peli(jid2); freddy21.taytaTiedoillaFreddy(jid2); 
     freddy11 = new Peli(jid1); freddy11.taytaTiedoillaFreddy(jid1); 
     freddy22 = new Peli(jid2); freddy22.taytaTiedoillaFreddy(jid2); 
     freddy12 = new Peli(jid1); freddy12.taytaTiedoillaFreddy(jid1); 
     freddy23 = new Peli(jid2); freddy23.taytaTiedoillaFreddy(jid2); 
     try {
     rekisteri.lisaa(sasu1); 
     rekisteri.lisaa(sasu2); 
     rekisteri.lisaa(freddy21); 
     rekisteri.lisaa(freddy11); 
     rekisteri.lisaa(freddy22); 
     rekisteri.lisaa(freddy12); 
     rekisteri.lisaa(freddy23); 
     } catch ( Exception e) {
        System.err.println(e.getMessage()); 
     }
   }
  // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testKorvaaTaiLisaa71 
   * @throws SailoException when error
   */
  @Test
  public void testKorvaaTaiLisaa71() throws SailoException {    // Rekisteri: 71
    alustaRekisteri(); 
    assertEquals("From: Rekisteri line: 74", 2, rekisteri.etsi("*",0).size()); 
    rekisteri.korvaaTaiLisaa(sasu1); 
    assertEquals("From: Rekisteri line: 76", 2, rekisteri.etsi("*",0).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLisaa127 
   * @throws SailoException when error
   */
  @Test
  public void testLisaa127() throws SailoException {    // Rekisteri: 127
    alustaRekisteri(); 
    assertEquals("From: Rekisteri line: 130", 2, rekisteri.etsi("*",0).size()); 
    rekisteri.lisaa(sasu1); 
    assertEquals("From: Rekisteri line: 132", 3, rekisteri.etsi("*",0).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testAnnaPelit167 
   * @throws SailoException when error
   */
  @Test
  public void testAnnaPelit167() throws SailoException {    // Rekisteri: 167
    alustaRekisteri(); 
    Pelaaja sasu3 = new Pelaaja(); 
    sasu3.rekisteroi(); 
    rekisteri.lisaa(sasu3); 
    List<Peli> loytyneet; 
    loytyneet = rekisteri.annaPelit(sasu3); 
    assertEquals("From: Rekisteri line: 178", 0, loytyneet.size()); 
    loytyneet = rekisteri.annaPelit(sasu1); 
    assertEquals("From: Rekisteri line: 180", 2, loytyneet.size()); 
    assertEquals("From: Rekisteri line: 181", true, loytyneet.get(0) == freddy11); 
    assertEquals("From: Rekisteri line: 182", true, loytyneet.get(1) == freddy12); 
    loytyneet = rekisteri.annaPelit(sasu2); 
    assertEquals("From: Rekisteri line: 184", 3, loytyneet.size()); 
    assertEquals("From: Rekisteri line: 185", true, loytyneet.get(0) == freddy21); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista219 
   * @throws Exception when error
   */
  @Test
  public void testPoista219() throws Exception {    // Rekisteri: 219
    alustaRekisteri(); 
    assertEquals("From: Rekisteri line: 222", 2, rekisteri.etsi("*",0).size()); 
    assertEquals("From: Rekisteri line: 223", 2, rekisteri.annaPelit(sasu1).size()); 
    assertEquals("From: Rekisteri line: 224", 1, rekisteri.poista(sasu1)); 
    assertEquals("From: Rekisteri line: 225", 1, rekisteri.etsi("*",0).size()); 
    assertEquals("From: Rekisteri line: 226", 0, rekisteri.annaPelit(sasu1).size()); 
    assertEquals("From: Rekisteri line: 227", 3, rekisteri.annaPelit(sasu2).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoistaPeli242 
   * @throws Exception when error
   */
  @Test
  public void testPoistaPeli242() throws Exception {    // Rekisteri: 242
    alustaRekisteri(); 
    assertEquals("From: Rekisteri line: 245", 2, rekisteri.annaPelit(sasu1).size()); 
    rekisteri.poistaPeli(freddy11); 
    assertEquals("From: Rekisteri line: 247", 1, rekisteri.annaPelit(sasu1).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta259 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta259() throws SailoException {    // Rekisteri: 259
    String hakemisto = "testi"; 
    File dir = new File(hakemisto); 
    File ftied  = new File(hakemisto+"/pelaajat.dat"); 
    File fhtied = new File(hakemisto+"/pelit.dat"); 
    dir.mkdir(); 
    ftied.delete(); 
    fhtied.delete(); 
    rekisteri = new Rekisteri();  // tiedostoja ei ole, tulee poikkeus
    try {
    rekisteri.lueTiedostosta(hakemisto); 
    fail("Rekisteri: 274 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    alustaRekisteri(); 
    rekisteri.setTiedosto(hakemisto);  // nimi annettava koska uusi poisti sen
    rekisteri.tallenna(); 
    rekisteri = new Rekisteri(); 
    rekisteri.lueTiedostosta(hakemisto); 
    Collection<Pelaaja> kaikki = rekisteri.etsi("",-1); 
    Iterator<Pelaaja> it = kaikki.iterator(); 
    assertEquals("From: Rekisteri line: 283", sasu1, it.next()); 
    assertEquals("From: Rekisteri line: 284", sasu2, it.next()); 
    assertEquals("From: Rekisteri line: 285", false, it.hasNext()); 
    List<Peli> loytyneet = rekisteri.annaPelit(sasu1); 
    Iterator<Peli> ih = loytyneet.iterator(); 
    assertEquals("From: Rekisteri line: 288", freddy11, ih.next()); 
    assertEquals("From: Rekisteri line: 289", freddy12, ih.next()); 
    assertEquals("From: Rekisteri line: 290", false, ih.hasNext()); 
    loytyneet = rekisteri.annaPelit(sasu2); 
    ih = loytyneet.iterator(); 
    assertEquals("From: Rekisteri line: 294", freddy21, ih.next()); 
    assertEquals("From: Rekisteri line: 295", freddy22, ih.next()); 
    assertEquals("From: Rekisteri line: 296", freddy23, ih.next()); 
    assertEquals("From: Rekisteri line: 297", false, ih.hasNext()); 
    rekisteri.lisaa(sasu2); 
    rekisteri.lisaa(freddy23); 
    rekisteri.tallenna();  // tekee molemmista .bak
    assertEquals("From: Rekisteri line: 302", true, ftied.delete()); 
    assertEquals("From: Rekisteri line: 303", true, fhtied.delete()); 
    File fbak = new File(hakemisto+"/pelaajat.bak"); 
    File fhbak = new File(hakemisto+"/pelit.bak"); 
    assertEquals("From: Rekisteri line: 306", true, fbak.delete()); 
    assertEquals("From: Rekisteri line: 307", true, fhbak.delete()); 
    assertEquals("From: Rekisteri line: 308", true, dir.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsi329 
   * @throws CloneNotSupportedException when error
   * @throws SailoException when error
   */
  @Test
  public void testEtsi329() throws CloneNotSupportedException, SailoException {    // Rekisteri: 329
    alustaRekisteri(); 
    Pelaaja pelaaja3 = new Pelaaja(); pelaaja3.rekisteroi(); 
    pelaaja3.aseta(1,"Haavisto Kullervo"); 
    rekisteri.lisaa(pelaaja3); 
    Collection<Pelaaja> loytyneet = rekisteri.etsi("*Haavisto*",1); 
    assertEquals("From: Rekisteri line: 336", 1, loytyneet.size()); 
    Iterator<Pelaaja> it = loytyneet.iterator(); 
    assertEquals("From: Rekisteri line: 338", true, it.next() == pelaaja3); 
  } // Generated by ComTest END
}