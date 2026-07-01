package com.example.adwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;
public class MainActivity extends AppCompatActivity {

    private EditText etCardNumber, etPassword;
    private Button btnLogin;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        etCardNumber = findViewById(R.id.et_card_number);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        dBhelper = new DBhelper(this);

        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String id = etCardNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            } else if (dBhelper.checkLogin(id, password)) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("card_id", id);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
