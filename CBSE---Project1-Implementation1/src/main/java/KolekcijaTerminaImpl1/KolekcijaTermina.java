package KolekcijaTerminaImpl1;

import RasporedSpecifikacija.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class KolekcijaTermina extends Raspored {


    static {
        RasporedManager.setujRaspored(new KolekcijaTermina());
    }

    @Override
    public void dodaj_Termin(String termin) {

        System.out.println("Pre dodavanja termina");
        for(Termin termin1 : termini){
            System.out.println(termin1);
        }

        ///dodavanje novog termina uz provere o zauzetost, obraditi situaciju da je termin već zauzet

        ///prvi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao kraj
        ///drugi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao vreme trajanja koje se dodaje na pocetak

        String niz[] = termin.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datum);
        LocalDateTime pocetak = LocalDateTime.parse(niz[0], formatter);



        LocalDateTime kraj = LocalDateTime.parse(niz[1], formatter);
        LocalTime vremeTrajanjaIliZavrsetka = kraj.toLocalTime();

        if(vremeTrajanjaIliZavrsetka.isBefore(pocetakRadnogVremena)){

            LocalDate noviKraj = kraj.toLocalDate();
            LocalTime noviKrajVreme = pocetak.toLocalTime().plusHours(vremeTrajanjaIliZavrsetka.getHour()).plusMinutes(vremeTrajanjaIliZavrsetka.getMinute()).plusSeconds(vremeTrajanjaIliZavrsetka.getSecond());
            kraj = noviKraj.atTime(noviKrajVreme);
        }

        String nazivProstorije = niz[2];
        String dodatneInformacije = niz[3];

        ///provera da li postoji prostorija za novi termin

        Prostorija p = null;
        for(Prostorija prostorija : prostorije){
            if(prostorija.getOznaka_prostorije().equals(nazivProstorije)){

                p = prostorija;
            }
        }

        if(p == null){
            System.out.println("Prostorija ne postoji");
            return;
        }

        Termin terminNovi = new Termin(pocetak,kraj,p);

        ///provera validnosti termina

        if(!validnostTermina(terminNovi)){
            return;
        }


        ///provera da li postoji vec isti termin
        for(Termin terminn : termini){

            if(terminn.equals(terminNovi)){
                System.out.println("Ovaj termin vec postoji");
                return;
            }
        }

        ///provera da li se prekapa vreme i prostorija
        for(Termin termin1 : termini){

            if(periodsOverlap(pocetak, kraj, termin1.getPocetak_termina(), termin1.getKraj_termina())){
                if(termin1.getProstorija().getOznaka_prostorije().equals(nazivProstorije)) {
                    System.out.println("Vas termini se preklapa sa vec posotojecim terminom " + termin1);
                    return;
                }
            }
        }

        ///dodatne informacije za termin

        String dodatneInfoNiz[] = dodatneInformacije.split(";");

        HashMap<String,String> dodatne = new HashMap<>();

        for(int i=0;i<dodatneInfoNiz.length;i++){

            boolean flag = true;
            String informacija = dodatneInfoNiz[i];
            String pomocniNiz[] = informacija.split(":");
            for(ConfigMapping configMapping : getColumnMappings()){


                if(pomocniNiz[0].toLowerCase().equals(configMapping.getCustom().toLowerCase())) {
                    flag = false;
                    break;
                }
            }
            if(!flag){
                dodatne.put(pomocniNiz[0].toLowerCase(),pomocniNiz[1]);
            }
            else{
                System.out.println("U vasim podacima ne postoji " + pomocniNiz[0]);
                break;
            }
        }
        if(dodatneInfoNiz.length == dodatne.size()) {

            for(Map.Entry<String,String> entry : dodatne.entrySet()){

                terminNovi.getDodatne_informacijeTermin().put(entry.getKey(),entry.getValue());
            }
            termini.add(terminNovi);
            System.out.println("Termin je uspesno dodat!");
            System.out.println("Posle dodavanja termina");
            for(Termin termin1 : termini){
                System.out.println(termin1);
            }
        }
    }



    @Override
    public void premesti_Termin(String termin) {

        ///prvi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao kraj,naziv prostorije
        ///MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,nazivStareProstorije,MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,nazivNoveProstorije
        ///drugi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao vreme trajanja koje se dodaje na pocetak,naziv prostorije
        ///MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,naziv stareProstorije,MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,naziv noveProstorije,

        System.out.println("Pre dodavanja termina");
        for(Termin termin1 : termini){
            System.out.println(termin1);
        }

        String niz[] = termin.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datum);
        LocalDateTime pocetak = LocalDateTime.parse(niz[0], formatter);
        String krajString = niz[1];



        LocalDateTime kraj = LocalDateTime.parse(niz[1], formatter);
        LocalTime vremeTrajanjaIliZavrsetka = kraj.toLocalTime();

        if(vremeTrajanjaIliZavrsetka.isBefore(pocetakRadnogVremena)){

            LocalDate noviKraj = kraj.toLocalDate();
            LocalTime noviKrajVreme = pocetak.toLocalTime().plusHours(vremeTrajanjaIliZavrsetka.getHour()).plusMinutes(vremeTrajanjaIliZavrsetka.getMinute()).plusSeconds(vremeTrajanjaIliZavrsetka.getSecond());
            kraj = noviKraj.atTime(noviKrajVreme);
        }


        String nazivProstorije = niz[2];

        Prostorija p = null;
        for(Prostorija prostorija : prostorije){
            if(prostorija.getOznaka_prostorije().equals(nazivProstorije)){
                p = prostorija;
                break;
            }
        }

        if(p == null){

            System.out.println("Ova prostorija ne postoji " + nazivProstorije);
            return;
        }

        ///provera da li termin koji je korsinik uneo postoji

        Termin stariTerminPomocna = new Termin(pocetak,kraj,p);
        Termin stariTermin = null;
        boolean postoji = false;

        for(Termin termin1 : termini){

            if(stariTerminPomocna.equals(termin1)){
                postoji = true;
                stariTermin = termin1;
                break;
            }
        }

        if(!postoji){

            System.out.println("Termin koji ste uneli ne postoji");
            return;
        }



        LocalDateTime pocetakNovi = LocalDateTime.parse(niz[3], formatter);
        String krajStringNovi = niz[4];

        LocalDateTime krajNovi = LocalDateTime.parse(niz[4], formatter);
        LocalTime vremeTrajanjaIliZavrsetkaNovog = krajNovi.toLocalTime();

        if(vremeTrajanjaIliZavrsetkaNovog.isBefore(pocetakRadnogVremena)){

            LocalDate noviKraj = krajNovi.toLocalDate();
            LocalTime noviKrajVreme = pocetakNovi.toLocalTime().plusHours(vremeTrajanjaIliZavrsetkaNovog.getHour()).plusMinutes(vremeTrajanjaIliZavrsetkaNovog.getMinute()).plusSeconds(vremeTrajanjaIliZavrsetkaNovog.getSecond());
            krajNovi = noviKraj.atTime(noviKrajVreme);
        }

        String nazivProstorijeNovi = niz[5];

        Prostorija pNova = null;
        for(Prostorija prostorija : prostorije){
            if(prostorija.getOznaka_prostorije().equals(nazivProstorijeNovi)){
                pNova = prostorija;
                break;
            }
        }

        if(pNova == null){

            System.out.println("Ova prostorija ne postoji " + nazivProstorijeNovi);
            return;
        }

        ///provera da li stari termin moze da se zameni novim terminom

        Termin terminNoviPomocna = new Termin(pocetakNovi,krajNovi,pNova);

        if(!validnostTermina(terminNoviPomocna)){
            return;
        }

        ///brisanje starog termina
        ///brisemo ga zato sto kada proveravamo da li se novi preklapa sa nekim drugim moramo da u proveri preskocimo stari

        Iterator<Termin> iterator = termini.iterator();

        while (iterator.hasNext()) {
            Termin te = iterator.next();
            if (te.equals(stariTermin)) {
                iterator.remove();
            }
        }

        ///provera da li je novi termin validan, ako jeste setujemo na stari termin nove podatke
        ///dodajemo ga opet u listu

        if(!zauzet_Termin(terminNoviPomocna,termini)){

            stariTermin.setPocetak_termina(pocetakNovi);
            stariTermin.setKraj_termina(krajNovi);
            stariTermin.setProstorija(pNova);
            System.out.println("Novi termin je uspesno zamenjen starim");
            System.out.println("Posle dodavanja termina");

        }
        else{
            System.out.println("Termin koji ste uneli se preklapa sa vec postojecim terminom");

        }
        termini.add(stariTermin);
        for(Termin termin1 : termini){
            System.out.println(termin1);
        }
    }



    @Override
    public void filtrirajSlobodneTerminePoOdredjenojProstoriji(String s) {


        ///MM/dd/yyyy,naziv ucionice

        String niz[] = s.split(",");
        String d[] = datum.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(d[0]);
        LocalDate pocetak = LocalDate.parse(niz[0], formatter);
        String nazivProstorije = niz[1];


        boolean postoji = false;
        for(Prostorija p : prostorije){

            if(p.getOznaka_prostorije().equals(nazivProstorije)){
                postoji = true;
                break;
            }
        }

        if(!postoji){
            System.out.println("Prostorija koju ste uneli ne postoji");
            return;
        }

        List<Termin> termins = new ArrayList<>();

        for(Termin termin : termini){

            if(termin.getProstorija().getOznaka_prostorije().equals(nazivProstorije) && niz[0].equals(termin.getPocetak_termina().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))){

                termins.add(termin);
            }
        }

        ///provera za prosotrije koje nikada nisu iskoriscene
        if(termins.isEmpty()){

            for(Prostorija p : prostorije){

                if(p.getOznaka_prostorije().equals(nazivProstorije)){
                    System.out.println("Ova prostorija je slobodna ceo dan");
                    return;
                }
            }
        }

        Collections.sort(termins, Comparator.comparing(Termin::getPocetak_termina));

        for(Termin t : termins){

            System.out.println(t);
        }

        List<Termin> slobodniTermini = new ArrayList<>();


        LocalTime radnoVremePocetak = pocetakRadnogVremena;
        LocalTime radnoVremeKraj = krajRadnogVremena;

        // Dodatna promenljiva za praćenje vremena do kojeg su zauzeti termini
        LocalTime trenutnoVreme = radnoVremePocetak;

        for (Termin termin : termins) {


            // Dodavanje slobodnog termina između trenutnog vremena i početka zauzetog termina
            if (Duration.between(trenutnoVreme, termin.getPocetak_termina()).toMinutes() >= 120) {
                slobodniTermini.add(new Termin(
                        LocalDateTime.of(pocetak, trenutnoVreme),
                        termin.getPocetak_termina(),
                        termin.getProstorija()
                ));
            }
            trenutnoVreme = termin.getKraj_termina().toLocalTime();
        }

        // Dodavanje poslednjeg slobodnog termina do kraja radnog vremena (21:00)
        if (Duration.between(trenutnoVreme, radnoVremeKraj).toMinutes() >= 120) {
            slobodniTermini.add(new Termin(
                    LocalDateTime.of(pocetak, trenutnoVreme),
                    LocalDateTime.of(pocetak, radnoVremeKraj),
                    termins.get(0).getProstorija()
            ));
        }

        System.out.println("Slobodni termini za prostoriju " + nazivProstorije + ":");
        for (Termin t : slobodniTermini) {
            System.out.println(t);
        }
    }



    @Override
    public void filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma(String s) {

        ///MM/dd/yyyy

        ///izlistati sve učionice koja je slobodna tog dana

        String d[] = datum.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(d[0]);
        LocalDate datum = LocalDate.parse(s, formatter);

        List<Prostorija> prostorijaList = new ArrayList<>();

        ///ako dodatum ne postoji u rasporedu
        boolean flagg = false;
        for(Termin termin : termini){

            LocalDateTime pocetakTermina = termin.getPocetak_termina();
            LocalDate pocetakTerminaSamoDatum = pocetakTermina.toLocalDate();

            if(pocetakTerminaSamoDatum.equals(datum)){

                flagg = true;
                break;
            }
        }

        if(!flagg){

            for(Termin termin : termini){

                if(!prostorijaList.contains(termin.getProstorija())){

                    prostorijaList.add(termin.getProstorija());
                }
            }
        }
        else {

            for(Prostorija p : prostorije){

                boolean flag = false;
                for(Termin termin : termini){

                    LocalDateTime pocetakTermina = termin.getPocetak_termina();
                    LocalDate pocetakTerminaSamoDatum = pocetakTermina.toLocalDate();

                    if(pocetakTerminaSamoDatum.equals(datum)){

                        if(p.getOznaka_prostorije().equals(termin.getProstorija().getOznaka_prostorije())){

                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag){

                    prostorijaList.add(p);
                }
            }
        }



        for (Prostorija prostorija : prostorije) {

            boolean flag = false;
            for (Termin termin : termini) {

                if (prostorija.getOznaka_prostorije().equals(termin.getProstorija().getOznaka_prostorije())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {

                if (!prostorijaList.contains(prostorija))
                    prostorijaList.add(prostorija);
            }
        }

        if (prostorijaList.isEmpty()) {
            System.out.println("Ne postoje prostorije koje su slobodne tog dana");
        } else {
            for (Prostorija prostorija : prostorijaList) {

                System.out.println(prostorija.getOznaka_prostorije());
            }
        }

    }

    @Override
    public void zauzetostTermina(String s) {


        System.out.println("Svi termini:");
        for(Termin termin1 : termini){
            System.out.println(termin1);
        }

        ///da li je prosledjeni termin zauzet
        ///prvi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao kraj,naziv ucionice
        ///drugi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao vreme trajanja koje se dodaje na pocetak,naziv ucionice

        String niz[] = s.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datum);
        LocalDateTime pocetak = LocalDateTime.parse(niz[0], formatter);
        String krajString = niz[1];


        LocalDateTime kraj = LocalDateTime.parse(niz[1], formatter);
        LocalTime vremeTrajanjaIliZavrsetka = kraj.toLocalTime();

        if(vremeTrajanjaIliZavrsetka.isBefore(pocetakRadnogVremena)){

            LocalDate noviKraj = kraj.toLocalDate();
            LocalTime noviKrajVreme = pocetak.toLocalTime().plusHours(vremeTrajanjaIliZavrsetka.getHour()).plusMinutes(vremeTrajanjaIliZavrsetka.getMinute()).plusSeconds(vremeTrajanjaIliZavrsetka.getSecond());
            kraj = noviKraj.atTime(noviKrajVreme);
        }

        String nazivProstorije = niz[2];

        Prostorija p = null;
        for(Prostorija prostorija : prostorije){
            if(prostorija.getOznaka_prostorije().equals(nazivProstorije)){

                p = prostorija;
            }
        }

        if(p == null){

            System.out.println("Ova prostorija ne postoji");
            return;
        }

        Termin termin = new Termin(pocetak,kraj,p);
        if(!validnostTermina(termin)){
            return;
        }

        if(zauzet_Termin(termin,termini)){

            System.out.println("Zauzet je");
        }
        else{

            System.out.println("Slobodan je");
        }

    }

}
