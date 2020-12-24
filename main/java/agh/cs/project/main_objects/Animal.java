package agh.cs.project.main_objects;
import agh.cs.project.secondary_objects.*;
import agh.cs.project.interfaces.IPositionChangeObserver;
import agh.cs.project.interfaces.IWorldMap;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Animal extends AbstractWorldMapElement implements Comparable<Animal>{
    private MapDirection orientation;
    private DNA genes;
    private static int startEnergy;
    private int currentEnergy;
    private static int moveEnergy;
    private int lifeLength;
    private IWorldMap map;
    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();

    public MapDirection getOrientation() {
        return orientation;
    }

    public int getCurrentEnergy() { return currentEnergy; }

    public DNA getGenes() { return genes; }

    public int getLifeLength() { return lifeLength; }

    protected List<IPositionChangeObserver> getObservers() {
        return observers;
    }

    public int getStartEnergy() { return startEnergy; }

    public void setOrientation(MapDirection orientation) { this.orientation = orientation; }

    /**
     * Kontruktor dla zwierzaków, wprowadzonych na początku symulacji (zaczynają ze "standardową" energią początkową)
     * @param map - mapa, po której porusza się zwierzak
     * @param initialPosition - początkowa pozycja zwierzaka
     */
    public Animal(IWorldMap map, Vector2d initialPosition){
        super(initialPosition);
        this.orientation = MapDirection.getRandomDirection();
        this.genes = new DNA();
        this.genes.setRandomGenes();
        this.currentEnergy = startEnergy;
        this.map = map;
        this.lifeLength = 0;
    }

    /**
     * Konstruktor dla zwierzaków potomnych (ich początkowa energia może być różna od "standardowej początkowej")
     * @param map - mapa, po której porusza się zwierzak
     * @param initialPosition - początkowa pozycja zwierzaka
     * @param currentEnergy - "obecna energia" zwierzaka, której w tym przypadku jednocześnie przyporządkowana jest
     *                      początkowa energia potomka
     */
    public Animal(IWorldMap map, Vector2d initialPosition, DNA genes, int currentEnergy){
        super(initialPosition);
        this.orientation = MapDirection.getRandomDirection();
        this.genes = genes;
        this.currentEnergy = currentEnergy;
        this.map = map;
        this.lifeLength = 0;
    }

    public static void setEnergies(int newStartEnergy, int newMoveEnergy){
        if(newMoveEnergy >= newStartEnergy * 0.5) { throw new IllegalArgumentException("Move energy value needs to be " +
                "smaller than half of the start energy!"); }
        startEnergy = newStartEnergy;
        moveEnergy = newMoveEnergy;
    }

    public void Move(){
        // przygotowanie zmiennej dla nowej pozycji (po wykonaniu ruchu)
        Vector2d old_position = position;
        Random random = new Random();
        int turn = this.genes.getGenes()[random.nextInt(32)];
        if(turn > 0 & turn < 4){
            for(int i = 0; i < turn; i++){
                orientation = orientation.Next();
            }
        }
        else if(turn > 4){
            for(int i = 8; i > turn; i--){
                orientation = orientation.Previous();
            }
        }
        else if(turn == 4){
            orientation = orientation.OppositeDirection();
        }
        Vector2d new_position = position.Add(orientation.ToUnitVector());
        new_position = map.PWB(new_position);
        position = new_position;
        lifeLength++;
        looseEnergy(moveEnergy);
        positionChanged(old_position);
    }

    // W niniejszym projekcie, metoda jest implementowana wewnątrz metody 'place' mapy
    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition){
        for(IPositionChangeObserver o: observers){
            o.positionChanged(oldPosition, this);
        }
    }

    public void replenishEnergy(int moreEnergy){
        currentEnergy = currentEnergy + moreEnergy;
    }

    private void looseEnergy(int lessEnergy){
        currentEnergy = currentEnergy - lessEnergy;
        if(currentEnergy < 0){ currentEnergy = 0; }
    }

    public Animal procreate(Animal partnerAnimal){
        // jeśli któryś z partnerów będzie miał za mało energii, nie dochodzi do rozrodu
        if(this.currentEnergy < startEnergy * 0.5 || partnerAnimal.currentEnergy < startEnergy * 0.5){ return null; }
        // pobranie części energii od rodziców dla dziecka
        // jeśli program pobierze od rodzica "zero energii", dziecko otrzymuje od niego jednostkę energii.
        int firstParentEnergy = this.currentEnergy / 4;
        if(firstParentEnergy == 0){ firstParentEnergy = 1; }
        int secondParentEnergy = partnerAnimal.currentEnergy / 4;
        if(secondParentEnergy == 0){ secondParentEnergy = 1; }
        int childEnergy = firstParentEnergy + secondParentEnergy;
        // odjęcie wykorzystanej energii od rodziców
        this.looseEnergy(firstParentEnergy);
        partnerAnimal.looseEnergy(secondParentEnergy);
        // krzyżowanie genów rodziców
        DNA childGenes = this.genes.crossingGenes(partnerAnimal.genes);
        // ustalenie pozycji dziecka
        // pozycja dziecka jest losowa, w obrębie miejsca, w którym znajdowali się rodzice
        int parentX = this.position.getX();
        int parentY = this.position.getY();
        Vector2d childVector = null;
        while (childVector == null){
            // max = parentx + 1; min = parentx - 1; random[min; max] = random.nextInt(max - min + 1) + min
            // random.nextInt(parentx+1 - parentx+1 + 1) + parentX-1
            int childX = new Random().nextInt(3) + parentX - 1;
            int childY = new Random().nextInt(3) + parentY - 1;
            childVector = new Vector2d(childX, childY);
            // dziecko nie może znajdować się na tym samym miejscu, co rodzice
            if(childVector.equals(this.position)) { childVector = null; }
        }
        return new Animal(this.map, childVector, childGenes, childEnergy);
    }

    @Override
    public int compareTo(Animal animalComp){
        // zwierzęta są ze sobą porównywane na podstawie ich obecnej energii
        return Integer.compare(this.currentEnergy, animalComp.getCurrentEnergy());
    }

   /**
     * Reprezentacja kolorystyczna zwierzaka, która zależy od jego stanu energii:
     * niebieski - zdrowy/dorosły (zdolny do rozmnażania)
     * pomarańczowy - słaby/młody
     * czerwony - wygłodzony
     * czarny - martwy
     * @return Kolor, który ma przyjąć zwierzak na mapie
     */
    @Override
    public Color toColor() {
        if(this.currentEnergy >= startEnergy * 0.5){
            return new Color(23, 23, 207);
        }
        else if(this.currentEnergy >= startEnergy * 0.25 && this.currentEnergy < startEnergy * 0.5){
            return new Color(224, 104, 13);
        }
        else if(this.currentEnergy > 0 && this.currentEnergy < startEnergy * 0.25){
            return new Color(224, 13, 13);
        }
        else{
            return new Color(0, 0, 0);
        }
    }
}
