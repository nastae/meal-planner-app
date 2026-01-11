INSERT INTO ingredient (id, name, kcal_per100, protein_per100, fat_per100, carbs_per100)
VALUES
(1, 'Kiaušinis', 155, 13, 11, 1.5),
(2, 'Palangos šviesi duona', 224, 5.3, 1.9, 44.3),
(3, 'Loyner, virta Liono dešra (kiauliena)', 264, 13, 23, 1),
(4, 'Virtas kiaulienos kumpis', 135, 18.5, 4, 1.5),
(5, 'Biorina Garintos grikių kruopos (grikiai)', 344, 13, 3.1, 63);

INSERT INTO recipe_class (id, title, instructions)
VALUES
(1, 'Kumpio sumuštiniai', 'Ant duonos riekelės dedamas plonai pjaustytas virtas kumpis.'),
(2, 'Virti grikiai', 'Grikius virkite vandenyje 15 minučių.'),
(3, 'Virti kiaušiniai', 'Kiaušinius virkite vandenyje 6 minutes nuo užvirimo.');

INSERT INTO recipe (id, recipe_class_id, meal_type)
VALUES
(1, 1, 'BREAKFAST'),    -- Sandwich breakfast (sumuštinis pusryčiams)
--(2, 1, 'DINNER'),       -- Buckwheat dinner
--(3, 2, 'LUNCH');        -- Sandwich lunch
(2, 2, 'BREAKFAST');    -- Buckwheat breakfast (grikių pusryčiai)

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, quantity, piece_weight)
VALUES
-- Buckwheat dinner (100g)
--(2, 1, 'GRAM', 100, null),

-- Sandwich
(1, 2, 'GRAM', 60, null),   -- bread (duona)
(1, 4, 'GRAM', 40, null),   -- ham (kumpis)

-- Buckwheat breakfast (53g)
(2, 5, 'GRAM', 53, null);

INSERT INTO composite_recipe (parent_recipe_id, child_recipe_id)
VALUES
(1, 2); -- Sandwich includes buckwheat side (sumuštinis prideda grikių pusę)