package gui.building;

import javafx.geometry.Point2D;

public class Sink extends Thing {
    Sink(Point2D p) {
        super(p, 1, 1, true);
        type = "sink";
        roomType = "bathroom";
    }
}