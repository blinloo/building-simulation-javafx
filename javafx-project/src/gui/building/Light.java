package gui.building;

import javafx.geometry.Point2D;

public class Light extends Thing {
    Light(Point2D p) {
        super(p, 1, 1, false);
        type = "light";
        roomType = "any";
    }
}
