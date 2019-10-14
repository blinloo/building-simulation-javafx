package gui.building;

import javafx.geometry.Point2D;

public class BedsideTable extends Thing {
    BedsideTable(Point2D p){
        super(p, 1, 1, true);
        type = "bedsideTable";
        roomType = "bedroom";
    }
}
