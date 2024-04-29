package RasporedSpecifikacija;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter

public class Prostorija {

    private int kapacitet;
    private String oznaka_prostorije;

    private Map<String, String> dodatne_informacijeProstorija;

    public Prostorija() {

        dodatne_informacijeProstorija = new HashMap<>();
    }

    public Prostorija(int kapacitet, String oznaka_prostorije) {
        this.kapacitet = kapacitet;
        this.oznaka_prostorije = oznaka_prostorije;
        dodatne_informacijeProstorija = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prostorija that = (Prostorija) o;
        return Objects.equals(oznaka_prostorije, that.oznaka_prostorije);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oznaka_prostorije);
    }

    @Override
    public String toString() {
        return oznaka_prostorije + " " + kapacitet + " " + "dodatne informacijeProstorija " + dodatne_informacijeProstorija;
    }
}
