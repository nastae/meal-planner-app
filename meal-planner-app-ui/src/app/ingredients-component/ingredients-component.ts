import { FormsModule } from '@angular/forms';
import { Ingredient } from '../models/ingredient';
import { IngredientService } from './../services/ingredient-service';
import { Component, inject, OnInit, signal } from '@angular/core';

@Component({
  selector: 'app-ingredients-component',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './ingredients-component.html',
  styleUrl: './ingredients-component.css',
})
export class IngredientsComponent implements OnInit {

  private ingredientService = inject(IngredientService);

  ingredients = signal<Ingredient[]>([]);
  errorMessage = signal('');

  newIngredient: Ingredient = {
    id: 0,
    name: '',
    kcalPer100: null,
    proteinPer100: null,
    fatPer100: null,
    carbsPer100: null
  };

  ngOnInit(): void {
    console.log("ngOnInit");
    this.loadIngredients();
  }

  loadIngredients() {
    this.ingredientService.getAll().subscribe(data => {
      this.ingredients.set(data);
    });
  }

  create() {
    this.errorMessage.set('');

    this.ingredientService.create(this.newIngredient).subscribe({
      next: () => {
        this.loadIngredients();

        this.newIngredient = {
          id: 0, 
          name: '',
          kcalPer100: null,
          proteinPer100: null,
          fatPer100: null,
          carbsPer100: null
        };
      },
      error: err => {
        if (err.status === 409) {
          // TODO: fix to display after pressing create instantly error message
          console.log(err);
          this.errorMessage.set(err.error.message);
        }
      }
    });
  }

  update(ingredient: Ingredient) {
    const updated = { ...ingredient, name: ingredient.name + ' updated' };

    this.ingredientService.update(ingredient.id, updated).subscribe(() => {
      this.loadIngredients();
    });
  }

  delete(id: number) {
    this.ingredientService.delete(id).subscribe(() => {
      this.loadIngredients();
    });
  }
}
