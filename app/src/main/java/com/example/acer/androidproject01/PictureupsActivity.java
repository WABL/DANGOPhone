package com.example.acer.androidproject01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        List<String> steps = Arrays.asList(new String[]{"注册账号", "上传照片", "注册完成"});
        mStepView.setSteps(steps);
        int nextStep = mStepView.getCurrentStep() + 1;
        mStepView.selectedStep(nextStep);
    }

    public void gototp (View v)
    {

        Intent intent = new Intent();
        intent.setClass(PictureupsActivity.this, TakephotoActivity.class);

         /* 通过Bundle对象存储需要传递的数据 */
        Bundle bundle = new Bundle();
/*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
        // bundle.putString("Name", "feng88724");
        bundle.putBoolean("needface", true);

/*把bundle对象assign给Intent*/
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
