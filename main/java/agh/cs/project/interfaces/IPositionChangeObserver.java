package agh.cs.project.interfaces;
import agh.cs.project.main_objects.Animal;
import agh.cs.project.secondary_objects.Vector2d;

public interface IPositionChangeObserver {
    /**
     * Metoda, aktualizująca położenie zwierzęcia.
     * @param oldPosition - pozycja zwierzęcia przed wykonaniem ruchu.
     * @param animal - zwierzę, które wykonało ruch.
     */
    void positionChanged(Vector2d oldPosition, Animal animal);
}
