package agh.cs.project.main_objects;
import agh.cs.project.secondary_objects.Vector2d;
import agh.cs.project.interfaces.IMapElement;

abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position;

    public AbstractWorldMapElement(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) { this.position = position; }
}
