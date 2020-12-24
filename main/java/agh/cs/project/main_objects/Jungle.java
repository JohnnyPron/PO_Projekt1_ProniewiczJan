package agh.cs.project.main_objects;
import agh.cs.project.secondary_objects.Vector2d;
import agh.cs.project.interfaces.IWorldMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


public class Jungle {
    private final Vector2d bottomLeftCorner;
    private final Vector2d upperRightCorner;
    private int width;
    private int height;
    private IWorldMap map;
    private Map<Vector2d, Grass> grass = new LinkedHashMap<Vector2d, Grass>();

    public Vector2d getBottomLeftCorner() { return bottomLeftCorner; }

    public Vector2d getUpperRightCorner() { return upperRightCorner; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Map<Vector2d, Grass> getGrass() { return grass; }

    public Jungle(IWorldMap map, Vector2d bottomLeftCorner, Vector2d upperRightCorner, int tufts){
        this.map = map;
        this.bottomLeftCorner = bottomLeftCorner;
        this.upperRightCorner = upperRightCorner;
        this.width = bottomLeftCorner.xRange(upperRightCorner);
        this.height = bottomLeftCorner.yRange(upperRightCorner);
        if(tufts > width * height) tufts = width * height;
        for(int t = 0; t < tufts; t++){
            Vector2d new_v = null;  // utworzenie pustego wektora dla kępki trawy 't'
            while (new_v == null) {
                int rand_x = new Random().nextInt(upperRightCorner.getX() + 1 - bottomLeftCorner.getX()) +
                        bottomLeftCorner.getX();
                int rand_y = new Random().nextInt(upperRightCorner.getY() + 1 - bottomLeftCorner.getY()) +
                        bottomLeftCorner.getY();
                new_v = new Vector2d(rand_x, rand_y);  // przypisanie wektorowi konkretnej wartości
                // jeśli się okaże, że trawa na tej pozycji już występuje, wektor staje się pusty
                Grass existingGrass = grass.get(new_v);
                if(existingGrass != null){ new_v = null; }
            }
            grass.put(new_v, new Grass(new_v));
        }
    }

    public Grass growGrass(){
        Grass newTuft = null;
        // sprawdzenie, czy wszystkie pola dżungli są zajęte (czy to przez trawę lub zwierzęta)
        int occupiedPlaces = 0;
        for(int i = bottomLeftCorner.getX(); i <= upperRightCorner.getX(); i++){
            for(int j = bottomLeftCorner.getY(); j <= upperRightCorner.getY(); j++){
                Vector2d vectorToCheck = new Vector2d(i, j);
                if(map.isOccupied(vectorToCheck)){
                    occupiedPlaces++;
                }
            }
        }
        // jeśli nie, algorytm losuje / poszukuje wolnego miejsca dla nowej kępki trawy
        if(occupiedPlaces != this.height * this.width){
            Vector2d new_v = null;
            while (new_v == null) {
                int rand_x = new Random().nextInt(upperRightCorner.getX() + 1 - bottomLeftCorner.getX()) +
                        bottomLeftCorner.getX();
                int rand_y = new Random().nextInt(upperRightCorner.getY() + 1 - bottomLeftCorner.getY()) +
                        bottomLeftCorner.getY();
                new_v = new Vector2d(rand_x, rand_y);
                // jeśli miejsce na mapie jest zajęte przez inną trawę lub zwierzę, pozycja jest resetowana
                if(map.isOccupied(new_v)){ new_v = null; }
            }
            newTuft = new Grass(new_v);
            grass.put(new_v, newTuft);
        }
        // zwrócenie kępki trawy dla mapy
        return newTuft;
    }

    public void removeGrass(Vector2d grassPosition){ this.grass.remove(grassPosition); }
}
