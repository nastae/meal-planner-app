package com.recipe.meal_planner.loader;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
@AllArgsConstructor
public class CsvDataLoader implements CommandLineRunner {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        loadIngredients();
        updateSequences();
    }

    private void loadIngredients() throws Exception {
        InputStream inputStream = new ClassPathResource("data/ingredients.csv").getInputStream();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            reader.skip(1);
            List<String[]> rows = reader.readAll();
            for (String[] row: rows) {
                Long id = Long.parseLong(row[0]);
                String name = row[1];
                double kcalPer100 = Double.parseDouble(row[2]);
                double proteinPer100 = Double.parseDouble(row[3]);
                double fatPer100 = Double.parseDouble(row[4]);
                double carbsPer100 = Double.parseDouble(row[5]);
                System.out.println(name);

                jdbcTemplate.update(
                        "INSERT INTO ingredient (id, name, kcal_per100, protein_per100, fat_per100, carbs_per100) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET name=EXCLUDED.name, kcal_per100=EXCLUDED.kcal_per100, protein_per100=EXCLUDED.protein_per100, fat_per100=EXCLUDED.fat_per100, carbs_per100=EXCLUDED.carbs_per100",
                        id, name, kcalPer100, proteinPer100, fatPer100, carbsPer100
                );
            }
        }
    }

    private void updateSequences() {
        jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('ingredient', 'id'), (SELECT MAX(id) FROM ingredient));");

    }
}
