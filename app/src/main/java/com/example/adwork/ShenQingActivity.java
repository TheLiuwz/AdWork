package com.example.adwork;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;

/**
 * 申请详情 Activity
 * 功能：展示某次外出住宿申请的完整信息
 *       包括：用户基本信息、外出时间、申请理由、校外住宿地址、联系人、状态等
 * 数据库表：application + application_log（申请详情）、outside_address（住宿地址）
 * 入口：MyShiWuActivity → 点击某条申请记录
 */
public class ShenQingActivity extends AppCompatActivity {

    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dBhelper = new DBhelper(this);
        int appId = getIntent().getIntExtra("app_id", -1);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        Cursor cursor = dBhelper.getApplicationDetail(appId);
        if (cursor.moveToFirst()) {
            ((TextView) findViewById(R.id.tv_title))
                    .setText("外出住宿申请 #" + appId);
            ((TextView) findViewById(R.id.tv_status))
                    .setText(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            ((TextView) findViewById(R.id.tv_name))
                    .setText("姓名: " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ((TextView) findViewById(R.id.tv_card))
                    .setText("一卡通号: " + cursor.getString(cursor.getColumnIndexOrThrow("card_number")));
            ((TextView) findViewById(R.id.tv_class))
                    .setText("班级: " + cursor.getString(cursor.getColumnIndexOrThrow("class_name")));
            ((TextView) findViewById(R.id.tv_dorm))
                    .setText("当前住宿: " + cursor.getString(cursor.getColumnIndexOrThrow("dorm_info")));
            ((TextView) findViewById(R.id.tv_phone))
                    .setText("手机号码: " + cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            ((TextView) findViewById(R.id.tv_dates))
                    .setText("外出时间: " + cursor.getString(cursor.getColumnIndexOrThrow("start_date"))
                            + " 至 " + cursor.getString(cursor.getColumnIndexOrThrow("end_date")));
            ((TextView) findViewById(R.id.tv_reason))
                    .setText(cursor.getString(cursor.getColumnIndexOrThrow("reason")));
            ((TextView) findViewById(R.id.tv_submit_time))
                    .setText("提交于 " + cursor.getString(cursor.getColumnIndexOrThrow("submit_time")));
        }
        cursor.close();

        // 查询外出住宿地址信息
        Cursor addrCursor = dBhelper.getOutsideAddress(appId);
        if (addrCursor.moveToFirst()) {
            ((TextView) findViewById(R.id.tv_outside_address))
                    .setText("住宿地址: " + addrCursor.getString(addrCursor.getColumnIndexOrThrow("address")));
            ((TextView) findViewById(R.id.tv_contact_person))
                    .setText("联系人: " + addrCursor.getString(addrCursor.getColumnIndexOrThrow("contact_person")));
            ((TextView) findViewById(R.id.tv_contact_phone))
                    .setText("联系电话: " + addrCursor.getString(addrCursor.getColumnIndexOrThrow("contact_phone")));
        }
        addrCursor.close();
    }
}
