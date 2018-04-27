package compsci701.uoa.calcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.awt.Label;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    private Label _bmiLabel;
    private Label _rciLabel;
    private Button _breakfastLabel;  // check the actual type first
    private Button _lunchLabel;      // check the actual type first
    private Button _dinnerLabel;     // check the actual type first

    private User _user;
    private ArrayList<MealPlan> _mealPlans;
    private Meal _breakfast;
    private Meal _lunch;
    private Meal _dinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        _bmiLabel = findViewById(R.id.bmi_label);
        _rciLabel = findViewById(R.id.rci_label);

        _breakfastLabel = findViewById(R.id.breakfast_label);
        _lunchLabel = findViewById(R.id.lunch_label);
        _dinnerLabel = findViewById(R.id.dinner_label);

        _user = (User) getIntent().getSerializableExtra("USER");
        _mealPlans = (ArrayList<MealPlan>) getIntent().getSerializableExtra("MEALS");

        _bmiLabel.setText(_user.getBMI()+"");
        _rciLabel.setText(_user.getDCN()+"");

        Double mealCalories = _user.getDCN() / 3;

        int[] mealCalorieRanges = {400, 466, 533, 600, 666, 733, 800, 866, 933, 1000};

        for (int i = 0; i < mealCalorieRanges.length; i++) {
            if (mealCalories < mealCalorieRanges[i]) {
                _breakfast = _mealPlans.get(i).getBreakfast();
                _lunch = _mealPlans.get(i).getLunch();
                _dinner = _mealPlans.get(i).getDinner();
                break;
            }
        }

        _breakfastLabel.setText(_breakfast.getName());
        _lunchLabel.setText(_lunch.getName());
        _dinnerLabel.setText(_dinner.getName());

        _breakfastLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // change to activity for individual meal
                intent.putExtra("USER", _user);
                intent.putExtra("MEAL", _breakfast);
                startActivity(intent);
            }
        });

        _lunchLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // change to activity for individual meal
                intent.putExtra("USER", _user);
                intent.putExtra("MEAL", _lunch);
                startActivity(intent);
            }
        });

        _dinnerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // change to activity for individual meal
                intent.putExtra("USER", _user);
                intent.putExtra("MEAL", _dinner);
                startActivity(intent);
            }
        });

    }

}