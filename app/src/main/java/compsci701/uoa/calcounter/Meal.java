package compsci701.uoa.calcounter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by natha on 27/04/2018.
 */

public class Meal implements Serializable {

    public enum MealType {
        Breakfast,
        Lunch,
        Dinner
    }

    private String _name;
    private MealType _mealType;
    private Double _calories;
    private String[] _ingredients;

    public Meal(String name, MealType mealType, Double calories, String[] ingredients) {
        _name = name;
        _mealType = mealType;
        _calories = calories;
        _ingredients = ingredients;
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
