package agh.cs.project.secondary_objects;
import java.util.Objects;

public class Vector2d {
    final int x;
    final int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean Precedes(Vector2d other){
        return this.getX() <= other.getX() & this.getY() <= other.getY();
    }

    public boolean Follows(Vector2d other){
        return this.getX() >= other.getX() & this.getY() >= other.getY();
    }

    public Vector2d Add(Vector2d other){
        int new_x = this.getX() + other.getX();
        int new_y = this.getY() + other.getY();
        return new Vector2d(new_x, new_y);
    }

    public Vector2d Subtract(Vector2d other){
        int new_x = this.getX() - other.getX();
        int new_y = this.getY() - other.getY();
        return new Vector2d(new_x, new_y);
    }

    public int xRange(Vector2d other){
        if(this.x >= other.x){
            return this.x - other.x + 1;
        }
        else{
            return other.x - this.x + 1;
        }
    }

    public int yRange(Vector2d other){
        if(this.y >= other.y){
            return this.y - other.y + 1;
        }
        else{
            return other.y - this.y + 1;
        }
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x &&
                y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
