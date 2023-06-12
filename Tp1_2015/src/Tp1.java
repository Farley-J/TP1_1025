import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ListIterator;
import java.util.LinkedList;

import static java.lang.Integer.parseInt;


public class Tp1 {
    public static void main(String[] args) {
        Tp1 sys = new Tp1();
        LinkedList<Entrepot> listeEntrepot = new LinkedList<>();

        String fileInput = "src/tp1Input/data/"+ args[0];
        String fileOutput = "src/tp1Input/data/"+ args[1];

        Camion camion = sys.storeData(fileInput, listeEntrepot);

        sys.defineDistances(listeEntrepot, camion);


        sys.trierEntrepots(listeEntrepot);




        LinkedList<Entrepot> visited = sys.loadBoxes(camion, listeEntrepot);
        long debutAlgo = System.currentTimeMillis();
        sys.finalMessage(fileOutput, camion, visited);
        long finAlgo = System.currentTimeMillis();
        System.out.println(debutAlgo);
        System.out.println(finAlgo);
        System.out.println("Elapsed time: "+ (finAlgo - debutAlgo) + " milliseconds");

    }
    public LinkedList<Entrepot> loadBoxes(Camion camion, LinkedList<Entrepot> listeEntrepot){
        LinkedList<Entrepot> visited = new LinkedList<>();
        ListIterator<Entrepot> iter = listeEntrepot.listIterator();
//
        while(camion.getNbBoite() != 0){
            Entrepot entrepot = iter.next();
            int nbBoite = entrepot.getCapacite();
            if(nbBoite > camion.getNbBoite()){
                entrepot.setCapacite(nbBoite - camion.getNbBoite());
                camion.setNbBoite(0);
            }else{
                camion.setNbBoite(camion.getNbBoite() - nbBoite);
                entrepot.setCapacite(0);
            }
            visited.add(entrepot);
        }
        return visited;
    }
    public void finalMessage(String filePath, Camion camion, LinkedList<Entrepot> visited){
        LinkedList<String> message = new LinkedList<>();
        ListIterator<String> iterM = message.listIterator();


        message.add(camion.toString());

        ListIterator<Entrepot> iter = visited.listIterator();
        while(iter.hasNext()){
            Entrepot entrepot = iter.next();
            String entrepotLine = entrepot.toString();
            message.add(entrepotLine);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : message) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

    }

    public Camion storeData(String filepath, LinkedList<Entrepot> listeEntrepot) {
        String[] quantite = new String[0];
        try {
            int etat = 0;
            File myObj = new File(filepath);

            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (etat == 1) {
                    String[] info = data.split("\\s+");
                    Entrepot entrepot = new Entrepot(parseInt(info[0]), coordinates(info[1]));
                    listeEntrepot.add(entrepot);
                } else {
                    quantite = data.split("\\s+");
                    etat = 1;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        int capaciteCamion = parseInt(quantite[1]);
        int nbBoite = parseInt(quantite[0]);
        Camion camion = new Camion(capaciteCamion, nbBoite);
        return camion;
    }

    public List<Double> coordinates(String coordinates){
        List<Double> pair = new LinkedList<>();
        Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = regex.matcher(coordinates);

        // Find and print all matches
        while (matcher.find()) {
            double number = Double.parseDouble(matcher.group());
            pair.add(number);
        }
        return pair;

    }
    public void defineDistances(LinkedList<Entrepot> listeEntrepot, Camion camion){
        ListIterator<Entrepot> iter = listeEntrepot.listIterator();
        Entrepot max = defineMax(listeEntrepot, camion);

        while (iter.hasNext()){
            Entrepot entrepot = iter.next();
            double lat1 = max.getPosition().get(0);
            double lat2 = entrepot.getPosition().get(0);
            double long1 = max.getPosition().get(1);
            double long2 = entrepot.getPosition().get(1);
            double distance = calculDistance(lat1, lat2, long1, long2);
            entrepot.setDistance(distance);

        }
    }
    public void trierEntrepots (LinkedList<Entrepot> listeEntrepot){
        int a = 1 ;
        Entrepot smaller = null;
        for(int i = 0; i < listeEntrepot.size(); i++) {
            Entrepot selec = listeEntrepot.get(i);
            int indexSelec = listeEntrepot.indexOf(selec);
            int indexTemp = listeEntrepot.indexOf(selec);

            for (int h = a; h < listeEntrepot.size(); h++) {
                Entrepot comp = listeEntrepot.get(h);
                if (compareDistances(listeEntrepot.get(indexTemp), comp)) {
                    smaller = comp;
                    indexTemp = listeEntrepot.indexOf(smaller);
                }
            }
            if (smaller != null) {

                Entrepot temp = selec;
                listeEntrepot.set(indexSelec, smaller);
                listeEntrepot.set(indexTemp,temp);
            }
            a++;
        }
    }
    public boolean compareDistances(Entrepot selec, Entrepot comp){
        boolean smaller = false;
        double latComp = comp.getPosition().get(0);
        double latSelec = selec.getPosition().get(0);
        double longComp = comp.getPosition().get(1);
        double longSelec = comp.getPosition().get(1);
        if(comp.getDistance() < selec.getDistance()){
            smaller = true;
        }else if(comp.getDistance() == selec.getDistance()){
            if(latComp < latSelec){
                smaller = true;
            }else if(latComp == latSelec){
                if(longComp < longSelec){
                    smaller = true;
                }
            }
        }
        return smaller;
    }
    public  Entrepot defineMax( LinkedList<Entrepot> listeEntrepot, Camion camion){
        ListIterator<Entrepot> iter = listeEntrepot.listIterator();

        Entrepot max = iter.next();
        while (iter.hasNext()) {
            Entrepot entrepot = iter.next();

            if(entrepot.getCapacite() > max.getCapacite()){
                max = entrepot;
            }
        }
        max.setDistance(0.0);
        camion.setPositionInitiale(max.getPosition());
        return max;
    }

    public double calculDistance(double latitude1,double latitude2, double longitude1,double longitude2){
        //rayon de la terre en m
        double r = 6371000;
        //conversion des coordonnées polaires en radians
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double long1 = Math.toRadians(longitude1);
        double long2 = Math.toRadians(longitude2);
        //différences des latitudes en longitudes
        double sLats = lat2 - lat1;
        double sLongs = long2 - long1;
        //calculs intermédiaires de la formule de haversine
        double a = Math.sin(sLats/2)*Math.sin(sLats/2);
        double b = Math.cos(lat1)*Math.cos(lat2)*(Math.sin(sLongs/2)*Math.sin(sLongs/2));
        //formule de haversine
        double result = 2*r*Math.asin(Math.sqrt(a+b));
        //conversion du résultat pour avoir une décimale seulement
        double distance = (double)Math.round(result * 10) / 10.0;

        return distance;
    }
}