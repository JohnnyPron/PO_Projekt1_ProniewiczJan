package agh.cs.project.world_map;
import agh.cs.project.secondary_objects.Vector2d;
import agh.cs.project.helpers.*;
import agh.cs.project.interfaces.IPositionChangeObserver;
import agh.cs.project.interfaces.IWorldMap;
import agh.cs.project.main_objects.*;
import java.util.*;

public class GrassField implements IWorldMap, IPositionChangeObserver {
    private int width;
    private int height;
    private Jungle jungle;
    protected Map<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<Vector2d, ArrayList<Animal>>();
    private Map<Vector2d, Grass> grass = new LinkedHashMap<Vector2d, Grass>();

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Jungle getJungle() { return jungle; }

    protected Map<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrass() {
        return grass;
    }

    public GrassField(int width, int height, float jungleRatio, int tufts){
        if(jungleRatio <= 0 || jungleRatio > 0.5){
            throw new IllegalArgumentException("Jungle cannot contain more than half of the map!");
        }
        this.width = width;
        this.height = height;
        // podzielenie trawy między dżunglą a stepem
        int jungleTufts = Math.round(tufts * jungleRatio);
        int steppeTufts = tufts - jungleTufts;
        // wyznaczenie miejsca na dżunglę; biorąc pod uwagę, że dżungla będzie na środku mapy, lepiej będzie, jeśli
        // "parzystości" jej wymiarów będą odpowiadały "parzystościom" wymiarów mapy, tj. jeśli szerokość mapy jest
        // parzysta, to szerokość dżungli - także.
        float jRatioForSides = (float) Math.sqrt(jungleRatio);
        int jungleWidth = MultiplicationParser.parseResultOddOrEven(width, jRatioForSides);
        int jungleHeight = MultiplicationParser.parseResultOddOrEven(height, jRatioForSides);
        // ustalenie odpowiednich koordynatów, żeby dźungla była na środku; należy wziąć pod uwagę szerokości stepu
        // między krawędziami mapy a krawędziami dżungli (potocznie: step jest ramką wokół dżungli); Przykładowe obliczenia:
        // width = 12; jungleWidth = 4;
        // szerokości stepu między brzegami mapy a brzegami dżungli: (width - jungleWidth) / 2 = 4
        // koordynaty X dla poszczególnych stref (step-dżungla-step): 0123-4567-89(10)(11)
        int steppeWidth = (width - jungleWidth) / 2;
        int steppeHeight = (height - jungleHeight) / 2;
        this.jungle = new Jungle(this, new Vector2d(steppeWidth, steppeHeight),
                new Vector2d(width - steppeWidth - 1, height - steppeHeight - 1), jungleTufts);
        // umieszczenie trawy z dżungli w słowniku mapy
        Map<Vector2d, Grass> jungleGrass = this.jungle.getGrass();
        for(Vector2d gpos: jungleGrass.keySet()){
            Grass g = jungleGrass.get(gpos);
            this.grass.put(g.getPosition(), g);
        }
        // umieszczenie trawy na stepie
        // zabezpieczenie przed wprowadzeniem większej liczby kępek trawy, niż mapa będzie w stanie pomieścić
        int steppeArea = width * height - this.jungle.getWidth() * this.jungle.getHeight();
        if(steppeTufts > steppeArea){steppeTufts = steppeArea;}
        for(int t = 0; t < steppeTufts; t++){
            Vector2d new_v = null;  // utworzenie pustego wektora dla kępki trawy 't'
            while (new_v == null) {
                // pętla ponawia iterację, jeżeli wylosowane koordynaty będą wskazywały na dźunglę
                int rand_x = new Random().nextInt(width);
                int rand_y = new Random().nextInt(height);
                if(rand_x >= steppeWidth && rand_x < (width - steppeWidth) && rand_y >= steppeHeight &&
                        rand_y < (height - steppeHeight)){ continue; }
                new_v = new Vector2d(rand_x, rand_y);  // przypisanie wektorowi konkretnej wartości
                // jeśli się okaże, że trawa na tej pozycji już występuje, wektor staje się pusty
                Grass existingGrass = grass.get(new_v);
                if(existingGrass != null){ new_v = null; }
            }
            grass.put(new_v, new Grass(new_v));
        }
    }

    private void removeAnimal(Vector2d position, Animal animal){
        ArrayList<Animal> animal_list = animals.get(position);
        animal_list.remove(animal);
        // usunięcie całej pozycji ze słownika, jeśli w danym miejscu już nie ma zwierząt
        if(animal_list.size() == 0){
            animals.remove(position);
        }
        // umiejscowienie listy z powrotem po usunięciu zwierzaka
        else{
            animals.put(position, animal_list);
        }
    }

