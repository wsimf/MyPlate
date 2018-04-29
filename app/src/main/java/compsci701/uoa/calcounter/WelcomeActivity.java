package compsci701.uoa.calcounter;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import compsci701.uoa.calcounter.model.User;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(this).contains(User.getStoreName())) {
            goToHome();
            return;
        }

        setContentView(R.layout.activity_welcome);

        final EditText _nameText = findViewById(R.id.name_txt);
        final EditText _ageText = findViewById(R.id.age_txt);
        final Spinner _genderSpinner = findViewById(R.id.gender_spn);
        final EditText _heightText = findViewById(R.id.height_txt);
        final EditText _weightText = findViewById(R.id.weight_txt);
        final Spinner _activeSpinner = findViewById(R.id.activity_level_spn);
        final Button _createUser = findViewById(R.id.save_btn);

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

                final User user = new User(name, age, gender, height, weight, activityFactor);
                user.save(WelcomeActivity.this);

                goToHome();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
