package compsci701.uoa.calcounter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {


    private EditText _nameText;
    private EditText _ageText;
    private Spinner _genderSpinner;
    private EditText _heightText;
    private EditText _weightText;
    private Spinner _activeSpinner;
    private Button _createUser;

    private User _currentUser;
    private ArrayList<MealPlan> _mealPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        _nameText = findViewById(R.id.name_text);
        _ageText = findViewById(R.id.age_text);
        _genderSpinner = findViewById(R.id.gender_spinner);
        _heightText = findViewById(R.id.height_text);
        _weightText = findViewById(R.id.weight_text);
        _activeSpinner = findViewById(R.id.active_spinner);
        _createUser = findViewById(R.id.create_user_button);


        User.Gender[] genders = User.Gender.values();
        User.ActivityFactor[] activityFactors = User.ActivityFactor.values();

        ArrayList<String> genderNames = new ArrayList<String>();

        for (User.Gender i : genders) {
            genderNames.add(i.toString());
        }

        ArrayList<String> activityFactorNames = new ArrayList<String>();

        for (User.ActivityFactor i : activityFactors) {
            activityFactorNames.add(i.toString());
        }

        // the spinners are populated

        ArrayAdapter<String> genderSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderNames);
        _genderSpinner.setAdapter(genderSpinnerArrayAdapter);

        ArrayAdapter<String> activityFactorSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, activityFactorNames);
        _activeSpinner.setAdapter(activityFactorSpinnerArrayAdapter);

        _createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = _nameText.getText().toString();
                int age = Integer.parseInt(_ageText. getText().toString());
                User.Gender gender = User.Gender.valueOf(_genderSpinner.getSelectedItem().toString());
                double height = Double.parseDouble(_heightText.getText().toString());
                double weight = Double.parseDouble(_weightText.getText().toString());
                User.ActivityFactor activityFactor = User.ActivityFactor.valueOf(_activeSpinner.getSelectedItem().toString());

                _currentUser = new User(name, age, gender, height, weight, activityFactor);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("USER", _currentUser);
                intent.putExtra("MEALS", _mealPlans);
                startActivity(intent);
            }
        });

        //  this section converts a string into a series of json objects, which are then converted into
        //  Meal objects and MealPlan objects.

        _mealPlans = new ArrayList<MealPlan>();

        try {
            JSONObject meals = new JSONObject(loadJSONFromAsset(this));
            JSONArray mealsArray = meals.getJSONArray("meals");

            for (int i = 0; i < mealsArray.length; i++) {
                JSONObject m = mealsArray.getJSONObject(i);
                String name = m.getString("name");

                JSONObject breakfast = m.getJSONObject("Breakfast");
                String breakfastName = breakfast.getString("Name");
                Double breakfastCalories = breakfast.getDouble("Calories");
                JSONArray breakfastIngredientsArray = breakfast.getJSONArray("Ingredients");
                String[] breakfastIngredients = new String[breakfastIngredientsArray.length];
                for (int j = 0; j < breakfastIngredientsArray.length; j++) {
                    breakfastIngredients[i] = (String) breakfastIngredientsArray.get(i);
                }

                Meal breakfastMeal = new Meal(breakfastName, Meal.MealType.Breakfast, breakfastCalories, breakfastIngredients);

                JSONObject lunch = m.getJSONObject("Lunch");
                String lunchName = lunch.getString("Name");
                Double lunchCalories = lunch.getDouble("Calories");
                JSONArray lunchIngredientsArray = lunch.getJSONArray("Ingredients");
                String[] lunchIngredients = new String[lunchIngredientsArray.length];
                for (int j = 0; j < lunchIngredientsArray.length; j++) {
                    lunchIngredients[i] = (String) lunchIngredientsArray.get(i);
                }

                Meal lunchMeal = new Meal(lunchName, Meal.MealType.Lunch, LunchCalories, LunchIngredients);

                JSONObject dinner = m.getJSONObject("Dinner");
                String dinnerName = dinner.getString("Name");
                Double dinnerCalories = dinner.getDouble("Calories");
                JSONArray dinnerIngredientsArray = dinner.getJSONArray("Ingredients");
                String[] dinnerIngredients = new String[dinnerIngredientsArray.length];
                for (int j = 0; j < dinnerIngredientsArray.length; j++) {
                    dinnerIngredients[i] = (String) dinnerIngredientsArray.get(i);
                }

                Meal dinnerMeal = new Meal(dinnerName, Meal.MealType.Dinner, dinnerCalories, dinnerIngredients);

                mealPlans.add(new MealPlan(name, breakfastMeal, lunchMeal, dinnerMeal));
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

    }

    /*
        This method reads from the meals.json file and converts it into a single string.

            - as of now, it assumes that the json being read is in plain text, and isn't encoded.

     */

    private String loadJSONFromAsset(Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open("meals.json");

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
