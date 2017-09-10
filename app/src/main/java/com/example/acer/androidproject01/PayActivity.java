package com.example.acer.androidproject01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    public void pay (View v)
    {
        Intent intent = new Intent();
        intent.setClass(PayActivity.this, TakephotoActivity.class);

          /* 通过Bundle对象存储需要传递的数据 */
        Bundle bundle = new Bundle();
/*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
        // bundle.putString("Name", "feng88724");
        bundle.putBoolean("needface", false);

/*把bundle对象assign给Intent*/
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
