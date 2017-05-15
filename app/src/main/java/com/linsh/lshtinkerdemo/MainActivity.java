package com.linsh.lshtinkerdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        tv1.setText("当前版本: " + BuildConfig.VERSION_NAME);
        tv2.setText("原始版本: " + "1.1.0");

        tv3.setText("插件版本: " + "1.1.1");
        tv4.setText("描述: " + "补丁for1.1.0");
    }

    public void update(View view) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            Toast.makeText(this, "正在升级...", Toast.LENGTH_SHORT).show();
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        } else {
            Toast.makeText(this, "没有可以升级的补丁", Toast.LENGTH_SHORT).show();
        }
    }
}
