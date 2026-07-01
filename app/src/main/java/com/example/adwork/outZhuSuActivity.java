package com.example.adwork;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adwork.Tools.DBhelper;

/**
 * 外出住宿处理 Activity
 * 功能：填写校外住宿申请表单，提交后数据存入数据库
 *       自动填充用户信息（姓名、一卡通号、班级、当前宿舍）
 *       需填写：手机号、出发/返回日期、申请理由、校外住宿地址、联系人、联系电话
 * 数据库表：application（存申请数据）、application_log（存提交日志）、outside_address（存住宿地址）
 * 入口：TransactionFragment → 外出住宿处理
 */
public class outZhuSuActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate, etReason, etPhone;
    private EditText etOutsideAddress, etContactPerson, etContactPhone;
    private TextView tvName, tvCardNumber, tvClass, tvDorm;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dBhelper = new DBhelper(this);
        String cardId = getIntent().getStringExtra("card_id");

        tvName = findViewById(R.id.tv_name);
        tvCardNumber = findViewById(R.id.tv_card_number);
        tvClass = findViewById(R.id.tv_class);
        tvDorm = findViewById(R.id.tv_dorm);
        etPhone = findViewById(R.id.et_phone);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etReason = findViewById(R.id.et_reason);
        etOutsideAddress = findViewById(R.id.et_outside_address);
        etContactPerson = findViewById(R.id.et_contact_person);
        etContactPhone = findViewById(R.id.et_contact_phone);

        loadUserInfo(cardId);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String reason = etReason.getText().toString().trim();
            String outsideAddr = etOutsideAddress.getText().toString().trim();
            String contactPerson = etContactPerson.getText().toString().trim();
            String contactPhone = etContactPhone.getText().toString().trim();

            if (phone.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty()
                    || outsideAddr.isEmpty()) {
                Toast.makeText(this, "请填写完整信息（住宿地址为必填）", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = dBhelper.getUserInfo(cardId);
                String name = "", className = "", dormInfo = "";
                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    className = cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                    String b = cursor.getString(cursor.getColumnIndexOrThrow("dorm_building"));
                    String d = cursor.getString(cursor.getColumnIndexOrThrow("dorm_number"));
                    dormInfo = b.toUpperCase() + " - " + d;
                }
                cursor.close();

                long appId = dBhelper.insertApplication(cardId, name, className, dormInfo, phone, startDate, endDate, reason);
                dBhelper.insertLog(appId, cardId, "待审核");
                dBhelper.insertOutsideAddress(appId, outsideAddr, contactPerson, contactPhone);

                Toast.makeText(this, "申请已提交，等待审核", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadUserInfo(String cardId) {
        Cursor cursor = dBhelper.getUserInfo(cardId);
        if (cursor.moveToFirst()) {
            tvName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            tvCardNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow("card_number")));
            tvClass.setText(cursor.getString(cursor.getColumnIndexOrThrow("class_name")));
            String building = cursor.getString(cursor.getColumnIndexOrThrow("dorm_building"));
            String dorm = cursor.getString(cursor.getColumnIndexOrThrow("dorm_number"));
            tvDorm.setText(building.toUpperCase() + " — " + dorm);
        }
        cursor.close();
    }
}
