package compsci701.uoa.calcounter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Meal implements Serializable {

    public enum MealType {
        Breakfast, Lunch, Dinner
    }

    private String _name;
    private MealType _mealType;
    private Double _calories;
    private String[] _ingredients;

    public Meal(MealType mealType, JSONObject json) {
        _mealType = mealType;
        try {
            _name = json.getString("Name");
            _calories = json.getDouble("Calories");

            JSONArray jsonArray = json.getJSONArray("Ingredients");
            _ingredients = new String[jsonArray.length()];
            for (int j = 0; j < jsonArray.length(); j++) {
                _ingredients[j] = (String) jsonArray.get(j);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return _name;
    }

    public Double getCalories() {
        return _calories;
    }

    public String[] getIngredients() {
        return _ingredients;
    }

    public MealType getMealType() {
        return _mealType;
    }
}
