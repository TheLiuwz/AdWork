package com.example.adwork;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 功能：展示用户所有申请记录列表，点击后在屏幕中央显示审核状态
 *       状态分为：审核中（橙色）、审核完成（绿色）、已退回（红色）
 *       无申请记录时显示"当前无事务处理"
 * 数据库表：application + application_log
 * 入口：ShiWuFragment → 审核进度
 */
public class ShenHeProgressActivity extends AppCompatActivity {

    private DBhelper dBhelper;
    private String cardId;
    private ListView listView;
    private LinearLayout llStatusDialog;
    private TextView tvDialogTitle, tvDialogStatus, tvDialogTime;

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
        TextView tvEmpty = findViewById(R.id.tv_empty);
        llStatusDialog = findViewById(R.id.ll_status_dialog);
        tvDialogTitle = findViewById(R.id.tv_dialog_title);
        tvDialogStatus = findViewById(R.id.tv_dialog_status);
        tvDialogTime = findViewById(R.id.tv_dialog_time);
        Button btnClose = findViewById(R.id.btn_dialog_close);

        // 关闭弹窗
        btnClose.setOnClickListener(v -> llStatusDialog.setVisibility(View.GONE));
        llStatusDialog.setOnClickListener(v -> llStatusDialog.setVisibility(View.GONE));

        // 点击事务条目，显示审核状态
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            showStatusDialog((int) id);
        });

        loadData(tvEmpty);
    }

    private void loadData(TextView tvEmpty) {
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

    private void showStatusDialog(int appId) {
        Cursor cursor = dBhelper.getApplicationDetail(appId);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String submitTime = cursor.getString(cursor.getColumnIndexOrThrow("submit_time"));

            tvDialogTitle.setText("外出住宿申请 #" + appId);
            tvDialogTime.setText("提交于 " + submitTime);

            // 根据状态设置颜色和文字
            switch (status) {
                case "审核完成":
                    tvDialogStatus.setText("✓ 审核完成");
                    tvDialogStatus.setTextColor(0xFF4CAF50); // 绿色
                    break;
                case "已退回":
                    tvDialogStatus.setText("✗ 已退回");
                    tvDialogStatus.setTextColor(0xFFE53935); // 红色
                    break;
                case "审核中":
                default:
                    tvDialogStatus.setText("● 审核中");
                    tvDialogStatus.setTextColor(0xFFFF9800); // 橙色
                    break;
            }

            llStatusDialog.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }
}
