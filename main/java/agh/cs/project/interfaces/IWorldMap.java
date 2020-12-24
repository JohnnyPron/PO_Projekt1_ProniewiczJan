package agh.cs.project.interfaces;
import agh.cs.project.main_objects.Animal;
import agh.cs.project.secondary_objects.Vector2d;

import java.util.ArrayList;

/**
 * Interfejs, zarządzający procesami na mapie, tj. dotyczących właściwości samej mapy lub jej obiektów.
 */
public interface IWorldMap {
    /**
     * Umieszczenie zwierzęcia na mapie, na wolnym miejscu.
     * @param animal - Zwierzę, do umieszczenia na mapie.
     * @return True, jeśli zwierzak został umiejscowiony; False, jeżeli próbowano umieścić zwierzaka na zajętym miejscu
     */
    boolean place(Animal animal);

    /**
     * Umieszczenie zwierzaka na mapie, mimo zajętych miejsc.
     * @param animal - zwierzę, do umieszczenia na mapie.
     */
    void placeAlternative(Animal animal);

    /**
     * Metoda sprawdzająca, czy miejsce jest zajęte przez jakiś obiekt (zwierzę lub trawę).
     * @param position - pozycja do sprawdzenia.
     * @return True jeśli miejsce jest zajęte przez obiekt; false w przeciwnym wypadku.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Zwracanie obiektu na podanej pozycji (zwierzę lub trawę).
     * @param position - pozycja do sprawdzenia (czy znajduje się na niej obiekt).
     * @return Znaleziony obiekt, bądź null, jeśli pozycja jest pusta.
     */
    Object objectAt(Vector2d position);

    /**
     * Metoda, obsłująca periodyczne warunki brzegowe dla zwierzaków.
     * @param new_position - pozycja zwierzaka po wykonaniu ruchu (dowolna; nie ważne, czy zwierzak wyszedł poza mapę)
     * @return Zmieniona pozyja zwierzaka względem krawędzi mapy bądź niezmieniona pozycja, jeśli zwierzak nie wychodzi
     * poza mapę.
     */
    Vector2d PWB(Vector2d new_position);

    /**
     * Metoda usuwająca martwe zwierzę z mapy.
     * @param deadAnimal - martwy zwierzak do usunięcia.
     */
    void removeDeadAnimal(Animal deadAnimal);

    /**
     * Metoda dodająca dwie kępki trawy na mapę (po jednej na step i dżunglę).
     */
    void growGrass();

    /**
     * Proces jedzenia trawy przez zwierzęta na mapie.
     */
    void grassEating();

    /**
     * Proces rozmnażania zwierząt na mapie.
     * @return Lista powstałych potomków (niezbędna dla symulatora)
     */
    ArrayList<Animal> multiplyAnimals();
}