    private void addAnimalToList(Animal animal){
        ArrayList<Animal> animal_list = animals.get(animal.getPosition());
        if(animal_list == null){
            animal_list = new ArrayList<Animal>();
        }
        animal_list.add(animal);
        // sortowanie listy malejąco, względem energii zwierząt
        Collections.sort(animal_list);
        Collections.reverse(animal_list);
        animals.put(animal.getPosition(), animal_list);
    }

    public boolean place(Animal animal){
        // miejsce musi być wolne od innych zwierząt i trawy
        if(!isOccupied(animal.getPosition())){
            // jeśli zwierze zostanie umieszczone poza mapą, jego pozycja jest modyfikowana na podstawie PWB
            if(!animal.getPosition().Follows(new Vector2d(0, 0)) ||
                    !animal.getPosition().Precedes(new Vector2d(width - 1, height - 1))){
                animal.setPosition(PWB(animal.getPosition()));
            }
            // kiedy zwierzę zostaje dodane do mapy, ona staje się jego obserwatorem
            animal.addObserver(this);
            ArrayList<Animal> animal_list = new ArrayList<Animal>();
            animal_list.add(animal);
            animals.put(animal.getPosition(), animal_list);
            return true;
        }
        else{ return false; }
    }

    public void placeAlternative(Animal animal){
        // jeśli zwierze zostanie umieszczone poza mapą, jego pozycja jest modyfikowana na podstawie PWB
        if(!animal.getPosition().Follows(new Vector2d(0, 0)) ||
                !animal.getPosition().Precedes(new Vector2d(width - 1, height - 1))){
            animal.setPosition(PWB(animal.getPosition()));
        }
        // kiedy zwierzę zostaje dodane do mapy, ona staje się jego obserwatorem
        animal.addObserver(this);
        // umieszczenie zwierzaka na liście z innymi zwierzakami
        addAnimalToList(animal);
    }

    public void positionChanged(Vector2d oldPosition, Animal animal){
        // usunięcie zwierzaka z poprzedniej listy (dla pozycji, gdzie stał wcześniej)
        removeAnimal(oldPosition, animal);
        // umieszczenie zwierzaka na nowej pozycji (samego lub z innymi zwierzętami)
        addAnimalToList(animal);
    }

    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    public Object objectAt(Vector2d position){
        ArrayList<Animal> animal_list = animals.get(position);
        Object obj;
        if(animal_list != null){
            obj = animal_list.get(0);
        }
        else {
            obj = grass.get(position);
        }
        return obj;
    }

    public Vector2d PWB(Vector2d new_position){
        // metoda zmienia położenie zwierzaka, jeśli ten wyjdzie "poza mapę", względem odpowiednich krawędzi
        if(new_position.getX() < 0) new_position = new_position.Add(new Vector2d(width, 0));
        if(new_position.getX() >= width) new_position = new_position.Subtract(new Vector2d(width, 0));
        if(new_position.getY() < 0) new_position = new_position.Add(new Vector2d(0, height));
        if(new_position.getY() >= height) new_position = new_position.Subtract(new Vector2d(0, height));
        return new_position;
    }

    public void removeDeadAnimal(Animal deadAnimal){
        // dodatkowe upewnienie się, czy zwierzak jest martwy
        if(deadAnimal.getCurrentEnergy() == 0){
            removeAnimal(deadAnimal.getPosition(), deadAnimal);
            // ponieważ zwierzak został permanentnie usunięty z mapy, ona nie jest już jego obserwatorem
            deadAnimal.removeObserver(this);
        }
    }

