package RasporedSpecifikacija;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class Termin {

    private LocalDateTime pocetak_termina;
    private LocalDateTime kraj_termina;
    private Prostorija prostorija;
    private Map<String, String> dodatne_informacijeTermin = new HashMap<>();

    public Termin() {
    }


    public Termin(LocalDateTime pocetak_termina, LocalDateTime kraj_termina, Prostorija prostorija) {
        this.pocetak_termina = pocetak_termina;
        this.kraj_termina = kraj_termina;
        this.prostorija = prostorija;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Termin termin = (Termin) o;
        return Objects.equals(pocetak_termina, termin.pocetak_termina) && Objects.equals(kraj_termina, termin.kraj_termina)  && Objects.equals(prostorija, termin.prostorija);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pocetak_termina, kraj_termina, prostorija);
    }

    public String toString() {
        return "Termin {" +
                "start=" + pocetak_termina +
                ", end=" + kraj_termina +
                ", place='" + prostorija + '\'' +
                "dodatne informacijeTermin " + dodatne_informacijeTermin +
                '}';
    }

}
