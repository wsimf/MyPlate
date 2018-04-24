package compsci701.uoa.calcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        _nameText = findViewById(R.id.name_txt);
        _ageText = findViewById(R.id.age_txt);
        _genderSpinner = findViewById(R.id.gender_spn);
        _heightText = findViewById(R.id.height_txt);
        _weightText = findViewById(R.id.weight_txt);
        _activeSpinner = findViewById(R.id.activity_level_spn);
        _createUser = findViewById(R.id.save_btn);

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
                startActivity(intent);
            }
        });
    }
}
