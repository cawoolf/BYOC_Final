package com.rayadev.byoc.util;


import android.util.Log;

public class ConverterUtil {


    public ConverterUtil() {

    }

    //Reference points for the units.
    public enum Unit {

        //Test Unit
        TEST_UNIT,

        //Area
        SQ_KILOMETER,
        SQ_METER,
        SQ_CENTIMETER,
        ACRE,
        SQ_MILE,
        SQ_FOOT,
        SQ_INCH,

        //Distance
        INCH,
        CENTIMETER,
        FOOT,
        YARD,
        METER,
        MILE,
        MILLIMETER,
        KILOMETER,

        //Currency
        USD,
        EUR,
        CAD,
        MXN,
        JPY,
        AUD,
        NZD,
        CNY,

        //Temperature
        CELSIUS,
        FAHRENHEIT,
        KELVIN,

        //Time
        DECADE,
        YEAR,
        MONTH,
        WEEK,
        DAY,
        HOUR,
        SECOND,

        //Volume
        GALLON,
        QUART,
        FLUID_OUNCE,
        CUP,
        TABLESPOON,
        CUBIC_METER,
        LITRE,
        CENTILITER,
        MILLILITER,

        //Weight
        METRIC_TONNE,
        KILOGRAM,
        GRAM,
        TON,
        POUND,
        OUNCE;

        // Helper method to select the unit, and convert text to one of the above constants
        public static Unit fromString(String text) {


            Unit foundUnit = TEST_UNIT;

            if(text.contains("")){
                foundUnit = sortIrregularUnitName(text);
                return foundUnit;
            }
            else if (text != null) {
                for (Unit unit : Unit.values()) {
                    if (text.equalsIgnoreCase(unit.toString())) {
                        foundUnit = unit;
                    }
                }

                return foundUnit;
            }
            throw new IllegalArgumentException("Cannot find a value for " + text);
        }

        //Handles the space " " in the text for Sq Kilometer... ext. Sets the proper enum Unit.
        private static Unit sortIrregularUnitName(String text) {

            Unit unit = TEST_UNIT;
            String[] irregularUnitNames = {"Sq Kilometer", "Sq Meter", "Sq Centimeter",
                    "Sq Mile", "Sq Foot", "Sq Inch","Fluid Ounce", "Cubic Meter", "Metric Tonne"};
            Unit[] unitEnums = {SQ_KILOMETER, SQ_METER, SQ_CENTIMETER, SQ_MILE, SQ_FOOT, FLUID_OUNCE, CUBIC_METER, METRIC_TONNE};

            for(int i = 0; i < irregularUnitNames.length; i++) {
                String unitString = irregularUnitNames[i];
                if(text.equals(unitString)) {
                    unit = unitEnums[i];
                    Log.i("UTAG", "Irregular Unit Name" + unit.toString());
                    return unit;
                }
            }

            return unit;
        }

    }



    // What can I multiply by to get me from my fromUnit to my toUnit?
    public double multiplier;

    //Constructor that creates the proper conversion ratios for the results.
    public ConverterUtil(Unit from, Unit to) {  //Add a category constant to this..IE distance ect.


        double constant = 1;
        // Set the multiplier, else if fromUnit = toUnit, then it is 1
        //Just using distance for now.
        switch (from) {
            case INCH:
                if (to == Unit.CENTIMETER) {
                    constant = 2.54;
                } else if (to == Unit.FOOT) {
                    constant = 0.0833333;
                } else if (to == Unit.YARD) {
                    constant = 0.0277778;
                } else if (to == Unit.METER) {
                    constant = 0.0254;
                } else if (to == Unit.MILE) {
                    constant = 1.5783e-5;
                } else if (to == Unit.KILOMETER) {
                    constant = 2.54e-5;
                }
                break;
            case CENTIMETER:
                if (to == Unit.INCH) {
                    constant = 0.393701;
                } else if (to == Unit.FOOT) {
                    constant = 0.0328084;
                } else if (to == Unit.YARD) {
                    constant = 0.0109361;
                } else if (to == Unit.METER) {
                    constant = 0.01;
                } else if (to == Unit.MILE) {
                    constant = 6.2137e-6;
                } else if (to == Unit.KILOMETER) {
                    constant = 1e-5;
                }
                break;
            case FOOT:
                if (to == Unit.INCH) {
                    constant = 12;
                } else if (to == Unit.CENTIMETER) {
                    constant = 30.48;
                } else if (to == Unit.YARD) {
                    constant = 0.333333;
                } else if (to == Unit.METER) {
                    constant = 0.3048;
                } else if (to == Unit.MILE) {
                    constant = 0.000189394;
                } else if (to == Unit.KILOMETER) {
                    constant = 0.0003048;
                }
                break;
            case YARD:
                if (to == Unit.INCH) {
                    constant = 36;
                } else if (to == Unit.CENTIMETER) {
                    constant = 91.44;
                } else if (to == Unit.FOOT) {
                    constant = 3;
                } else if (to == Unit.METER) {
                    constant = 0.9144;
                } else if (to == Unit.MILE) {
                    constant = 0.000568182;
                } else if (to == Unit.KILOMETER) {
                    constant = 0.0009144;
                }
                break;
            case METER:
                if (to == Unit.INCH) {
                    constant = 39.3701;
                } else if (to == Unit.CENTIMETER) {
                    constant = 100;
                } else if (to == Unit.FOOT) {
                    constant = 3.28084;
                } else if (to == Unit.YARD) {
                    constant = 1.09361;
                } else if (to == Unit.MILE) {
                    constant = 0.000621371;
                } else if (to == Unit.KILOMETER) {
                    constant = 0.001;
                }
                break;
            case MILE:
                if (to == Unit.INCH) {
                    constant = 63360;
                } else if (to == Unit.CENTIMETER) {
                    constant = 160934;
                } else if (to == Unit.FOOT) {
                    constant = 5280;
                } else if (to == Unit.YARD) {
                    constant = 1760;
                } else if (to == Unit.METER) {
                    constant = 1609.34;
                } else if (to == Unit.KILOMETER) {
                    constant = 1.60934;
                }
                break;
            case KILOMETER:
                if (to == Unit.INCH) {
                    constant = 39370.1;
                } else if (to == Unit.CENTIMETER) {
                    constant = 100000;
                } else if (to == Unit.FOOT) {
                    constant = 3280.84;
                } else if (to == Unit.YARD) {
                    constant = 1093.61;
                } else if (to == Unit.METER) {
                    constant = 1000;
                } else if (to == Unit.MILE) {
                    constant = 0.621371;
                }
                break;
            case MILLIMETER: //Some bugs with converting Millimeter.. Oh each other UNIT now needs a MILLIMETER
                if (to == Unit.INCH) {
                    constant = 0.0393701;
                } else if (to == Unit.CENTIMETER) {
                    constant = 0.1;
                } else if (to == Unit.FOOT) {
                    constant = 0.00328084;
                } else if (to == Unit.YARD) {
                    constant = 0.0010936133333333;
                } else if (to == Unit.METER) {
                    constant = 0.001;
                } else if (to == Unit.MILE) {
                    constant = 6.2137e-7;
                }
                 else if (to == Unit.KILOMETER) {
                    constant = 1e-6;
                }
        }

        multiplier = constant;
    }

    // Convert the unit!
    public double convert(double input) {
        return input * multiplier;
    }


}
