import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IngredientsComponent } from "./ingredients-component/ingredients-component";

@Component({
  selector: 'app-root',
  // RouterOutlet
  imports: [IngredientsComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('meal-planner-app-ui');
}
