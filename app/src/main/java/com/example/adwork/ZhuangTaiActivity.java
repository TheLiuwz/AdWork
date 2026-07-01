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
