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
        HECTARE,
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
        MINUTE,
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

            if (text.contains(" ")) {
                foundUnit = sortIrregularUnitName(text);
                return foundUnit;
            } else if (text != null) {
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
                    "Sq Mile", "Sq Foot", "Sq Inch", "Fluid Ounce", "Cubic Meter", "Metric Tonne"};
            Unit[] unitEnums = {SQ_KILOMETER, SQ_METER, SQ_CENTIMETER, SQ_MILE, SQ_FOOT, SQ_INCH, FLUID_OUNCE, CUBIC_METER, METRIC_TONNE};

            for (int i = 0; i <= irregularUnitNames.length; i++) {
                String unitString = irregularUnitNames[i];
                if (text.equals(unitString)) {
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


        //Initialize constant to prevent crash.
        double constant = 1;

        switch (from) {

            //AREA CASE STATEMENTS
            case HECTARE:
                if (to == Unit.HECTARE) {
                    //HECTARE Same Unit, constant = 1;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 0.01;
                } else if (to == Unit.SQ_METER) {
                    constant = 10000;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 1e+8;
                } else if (to == Unit.ACRE) {
                    constant = 2.47105;
                } else if (to == Unit.SQ_MILE) {
                    constant = 0.00386102;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 107639;
                } else if (to == Unit.SQ_INCH) {
                    constant = 1.55e+7;
                }
                break;

            case SQ_KILOMETER:
                if (to == Unit.HECTARE) {
                    constant = 100;
                } else if (to == Unit.SQ_KILOMETER) {
                    //SQ_KILOMETER Same Unit, constant = 1;
                    constant = 1;
                } else if (to == Unit.SQ_METER) {
                    constant = 1000000;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 1e+10;
                } else if (to == Unit.ACRE) {
                    constant = 247.105;
                } else if (to == Unit.SQ_MILE) {
                    constant = 0.386102;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 1.076e+7;
                } else if (to == Unit.SQ_INCH) {
                    constant = 1.55e+9;
                }
                break;

            case SQ_METER:
                if (to == Unit.HECTARE) {
                    constant = 0.0001;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 1e-6;
                } else if (to == Unit.SQ_METER) {
                    //Same Unit
                    constant = 1;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 10000;
                } else if (to == Unit.ACRE) {
                    constant = 0.000247105;
                } else if (to == Unit.SQ_MILE) {
                    constant = 3.86102e-7;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 10.7639;
                } else if (to == Unit.SQ_INCH) {
                    constant = 1550;
                }
                break;

            case SQ_CENTIMETER:
                if (to == Unit.HECTARE) {
                    constant = 1e-8;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 1e-10;
                } else if (to == Unit.SQ_METER) {
                    constant = 0.0001;
                } else if (to == Unit.SQ_CENTIMETER) {
                    //Same Unit
                    constant = 1;
                } else if (to == Unit.ACRE) {
                    constant = 2.47105e-8;
                } else if (to == Unit.SQ_MILE) {
                    constant = 3.86102e-11;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 0.00107639;
                } else if (to == Unit.SQ_INCH) {
                    constant = 0.155;
                }
                break;
            case ACRE:
                if (to == Unit.HECTARE) {
                    constant = 0.404686;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 0.00404686;
                } else if (to == Unit.SQ_METER) {
                    constant = 4046.86;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 4.047e+7;
                } else if (to == Unit.ACRE) {
                    //Same Unit
                    constant = 1;
                } else if (to == Unit.SQ_MILE) {
                    constant = 0.0015625;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 43560;
                } else if (to == Unit.SQ_INCH) {
                    constant = 6.273e+6;
                }
                break;
            case SQ_MILE:
                if (to == Unit.HECTARE) {
                    constant = 258.999;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 2.58999;
                } else if (to == Unit.SQ_METER) {
                    constant = 2.59e+6;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 2.59e+10;
                } else if (to == Unit.ACRE) {
                    constant = 640;
                } else if (to == Unit.SQ_MILE) {
                    //Same Unit
                    constant = 1;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 2.788e+7;
                } else if (to == Unit.SQ_INCH) {
                    constant = 4.014e+9;
                }
                break;

            case SQ_FOOT:
                if (to == Unit.HECTARE) {
                    constant = 9.2903e-6;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 9.2903e-8;
                } else if (to == Unit.SQ_METER) {
                    constant = 0.092903;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 929.03;
                } else if (to == Unit.ACRE) {
                    constant = 2.29568e-5;
                } else if (to == Unit.SQ_MILE) {
                    constant = 3.58701e-8;
                } else if (to == Unit.SQ_FOOT) {
                    //Same Unit
                    constant = 1;
                } else if (to == Unit.SQ_INCH) {
                    constant = 144;
                }
                break;
            case SQ_INCH:
                if (to == Unit.HECTARE) {
                    constant = 6.4516e-8;
                } else if (to == Unit.SQ_KILOMETER) {
                    constant = 6.4516e-10;
                } else if (to == Unit.SQ_METER) {
                    constant = 0.00064516;
                } else if (to == Unit.SQ_CENTIMETER) {
                    constant = 6.4516;
                } else if (to == Unit.ACRE) {
                    constant = 1.59423e-7;
                } else if (to == Unit.SQ_MILE) {
                    constant = 2.49098e-10;
                } else if (to == Unit.SQ_FOOT) {
                    constant = 0.00694444;
                } else if (to == Unit.SQ_INCH) {
                    //Same Unit
                    constant = 1;
                }
                break;


            //CURRENCY ***Has own Util***

            //DISTANCE CASE STATEMENTS
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 25.4;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 10;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 304.8;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 914.4;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 1000;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 1.609e+6;
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
                } else if (to == Unit.MILLIMETER) {
                    constant = 1000000;
                }
                break;
            case MILLIMETER: //Some bugs with converting Millimeter.. Oh each other case UNIT now needs a MILLIMETER
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
                } else if (to == Unit.KILOMETER) {
                    constant = 1e-6;
                } else if (to == Unit.MILLIMETER) {
                    constant = 1;
                }
                break;

            //TEMPERATURE CASE STATEMENTS
//          Temperature follows a formula not ratio.. Derp. Needs its own util sorta thing.
//           case FAHRENHEIT:
//                if(to == Unit.CELSIUS) {
//                    constant = 1;
//                }

            //TIME CASE STATEMENTS
            case DECADE:
                if (to == Unit.YEAR) {
                    constant = 10;
                }
                if (to == Unit.MONTH) {

                }
                if (to == Unit.WEEK) {

                }
                if (to == Unit.DAY) {

                }
                if (to == Unit.HOUR) {

                }
                if (to == Unit.MINUTE) {

                }
                if (to == Unit.SECOND) {

                }
                break;
            case YEAR:
                if (to == Unit.DECADE) {
                    constant = 0.10;
                }
                break;

            //VOLUME CASE STATEMENTS

            //WEIGHT CASE STATEMENTS
        }

        multiplier = constant;
    }

    // Convert the unit!
    public double convert(double input) {
        return input * multiplier;
    }


}
