package org.usfirst.frc.team5431.perception;

import org.opencv.core.Range;

import com.kylecorry.frc.vision.TargetSpecs;

public class PegSpecs implements TargetSpecs {
    @Override
    public Range getHue() {
        return new Range(50, 100); //Min H to Max H
    }

    @Override
    public Range getSaturation() {
        return new Range(15, 20); //Min S to Max S
    }

    @Override
    public Range getValue() {
        return new Range(10, 100); //Min V to Max V
    }

    @Override
    public double getWidth() {
        return 2; //Real life width (inches)
    }

    @Override
    public double getHeight() {
        return 5; //Real life height (inches)
    }

    @Override
    public double getArea() {
        return 10; //Real life area (inches cubed)
    }

    @Override
    public int getMinPixelArea() {
        return 50; //Minimum pixel area for the target
    }
}
