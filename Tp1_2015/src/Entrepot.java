import java.util.ArrayList;
import java.util.List;

public class Entrepot {
    //attributs
    private int capacite;
    private List<Double> position;

    private double distance;

    //constructeur
    public Entrepot(int capacite, List<Double> position ){
        this.capacite = capacite;
        this.position = position;
    }
    //méthodes
    public int getCapacite(){
        return this.capacite;
    }
    public void setCapacite(int capacite){
        this.capacite = capacite;
    }
    public List<Double> getPosition(){
        return this.position;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance(){
        return this.distance;
    }

    @Override
    public String toString() {
        String distanceS = String.valueOf(this.distance);
        String position = this.getPosition().toString();
        String replacedposition = position.replace('[', '(').replace(']', ')');
        if (this.distance == 0.0) {
            distanceS = "0";
        }

        return "Distance:" + distanceS + "\t" +
                "Number of boxes:" + capacite + "\t" +
                "Position:" + replacedposition + "\t";

    }
}

