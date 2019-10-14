package gui.building;

import javafx.geometry.Point2D;

public class Furnace extends Thing {
    Furnace(Point2D p) {
        super(p, 1, 1, true);
        type = "furnace";
        roomType = "kitchen";
    }
}