package org.example;


import RasporedSpecifikacija.Raspored;
import RasporedSpecifikacija.RasporedManager;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {



        try{
            //Class.forName("KolekcijaTerminaImpl1.KolekcijaTermina");
            Class.forName("NedeljniNivoImpl2.NedeljniNivo");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Raspored raspored = RasporedManager.getujRaspored();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite putanju do fajla i konfiguracionog fajla u obliku: putanjaDoFajla,putanjaDoKonfiguracije");
        String line = scanner.nextLine();

        try {
            raspored.ucitajFajl(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ///metoda dodaj prostorije RADI

       /* for(Prostorija prostorija : raspored.prostorije){
            System.out.println(prostorija);
        }
        System.out.println("Posle dodavanja prostorije");
        System.out.println("Unesite naziv,kapacitet i dodatne informacije");
        ///dodaje
        raspored.dodaj_Prostoriju("RAF12,25,racunar:NE;projektor:DA");
        ///ne dodaje jer postoji sa tim imenom
        raspored.dodaj_Prostoriju("RAF1,25,racunar:NE;projektor:DA");
        ///ne dodaje zato sto korsnik hoce da doda dodatnu informaciju a nije je naveo u configu
        raspored.dodaj_Prostoriju("RAF13,25,racunar:NE;tabla:DA");

        for(Prostorija prostorija : raspored.prostorije){
            System.out.println(prostorija);
        }*/

        ///metoda za dodavanje novog termina uz proveru zauzetosti  RADI

        /*System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm, naziv prostorije,dodatne informacije");

        ///ovaj termin vec postoji
        raspored.dodaj_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF1,predmet:DA;profesor:DA");
        ///prostorija ne postoji
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 12:00,RAF50,predmet:DA;profesor:DA");
        ///preklapa se vreme i mesto
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 12:00,RAF2,predmet:DA;profesor:DA");
        ///korsnik hoce da doda dodatnu informaciju a nije je naveo u configu
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 13:00,RAF1,predmet:DA;grupa:DA");
        ///ovaj prolazi ne preklapa se vreme
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 13:00,RAF1,predmet:DA;profesor:DA");
        ///ovaj prolazi nije isto mesto
        raspored.dodaj_Termin("10/30/2023 09:15,10/30/2023 12:00,RAF9,predmet:NE;profesor:DA");
        ///ovaj ne prolazi zato sto nije validan (izuzet dan)
        raspored.dodaj_Termin("12/25/2023 09:15,12/25/2023 11:00,RAF10,predmet:NE;profesor:DA");
        ///ovaj ne prolazi zato sto nije validan (vreme)
        raspored.dodaj_Termin("10/30/2023 08:15,10/30/2023 12:00,RAF9,predmet:NE;profesor:DA");*/

        /*System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, vreme trajanja termina u obliku MM/dd/yyyy HH:mm, naziv prostorije,dodatne informacije");
        ///ovaj termin vec postoji
        raspored.dodaj_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1,predmet:DA;profesor:DA");
        ///prostorija ne postoji
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 00:45,RAF50,predmet:DA;profesor:DA");
        ///preklapa se vreme i mesto
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 01:30,RAF2,predmet:DA;profesor:DA");
        ///korsnik hoce da doda dodatnu informaciju a nije je naveo u configu
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 01:45,RAF1,grupa:301;profesor:Sava");
        ///ovaj prolazi ne preklapa se vreme
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 01:45,RAF1,predmet:TS;profesor:Sava");
        ///ovaj prolazi nije isto mesto
        raspored.dodaj_Termin("10/30/2023 09:15,10/30/2023 02:45,RAF9,predmet:UUP;profesor:Redzic");


        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        ///metoda za brisanje termina RADI
        /*System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm");

        ///obrisao je termin
        //raspored.obrisi_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF1");
        ///obrisao je termin
        raspored.obrisi_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1");
        ///nije ga obrisao jer prostorija ne postoji
        raspored.obrisi_Termin("10/30/2023 10:00,10/30/2023 11:45,RAF100");
        ///nije ga obrisao jer ne postoji
        raspored.obrisi_Termin("10/30/2023 10:00,10/30/2023 11:45,RAF7");

        System.out.println("Nakon brisanja termina");

        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/


        /*///metoda za premestanje termina RADI
        ///1.nacin sa pocetak i kraj
        ///izbacuje gresku je stara prostoija ne postoji
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF40,10/30/2023 10:00,10/30/2023 11:45,RAF5");
        ///izbacuje gresku zato sto stari termin ne postoji
        raspored.premesti_Termin("10/30/2023 01:15,10/30/2023 02:00,RAF1,10/30/2023 10:00,10/30/2023 11:45,RAF5");
        ///izbacuje gresku jer prostoija u novom terminu ne postoji
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF1,10/30/2023 10:00,10/30/2023 11:45,RAF100");
        ///izbacuje gresku zato sto se novi termin prekalpa vec sa nekim
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF1,10/30/2023 10:15,10/30/2023 11:30,RAF2");
        ///uspesno premesta termin
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 11:00,RAF1,10/30/2023 17:00,10/30/2023 18:00,RAF4");
        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        /*///2.nacin sa pocetak i vreme trajanja
        ///izbacuje gresku je stara prostoija ne postoji
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF40,10/30/2023 10:00,10/30/2023 01:45,RAF5");
        ///izbacuje gresku zato sto stari termin ne postoji
        raspored.premesti_Termin("10/30/2023 01:15,10/30/2023 00:45,RAF1,10/30/2023 10:00,10/30/2023 01:45,RAF5");
        ///izbacuje gresku jer prostorija u novom terminu ne postoji
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1,10/30/2023 10:00,10/30/2023 01:45,RAF100");
        ///izbacuje gresku zato sto se novi termin prekalpa vec sa nekim
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1,10/30/2023 10:15,10/30/2023 01:15,RAF2");
        ///ne moze jer novi nije validan
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1,10/30/2023 08:00,10/30/2023 01:00,RAF4");
        ///uspesno premesta termin
        raspored.premesti_Termin("10/30/2023 09:15,10/30/2023 01:45,RAF1,10/30/2023 17:00,10/30/2023 01:00,RAF4");
        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/


        /*///zauzetost termina RADI
        System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm,naziv prostorije ili  pocetak termina u obliku MM/dd/yyyy HH:mm, vreme trajanja MM/dd/yyyy HH:mm,nazivProstorije");
        ///slobodan je
        raspored.zauzetostTermina("10/30/2023 12:15,10/30/2023 14:00,RAF2");
        ///zauzet je
        raspored.zauzetostTermina("10/30/2023 10:00,10/30/2023 11:00,RAF2");
        ///2.nacin zadavanja termina preko trajanja
        ///slobodan je
        raspored.zauzetostTermina("10/30/2023 15:00,10/30/2023 01:00,RAF5");
        ///zauzet je
        raspored.zauzetostTermina("10/30/2023 17:00,10/30/2023 01:15,RAF7");*/

        /*///metoda za proveru validnosti termina RADI


        ///nije validan pocinje pre
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 10, 2, 9, 0),
                LocalDateTime.of(2023, 10, 2, 11, 0),new Prostorija(50,"RAF1")));
        ///nije validan pocinje posle
        raspored.validnostTermina(new Termin(LocalDateTime.of(2024, 2, 20, 9, 0),
                LocalDateTime.of(2024, 2, 20, 11, 0),new Prostorija(50,"RAF2")));
        ///nije validan pocinje pre radnog vremena
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 20, 8, 0),
                LocalDateTime.of(2023, 11, 20, 11, 0),new Prostorija(50,"RAF3")));
        ///nije validan pocinje posle radnog vremena
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 20, 22, 0),
                LocalDateTime.of(2023, 11, 20, 11, 0),new Prostorija(50,"RAF4")));
        ///nije validan zato sto se nalazi u izuzetim danima
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 10, 8, 0),
                LocalDateTime.of(2023, 10, 20, 11, 0),new Prostorija(50,"RAF5")));
        ///nije validan zavrsava se pre pocetka radnog vremena
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 20, 9, 0),
                LocalDateTime.of(2023, 11, 20, 8, 0),new Prostorija(50,"RAF6")));
        ///nije validan zavrsava se posle radnog vremena
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 20, 9, 0),
                LocalDateTime.of(2023, 11, 20, 23, 0),new Prostorija(50,"RAF7")));
        ///validan
        raspored.validnostTermina(new Termin(LocalDateTime.of(2023, 11, 20, 9, 0),
                LocalDateTime.of(2023, 11, 20, 11, 0),new Prostorija(50,"RAF8")));*/


        ///filtrirajRaspored

        /*System.out.println("Filtriraj raspored");  RADI
        System.out.println("1. Pretraga po dodatnim informacijama u obliku: podatak:vrednost,podatak:vrednost...");
        ///greska
        raspored.filtrirajRasporedPoDodatnimInformacijama("profesor:Jefimija Najdic,predmet:TS");
        ///greska zato sto je uneo pogresnu info
        raspored.filtrirajRasporedPoDodatnimInformacijama("profesor:Jefimija Najdic,grupa:201");
        ///radi
        raspored.filtrirajRasporedPoDodatnimInformacijama("racunar:DA,profesor:Jefimija Najdic,predmet:SK,projektor:DA");
        System.out.println("\n");
        ///radi
        raspored.filtrirajRasporedPoDodatnimInformacijama("racunar:DA,predmet:UUP");*/


       /*///filtriraj slobodne termine po ucionici RADI
        System.out.println("2.Za odredjeni datum filtrirajte slobodne termine po ucionici u obliku: datum kao MM/dd/yyyy,naziv ucionice");
        raspored.filtrirajSlobodneTerminePoOdredjenojProstoriji("10/30/2023,RAF2");*/



        /*///filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama RADI
        //System.out.println("3. Za odredjeni datum filtrirajte slobodne termine za prostorije sa odredjenim osobinama u obliku: MM/dd/yyyy,podatak:vrednost;podatak:vrednost... ili u obliku MM/dd/yyyy,podatak:vrednost;podatak:vrednost...,znak:kapacitet");
        raspored.filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama("10/30/2023,racunar:NE;projektor:NE");
        System.out.println("\n");
        raspored.filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama("10/30/2023,racunar:DA,<:30");*/


        ///MM/dd/yyyy

        /*///izlistati sve učionice koja je slobodna tog dana RADI
        System.out.println("4. Izlistati sve učionice koja je slobodna tog dana, unesite datum u obliku: MM/dd/yyyy");
        raspored.filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma("10/31/2023");
        System.out.println("\n");
        raspored.filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma("10/28/2023");*/




        ///IMPLEMENTACIJA 2 TESTOVI


        /*///metoda za dodavanje novog termina uz proveru zauzetosti
        ///1 nacin zadavanja datuma
        System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm, naziv prostorije,dodatne informacije");


        ///ovaj termin vec postoji
        raspored.dodaj_Termin("10/05/2023 09:15,11/04/2023 11:00,RAF1,profesor:Jefimija Najdic;predmet:SK");
        //ovaj ne moze zato sto se preklapaju 10/05/2023 09:15,11/02/2023 11:00,UUP,Mladen Jovanovic,DA,NE
        raspored.dodaj_Termin("10/12/2023 10:15,10/27/2023 10:45,RAF1,profesor:Jefimija Najdic;predmet:SK");
        ///prostorija ne postoji
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 12:00,RAF50,profesor:Jefimija Najdic;predmet:SK");
        ///korsnik hoce da doda dodatnu informaciju a nije je naveo u configu
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 13:00,RAF1,predmet:SK;grupa:301");
        ///ovaj prolazi
        raspored.dodaj_Termin("10/08/2023 09:15,11/01/2023 11:00,RAF2,profesor:Jefimija Najdic;predmet:SK");
        ///ovaj ne prolazi, zauzet je ovim RAF1,10/12/2023 09:15,11/09/2023 11:00,SK,Nikola Redzic,DA,NE
        raspored.dodaj_Termin("10/05/2023 09:15,11/01/2023 11:00,RAF1,profesor:Jefimija Najdic;predmet:SK");
        ///ovaj ne prolazi zato sto nije validan
        raspored.dodaj_Termin("12/10/2023 08:15,12/28/2023 11:00,RAF10,profesor:Jefimija Najdic;predmet:SK");
        ///ovaj ne prolazi zato sto nije validan jer je izuzeti dan 11/15/2023
        raspored.dodaj_Termin("11/08/2023 10:15,11/29/2023 11:30,RAF10,profesor:Jefimija Najdic;predmet:SK");
        ///dodaje, primer za jedan dan u drugo imp
        raspored.dodaj_Termin("11/16/2023 17:00,11/16/2023 18:45,RAF9,profesor:Jefimija Najdic;predmet:SK");*/

        //2 nacin zadavanja datuma

        /*///ovaj termin vec postoji
        raspored.dodaj_Termin("10/05/2023 09:15,11/04/2023 01:45,RAF1,profesor:Jefimija Najdic;predmet:SK");
        //ovaj ne moze zato sto se preklapaju
        raspored.dodaj_Termin("10/12/2023 10:15,10/27/2023 00:30,RAF1,profesor:Jefimija Najdic;predmet:SK");
        ///prostorija ne postoji
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 00:45,RAF50,profesor:Jefimija Najdic;predmet:SK");
        ///korsnik hoce da doda dodatnu informaciju a nije je naveo u configu
        raspored.dodaj_Termin("10/30/2023 11:15,10/30/2023 01:45,RAF1,predmet:SK;grupa:301");
        ///ovaj prolazi
        raspored.dodaj_Termin("10/08/2023 09:15,11/01/2023 01:45,RAF2,profesor:Jefimija Najdic;predmet:SK");
        ///ovaj ne prolazi zato sto nije validan
        raspored.dodaj_Termin("12/10/2023 08:15,12/28/2023 02:45,RAF10,profesor:Jefimija Najdic;predmet:SK");
        ///ovaj ne prolazi zato sto nije validan jer je izuzeti dan
        raspored.dodaj_Termin("11/08/2023 10:15,11/29/2023 01:15,RAF10,profesor:Jefimija Najdic;predmet:SK");*/

        /*for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        /*///metoda za brisanje termina RADI
        System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm");

        ///obrisao je termin
        raspored.obrisi_Termin("10/05/2023 09:15,11/02/2023 11:00,RAF1");
        ///obrisao je termin
        raspored.obrisi_Termin("10/05/2023 10:00,11/02/2023 01:45,RAF2");
        ///nije ga obrisao jer prostorija ne postoji
        raspored.obrisi_Termin("10/05/2023 12:15,11/02/2023 14:45,RAF100");
        ///nije ga obrisao jer ne postoji
        raspored.obrisi_Termin("10/05/2023 10:00,10/30/2023 02:45,RAF8");

        System.out.println("Nakon brisanja termina");

        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        ///RADI
        /*System.out.println("1. Pretraga po dodatnim informacijama u obliku: podatak:vrednost,podatak:vrednost...");
        ///greska
        raspored.filtrirajRasporedPoDodatnimInformacijama("profesor:Jefimija Najdic,predmet:TS");
        ///greska zato sto je uneo pogresnu info
        raspored.filtrirajRasporedPoDodatnimInformacijama("profesor:Jefimija Najdic,grupa:201");
        ///radi
        raspored.filtrirajRasporedPoDodatnimInformacijama("racunar:DA,profesor:Jefimija Najdic,predmet:SK,projektor:DA");
        System.out.println("\n");
        ///radi
        raspored.filtrirajRasporedPoDodatnimInformacijama("racunar:DA,predmet:UUP");*/


        /*///filtriraj slobodne termine po ucionici RADI
        System.out.println("2.Za odredjeni datum filtrirajte slobodne termine po ucionici u obliku: datum kao MM/dd/yyyy,naziv ucionice");
        raspored.filtrirajSlobodneTerminePoOdredjenojProstoriji("10/12/2023,RAF1");*/


        /*///filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama RADI
        System.out.println("3. Za odredjeni datum filtrirajte slobodne termine za prostorije sa odredjenim osobinama u obliku: MM/dd/yyyy,podatak:vrednost;podatak:vrednost... ili u obliku MM/dd/yyyy,podatak:vrednost;podatak:vrednost...,znak:kapacitet");
        raspored.filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama("10/30/2023,racunar:DA;projektor:NE");
        System.out.println("\n");
        raspored.filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama("10/12/2023,projektor:NE,<:45");*/


        ///MM/dd/yyyy

        /*///izlistati sve učionice koja je slobodna tog dana RADI
        System.out.println("4. Izlistati sve učionice koja je slobodna tog dana, unesite datum u obliku: MM/dd/yyyy");
        raspored.filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma("10/14/2023");*/


        /*///zauzetost termina RADI
        System.out.println("Unesite pocetak termina u obliku MM/dd/yyyy HH:mm, kraj termina u obliku MM/dd/yyyy HH:mm,naziv prostorije ili  pocetak termina u obliku MM/dd/yyyy HH:mm, vreme trajanja MM/dd/yyyy HH:mm,nazivProstorije");
        ///slobodan je
        raspored.zauzetostTermina("10/07/2023 12:15,10/30/2023 14:00,RAF2");
        ///zauzet je
        raspored.zauzetostTermina("10/05/2023 10:00,11/01/2023 11:30,RAF1");
        ///2.nacin zadavanja termina preko trajanja
        ///slobodan je
        raspored.zauzetostTermina("10/05/2023 09:00,11/01/2023 01:00,RAF6");
        ///zauzet je
        raspored.zauzetostTermina("10/20/2023 17:00,10/28/2023 02:15,RAF7");*/


        /*///metoda za premestanje termina RADI
        ///1.nacin sa pocetak i kraj
        ///izbacuje gresku je stara prostoija ne postoji
        raspored.premesti_Termin("10/05/2023 09:15,10/05/2023 11:00,RAF40,10/30/2023 10:00,10/30/2023 11:45,RAF5");
        ///izbacuje gresku zato sto stari termin ne postoji
        raspored.premesti_Termin("10/10/2023 16:00,10/30/2023 17:45,RAF1,10/30/2023 10:00,10/30/2023 11:45,RAF5");
        ///izbacuje gresku zato sto stari termin nije validan
        raspored.premesti_Termin("10/06/2023 16:00,11/02/2023 17:45,RAF1,10/30/2023 08:00,10/30/2023 11:45,RAF5");
        ///izbacuje gresku jer prostoija u novom terminu ne postoji
        raspored.premesti_Termin("10/06/2023 16:00,11/02/2023 17:45,RAF1,10/30/2023 08:00,10/30/2023 11:45,RAF100");
        ///izbacuje gresku zato sto se novi termin prekalpa vec sa RAF6,10/05/2023 15:15,11/02/2023 17:00,TS,Sava Ivkovic,NE,NE
        raspored.premesti_Termin("10/05/2023 09:15,11/02/2023 11:00,RAF1,10/19/2023 16:30,10/30/2023 17:30,RAF6");
        ///uspesno premesta termin
        raspored.premesti_Termin("10/05/2023 09:15,11/02/2023 11:00,RAF1,10/08/2023 17:00,10/25/2023 18:00,RAF4");
        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        /*///metoda za premestanje termina
        ///2.nacin sa pocetak i kraj
        ///izbacuje gresku je stara prostoija ne postoji
        raspored.premesti_Termin("10/05/2023 09:15,10/05/2023 01:45,RAF40,10/30/2023 10:00,10/30/2023 01:45,RAF5");
        ///izbacuje gresku zato sto stari termin ne postoji
        raspored.premesti_Termin("10/10/2023 16:00,10/30/2023 01:45,RAF1,10/30/2023 10:00,10/30/2023 01:45,RAF5");
        ///izbacuje gresku zato sto stari termin nije validan
        raspored.premesti_Termin("10/06/2023 16:00,11/02/2023 01:45,RAF1,10/30/2023 08:00,10/30/2023 03:45,RAF5");
        ///izbacuje gresku jer prostoija u novom terminu ne postoji
        raspored.premesti_Termin("10/06/2023 16:00,11/02/2023 01:45,RAF1,10/30/2023 08:00,10/30/2023 03:45,RAF100");
        ///izbacuje gresku zato sto se novi termin prekalpa vec sa nekim
        raspored.premesti_Termin("10/05/2023 09:15,11/02/2023 01:45,RAF1,10/19/2023 16:30,10/30/2023 01:00,RAF6");
        ///uspesno premesta termin
        raspored.premesti_Termin("10/05/2023 09:15,11/02/2023 01:45,RAF1,10/08/2023 17:00,10/25/2023 01:00,RAF4");
        for(Termin termin : raspored.termini){
            System.out.println(termin);
        }*/

        System.out.println("Izaberite opciju iz menija za rad sa rasporedom");
        while(true){

            System.out.println("0. Izlaz");
            System.out.println("1. Dodavanje nove prostorije");
            System.out.println("2. Dodavanje termina");
            System.out.println("3. Premesti termin");
            System.out.println("4. Proverite zauzetost termina");
            System.out.println("5. Brisanje termina");
            System.out.println("6. Filtriraj slobodne termine po odredjenoj prostoriji");
            System.out.println("7. Filtriraj slobodne termine za prostorije prema odredjenim osobinama");
            System.out.println("8. Filtriraj sve prostorije koje su slobodne odredjenog datuma");
            System.out.println("9. Filtriraj raspored po dodatnim informacijama");
            System.out.println("10. Export podataka");
            System.out.println("Napomena: Termin se moze zadati na 2 nacina:\n1. MM/dd/yyyy HH:mm kao pocetno vreme i MM/dd/yyyy HH:mm kao vreme zavrsetka\n2. MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao vreme trajanja koje se dodaje na pocetak");

            line = scanner.nextLine();
            int broj = Integer.parseInt(line);

            if(broj == 0){
                break;
            }
            else if(broj == 1){

                System.out.println("Unesite naziv,kapacitet i dodatne informacije u obliku: naziv,kapacitet,podatak:DA/NE;podatak:DA/NE;...");
                line = scanner.nextLine();
                raspored.dodaj_Prostoriju(line);
            }
            else if(broj == 2){

                System.out.println("Unesite termin u obliku:MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,naziv prostorije,dodatne informacije u obliku:podatak:DA/NE;podatak:DA/NE;...");
                line = scanner.nextLine();
                raspored.dodaj_Termin(line);
            }
            else if(broj == 3){

                System.out.println("Unesite stari i novi termin u obliku:MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,nazivStareProstorije,MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,nazivNoveProstorije");
                line = scanner.nextLine();
                raspored.premesti_Termin(line);
            }
            else if(broj == 4){

                System.out.println("Unesite termin u obliku:MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,naziv ucionice");
                line = scanner.nextLine();
                raspored.zauzetostTermina(line);
            }
            else if(broj == 5){

                System.out.println("Unesite termin u obliku:MM/dd/yyyy HH:mm,MM/dd/yyyy HH:mm,naziv ucionice");
                line = scanner.nextLine();
                raspored.obrisi_Termin(line);
            }
            else if(broj == 6){

                System.out.println("Unesite datum u obliku:MM/dd/yyyy,naziv ucionice");
                line = scanner.nextLine();
                raspored.filtrirajSlobodneTerminePoOdredjenojProstoriji(line);
            }
            else if(broj == 7){

                System.out.println("1.nacin unosa:MM/dd/yyyy,podatak:vrednost;podatak:vrednost...\n 2.nacin unosa: MM/dd/yyyy,podatak:vrednost;podatak:vrednost...,znak:kapacitet \n znak moze biti >,< ili =");
                line = scanner.nextLine();
                raspored.filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama(line);
            }
            else if(broj == 8) {

                System.out.println("Unesite datum u obliku:MM/dd/yyyy");
                line = scanner.nextLine();
                raspored.filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma(line);
            }
            else if(broj == 9){

                System.out.println("Unesite informacije u obliku: podatak:vrednost,podatak:vrednost");
                line = scanner.nextLine();
                raspored.filtrirajRasporedPoDodatnimInformacijama(line);
            }
            else if(broj == 10){
                System.out.println("Exportujte podatke u csv,json ili pdf");
                line = scanner.nextLine();
                try {
                    raspored.exportFajl(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                System.out.println("Nevalidan unos!");
            }
        }
    }
}