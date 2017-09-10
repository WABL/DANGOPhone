package com.example.acer.androidproject01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.githang.stepview.StepView;

import java.util.Arrays;
import java.util.List;

public class PictureupsActivity extends AppCompatActivity {
    private ImageView img;
    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictureups);

        mStepView = (StepView) findViewById(R.id.step_view);
        List<String> steps = Arrays.asList(new String[]{"注册账号", "上传照片", "登录跳转"});
        mStepView.setSteps(steps);
        int nextStep = mStepView.getCurrentStep() + 1;
        mStepView.selectedStep(nextStep);
    }
}
