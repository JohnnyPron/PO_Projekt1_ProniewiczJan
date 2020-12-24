package agh.cs.project.world_map;
import agh.cs.project.secondary_objects.DNA;
import agh.cs.project.world_sim.SimulationEngine;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatisticsVisualizer extends JPanel {
    private GrassField map;
    private SimulationEngine simulator;
    private int totalDays;

    public StatisticsVisualizer(GrassField map, SimulationEngine simulator){
        this.map = map;
        this.simulator = simulator;
        this.totalDays = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // określenie wyjściowych koordynatów dla łańcuchów znaków, przedstawiających statystyki
        int statX1 = (int) Math.round(getWidth() * 0.15);
        int statX2 = (int) Math.round(getWidth() * 0.55);
        int statY = 50;
        int a = 50;
        // zebranie statystyk
        totalDays++;
        int animalPopulation = simulator.returnAnimalPopulation();
        int grassPopulation = simulator.returnGrassPopulation();
        ArrayList<DNA> dominatingGenes = simulator.returnDominatingDNA();
        float avgAnimalEnergy = simulator.avgAnimalEnergy();
        float avgLengthOfLife = simulator.avgLengthOfLife();
        // przedstawienie statystyk w formie liczbowej
        g.drawString("Num. of days: " + totalDays, statX1, statY);
        g.drawString("Animals population: " + animalPopulation, statX1, statY + a);
        g.drawString("Grass population: " + grassPopulation, statX2, statY + a);
        g.drawString("Avg. animal energy: " + avgAnimalEnergy, statX1, statY + 2 * a);
        g.drawString("Avg. animal lifespan: " + avgLengthOfLife, statX2, statY + 2 * a);
        g.drawString("Dominating Genes: ", statX1 - 50, statY + 3 * a);
        if(dominatingGenes.size() != 0){
            for(int i = 0; i < dominatingGenes.size(); i++){
                g.drawString(dominatingGenes.get(i).toString(), statX1 - 50, statY + 3 * a + (i + 1) * 15);
            }
        }
        // Rysowanie legendy
        int legendPointX = (int) Math.round(getWidth() * 0.1);;  // oryginalnie 'cpx'
        int legendPointY = getHeight() * 2 / 3 - 70; // oryginalnie 'cpy' i 'yp'
        g.setColor(new Color(0, 0, 0));
        g.drawString("Legend: ", legendPointX, legendPointY);
        int b = getHeight() / 22;  // oryginalnie 'a'
        // rysowanie nazw elementów mapy
        String[] legendNames = {"Steppe Field ", "Jungle Field ", "Grass ", "Healthy Animal ", "Weak Animal ",
                "Starving Animal ", "Dead Animal "};
        for(int i = 0; i < legendNames.length; i++){
            g.drawString(legendNames[i], legendPointX + getWidth() / 10, legendPointY + (i + 1) * b);
        }

        // rysowanie elementów mapy w legendzie
        Color[] colors = {new Color(255, 254, 181), new Color(124, 230, 44),
                new Color(10, 128, 22), new Color(23, 23, 207), new Color(224, 104, 13),
                new Color(224, 13, 13), new Color(0, 0, 0)};
        for(int i = 0; i < colors.length; i++){
            g.setColor(colors[i]);
            int y = legendPointY + (i + 1) * b - 10;
            if(i > 2){ g.fillOval(legendPointX, y, getWidth() / 20, getHeight() / 40); }
            else{ g.fillRect(legendPointX, y, getWidth() / 20, getHeight() / 40); }
        }
    }
}
