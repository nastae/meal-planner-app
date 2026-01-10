package com.recipe.meal_planner.util;

import com.recipe.meal_planner.model.MeasurementUnit;

public class UnitConverter {

    /**
     * Returns the conversion factor to the base unit (grams for MASS, milliliters for VOLUME)
     */
    public static double getUnitToBaseFactor(MeasurementUnit unit) {
        switch (unit) {
            case GRAM: return 1.0;          // base unit for MASS
            case KILOGRAM: return 1000.0;   // 1 kg = 1000 g
            case ML: return 1.0;            // base unit for VOLUME
            case LITER: return 1000.0;      // 1 liter = 1000 ml
            case CUP: return 240.0;         // 1 cup = 240 ml
            case TSP: return 5.0;           // 1 teaspoon = 5 ml
            case TBSP: return 15.0;         // 1 tablespoon = 15 ml
            default:
                throw new IllegalArgumentException("No conversion factor defined for unit: " + unit);
        }
    }
}
