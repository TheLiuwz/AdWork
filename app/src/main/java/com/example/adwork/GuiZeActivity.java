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
 * 规则查看 Activity
 * 功能：从数据库读取规章制度内容并展示
 * 数据库表：regulation（规章制度表）
 * 入口：ShiWuFragment → 规则查看
 */
public class GuiZeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guize);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 从数据库读取规章制度
        DBhelper dBhelper = new DBhelper(this);
        Cursor cursor = dBhelper.getAllRegulations();
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String docNumber = cursor.getString(cursor.getColumnIndexOrThrow("doc_number"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));

            TextView tvTitle = findViewById(R.id.tv_title);
            TextView tvDocNumber = findViewById(R.id.tv_doc_number);
            TextView tvContent = findViewById(R.id.tv_content);

            tvTitle.setText(title);
            tvDocNumber.setText(docNumber);
            tvContent.setText(content);
        }
        cursor.close();
    }
}
