package com.example.adwork.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adwork.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 共 9 张表：
 *   1. account 账号密码表（一卡通号、密码）
 *   2. personal_info 个人信息表（姓名、性别、学院、班级、学号、宿舍）
 *   3. dorm_building 宿舍楼信息（楼名、楼层、管理员）
 *   4. dorm_room 宿舍房间（房间号、床位数、入住状态）
 *   5. application 外出住宿申请数据
 *   6. application_log 申请提交日志（时间、状态）
 *   7. approval 审批记录（审批人、意见、结果）
 *   8. outside_address 外出住宿地址信息（关联申请ID）
 *   9. regulation 规章制度表
 */
public class DBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "adwork.db";
    private static final int DB_VERSION = 9;
    private final Context mContext;

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 表1: 账号密码表
        db.execSQL("CREATE TABLE account ("
                + "card_number TEXT PRIMARY KEY, "
                + "password TEXT NOT NULL)");

        ContentValues acv = new ContentValues();
        acv.put("card_number", "00211054");
        acv.put("password", "123456");
        db.insert("account", null, acv);

        // 表2: 个人信息表
        db.execSQL("CREATE TABLE personal_info ("
                + "card_number TEXT PRIMARY KEY, "
                + "name TEXT, "
                + "gender TEXT, "
                + "college TEXT, "
                + "class_name TEXT, "
                + "student_id TEXT, "
                + "dorm_building TEXT, "
                + "dorm_number TEXT)");

        ContentValues pcv = new ContentValues();
        pcv.put("card_number", "00211054");
        pcv.put("name", "张三");
        pcv.put("gender", "男");
        pcv.put("college", "人工智能学院");
        pcv.put("class_name", "计科学244");
        pcv.put("student_id", "2024433225418");
        pcv.put("dorm_building", "qx5");
        pcv.put("dorm_number", "624");
        db.insert("personal_info", null, pcv);

        // 表3: 宿舍楼表
        db.execSQL("CREATE TABLE dorm_building ("
                + "building_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "building_name TEXT, "
                + "floors INTEGER, "
                + "admin_name TEXT, "
                + "admin_phone TEXT)");

        ContentValues bld = new ContentValues();
        bld.put("building_name", "QX5");
        bld.put("floors", 6);
        bld.put("admin_name", "王管理员");
        bld.put("admin_phone", "83640001");
        db.insert("dorm_building", null, bld);

        // 表4: 宿舍房间表
        db.execSQL("CREATE TABLE dorm_room ("
                + "room_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "building_name TEXT, "
                + "room_number TEXT, "
                + "bed_count INTEGER, "
                + "occupied_count INTEGER, "
                + "status TEXT)");

        ContentValues rm = new ContentValues();
        rm.put("building_name", "QX5");
        rm.put("room_number", "624");
        rm.put("bed_count", 6);
        rm.put("occupied_count", 4);
        rm.put("status", "部分空闲");
        db.insert("dorm_room", null, rm);

        // 表5: 申请表
        db.execSQL("CREATE TABLE application ("
                + "app_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "card_number TEXT, "
                + "name TEXT, "
                + "class_name TEXT, "
                + "dorm_info TEXT, "
                + "phone TEXT, "
                + "start_date TEXT, "
                + "end_date TEXT, "
                + "reason TEXT)");

        // 表6: 申请日志表
        db.execSQL("CREATE TABLE application_log ("
                + "log_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "card_number TEXT, "
                + "submit_time TEXT, "
                + "status TEXT)");

        // 表7: 审批记录表
        db.execSQL("CREATE TABLE approval ("
                + "approval_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "approver_name TEXT, "
                + "opinion TEXT, "
                + "approve_time TEXT, "
                + "result TEXT)");

        // 表8: 外出住宿地址表
        db.execSQL("CREATE TABLE outside_address ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "app_id INTEGER, "
                + "address TEXT, "
                + "contact_person TEXT, "
                + "contact_phone TEXT)");

        // 表9: 规章制度表
        db.execSQL("CREATE TABLE regulation ("
                + "reg_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT, "
                + "doc_number TEXT, "
                + "content TEXT)");

        String title = "嘉兴大学学生申请校外住宿管理规定";
        String docNumber = "嘉大学字〔2024〕10号";
        String content = mContext.getString(R.string.regulation_text);
        ContentValues reg = new ContentValues();
        reg.put("title", title);
        reg.put("doc_number", docNumber);
        reg.put("content", content);
        db.insert("regulation", null, reg);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] tables = {"account", "personal_info", "dorm_building", "dorm_room",
                "application", "application_log", "approval", "outside_address", "regulation"};
        for (String t : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + t);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // 确认账号密码是否正确

    public boolean checkLogin(String cardNumber, String password) {
        Cursor c = getReadableDatabase().query("account", null,
                "card_number=? AND password=?",
                new String[]{cardNumber, password},
                null, null, null);
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    // 根据一卡通号获取个人信息
    public Cursor getUserInfo(String cardNumber) {
        return getReadableDatabase().query("personal_info", null,
                "card_number=?", new String[]{cardNumber},
                null, null, null);
    }
    // 将申请内容存入表
    public long insertApplication(String cardNumber, String name, String className,
                                  String dormInfo, String phone, String startDate,
                                  String endDate, String reason) {
        ContentValues cv = new ContentValues();
        cv.put("card_number", cardNumber);
        cv.put("name", name);
        cv.put("class_name", className);
        cv.put("dorm_info", dormInfo);
        cv.put("phone", phone);
        cv.put("start_date", startDate);
        cv.put("end_date", endDate);
        cv.put("reason", reason);
        return getWritableDatabase().insert("application", null, cv);
    }
//获取已经有的可以确定的一些个人信息 然后存入申请表里先行完善
    public void insertLog(long appId, String cardNumber, String status) {
        ContentValues cv = new ContentValues();
        cv.put("app_id", appId);
        cv.put("card_number", cardNumber);
        cv.put("submit_time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        cv.put("status", status);
        getWritableDatabase().insert("application_log", null, cv);
    }
//通过一卡通号获取申请表的相关数据
    public Cursor getApplicationsByUser(String cardNumber) {
        return getReadableDatabase().rawQuery(
                "SELECT a.app_id AS _id, a.app_id, a.name, a.reason, a.start_date, a.end_date, "
                        + "l.submit_time, l.status "
                        + "FROM application a "
                        + "JOIN application_log l ON a.app_id = l.app_id "
                        + "WHERE a.card_number=? "
                        + "ORDER BY l.submit_time DESC",
                new String[]{cardNumber});
    }
//获取申请详情
    public Cursor getApplicationDetail(int appId) {
        return getReadableDatabase().rawQuery(
                "SELECT a.*, l.submit_time, l.status "
                        + "FROM application a "
                        + "JOIN application_log l ON a.app_id = l.app_id "
                        + "WHERE a.app_id=?",
                new String[]{String.valueOf(appId)});
    }
    // 存储外出地址
    public void insertOutsideAddress(long appId, String address, String contactPerson,
                                     String contactPhone) {
        ContentValues cv = new ContentValues();
        cv.put("app_id", appId);
        cv.put("address", address);
        cv.put("contact_person", contactPerson);
        cv.put("contact_phone", contactPhone);
        getWritableDatabase().insert("outside_address", null, cv);
    }
    // 获取所有的规章制度

    public Cursor getAllRegulations() {
        return getReadableDatabase().query("regulation", null,
                null, null, null, null, null);
    }
}