package com.example.adwork;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;

/**
 * 审核状态全屏展示 Activity
 * 功能：白色全屏背景，正中央显示审核状态文字（审核中/审核完成/已退回）
 * 数据库表：application_log（查询状态）
 * 入口：ShenHeProgressActivity → 点击某条申请记录
 */
public class ZhuangTaiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status_full_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int appId = getIntent().getIntExtra("app_id", -1);
        TextView tvStatus = findViewById(R.id.tv_status);

        DBhelper dBhelper = new DBhelper(this);
        Cursor cursor = dBhelper.getApplicationDetail(appId);
        if (cursor.moveToFirst()) {
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            tvStatus.setText(status);
        }
        cursor.close();
    }
}
