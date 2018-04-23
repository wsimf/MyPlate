package compsci701.uoa.calcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private EditText heightText;
    private TextView bmiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        heightText = (EditText) findViewById(R.id.height);
        bmiView = (TextView) findViewById(R.id.bmi);
    }

    private void calculate() {
        Float height = Float.valueOf(heightText.getText().toString());

        bmiView.setText("");
    }
}
