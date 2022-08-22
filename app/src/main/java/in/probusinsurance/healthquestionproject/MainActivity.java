package in.probusinsurance.healthquestionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.HealthQuestionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fun_startactivity(View view) {
        startActivity(new Intent(this, HealthQuestionActivity.class));
    }
}