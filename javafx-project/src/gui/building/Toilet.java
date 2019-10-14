package gui.building;

import javafx.geometry.Point2D;

public class Toilet extends Thing {
    Toilet(Point2D p) {
        super(p, 1, 1, true);
        type = "toilet";
        roomType = "bathroom";
    }
}