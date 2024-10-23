package com.gmyboy.codelab.accesibility;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmyboy.codelab.accesibility.util.AccessibilityUtil;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FLOAT_WINDOW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        if (!Settings.canDrawOverlays(this)) {
            // 请求用户授予权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_FLOAT_WINDOW);
        }*/

        Intent intent = new Intent(this, AutoClickAccessibilityService.class);
        startService(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FLOAT_WINDOW:
                    if (AccessibilityUtil.isAccessibilitySettingsOn(this)) {
                        Intent intent = new Intent(this, AutoClickAccessibilityService.class);
                        startService(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "请打开无障碍功能", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}