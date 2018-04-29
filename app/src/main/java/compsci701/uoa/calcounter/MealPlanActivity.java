package compsci701.uoa.calcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import compsci701.uoa.calcounter.model.Meal;

public class MealPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        final Meal meal = (Meal) getIntent().getSerializableExtra("meal");

        final TextView name = findViewById(R.id.name_lbl);
        name.setText(meal.getName());

        final TextView type = findViewById(R.id.type_heading_lbl);
        type.setText(meal.getMealType().toString());
        
        final TextView rc = findViewById(R.id.rc_amount_lbl);
        rc.setText(String.valueOf(meal.getCalories()));

        final TextView ingredients = findViewById(R.id.in_summery_lbl);
        final StringBuilder builder = new StringBuilder();
        for (String ingredient : meal.getIngredients()) {
            builder.append(ingredient).append("\n");
        }

        ingredients.setText(builder.toString());
    }

}
