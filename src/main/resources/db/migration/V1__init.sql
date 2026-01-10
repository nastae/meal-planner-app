--------------------------------------------------------
-- ENUMS
--------------------------------------------------------

CREATE TYPE meal_type AS ENUM (
    'BREAKFAST',
    'LUNCH',
    'DINNER',
    'LATE_NIGHT_SNACK'
);

CREATE TYPE measurement_unit AS ENUM (
    'GRAM',
    'KILOGRAM',
    'ML',
    'LITER',
    'CUP',
    'TSP'
    'TBSP',
    'PIECE'
);

--------------------------------------------------------
-- RECIPE CLASS (canonical dishes)
--------------------------------------------------------

CREATE TABLE recipe_class (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    instructions TEXT
);

--------------------------------------------------------
-- INGREDIENT
--------------------------------------------------------

CREATE TABLE ingredient (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,

    kcal_per_100 DOUBLE PRECISION NOT NULL,
    protein_per_100 DOUBLE PRECISION NOT NULL,
    fat_per_100 DOUBLE PRECISION NOT NULL,
    carbs_per_100 DOUBLE PRECISION NOT NULL
);

--------------------------------------------------------
-- RECIPE (meal-sized portions)
--------------------------------------------------------

CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    recipe_class_id BIGINT NOT NULL,
    meal_type meal_type NOT NULL,

    CONSTRAINT fk_recipe_class
        FOREIGN KEY (recipe_class_id)
        REFERENCES recipe_class(id)
        ON DELETE CASCADE
);

--------------------------------------------------------
-- COMPOSITE RECIPES (Recipe → Recipe)
--------------------------------------------------------

CREATE TABLE composite_recipe (
    parent_recipe_id BIGINT NOT NULL,
    child_recipe_id BIGINT NOT NULL,

    PRIMARY KEY (parent_recipe_id, child_recipe_id),

    CONSTRAINT fk_parent_recipe
        FOREIGN KEY (parent_recipe_id)
        REFERENCES recipe(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_child_recipe
        FOREIGN KEY (child_recipe_id)
        REFERENCES recipe(id)
        ON DELETE CASCADE,

    CONSTRAINT no_self_reference
        CHECK (parent_recipe_id <> child_recipe_id)
);

--------------------------------------------------------
-- RECIPE INGREDIENT
--------------------------------------------------------

CREATE TABLE recipe_ingredient (
    id BIGSERIAL PRIMARY KEY,

    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,

    unit measurement_unit NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,

    -- Only used when unit = PIECE
    piece_weight DOUBLE PRECISION,

    CONSTRAINT fk_recipe
        FOREIGN KEY (recipe_id)
        REFERENCES recipe(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ingredient
        FOREIGN KEY (ingredient_id)
        REFERENCES ingredient(id)
--        Or set null, see use case
--        ON DELETE SET NULL
        ON DELETE RESTRICT,

    CONSTRAINT piece_weight_required
        CHECK (
            (unit <> 'PIECE')
            OR (piece_weight IS NOT NULL AND piece_weight > 0)
        )
);

--------------------------------------------------------
-- INDEXES (important for performance)
--------------------------------------------------------

CREATE INDEX idx_recipe_class ON recipe(recipe_class_id);
CREATE INDEX idx_recipe_meal_type ON recipe(meal_type);
CREATE INDEX idx_recipeingredient_recipe ON recipe_ingredient(recipe_id);
CREATE INDEX idx_recipeingredient_ingredient ON recipe_ingredient(ingredient_id);
CREATE INDEX idx_composite_parent ON composite_recipe(parent_recipe_id);
CREATE INDEX idx_composite_child ON composite_recipe(child_recipe_id);