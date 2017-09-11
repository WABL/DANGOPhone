package com.example.acer.androidproject01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.githang.stepview.StepView;

import java.util.Arrays;
import java.util.List;

public class FinishSignup extends AppCompatActivity {
    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_signup);

        mStepView = (StepView) findViewById(R.id.step_view);
        List<String> steps = Arrays.asList(new String[]{"注册账号", "上传照片", "注册完成"});
        mStepView.setSteps(steps);
        int nextStep = mStepView.getCurrentStep() + 2;
        mStepView.selectedStep(nextStep);
    }

    public void gotomain (View v)
    {

        Intent intent = new Intent();
        intent.setClass(FinishSignup.this, MainActivity.class);
        startActivity(intent);
    }
}
