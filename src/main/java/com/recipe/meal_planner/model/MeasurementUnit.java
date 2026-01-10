package com.recipe.meal_planner.model;


//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "measurement_unit")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public enum MeasurementUnit {
//    GRAM(1.0, UnitType.MASS),           // gramai
    GRAM(UnitType.MASS),           // gramai
//    KILOGRAM(1000.0, UnitType.MASS),    // kilogramai
    KILOGRAM(UnitType.MASS),    // kilogramai

//    ML(1.0, UnitType.VOLUME),           // mililitrai
    ML(UnitType.VOLUME),           // mililitrai
//    LITER(1000.0, UnitType.VOLUME),     // litrai
    LITER(UnitType.VOLUME),     // litrai
//    CUP(240.0, UnitType.VOLUME),        // puodelis
    CUP(UnitType.VOLUME),        // puodelis
//    TSP(5.0, UnitType.VOLUME),          // arbatinis šaukštelis
    TSP(UnitType.VOLUME),          // arbatinis šaukštelis
//    TBSP(15.0, UnitType.VOLUME),        // šaukštas (3 arbatiniai šaukšteliai)
    TBSP(UnitType.VOLUME),        // šaukštas (3 arbatiniai šaukšteliai)
//    TODO: maybe need fix

    PIECE(UnitType.PIECE);           // gabalas
//    PIECE(50.0, UnitType.PIECE); // e.g., one slice of ham = 50g

//    private final double toBaseUnit;
    private final UnitType type;

//    MeasurementUnit(double toBaseUnit, UnitType type) {
    MeasurementUnit(UnitType type) {
//        this.toBaseUnit = toBaseUnit;
        this.type = type;
    }

//    public double getToBaseUnit() { return toBaseUnit; }
    public UnitType getType() { return type; }


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(unique = true, nullable = false)
//    private UnitName name; // CUP, TSP, GRAM, ML, PIECE

//    // Calculate kcal given ingredient per100g/ml
//    public double calculateKcal(double ingredientKcalPer100, double quantity) {
//        double baseQuantity = quantity;
//        if (type == UnitType.MASS || type == UnitType.VOLUME) {
//            baseQuantity = quantity * toBaseUnit; // convert cups/tsp to grams/ml
//        }
//        return ingredientKcalPer100 * baseQuantity / 100;
//    }
}

//public enum MeasurementUnit {
//    GRAM(UnitType.MASS, 1),
//    KILOGRAM(UnitType.MASS, 1000),
//
//    ML(UnitType.VOLUME, 1),
//    LITER(UnitType.VOLUME, 1000),
//    CUP(UnitType.VOLUME, 240),   // cooking cup ≈ 240 ml
//
//    PIECE(UnitType.PIECE, 1);
//
//    private final UnitType type;
//    private final double toBaseFactor;
//
//    MeasurementUnit(UnitType type, double toBaseFactor) {
//        this.type = type;
//        this.toBaseFactor = toBaseFactor;
//    }
//
//    public UnitType getType() {
//        return type;
//    }
//
//    public double getToBaseFactor() {
//        return toBaseFactor;
//    }
//}
