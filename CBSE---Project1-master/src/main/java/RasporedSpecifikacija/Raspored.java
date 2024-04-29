package RasporedSpecifikacija;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
public abstract class Raspored {

    public List<Termin> termini;
    public List<Prostorija> prostorije;


    public LocalDate pocetakVazenjaRasporeda;
    public LocalDate krajVazenjaRasporeda;
    public LocalTime pocetakRadnogVremena;
    public LocalTime krajRadnogVremena;

    public List<LocalDate> izuzetiDani;
    public String datum;
    public LocalDateTime pocetakDatum;
    public LocalDateTime krajDatum;
    List<ConfigMapping> columnMappings;

    public Raspored() {
        this.termini = new ArrayList<>();
        this.prostorije = new ArrayList<>();
        this.izuzetiDani = new ArrayList<>();
    }

    /**
     * Dodavanje novog termina uz provere o zauzetosti.
     *
     * Pre dodavanja vrsi sledece provere:
     * 1.Da li termin vec postoji
     * 2.Da li postoji uneta prostorija
     * 3.Da li se preklapa sa drugim terminom
     * 4.Da li je korisnik uneo validne dodatne informacije
     * 5.Da li je termin validan (postuje sve parametre iz config-a)
     *
     * @param termin termin koji se dodaje.
     */
    public abstract void dodaj_Termin(String termin);

    /**
     * Brisanje i dodavanje novog termina sa istim vezanim podacima.
     *
     * Pre dodavanja vrsi sledece provere:
     * 1.Da li termin vec postoji
     * 2.Da li postoje stara i nova prostorija
     * 3.Da li stari termin postoji
     * 4.Da je novi termin validan (postuje sve parametre iz config-a)
     * 5.Da li se novi termin preklapa sa nekim drugim
     *
     * @param termin stari i novi termin
     */
    public abstract void premesti_Termin(String termin);

    /**
     * Filtriranje svih slobodnih termina za unetu prostoriju odredjenog datuma.
     *
     * @param vrednost datum i naziv prostorije
     */
    public abstract void filtrirajSlobodneTerminePoOdredjenojProstoriji(String vrednost);

    /**
     * Filtriranje svih prostorija koje su slobodne ceo dan odredjenog datuma.
     *
     * @param vrednost datum
     */
    public abstract void filtrirajSveProstorijeKojeSuSlobodneOdredjenogDatuma(String vrednost);

    /**
     * Proverava da li je odredjeni termin zauzet.
     *
     * @param termin
     */
    public abstract void zauzetostTermina(String termin);


    /**
     * Vrsi ucitavanje iz razlicitih fajlova.
     *
     * @param line ime fajla i ime config fajla
     * @throws IOException
     */
    public void ucitajFajl(String line)throws IOException{

        ucitajProstorijeIzFajla("metadata.txt");
        ucitajDatumeIzFajla("metadata.txt");
        ucitajIzuzeteDane("metadata.txt");

        if(line.contains("csv")) {
            ///ucitavanje iz csv

            System.out.println("Ucitavam iz csv-a");
            loadData(line.split(",")[0], line.split(",")[1]);


        }
        else {
            ///ucitavanje iz json-a

            System.out.println("Ucitavam iz json-a");
            ucitajTermineIzJson(line.split(",")[0], line.split(",")[1]);

            for (Termin termin : termini) {

                System.out.println(termin);
            }
        }
    }

    /**
     * Exportuje fajl u odredjenom formatu.
     *
     * @param line naziv fajla
     * @throws IOException
     */

    public void exportFajl(String line)throws IOException{


        if(line.contains("json")){
            exportJson(line);
        }
        else if(line.contains("csv")){
            exportData(line);
        }
        else if(line.contains("pdf")){
            exportPdf(line);
        }

    }


