package agh.cs.project.helpers;
import agh.cs.project.main_objects.Animal;
import java.util.ArrayList;
import java.util.Collections;

public class AnimalPicker {
    public static ArrayList<Animal> getStrongestAnimals(ArrayList<Animal> animalList, int minAnimalsNeeded){
        // sortowanie listy malejąco, względem energii zwierząt
        Collections.sort(animalList);
        Collections.reverse(animalList);
        // przygotowanie listy najsilniejszych zwierząt
        ArrayList<Animal> strongestAnimals = new ArrayList<Animal>();
        // iterowanie po kolejnych zwierzakach z listy, w kolejności od najsilniejszego do najsłabszego
        for(int i = 0; i < animalList.size(); i++){
            Animal animalCandidate = animalList.get(i);
            // jeśli następny zwierzak ma mniej energii niż poprzedni, pętla zostaje przerwana i już nie szuka kolejnych
            // zwierząt; warunek jest brany pod uwagę, jeżeli lista zawiera już minimalną liczbę poszukiwanych zwierząt
            if(i >= minAnimalsNeeded){
                if(animalCandidate.getCurrentEnergy() < animalList.get(i-1).getCurrentEnergy()){ break; }
            }
            // dodanie najsilniejszego zwierzaka na listę
            strongestAnimals.add(animalCandidate);
        }
        return strongestAnimals;
    }
}
