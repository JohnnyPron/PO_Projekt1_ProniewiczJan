package agh.cs.project.interfaces;

import agh.cs.project.secondary_objects.DNA;

import java.util.ArrayList;

/**
 * Interfejs, odpowiedzialny za przeprowadzanie symulacji życia i ewolucji na mapie.
 */
public interface IEngine {
    /**
     * Metoda, odpowiedzialna za skręt i przemieszczenie każdego zwierzaka na nową pozycję.
     */
    void run();

    /**
     * Usunięcie wszystkich martwych zwierząt z mapy.
     */
    void removeDeadAnimals();

    /**
     * Rozmnażanie zwierząt na mapie i dodanie potomków do listy zwierząt symulatora.
     */
    void multiplyAnimals();

    /**
     * Metoda główna, inicjująca przebieg symulacji
     */
    void startSimulation();

    /**
     * Zwrócenie populacji zwierząt na mapie.
     * @return - liczebność żywych zwierząt na mapie.
     */
    int returnAnimalPopulation();

    /**
     * Zwrócenie "populacji" roślin na mapie.
     * @return - liczebność niezjedzonych roślin na mapie.
     */
    int returnGrassPopulation();

    /**
     * Zidentyfikowanie najczęściej występującego genotypu (lub jednego z nich)
     * @return najczęściej występujący genotyp
     */
    ArrayList<DNA> returnDominatingDNA();

    /**
     * Obliczenie średniej energii żyjących zwierząt.
     * @return - średnia energia żyjących zwierząt.
     */
    float avgAnimalEnergy();

    /**
     * Obliczenie średniej długości życia zwierząt, na podstawie statystyk martwych osobników.
     * @return - średnia długość życia (liczba dni) zwierząt.
     */
    float avgLengthOfLife();
}
