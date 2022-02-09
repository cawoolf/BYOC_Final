package com.rayadev.byoc.util;

public class CustomConverterUtil extends ConverterUtil{

    private double mUnitAValue, mUnitBValue;

    public CustomConverterUtil(double unitAValue, double unitBValue) {
        super();
        this.mUnitAValue = unitAValue;
        this.mUnitBValue = unitBValue;
    }

    //Need to get the ratios correct here. Test
    public double convertABCustomUnits(double valueA, double valueB, double input) {
        double ratio = valueB/valueA;
        return input * ratio;

    }

    public double convertBACustomUnits(double valueA, double valueB, double input) {
        double ratio = valueA/valueB;
        return input * ratio;
    }

    public double getUnitAValue() {
        return mUnitAValue;
    }

    public double getUnitBValue() {
        return mUnitBValue;
    }
}
