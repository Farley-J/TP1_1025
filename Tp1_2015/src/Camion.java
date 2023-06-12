import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Camion {
    //attributs
    private int capaciteCamion;
    private int nbBoite;
    private List<Double> positionInitiale;

    //constructeur
    public Camion (int capaciteCamion, int nbBoite ){
        this.capaciteCamion = capaciteCamion;
        this.nbBoite = nbBoite;
    }
    //méthodes
    public int getCapaciteCamion(){
        return this.capaciteCamion;
    }
    public int getNbBoite(){
        return this.nbBoite;
    }

    public void setCapaciteCamion(int capaciteCamion) {
        this.capaciteCamion = capaciteCamion;
    }
    public void setNbBoite(int nbBoite) {
        this.nbBoite = nbBoite;
    }
    public List<Double> getPositionInitiale(){
        return this.positionInitiale;
    }
    public void setPositionInitiale(List<Double> positionInitiale){
        this.positionInitiale = positionInitiale;
    }

    @Override
    public String toString() {
        String position = this.getPositionInitiale().toString();
        String replacedposition = position.replace('[', '(').replace(']', ')');
        return "Truck Position: " + replacedposition;
    }
}
