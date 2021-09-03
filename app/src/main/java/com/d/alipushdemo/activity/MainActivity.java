package com.d.alipushdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;


import com.alibaba.sdk.android.push.popup.PopupNotifyClick;
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d.alipushdemo.R;
import com.huawei.hms.aaid.HmsInstanceId;

import java.util.Map;

/**
 * Created by HJD on 2021/9/2 and 9:22.
 */
public class MainActivity extends AppCompatActivity {
    TextView tv_title, content, ext;
    public static String TITLE = "";
    public static String CONTENT = "";
    public static String EXT = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = findViewById(R.id.tv_title);
        content = findViewById(R.id.tv_content);
        ext = findViewById(R.id.ext);

        tv_title.setText("推送标题：" + TITLE);
        content.setText("推送的内容：" + CONTENT);

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            goToSetting();
        }

//        new PopupNotifyClick(new PopupNotifyClickListener() {
//            @Override
//            public void onSysNoticeOpened(String s, String s1, Map<String, String> map) {
//                LogUtils.d("OnMiPushSysNoticeOpened, title: " + s + ", content: " + s1 + ", extMap: " + map);
//                ToastUtils.showShort(s + s1);
//                TITLE = s;
//                CONTENT = s1;
//            }
//        }).onCreate(this, getIntent());
    }

    private void goToSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {// android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {//其它
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
