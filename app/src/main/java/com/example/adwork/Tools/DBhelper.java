package com.example.adwork.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 数据库帮助类（SQLite）
 * 管理所有数据表的创建、升级和基本 CRUD 操作
 * 共 10 张表：
 *   1. user              - 用户信息（账号密码、姓名、班级、宿舍等）
 *   2. dorm_building     - 宿舍楼信息（楼名、楼层、管理员）
 *   3. dorm_room         - 宿舍房间（房间号、床位数、入住状态）
 *   4. application       - 外出住宿申请数据
 *   5. application_log   - 申请提交日志（时间、状态）
 *   6. approval          - 审批记录（审批人、意见、结果）
 *   7. notification      - 通知公告
 *   8. regulation        - 规章制度
 *   9. audit_log         - 系统操作日志
 *  10. outside_address   - 外出住宿地址信息（关联申请ID）
 */
public class DBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "adwork.db";
    private static final int DB_VERSION = 7;

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 表1: 用户表
        db.execSQL("CREATE TABLE user ("
                + "card_number TEXT PRIMARY KEY, "
                + "password TEXT NOT NULL, "
                + "name TEXT, "
                + "gender TEXT, "
                + "college TEXT, "
                + "class_name TEXT, "
                + "student_id TEXT, "
                + "dorm_building TEXT, "
                + "dorm_number TEXT)");

        ContentValues ucv = new ContentValues();
        ucv.put("card_number", "00211054");
        ucv.put("password", "123456");
        ucv.put("name", "张三");
        ucv.put("gender", "男");
        ucv.put("college", "人工智能学院");
        ucv.put("class_name", "计科学244");
        ucv.put("student_id", "2024433225418");
        ucv.put("dorm_building", "qx5");
        ucv.put("dorm_number", "624");
        db.insert("user", null, ucv);

        // 表2: 宿舍楼表
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

        // 表3: 宿舍房间表
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
        rm.put("bed_count", 5);
        rm.put("occupied_count", 4);
        rm.put("status", "部分空闲");
        db.insert("dorm_room", null, rm);

        // 表4: 申请表
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

        // 表5: 申请日志表
        db.execSQL("CREATE TABLE application_log ("
                + "log_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "app_id INTEGER, "
                + "card_number TEXT, "
                + "submit_time TEXT, "
                + "status TEXT)");

        // 表6: 审批记录表
        db.execSQL("CREATE TABLE approval ("
                + "approval_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "app_id INTEGER, "
                + "approver_name TEXT, "
                + "opinion TEXT, "
                + "approve_time TEXT, "
                + "result TEXT)");

        // 表7: 通知公告表
        db.execSQL("CREATE TABLE notification ("
                + "notify_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT, "
                + "content TEXT, "
                + "publish_time TEXT, "
                + "target_role TEXT)");
        ContentValues noti = new ContentValues();
        noti.put("title", "关于2024年暑假校外住宿申请通知");
        noti.put("content", "请需要暑假校外住宿的同学在7月15日前提交申请。");
        noti.put("publish_time", "2024-06-20 09:00");
        noti.put("target_role", "全部学生");
        db.insert("notification", null, noti);

        // 表8: 规章制度表
        db.execSQL("CREATE TABLE regulation ("
                + "reg_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT, "
                + "content TEXT, "
                + "version TEXT, "
                + "effective_date TEXT)");
        ContentValues reg = new ContentValues();
        reg.put("title", "嘉兴大学学生申请校外住宿管理规定");
        reg.put("content", "嘉大学字〔2024〕10号");
        reg.put("version", "2024版");
        reg.put("effective_date", "2024-01-01");
        db.insert("regulation", null, reg);

        // 表9: 操作日志表
        db.execSQL("CREATE TABLE audit_log ("
                + "audit_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "operator TEXT, "
                + "action_type TEXT, "
                + "target TEXT, "
                + "detail TEXT, "
                + "operate_time TEXT)");

        // 表10: 外出住宿地址表
        db.execSQL("CREATE TABLE outside_address ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "app_id INTEGER, "
                + "address TEXT, "
                + "contact_person TEXT, "
                + "contact_phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] tables = {"user", "dorm_building", "dorm_room", "application",
                "application_log", "approval", "notification", "regulation",
                "audit_log", "outside_address"};
        for (String t : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + t);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // ========== 用户相关 ==========

    public boolean checkLogin(String cardNumber, String password) {
        Cursor c = getReadableDatabase().query("user", null,
                "card_number=? AND password=?",
                new String[]{cardNumber, password},
                null, null, null);
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    public Cursor getUserInfo(String cardNumber) {
        return getReadableDatabase().query("user", null,
                "card_number=?", new String[]{cardNumber},
                null, null, null);
    }

    // ========== 申请相关 ==========

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

    public void insertLog(long appId, String cardNumber, String status) {
        ContentValues cv = new ContentValues();
        cv.put("app_id", appId);
        cv.put("card_number", cardNumber);
        cv.put("submit_time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        cv.put("status", status);
        getWritableDatabase().insert("application_log", null, cv);
    }

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

    public Cursor getApplicationDetail(int appId) {
        return getReadableDatabase().rawQuery(
                "SELECT a.*, l.submit_time, l.status "
                        + "FROM application a "
                        + "JOIN application_log l ON a.app_id = l.app_id "
                        + "WHERE a.app_id=?",
                new String[]{String.valueOf(appId)});
    }

    public int getApplicationCount(String cardNumber) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM application WHERE card_number=?",
                new String[]{cardNumber});
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    // ========== 外出住宿地址相关 ==========

    public void insertOutsideAddress(long appId, String address, String contactPerson,
                                      String contactPhone) {
        ContentValues cv = new ContentValues();
        cv.put("app_id", appId);
        cv.put("address", address);
        cv.put("contact_person", contactPerson);
        cv.put("contact_phone", contactPhone);
        getWritableDatabase().insert("outside_address", null, cv);
    }

    public Cursor getOutsideAddress(int appId) {
        return getReadableDatabase().query("outside_address", null,
                "app_id=?", new String[]{String.valueOf(appId)},
                null, null, null);
    }

    // ========== 审批相关 ==========

    public void insertApproval(int appId, String approver, String opinion, String result) {
        ContentValues cv = new ContentValues();
        cv.put("app_id", appId);
        cv.put("approver_name", approver);
        cv.put("opinion", opinion);
        cv.put("approve_time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        cv.put("result", result);
        getWritableDatabase().insert("approval", null, cv);
    }

    // ========== 通知相关 ==========

    public Cursor getAllNotifications() {
        return getReadableDatabase().query("notification", null,
                null, null, null, null, "publish_time DESC");
    }

    // ========== 规章制度相关 ==========

    public Cursor getAllRegulations() {
        return getReadableDatabase().query("regulation", null,
                null, null, null, null, "effective_date DESC");
    }

    // ========== 操作日志相关 ==========

    public void insertAuditLog(String operator, String action, String target, String detail) {
        ContentValues cv = new ContentValues();
        cv.put("operator", operator);
        cv.put("action_type", action);
        cv.put("target", target);
        cv.put("detail", detail);
        cv.put("operate_time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        getWritableDatabase().insert("audit_log", null, cv);
    }
}
