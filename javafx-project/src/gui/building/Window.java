package gui.building;

import javafx.geometry.Point2D;

public class Window extends Thing {
    Window(Point2D p) {
        super(p, 1, 1, true);
        type = "window";
        roomType = "any";
    }
}
