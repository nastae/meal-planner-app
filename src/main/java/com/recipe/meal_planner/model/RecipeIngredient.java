package com.recipe.meal_planner.model;

import com.recipe.meal_planner.util.UnitConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe_ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

//    @ManyToOne
//    @JoinColumn(name = "unit_id")
//    Option 1: Keep PIECE out of enum, mark isPiece in entity → cleaner separation
//    Option 2: Keep PIECE in enum for readability → must store weight dynamically per ingredient
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeasurementUnit unit;

    @Column(nullable = false)
    private Double quantity; // 50 g, 200 ml, 1 piece

    // Only used when unit = PIECE
    // Example: bread slice = 30g, egg = 60g, ham slice = 20g
    private Double pieceWeight; // only used if unit is PIECE

//    public double getQuantityInGrams() {
//        if (unit == null) throw new IllegalStateException("Unit not set");
//        if (unit.getType() == UnitType.PIECE) {
//            if (pieceWeight == null) throw new IllegalStateException("pieceWeight not set");
//            return quantity * pieceWeight;
//        }
//        // for MASS or VOLUME, you can convert to base unit here
//        return quantity * unit.getToBaseUnit();// getUnitToBaseFactor(unit)
//    }
    public double getQuantityInGrams() {
        if (unit == MeasurementUnit.PIECE) {
            return quantity * pieceWeight; // dynamic per ingredient
        }
        return quantity * UnitConverter.getUnitToBaseFactor(unit);
    }

    // helper method to calculate kcal
//    public double getKcal() {
//        return unit.calculateKcal(ingredient.getKcalPer100(), quantity);
//    }

//    public double getProtein() {
//        double baseQuantity = quantity;
//        if (unit.getType() == MeasurementUnit.UnitType.MASS || unit.getType() == MeasurementUnit.UnitType.VOLUME) {
//            baseQuantity = quantity * unit.getToBaseUnit();
//        }
//        return ingredient.getProteinPer100() * baseQuantity / 100;
//    }
}