    public void growGrass(){
        int occupiedSteppe = 0;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Vector2d vectorToCheck = new Vector2d(i, j);
                if(isOccupied(vectorToCheck) && (!vectorToCheck.Follows(jungle.getBottomLeftCorner()) ||
                        !vectorToCheck.Precedes(jungle.getUpperRightCorner()))){
                    occupiedSteppe = occupiedSteppe + 1;
                }
            }
        }
        if(occupiedSteppe != width * height - jungle.getWidth() * jungle.getHeight()){
            Vector2d newVector = null;
            while (newVector == null){
                int rand_x = new Random().nextInt(width);
                int rand_y = new Random().nextInt(height);
                newVector = new Vector2d(rand_x, rand_y);
                if(isOccupied(newVector) || (newVector.Follows(jungle.getBottomLeftCorner()) &&
                        newVector.Precedes(jungle.getUpperRightCorner()))){ newVector = null; }
            }
            Grass newTuft = new Grass(newVector);
            grass.put(newVector, newTuft);
        }
        Grass jungleGrass = jungle.growGrass();
        if(jungleGrass != null){ grass.put(jungleGrass.getPosition(), jungleGrass); }
    }

    public void grassEating(){
        // przygotowanie listy kępek trawy, które zostaną zjedzone
        ArrayList<Grass> eatenGrass = new ArrayList<Grass>();
        // iteracja po kolejnych kępkach trawy na mapie
        for(Vector2d pos: grass.keySet()){
            // pobranie listy zwierząt dla danego pola z trawą
            ArrayList<Animal> animal_list = animals.get(pos);
            // wykonywanie dalszych operacji, jeśli na polu trawy rzeczywiście przebywają zwierzęta
            if(animal_list != null){
                // pobranie aktualnej trawy do zjedzenia
                Grass grassToEat = grass.get(pos);
                eatenGrass.add(grassToEat);
                // przygotowanie listy dla najsilniejszych zwierząt (o największej energii)
                ArrayList<Animal> strongestAnimals = AnimalPicker.getStrongestAnimals(animal_list, 1);
                // rozdzielenie energii trawy między najsilniejszymi zwierzętami
                int splitEnergy;
                // jeśli zwierząt jest więcej niż energii, jaką może zapewnić trawa, każdy ze zwierzaków otrzymuje po jednostce energii
                if(grassToEat.getPlantEnergy() < strongestAnimals.size()){
                    splitEnergy = 1;
                }
                else{
                    splitEnergy = grassToEat.getPlantEnergy() / strongestAnimals.size();
                }
                // uzupełnienie energii przez zwierzęta
                for(Animal an: strongestAnimals){
                    an.replenishEnergy(splitEnergy);
                }
            }
        }
        // usunięcie zjedzonych kępek trawy z mapy
        for(Grass gr: eatenGrass){
            grass.remove(gr.getPosition());
            // usunięcie trawy ze słownika dżungli (jeśli danej trawy tam nie było, nic się nie stanie)
            this.jungle.removeGrass(gr.getPosition());
        }
    }

    public ArrayList<Animal> multiplyAnimals(){
        ArrayList<Animal> children = new ArrayList<Animal>();
        for(Vector2d pos: new LinkedHashMap<Vector2d, ArrayList<Animal>>(animals).keySet()){
            ArrayList<Animal> animal_list = animals.get(pos);
            if(animal_list.size() == 1) { continue; }
            ArrayList<Animal> strongestAnimals = AnimalPicker.getStrongestAnimals(animal_list, 2);
            Animal firstParent, secondParent;
            // Dwa przypadki:
            // 1. Dwa pierwsze zwierzęta mają tyle samo energii, zatem "nadprogramowe" też mają tyle samo;
            // 2. Dwa pierwsze zwierzęta różnią się pod względem energii, zatem "nadprogramowe" bedą miały tyle samo
            // energii, co drugi zwierzak.
            if(strongestAnimals.get(0).getCurrentEnergy() != strongestAnimals.get(1).getCurrentEnergy()){
                firstParent = strongestAnimals.get(0);
                secondParent = strongestAnimals.get(new Random().nextInt(strongestAnimals.size() - 1) + 1);
            }
            else {
                int randomIndex1 = new Random().nextInt(strongestAnimals.size());
                int randomIndex2 = new Random().nextInt(strongestAnimals.size());
                while (randomIndex1 == randomIndex2){
                    randomIndex2 = new Random().nextInt(strongestAnimals.size());
                }
                firstParent = strongestAnimals.get(randomIndex1);
                secondParent = strongestAnimals.get(randomIndex2);
            }
            Animal child = firstParent.procreate(secondParent);
            if(child != null){
                boolean childPlaced = place(child);
                if(!childPlaced){
                    Vector2d originalPosition = child.getPosition();
                    Vector2d parentPosition = firstParent.getPosition();
                    Vector2d[] aroundParents = {parentPosition.Add(new Vector2d(1, 0)),
                            parentPosition.Add(new Vector2d(1, -1)), parentPosition.Add(new Vector2d(0, -1)),
                            parentPosition.Add(new Vector2d(-1, -1)), parentPosition.Add(new Vector2d(-1, 0)),
                            parentPosition.Add(new Vector2d(-1, 1)), parentPosition.Add(new Vector2d(0, 1)),
                            parentPosition.Add(new Vector2d(1, 1))};
                    List<Vector2d> aroundParentsList = Arrays.asList(aroundParents);
                    Collections.shuffle(aroundParentsList);
                    aroundParentsList.toArray(aroundParents);
                    for(int i = 0; i < 8; i++){
                        child.setPosition(aroundParents[i]);
                        childPlaced = place(child);
                        if(childPlaced){ break; }
                        if(i == 7){
                            child.setPosition(originalPosition);
                            placeAlternative(child);
                        }
                    }
                }
                children.add(child);
            }
        }
        // zwrócenie listy potomków dla symulatora.
        return children;
    }
}
