package gui.building;

import javafx.geometry.Point2D;

public class Table extends Thing
{
    Table(Point2D p, int w, int h)
    {
        super(p, w, h, true);
        type = "table";
        roomType = "kitchen";
    }

    public boolean getState(){
        return false;
    }
}
