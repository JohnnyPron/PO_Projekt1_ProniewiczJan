package agh.cs.project.world_sim;
import agh.cs.project.interfaces.IEngine;
import agh.cs.project.interfaces.IWorldMap;
import agh.cs.project.main_objects.Animal;
import agh.cs.project.main_objects.Grass;
import agh.cs.project.secondary_objects.DNA;
import agh.cs.project.secondary_objects.Vector2d;
import agh.cs.project.world_map.*;

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class SimulationEngine implements IEngine, ActionListener {
    private GrassField map;
    private List<Animal> animals = new ArrayList<Animal>();
    private List<Animal> deadAnimals = new ArrayList<Animal>();
    public JFrame frame;
    public MapVisualizer mapVisualizer;
    public StatisticsVisualizer statisticsVisualizer;
    public Timer timer;
    boolean pause;

    public List<Animal> getAnimals() { return animals; }

    public SimulationEngine(int mapWidth, int mapHeight, int animalNum, int grassNum, int startEnergy, int moveEnergy,
                            int plantEnergy, float jungleRatio){
        if(mapWidth <=0 || mapHeight <= 0 || animalNum <= 0 || grassNum <= 0 || startEnergy <= 0 || moveEnergy <= 0 ||
        plantEnergy <= 0 || jungleRatio <= 0){ throw new IllegalArgumentException("Negative or zero parameter value(s) detected!"); }
        // ustawienie ilości początkowej energii i energii ruchu dla wszyskich zwierząt
        Animal.setEnergies(startEnergy, moveEnergy);
        // ustawienie ilości energii, dawanej przez trawę
        Grass.setPlantEnergy(plantEnergy);
        // utworzenie mapy, na której przeprowadzana będzie symulacja
        this.map = new GrassField(mapWidth, mapHeight, jungleRatio, grassNum);
        // wprowadzenie zwierząt na losowe miejsca na mapie
        int tryNum = 0;  // liczba prób, w której algorytm próbował znaleźć wolne miejsce dla danego zwierzaka
        for(int i = 0; i < animalNum; i++){
            boolean an_placed = false;
            while (!an_placed){
                // losowanie koordynatów zwierzęcia
                int randX = new Random().nextInt(mapWidth);
                int randY = new Random().nextInt(mapHeight);
                Vector2d animalPosition = new Vector2d(randX, randY);
                Animal an = new Animal(map, animalPosition);
                // umieszczenie zwierzaka na mapie; jeśli liczba prób była mniejsza, niż pole mapy, algorytm szuka
                // wolnego miejsca; w przeciwnym wypadku, można dojść do wniosku, że (z pewnym prawdopodobieństwem)
                // wszystkie miejsca są zajęte i należy umieścić zwierzę w sposób "alternatywny"
                if(tryNum < mapWidth * mapHeight){
                    an_placed = map.place(an);
                }
                else{
                    map.placeAlternative(an);
                    an_placed = true;
                }
                // dodanie zwierzaka do listy symulatora, jeżeli został on pomyślnie umieszczony na mapie
                if(an_placed){
                    animals.add(an);
                    // resetowanie licznika prób, jeśli ten nie przekroczył wartości pola mapy; w przeciwnym wypadku,
                    // jego wartość pozostaje niezmieniona, a następne zwierzęta są umieszczane w sposób "alternatywny"
                    if(tryNum < mapWidth * mapHeight){ tryNum = 0; }
                }
                // zwiększenie licznika prób, jeżeli dodanie zwierzaka się nie powiodło
                else {
                    tryNum++;
                }
            }
            // jeśli mapa zostanie wypełniona zwierzętami "po brzegi" (z pewnym prawdopodobieństwem), pętla przerywa
            // proces dodawania następnych
            if(animals.size() == mapWidth * mapHeight){ break; }
        }
        timer = new Timer(30, this);
        pause = false;
        frame = new JFrame("Evolution Simulator");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(1, 2));
        mapVisualizer = new MapVisualizer(this.map, this);
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton pauseButton = new JButton("Pause/Resume");
        pauseButton.addActionListener(e -> {
            if(!pause){
                pause = true;
                timer.stop();
            }
            else{
                pause = false;
                timer.start();
            }
        });
        buttonPanel.add(pauseButton);
        mapPanel.add(mapVisualizer, BorderLayout.CENTER);
        mapPanel.add(buttonPanel, BorderLayout.SOUTH);
        statisticsVisualizer = new StatisticsVisualizer(this.map, this);
        statisticsVisualizer.setSize(1, 1);
        main.add(statisticsVisualizer);
        main.add(mapPanel);
        frame.add(main);
    }

    public void startSimulation(){
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        mapVisualizer.repaint();
        statisticsVisualizer.repaint();
        removeDeadAnimals();
        run();
        map.grassEating();
        multiplyAnimals();
        map.growGrass();
        if(animals.size() == 0){
            timer.stop();
            JOptionPane.showMessageDialog(frame, "The species has become extinct!", "Game Over", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void run(){
        for(Animal a: animals){ a.Move(); }
    }

    public void removeDeadAnimals(){
        for(Animal an: new ArrayList<>(animals)){
            if(an.getCurrentEnergy() == 0){
                map.removeDeadAnimal(an);
                animals.remove(an);
                deadAnimals.add(an);
            }
        }
    }

    public void multiplyAnimals(){
        ArrayList<Animal> children = map.multiplyAnimals();
        animals.addAll(children);
    }

    public int returnAnimalPopulation(){ return animals.size(); }

    public int returnGrassPopulation(){ return map.getGrass().size(); }

    public ArrayList<DNA> returnDominatingDNA(){
        if(animals.size() == 0) { return new ArrayList<DNA>(); }
        ArrayList<DNA> genes = new ArrayList<DNA>();
        for(Animal an: animals){
            genes.add(an.getGenes());
        }
        ArrayList<DNA> checkedGenes = new ArrayList<DNA>();
        ArrayList<Integer> instances = new ArrayList<Integer>();
        for(DNA dna: genes){
            if(!checkedGenes.contains(dna)){
                int numOfInstances = Collections.frequency(genes, dna);
                checkedGenes.add(dna);
                instances.add(numOfInstances);
            }
        }
        int maxNum = Collections.max(instances);
        int index = instances.indexOf(maxNum);
        ArrayList<DNA> dominating = new ArrayList<DNA>();
        while (index != -1){
            dominating.add(checkedGenes.get(index));
            if(dominating.size() == 5){ break; }  // postanowiono, że program będzie pokazywał maksymalnie 5 dominujących genotypów
            checkedGenes.remove(index);
            instances.remove(index);
            index = instances.indexOf(maxNum);
        }
        return dominating;
    }

    public float avgAnimalEnergy(){
        int energySum = 0;
        for(Animal an: animals){
            energySum = energySum + an.getCurrentEnergy();
        }
        if(animals.size() == 0) { return 0f; }
        else { return (float) energySum / animals.size(); }
    }

    public float avgLengthOfLife(){
        int daysSum = 0;
        for(Animal dan: deadAnimals){
            daysSum = daysSum + dan.getLifeLength();
        }
        if(deadAnimals.size() == 0) { return 0f; }
        else { return (float) daysSum / deadAnimals.size(); }
    }
}
