package agh.cs.project.main_objects;
import agh.cs.project.secondary_objects.Vector2d;
import java.awt.*;

public class Grass extends AbstractWorldMapElement {
    private static int plantEnergy;

    public int getPlantEnergy() { return plantEnergy; }

    public Grass(Vector2d position){
        super(position);
    }

    public static void setPlantEnergy(int newPlantEnergy){
        plantEnergy = newPlantEnergy;
    }

    @Override
    public Color toColor() { return new Color(10, 128, 22); }
}