    /**
     * Ucitavanje podataka iz csv-a.
     *
     * @param filepath ime fajla
     * @param configPath ime config-a
     * @return true ako je uspesno ucitao, odnosno false ako nije
     * @throws IOException
     */
    public boolean loadData(String filepath, String configPath) throws IOException{

        columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        FileReader fileReader = new FileReader(filepath);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));

        for (CSVRecord record : parser) {
            Termin termin = new Termin();


            for (ConfigMapping entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if(columnIndex == -1) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {


                    case "prostorija":
                        termin.setProstorija(new Prostorija());
                        termin.getProstorija().setOznaka_prostorije(record.get(columnIndex));
                        break;
                    case "pocetak_termina":
                        LocalDateTime startDateTime = LocalDateTime.parse(record.get(columnIndex), formatter);
                        termin.setPocetak_termina(startDateTime);
                        break;
                    case "kraj_termina":
                        LocalDateTime endDateTime = LocalDateTime.parse(record.get(columnIndex), formatter);
                        termin.setKraj_termina(endDateTime);
                        break;
                    case "dodatne_informacijeProstorija":
                        termin.getProstorija().getDodatne_informacijeProstorija().put(columnName, record.get(columnIndex));
                        break;

                    case "dodatne_informacijeTermin":
                        termin.getDodatne_informacijeTermin().put(columnName, record.get(columnIndex));
                        break;
                }
            }


            /*if(validnostTermina(termin)){
                if(!zauzet_Termin(termin,termini)){

                    getTermini().add(termin);
                }
            }*/
            getTermini().add(termin);
        }

        for(Prostorija prostorija : prostorije){

            for(Termin termin :termini){

                if(prostorija.getOznaka_prostorije().equals(termin.getProstorija().getOznaka_prostorije())){

                    termin.getProstorija().setKapacitet(prostorija.getKapacitet());
                }
            }
        }

        for(Termin termin : termini){

            System.out.println(termin);
        }
        return true;

    }

    /**
     * Vraca datum u odredjenom obliku.
     *
     * @param dateTimeString datum
     * @return
     */

    private LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }


    /**
     * @param columnMappings lista gde se nalaze podaci iz config-a
     * @return vraca format datuma.
     */
    private String getDateFormatFromConfig(List<ConfigMapping> columnMappings) {
        for (ConfigMapping entry : columnMappings) {
            if (entry.getIndex() == -1) {
                return entry.getOriginal();
            }
        }
        return null;
    }


    /**
     * Popunjava listu vezanu za config fajl.
     * @param filePath ime config fajla
     * @return lista gde se nalaze svi podaci iz config-a.
     * @throws FileNotFoundException
     */
    private static List<ConfigMapping>  readConfig(String filePath) throws FileNotFoundException {
        List<ConfigMapping> mappings = new ArrayList<>();

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" ", 3);

            mappings.add(new ConfigMapping(Integer.valueOf(splitLine[0]), splitLine[1], splitLine[2]));
        }

        scanner.close();


        return mappings;
    }

    /**
     * Exportuje fajl u csv format.
     *
     * @param path naziv fajla
     * @return true ako je export uspesan, odnosno false ako nije.
     * @throws IOException
     */
    public boolean exportData(String path) throws IOException {

        FileWriter fileWriter = new FileWriter(path);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        for (Termin appointment : termini) {
            csvPrinter.printRecord(
                    appointment.getPocetak_termina(),
                    appointment.getKraj_termina(),
                    appointment.getDodatne_informacijeTermin(),
                    appointment.getProstorija()
            );
        }

        csvPrinter.close();
        fileWriter.close();


        return true;
    }

    /**
     * Exportuje fajl u pdf format.
     *
     * @param path naziv fajla.
     * @throws IOException
     */

    public void exportPdf(String path) throws IOException {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            for (Termin termin : getTermini()) {
                document.add(new Paragraph("Pocetak: " + termin.getPocetak_termina()));
                document.add(new Paragraph("Kraj: " + termin.getKraj_termina()));
                document.add(new Paragraph("Prostorija: " + termin.getProstorija().getOznaka_prostorije()));
                document.add(new Paragraph("Dodatne informacije za prostoriju: " + termin.getProstorija().getDodatne_informacijeProstorija()));
                document.add(new Paragraph("Dodatne informacije za termin: " + termin.getDodatne_informacijeTermin()));

                document.add(new Paragraph("\n"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    /**
     * Ucitava podatke iz json fajla.
     *
     * @param path naziv fajla
     * @param config naziv config fajla
     *
     * @throws IOException
     */
    public void ucitajTermineIzJson(String path, String config) throws IOException{

        columnMappings = readConfig(config);

        Map<Integer, String> mappings = new HashMap<>();
        for (ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        FileReader fileReader = new FileReader(path);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(fileReader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getDateFormatFromConfig(columnMappings));

        for (JsonNode record : jsonNode) {
            Termin termin = new Termin();
            Prostorija prostor = new Prostorija();

            for (ConfigMapping entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if (columnIndex == -1) continue;

                String columnName = entry.getCustom();
                JsonNode node = record.get(columnName);

                if (node != null && !node.isNull()) {
                    String cellValue = node.asText();

                    switch (mappings.get(columnIndex)) {
                        case "prostorija":
                            prostor.setOznaka_prostorije(cellValue);
                            termin.setProstorija(prostor);
                            break;
                        case "pocetak_termina":
                            LocalDateTime startDateTime = LocalDateTime.parse(cellValue, formatter);
                            termin.setPocetak_termina(startDateTime);
                            break;
                        case "kraj_termina":
                            LocalDateTime endDateTime = LocalDateTime.parse(cellValue, formatter);
                            termin.setKraj_termina(endDateTime);
                            break;
                        case "dodatne_informacijeProstorija":
                            prostor.getDodatne_informacijeProstorija().put(columnName, cellValue);
                            break;
                        case "dodatne_informacijeTermin":
                            termin.getDodatne_informacijeTermin().put(columnName, cellValue);
                            break;
                    }
                } else {
                    System.out.println("Vrednost za ključ '" + columnName + "' nije prisutna u JSON zapisu.");
                }
            }
            /*if(validnostTermina(termin)){
                if(!zauzet_Termin(termin,termini)){

                    getTermini().add(termin);
                }
            }*/
            getTermini().add(termin);
        }

        for(Prostorija prostorija : prostorije){

            for(Termin termin :termini){

                if(prostorija.getOznaka_prostorije().equals(termin.getProstorija().getOznaka_prostorije())){

                    termin.getProstorija().setKapacitet(prostorija.getKapacitet());
                }
            }
        }

        for(Termin termin : termini){

            System.out.println(termin);
        }
    }

    /**
     * Exportuje u json format.
     *
     * @param path naziv fajla
     */

    public void exportJson(String path) {

        List<Termin>termins = new ArrayList<>();
        for(Prostorija prostor:prostorije){
            for(Termin termin:getTermini()){
                if(termin.getProstorija().equals(prostor)){
                    termin.getProstorija().setKapacitet(prostor.getKapacitet());
                    termins.add(termin);
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new LocalDateTimeTypeAdapterFactory())
                    .setPrettyPrinting()
                    .create();

            gson.toJson(termins, fileWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ucitavanje svih prostorija koje je korisnik zadao u fajlu.
     *
     * @param putanja naziv fajla.
     * @return lista svih prostorija.
     * @throws IOException
     */

    public List<Prostorija> ucitajProstorijeIzFajla(String putanja) throws IOException {


        try (BufferedReader citac = new BufferedReader(new FileReader(putanja))) {
            String linija;
            boolean uProstorijama = false;

            while ((linija = citac.readLine()) != null) {
                if (linija.startsWith("Prostorije u obliku:")) {
                    uProstorijama = true;
                    continue;
                } else if (linija.startsWith("Pocetak")) {
                    break;
                }

                if (uProstorijama) {
                    String[] delovi = linija.split(",");
                    if (delovi.length == 4) {
                        String oznakaProstorije = delovi[0];
                        int kapacitet = Integer.parseInt(delovi[1]);
                        String imaRacunar = delovi[2];
                        String imaProjektor = delovi[3];

                        Prostorija prostorija = new Prostorija(kapacitet, oznakaProstorije);

                        Map<String, String> dodatneInformacije = new HashMap<>();
                        dodatneInformacije.put("racunar", imaRacunar);
                        dodatneInformacije.put("projektor", imaProjektor);

                        prostorija.setDodatne_informacijeProstorija(dodatneInformacije);
                        prostorije.add(prostorija);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prostorije;
    }

    /**
     * Ucitavanje datuma vezanih za rad ustanove iz fajla.
     *
     * @param putanjaDoFajla naziv fajla
     * @throws IOException
     */

    public void ucitajDatumeIzFajla(String putanjaDoFajla) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(putanjaDoFajla));
        String linija;
        DateTimeFormatter datumFormat = null;
        DateTimeFormatter vremeFormat = null;

        while ((linija = reader.readLine()) != null) {
            if (linija.startsWith("DateFormat")) {




                String[] delovi = linija.split(" ");

                ///setovanje celogDatuma
                this.datum = delovi[1] + " " + delovi[2];

                datumFormat = DateTimeFormatter.ofPattern(delovi[1]);
                vremeFormat = DateTimeFormatter.ofPattern(delovi[2]);
                break;
            }
        }

        while ((linija = reader.readLine()) != null) {
            if (linija.contains("Pocetak")) {
                String[] delovi = linija.split(" ");

                pocetakDatum = LocalDateTime.parse(delovi[1] + " " + delovi[2], DateTimeFormatter.ofPattern(datum));


                pocetakVazenjaRasporeda = LocalDate.parse(delovi[1],datumFormat);
                pocetakRadnogVremena = LocalTime.parse(delovi[2], vremeFormat);
            } else if (linija.contains("Kraj")) {
                String[] delovi = linija.split(" ");

                krajDatum = LocalDateTime.parse(delovi[1] + " " + delovi[2], DateTimeFormatter.ofPattern(datum));

                krajVazenjaRasporeda = LocalDate.parse(delovi[1], datumFormat);
                krajRadnogVremena = LocalTime.parse(delovi[2], vremeFormat);
            }
        }

        reader.close();
    }

    /**
     * Ucitavanje izuzetih dana koje se odnose na rad ustanove iz fajla.
     *
     * @param putanjaDoFajla naziv fajla
     * @throws IOException
     */

    public void ucitajIzuzeteDane(String putanjaDoFajla) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(putanjaDoFajla));
        String linija;
        boolean izuzetiDaniPronadjeni = false;


        DateTimeFormatter datumFormat = null;

        while ((linija = reader.readLine()) != null) {
            if (linija.startsWith("DateFormat")) {
                String[] delovi = linija.split(" ");

                datumFormat = DateTimeFormatter.ofPattern(delovi[1]);

                break;
            }
        }

        while ((linija = reader.readLine()) != null) {
            if (izuzetiDaniPronadjeni) {
                if (!linija.trim().isEmpty()) {
                    LocalDate izuzetiDan = LocalDate.parse(linija.trim(),datumFormat);
                    izuzetiDani.add(izuzetiDan);
                }
            } else if (linija.startsWith("Izuzeti dani")) {
                izuzetiDaniPronadjeni = true;
            }
        }
        reader.close();
    }

    /**
     * Da li se dva datuma preklapaju?
     *
     * @param start1 pocetak termina
     * @param end1 kraj termina
     * @param start2 pocetak termina
     * @param end2 kraj termina
     *
     * @return true ako da, odnosno ne ako se ne preklapaju.
     */

    public boolean periodsOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    /**
     * Proverava da li je termin validan na osnovu svih podataka koje je kosrinik zadao u metadata fajlu.
     *
     * @param termin termin
     * @return da ako je validan, odnosno ne ako nije.
     */

    public boolean validnostTermina(Termin termin){

        /// termin je validan ako je izmedju pocetnog i krajnjeg datuma, se njegovo vreme uklapa u radno vreme ustanovei ako se ne nalazi u izuzetim danima

        LocalDate datumProverePocetak = termin.getPocetak_termina().toLocalDate();
        LocalTime vremeProverePocetak = termin.getPocetak_termina().toLocalTime();
        LocalTime vremeProvereKraj = termin.getKraj_termina().toLocalTime();

        if ((((termin.getPocetak_termina().isEqual(pocetakDatum) || termin.getPocetak_termina().isAfter(pocetakDatum))
                && (termin.getPocetak_termina().isEqual(krajDatum) || termin.getPocetak_termina().isBefore(krajDatum))) &&
                ((vremeProverePocetak.equals(pocetakRadnogVremena) || vremeProverePocetak.isAfter(pocetakRadnogVremena)) && (vremeProverePocetak.isBefore(krajRadnogVremena))))

                && (vremeProvereKraj.isAfter(pocetakRadnogVremena) && ((vremeProvereKraj.isBefore(krajRadnogVremena) || (vremeProvereKraj.equals(krajRadnogVremena)))))) {


            if(!izuzetiDani.contains(datumProverePocetak)){
                //System.out.println("Termin je validan");
                return true;
            }
            else{
                System.out.println("Vas termin nije validan jer ne ispunjava kriterijume koje ste zadali u metadata fajlu");
                return false;
            }
        }
        System.out.println("Vas termin nije validan jer ne ispunjava kriterijume koje ste zadali u metadata fajlu");
        return false;
    }

    /**
     * Prolazi kroz sve termine i gleda da li se on preklapa sa prosledjenim.
     *
     * @param termin konkretan termin
     * @param termins lista svih termina
     * @return true ako se je zauzet, false ako nije.
     */

    public boolean zauzet_Termin(Termin termin, List<Termin> termins){

        for(Termin termin1 : termins){

            if(termin.getProstorija().getOznaka_prostorije().equals(termin1.getProstorija().getOznaka_prostorije())){
                if(periodsOverlap(termin.getPocetak_termina(),termin.getKraj_termina(),termin1.getPocetak_termina(),termin1.getKraj_termina())){
                    System.out.println("Vas termin je zauzet terminom " + termin1);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Dodavanje prostorije.
     *
     * Pre dodavanja vrsi sledece provere:
     * 1.Da li korisnik pokusava da doda prostoriju sa istim imenom
     * 2.Da li su sve dodatne informacije validne
     *
     * @param prostorija prostorija koja se dodaje.
     */

    public void dodaj_Prostoriju(String prostorija){

        ///Ako je korsnik uneo manje informacije izbacuje se poruka u konzoli.
        ///Ako ime nove prostorije bude isto kao ime vec postojece prostorije, ona se nece dodati.
        ///Ako se zadaju pogresne dodatne informacije tj. one koje korisnik nije zada u config fajlu prostorija se nece dodati i
        ///ispisace se greska.

        System.out.println("Pre dodavanje prostorije:");
        for(Prostorija prostorija1 : prostorije){
            System.out.println(prostorija1);
        }

        String[] niz = prostorija.split(",");

        if(niz.length < 3){
            System.out.println("Niste unesli sve podatke!");
            return;
        }

        String naziv = niz[0];

        for(Prostorija prostorija1 : prostorije){

            if(prostorija1.getOznaka_prostorije().equals(naziv)){

                System.out.println("Prostorija sa ovim imenom vec postoji");
                return;
            }
        }

        int kapacitet = Integer.parseInt(niz[1]);
        String dodatneInformacije = niz[2];

        String dodatneInfoNiz[] = dodatneInformacije.split(";");

        HashMap<String,String> dodatne = new HashMap<>();

        for(int i=0;i<dodatneInfoNiz.length;i++){

            boolean flag = true;
            String informacija = dodatneInfoNiz[i];
            String pomocniNiz[] = informacija.split(":");
            for(ConfigMapping configMapping : columnMappings){


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
            Prostorija p = new Prostorija(kapacitet,naziv);
            for(Map.Entry<String,String> entry : dodatne.entrySet()){

                p.getDodatne_informacijeProstorija().put(entry.getKey(),entry.getValue());
            }
            prostorije.add(p);
        }

        System.out.println("Posle dodavanje prostorije:");
        for(Prostorija prostorija1 : prostorije){
            System.out.println(prostorija1);
        }


    }

    /**
     * Brisanje prosledjenog termina.
     *
     * Pre brisanja vrsi sledece provere:
     * 1.Da li prosledjeni termin uopste postoji
     *
     * @param termin naziv termina
     */

    public void obrisi_Termin(String termin){


        System.out.println("Pre brisanja termina");
        for(Termin termin1 : termini){
            System.out.println(termin1);
        }
        ///prvi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao kraj,naziv prostorije
        ///drugi tip MM/dd/yyyy HH:mm kao pocetak i MM/dd/yyyy HH:mm kao vreme trajanja koje se dodaje na pocetak,naziv prostorije

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

            System.out.println("Ova prostorija ne postoji.");
            return;
        }

        Termin terminZaBrisanje = new Termin(pocetak,kraj,p);

        ///da li uopste postoji termin koji hocemo da obrisemo

        boolean postoji = false;
        for(Termin termin1 : termini){
            if(termin1.equals(terminZaBrisanje)){
                postoji = true;
                break;
            }
        }
        if(!postoji){

            System.out.println("Termin koji ste uneli ne postoji");
            return;
        }

        Iterator<Termin> iterator = termini.iterator();

        while (iterator.hasNext()) {
            Termin te = iterator.next();
            if (te.equals(terminZaBrisanje)) {
                iterator.remove();
                System.out.println("Termin je uspesno obrisan");
                System.out.println("Posle brisanja termina");
                for(Termin termin1 : termini){
                    System.out.println(termin1);
                }
            }
        }

    }

    /**
     * Filtriranje rasporeda po bilo kojim dodatnim informacijama.
     *
     * Pre filtriranja se proverava:
     * 1.Da li su prosledjene informacije validne
     *
     * @param s dodatne informacije
     */

    public void filtrirajRasporedPoDodatnimInformacijama(String s){


        ///podatak:vrednost,podatak:vrednost

        String informacije[] = s.split(",");
        List<Termin> filtriraniTermini = new ArrayList<>();
        HashMap<String,String> dodatne = new HashMap<>();

        ///provera da li su sve informacije dobre

        for(int i=0;i<informacije.length;i++){

            boolean flag = true;
            String informacija = informacije[i];
            String pomocniNiz[] = informacija.split(":");

            for(ConfigMapping configMapping : getColumnMappings()){


                if(pomocniNiz[0].toLowerCase().equals(configMapping.getCustom().toLowerCase())) {
                    flag = false;
                    break;
                }
            }
            if(!flag){
                dodatne.put(pomocniNiz[0],pomocniNiz[1]);
            }
            else{
                System.out.println("U vasim podacima ne postoji " + pomocniNiz[0]);
                return;
            }
        }

        ///prolazak kroz sve termine i pojedinacne mape trazeci odredjeni kljuc

        for(Termin termin : termini){

            int brojac = 0;
            for(Map.Entry<String,String> podatak : dodatne.entrySet()){

                String kljuc = podatak.getKey();
                String vrednost = podatak.getValue();

                if(termin.getDodatne_informacijeTermin().containsKey(kljuc)){

                    String vrednostDobijena = termin.getDodatne_informacijeTermin().get(podatak.getKey());

                    if(vrednost.equals(vrednostDobijena)){
                        brojac++;
                    }
                }
                else if(termin.getProstorija().getDodatne_informacijeProstorija().containsKey(podatak.getKey())){

                    String vrednostDobijena = termin.getProstorija().getDodatne_informacijeProstorija().get(kljuc);
                    if(vrednost.equals(vrednostDobijena)){
                        brojac++;
                    }
                }
            }
            if(brojac == dodatne.size()){
                filtriraniTermini.add(termin);
            }
        }

        if(filtriraniTermini.isEmpty()){
            System.out.println("Nema termina koji odgovara vasim zahtevima");
        }
        else {
            for (Termin termin : filtriraniTermini) {
                System.out.println(termin);
            }
        }
    }

    /**
     * Filtriranje svih slobodnih termina koji sadrze prostoriju sa zadatim osobinama za odrejeni datum.
     *
     * Pre filtriranje se vrse sledece provere:
     * 1.Da li su sve osobine validne
     *
     * @param s datum i informacije vezane za prostoriju.
     */

    public void filtrirajSlobodneTermineZaProstorijePremaOdredjenimOsobinama(String s){

        ///Izlistavanje slobodnih termina za prostoriju sa određenim osobinama (na primer učionica sa računarima, projektorom,
        //da ima više od 30 mesta i slično),

        ///1.oblik unosa : MM/dd/yyyy,podatak:vrednost;podatak:vrednost...
        ///2.oblik unosa : MM/dd/yyyy,podatak:vrednost;podatak:vrednost...,znak:kapacitet
        ///znak moze biti >,< ili =

        String niz[] = s.split(",");
        String d[] = datum.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(d[0]);
        LocalDate pocetak = LocalDate.parse(niz[0], formatter);
        String dodatneInformacije = niz[1];

        String dodatneInfoNiz[] = dodatneInformacije.split(";");

        HashMap<String,String> dodatne = new HashMap<>();

        ///provera da li su sve dodatne informacije validne

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
                dodatne.put(pomocniNiz[0],pomocniNiz[1]);
            }
            else{
                System.out.println("U vasim podacima ne postoji " + pomocniNiz[0]);
                break;
            }
        }

        String znak = null;
        int kapacitet = 0;
        boolean kapacitetFlag = false;
        if(niz.length == 3){

            kapacitetFlag = true;
            String poredjenje = niz[2];
            String niz1[] = poredjenje.split(":");

            znak = niz1[0];
            kapacitet = Integer.parseInt(niz1[1]);
        }

        List<Prostorija> odgovarajuceProstorije = new ArrayList<>();

        for(Prostorija prostorija : prostorije){

            int brojac = 0;
            for(Map.Entry<String,String> entry : dodatne.entrySet()) {

                String kljuc = entry.getKey();
                String vrednost = entry.getValue();


                String dobijenaVrednost = prostorija.getDodatne_informacijeProstorija().get(kljuc);


                if (dobijenaVrednost.equals(vrednost)) {

                    if (kapacitetFlag) {

                        if (znak.equals("=")) {
                            if (prostorija.getKapacitet() == kapacitet) {

                                brojac++;
                            }
                        } else if (znak.equals(">")) {
                            if (prostorija.getKapacitet() > kapacitet) {

                                brojac++;
                            }
                        } else if (znak.equals("<")) {
                            if (prostorija.getKapacitet() < kapacitet) {

                                brojac++;
                            }
                        }
                    } else {
                        brojac++;
                    }
                }

            }
            if(brojac == dodatne.size()){
                odgovarajuceProstorije.add(prostorija);
            }
        }
        System.out.println("Odgovarajuce prostorije");
        for(Prostorija prostorija : odgovarajuceProstorije){

            System.out.println(prostorija);
        }



        for(Prostorija prostorija : odgovarajuceProstorije){

            System.out.println(prostorija.getOznaka_prostorije() + " je zauzeta u sledecim terminima: ");
            filtrirajSlobodneTerminePoOdredjenojProstoriji(niz[0] + "," + prostorija.getOznaka_prostorije());
        }
    }


}
