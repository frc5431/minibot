package org.usfirst.frc.team5431.perception;

import com.kylecorry.frc.vision.TargetGroupSpecs;

public class PegGroupSpecs implements TargetGroupSpecs {
    @Override
    public double getTargetWidthRatio() {
        return 95; //Percentage width ratio (How close should the widths be (0 - 100))
    }

    @Override
    public double getTargetHeightRatio() {
        return 95; //Percentage height ratio (How close should the hieghts be (0 - 100)) 
    }

    @Override
    public Alignment getAlignment() {
        return Alignment.TOP;
    }

    @Override
    public double getGroupWidth() {
        return 11.5; //The total width of the group in inches
    }

    @Override
    public double getGroupHeight() {
        return 5; //The total height of the group in inches
    }
}