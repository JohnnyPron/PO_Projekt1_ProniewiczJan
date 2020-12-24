package agh.cs.project.world_map;
import agh.cs.project.interfaces.IWorldMap;
import agh.cs.project.main_objects.*;
import agh.cs.project.secondary_objects.Vector2d;
import agh.cs.project.world_sim.SimulationEngine;
import javax.swing.*;
import java.awt.*;

public class MapVisualizer extends JPanel{
    private GrassField map;
    private SimulationEngine simulator;
    private int widthScale;
    private int heightScale;

    public MapVisualizer(GrassField map, SimulationEngine simulator) {
        this.map = map;
        this.simulator = simulator;
        this.setSize((int) (simulator.frame.getWidth() * 0.5), simulator.frame.getHeight() - 38);
        this.widthScale = Math.round((float)this.getWidth() / map.getWidth());
        this.heightScale = this.getHeight() / map.getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSteppe(g);
        drawJungle(g);
        drawGrass(g);
        drawAnimals(g);
    }

    private void drawSteppe(Graphics g) {
        g.setColor(new Color(255, 254, 181));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    private void drawJungle(Graphics g) {
        Jungle jungle = map.getJungle();
        g.setColor(new Color(124, 230, 44));
        g.fillRect(jungle.getBottomLeftCorner().getX() * widthScale,
                jungle.getBottomLeftCorner().getY() * heightScale,
                jungle.getWidth() * widthScale,
                jungle.getHeight() * heightScale);
    }

    private void drawGrass(Graphics g) {
        for(Vector2d pos: map.getGrass().keySet()){
            // może się zdarzyć sytuacja, że danego dnia zwierzę i trawa są na jednym polu;
            // należy pamiętać, że w metodzie objectAt, zwierzę ma większy priorytet niż trawa
            Object grass = map.objectAt(pos);
            if(grass instanceof Grass){
                g.setColor(((Grass) grass).toColor());
                int x = ((Grass) grass).getPosition().getX() * widthScale;
                int y = ((Grass) grass).getPosition().getY() * heightScale;
                g.fillRect(x, y, widthScale, heightScale);
            }
        }
    }

    private void drawAnimals(Graphics g){
        for(Vector2d pos: map.getAnimals().keySet()){
            Animal animal = (Animal)map.objectAt(pos);
            g.setColor(animal.toColor());
            int x = animal.getPosition().getX() * widthScale;
            int y = animal.getPosition().getY() * heightScale;
            g.fillOval(x, y, widthScale, heightScale);
        }
    }
}