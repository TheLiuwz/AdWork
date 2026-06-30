package com.example.adwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;

/**
 * 登录页面Activity
 * 功能：
 * 1. 检查是否已登录，已登录则直接跳转主界面
 * 2. 未登录时显示登录界面，验证账号密码
 * 3. 登录成功后保存登录状态并跳转主界面
 */
public class MainActivity extends AppCompatActivity {

    private EditText etCardNumber, etPassword; // 一卡通号和密码输入框
    private Button btnLogin; // 登录按钮
    private DBhelper dBhelper; // 数据库助手

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 设置沉浸式状态栏，适配系统窗口
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 检查是否已登录（从SharedPreferences读取保存的一卡通号）
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String savedId = sp.getString("card_id", null);
        if (savedId != null) {
            // 已登录，直接跳转到主界面
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("card_id", savedId);
            startActivity(intent);
            finish();
            return;
        }

        // 初始化控件
        etCardNumber = findViewById(R.id.et_card_number);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        dBhelper = new DBhelper(this);

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String id = etCardNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (id.isEmpty() || password.isEmpty()) {
                // 输入为空，提示用户
                Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            } else if (dBhelper.checkLogin(id, password)) {
                // 账号密码正确，保存登录状态并跳转
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                sp.edit().putString("card_id", id).apply(); // 保存一卡通号
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("card_id", id);
                startActivity(intent);
                finish();
            } else {
                // 账号密码错误，提示用户
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
