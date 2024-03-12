package sumagoinfotech.ipt.task1.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sumagoinfotech.ipt.task1.data.UserBean;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_PROFILE_STATE = "profile_state";
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_PROFILE_STATE + " INTEGER"
            + ")";
    Context context;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @SuppressLint("Range")
    public int getId(String email, String password) {
        int id=-1;
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        db.close();
        return id;
    }

    public boolean insertUser(String email, String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USER_NAME, userName);
        long result = -1;
        try {
            result = db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user data: " + e.getMessage());
        } finally {
            db.close();
        }
        return result != -1;
    }

    @SuppressLint("Range")
    public UserBean getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_EMAIL, COLUMN_USER_NAME};
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        UserBean userBean = null;
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            userBean = new UserBean(email, password, context);
            cursor.close();
        }
        db.close();
        return userBean;
    }
    public boolean userExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"COUNT(*)"};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            db.close();
            return count > 0;
        }
        db.close();
        return false;
    }

    public boolean checkCredentials(UserBean ub) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"COUNT(*)"};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {ub.getEmail(), ub.getUser_name()};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            db.close();
            return count > 0;
        }
        db.close();
        return false;
    }

}
