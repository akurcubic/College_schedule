package RasporedSpecifikacija;

public class RasporedManager {

    private static Raspored raspored;

    public static void setujRaspored(Raspored sch){

        raspored = sch;
    }
    public static Raspored getujRaspored(){

        return  raspored;
    }


}
