package com.example.adwork;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.ApplicationAdapter;
import com.example.adwork.Tools.DBhelper;

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

        ApplicationAdapter adapter = new ApplicationAdapter(this, cursor);
        listView.setAdapter(adapter);
    }
}
