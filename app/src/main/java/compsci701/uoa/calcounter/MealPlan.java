package compsci701.uoa.calcounter;

import java.io.Serializable;

/**
 * Created by natha on 27/04/2018.
 */

public class MealPlan implements Serializable {

    private String _name;
    private Meal _breakfast;
    private Meal _lunch;
    private Meal _dinner;

    private Double _calories;

    public MealPlan(String name, Meal breakfast, Meal lunch, Meal dinner) {
        _name = name;
        _breakfast = breakfast;
        _lunch = lunch;
        _dinner = dinner;

        _calories = _breakfast.getCalories() + _lunch.getCalories() + _dinner.getCalories();
    }

    public Meal getBreakfast() {
        return _breakfast;
    }

    public Meal getLunch() {
        return _lunch;
    }

    public Meal getDinner() {
        return _dinner;
    }

    public Double getTotalCalories() {
        return _calories;
    }
}
