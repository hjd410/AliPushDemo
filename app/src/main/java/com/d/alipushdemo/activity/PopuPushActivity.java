package com.d.alipushdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d.alipushdemo.R;

import java.util.Map;

/**
 * Created by HJD on 2021/9/2 and 11:44.
 * <p>
 * 99bbeaf07e8b4758be8a3bcc08f7b665
 * com.d.alipushdemo.activity.PopuPushActivity
 */
public class PopuPushActivity extends AndroidPopupActivity {

    TextView tv_title, content, ext;
    public static String TITLE = "";
    public static String CONTENT = "";
    public static String EXT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popu_push);
        tv_title = findViewById(R.id.tv_title);
        content = findViewById(R.id.tv_content);
        ext = findViewById(R.id.ext);

        tv_title.setText("推送标题：" + TITLE);
        content.setText("推送的内容" + CONTENT);
    }

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        LogUtils.d("OnMiPushSysNoticeOpened, title: " + title + ", content: " + summary + ", extMap: " + extMap);
        ToastUtils.showShort(title + "___" + summary);
        TITLE = title;
        CONTENT = summary;
//        ext.setText("推送的附参数：" + extMap);
    }
}
