package com.example.adwork;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
 * 我的事务 Activity
 * 功能：展示当前用户所有外出住宿申请记录列表，按提交时间倒序排列
 *       点击某条记录跳转到 TransactionDetailActivity 查看详情
 * 数据库表：application + application_log（联表查询申请记录）
 * 入口：GeRenFragment → 我的事务
 */
public class MyShiWuActivity extends AppCompatActivity {

    private DBhelper dBhelper;
    private String cardId;
    private ListView listView;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_shiwu);

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
        tvCount = findViewById(R.id.tv_count);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Cursor cursor = dBhelper.getApplicationsByUser(cardId);

        int count = cursor.getCount();
        tvCount.setText("共 " + count + " 条申请记录");

        String[] from = {"app_id", "reason", "start_date", "end_date", "submit_time", "status"};
        int[] to = {R.id.tv_app_id, R.id.tv_reason, R.id.tv_date_range, R.id.tv_submit_time, R.id.tv_submit_time, R.id.tv_status};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_shenqin, cursor, from, to, 0) {
            @Override
            public void bindView(View view, android.content.Context context, Cursor cursor) {
                int appId = cursor.getInt(cursor.getColumnIndexOrThrow("app_id"));
                String reason = cursor.getString(cursor.getColumnIndexOrThrow("reason"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                String submitTime = cursor.getString(cursor.getColumnIndexOrThrow("submit_time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

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
