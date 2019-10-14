package gui.building;

import javafx.geometry.Point2D;

public class Bed extends Thing {
    Bed(Point2D p, int w, int h) {
        super(p, w, h,true);
        type = "bed";
        roomType = "bedroom";
    }
}