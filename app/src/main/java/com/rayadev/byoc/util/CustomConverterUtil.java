package com.rayadev.byoc.util;

public class CustomConverterUtil extends ConverterUtil{

    private double mUnitAValue, mUnitBValue;

    public CustomConverterUtil(double unitAValue, double unitBValue) {
        super();
        this.mUnitAValue = unitAValue;
        this.mUnitBValue = unitBValue;
    }

    public double convertCustomUnits(double input, double multiplier) {
        return input * multiplier;

    }

    public double getUnitAValue() {
        return mUnitAValue;
    }

    public double getUnitBValue() {
        return mUnitBValue;
    }
}
