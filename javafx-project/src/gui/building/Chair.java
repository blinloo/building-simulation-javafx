package gui.building;

import javafx.geometry.Point2D;

public class Chair extends Thing {
    Chair(Point2D p) {
        super(p, 1, 1, true);
        type = "chair";
        roomType = "kitchen";
    }
}
