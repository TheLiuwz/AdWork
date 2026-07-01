package com.example.adwork;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;

/**
 * 审核进度 Activity
 * 功能：展示用户所有申请记录列表，无记录时显示"当前无事务处理"
 *       点击某条记录跳转全屏页面展示审核状态（审核中/审核完成/已退回）
 * 数据库表：application + application_log
 * 入口：ShiWuFragment → 审核进度
 */
public class ShenHeProgressActivity extends AppCompatActivity {

    private DBhelper dBhelper;
    private String cardId;
    private ListView listView;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shenhe_progress);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dBhelper = new DBhelper(this);
        cardId = getIntent().getStringExtra("card_id");

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        listView = findViewById(R.id.lv_applications);
        tvEmpty = findViewById(R.id.tv_empty);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(this, ZhuangTaiActivity.class);
            intent.putExtra("app_id", (int) id);
            startActivity(intent);
        });

        loadData();
    }

    private void loadData() {
        Cursor cursor = dBhelper.getApplicationsByUser(cardId);

        if (cursor.getCount() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            cursor.close();
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_application, cursor,
                new String[]{"app_id", "reason", "start_date", "end_date", "submit_time", "status"},
                new int[]{R.id.tv_app_id, R.id.tv_reason, R.id.tv_date_range, R.id.tv_submit_time, R.id.tv_submit_time, R.id.tv_status},
                0) {
            @Override
            public void bindView(View view, android.content.Context context, Cursor cursor) {
                String reason = cursor.getString(cursor.getColumnIndexOrThrow("reason"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                String submitTime = cursor.getString(cursor.getColumnIndexOrThrow("submit_time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                int appId = cursor.getInt(cursor.getColumnIndexOrThrow("app_id"));

                ((TextView) view.findViewById(R.id.tv_app_id)).setText("外出住宿申请 #" + appId);
                ((TextView) view.findViewById(R.id.tv_reason)).setText(reason);
                ((TextView) view.findViewById(R.id.tv_date_range)).setText("时间: " + startDate + " 至 " + endDate);
                ((TextView) view.findViewById(R.id.tv_submit_time)).setText("提交于 " + submitTime);
                ((TextView) view.findViewById(R.id.tv_status)).setText(status);
            }
        };

        listView.setAdapter(adapter);
    }
}
