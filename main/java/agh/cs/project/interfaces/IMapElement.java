package agh.cs.project.interfaces;
import agh.cs.project.secondary_objects.Vector2d;
import java.awt.*;

public interface IMapElement {
    /**
     * Metoda, pobierająca aktualną pozycję obiektu.
     * @return Aktualna pozycja obiektu.
     */
    Vector2d getPosition();

    /**
     * Metoda, przydzielająca obiektowi konkretną pozycję "z zewnątrz".
     * @param position - pozycja do przydzielenia zwierzakowi.
     */
    void setPosition(Vector2d position);

    /**
     * Metoda, zwracająca barwę, jaką dany obiekt powinien mieć na mapie.
     * @return - kolorystyczna reprezentacja obiektu
     */
    Color toColor();
}
