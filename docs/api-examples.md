# Meal Planner – API Examples


## Ingredients API

### 🔹 Get all ingredients
**Request**

```
GET /api/ingredients
```

**Example**

```
GET http://localhost:8080/api/ingredients
```

### 🔹 Get ingredient by ID
**Request**

```
GET /api/ingredients/{id}
```

**Example**

```
GET http://localhost:8080/api/ingredients/1
```

### 🔹 Update Ingredient
**Request**

```
PUT /api/ingredients/{id}
```

**Example**

```
PUT http://localhost:8080/api/ingredients/1
```

**Body**
```json
{
  "name": "Vidutinis kiaušinis",
  "kcalPer100": 155,
  "proteinPer100": 13,
  "fatPer100": 11,
  "carbsPer100": 1.5
}
```

### 🔹 Delete ingredient
**Request**

```
DELETE /api/ingredients/{id}
```

**Example**

```
DELETE http://localhost:8080/api/ingredients/1
```