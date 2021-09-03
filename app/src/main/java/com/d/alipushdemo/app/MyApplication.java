package com.d.alipushdemo.app;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.impl.HuaweiMsgParseImpl;
import com.alibaba.sdk.android.push.noonesdk.PushInitConfig;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.d.alipushdemo.activity.MainActivity;

/**
 * Created by HJD on 2021/9/2 and 9:40.
 */
public class MyApplication extends Application {
    private static final String TAG = "Init";

    @Override
    public void onCreate() {
        super.onCreate();
        PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW).request();
        initCloudChannel(this);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {

        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("初始化完成" + response);
                LogUtils.i(pushService.getDeviceId());

            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtils.e("初始化失败 -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);

            }
        });
        // 创建notificaiton channel
        this.createNotificationChannel();

        ThirdPushManager.reportToken(this, ThirdPushReportKeyword.HUAWEI.thirdTokenKeyword, pushService.getDeviceId());
        ThirdPushManager.registerImpl(new HuaweiMsgParseImpl());

        // 初始化小米辅助推送
        MiPushRegister.register(applicationContext, "XIAOMI_ID", "XIAOMI_KEY");
        // 接入华为辅助推送
        HuaWeiRegister.register(this);
        // vivo通道注册
        VivoRegister.register(applicationContext);
        // OPPO通道注册
        OppoRegister.register(this, "72c34a14d37f4974b3f69e39f113e1f4", "b725d87de7a341eba05288aef0687fbc");

        // 接入FCM/GCM初始化推送
        GcmRegister.register(applicationContext, "send_id", "application_id");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "Notification Channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            //是否显示桌面徽标
            mChannel.setShowBadge(true);
            //设置在锁屏上显示
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public static enum ThirdPushReportKeyword {
        HUAWEI("HW_TOKEN", "huawei"),
        XIAOMI("MI_TOKEN", "xiaomi"),
        OPPO("OPPO_TOKEN", "oppo"),
        VIVO("VIVO_TOKEN", "vivo"),
        MEIZU("MZ_TOKEN", "meizu"),
        FCM("gcm", "gcm");

        public String thirdTokenKeyword;//厂商的设备ID标识
        public String thirdMsgKeyword;//厂商的消息标识

        private ThirdPushReportKeyword(String thirdTokenKeyword, String thirdMsgKeyword) {
            this.thirdTokenKeyword = thirdTokenKeyword;
            this.thirdMsgKeyword = thirdMsgKeyword;
        }
    }

}
