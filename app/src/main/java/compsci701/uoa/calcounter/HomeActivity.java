package compsci701.uoa.calcounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import compsci701.uoa.calcounter.model.Meal;
import compsci701.uoa.calcounter.model.MealPlan;
import compsci701.uoa.calcounter.model.User;
import compsci701.uoa.calcounter.security.DataDecoder;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<MealPlan> _mealPlans = new ArrayList<>(10);
    private MealPlan _selectedPlan = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final User _user = new DataDecoder().getUser(this);
        prepareMealPlans();

        final TextView _bmiLabel = findViewById(R.id.bmi_txt);
        final TextView _rciLabel = findViewById(R.id.rcc_txt);
        _bmiLabel.setText(String.valueOf(_user.getBMI()));
        _rciLabel.setText(String.valueOf(_user.getDCN()));

        final TextView _breakfastLabel = findViewById(R.id.breakfast_summery_lbl);
        final TextView _lunchLabel = findViewById(R.id.lunch_summery_lbl);
        final TextView _dinnerLabel = findViewById(R.id.dinner_summery_lbl);

        Double mealCalories = _user.getDCN() / 3;
        int[] mealCalorieRanges = {400, 466, 533, 600, 666, 733, 800, 866, 933, 1000};
        for (int i = 0; i < mealCalorieRanges.length; i++) {
            if (mealCalories < mealCalorieRanges[i]) {
                _selectedPlan = _mealPlans.get(i);
                break;
            }
        }

        if (_selectedPlan == null) {
            _selectedPlan = _mealPlans.get(_mealPlans.size() - 1);
        }

        _breakfastLabel.setText(_breakfastLabel.getText() + _selectedPlan.getBreakfast().getName());
        _lunchLabel.setText(_lunchLabel.getText() + _selectedPlan.getLunch().getName());
        _dinnerLabel.setText(_dinnerLabel.getText() + _selectedPlan.getDinner().getName());

        findViewById(R.id.breakfast_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMealView(_selectedPlan.getBreakfast());
            }
        });

        findViewById(R.id.lunch_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMealView(_selectedPlan.getLunch());
            }
        });

        findViewById(R.id.dinner_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMealView(_selectedPlan.getDinner());
            }
        });
    }

    private void prepareMealPlans() {
        try {
            JSONArray mealsArray = new DataDecoder().getMeals(this).getJSONArray("meals");
            for (int i = 0; i < mealsArray.length(); i++) {
                JSONObject m = mealsArray.getJSONObject(i);
                final Meal breakfast = new Meal(Meal.MealType.Breakfast, m.getJSONObject("Breakfast"));
                final Meal lunch = new Meal(Meal.MealType.Lunch, m.getJSONObject("Lunch"));
                final Meal dinner = new Meal(Meal.MealType.Dinner, m.getJSONObject("Dinner"));
                final String name = m.getString("name");
                _mealPlans.add(new MealPlan(name, breakfast, lunch, dinner));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goToMealView(final Meal meal) {
        Intent intent = new Intent(getApplicationContext(), MealPlanActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }
}