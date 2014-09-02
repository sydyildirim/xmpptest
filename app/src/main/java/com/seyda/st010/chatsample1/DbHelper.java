package com.seyda.st010.chatsample1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.Collection;

/**
 * Created by st010 on 02.09.2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AFAD_DB";

    // Table Names
    private static final String TABLE_USER = "userTable";
    private static final String TABLE_CONVERSATION = "conversationTable";
    private static final String TABLE_IM = "imTable";

    // Common column names
   // private static final String KEY_ID = "id";
   // private static final String KEY_CREATED_AT = "created_at";

    // USER Table - column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_STATUS = "status";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_USER_NAME = "user_name";

    // CONVERSATION Table - column names
    private static final String KEY_CONVERSATION_ID = "conversation_id";
    private static final String KEY_CONVERSATION_NAME = "conversation_name";
    private static final String KEY_CONVERSATION_LAST_SEEN = "conversation_last_seen";

    // IM Table - column names
    private static final String KEY_IM_ID = "im_id";
    private static final String KEY_SEND = "im_send"; //if sended msg, true

    // Table Create Statements
    // user table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"+ KEY_USERNAME
            + " TEXT," + KEY_USER_PASSWORD + " TEXT," + KEY_STATUS + " TEXT," + KEY_USER_TYPE
            + " TEXT" + ")";

    // conversation table create statement
    private static final String CREATE_TABLE_CONVERSATION = "CREATE TABLE " + TABLE_CONVERSATION
            + "(" + KEY_CONVERSATION_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER FOREIGN KEY,"
            + KEY_CONVERSATION_LAST_SEEN + " DATETIME,"
            + KEY_CONVERSATION_NAME + " TEXT" + ")";

    // im table create statement
    private static final String CREATE_TABLE_IM = "CREATE TABLE "
            + TABLE_IM + "(" + KEY_IM_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " INTEGER FOREIGN KEY," + KEY_CONVERSATION_ID + " INTEGER FOREIGN KEY,"
            + KEY_SEND + " BOOLEAN" + ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CONVERSATION);
        db.execSQL(CREATE_TABLE_IM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IM);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "USER" table methods ----------------//

    /*
 * Creating a user
 */
    public boolean createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_USERNAME, user.getUsername());
        contentValues.put(KEY_USER_PASSWORD, user.getPassword());
        contentValues.put(KEY_STATUS, user.getStatus());
        //contentValues.put(KEY_USER_TYPE, user.getType());
        //contentValues.put(KEY_USER_NAME, user.getUse_name());

        db.insert(TABLE_USER, null, contentValues);
        return true;
    }

    // ------------------------ "CONVERSATION" table methods ----------------//

    /*
* Creating a conversation
*/
    public boolean createConversation(Conversation conversation, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_CONVERSATION_NAME, conversation.getConversationName());
        contentValues.put(KEY_CONVERSATION_LAST_SEEN, conversation.getLast_seen());
        contentValues.put(KEY_USER_ID, conversation.getUserId());


        db.insert(TABLE_CONVERSATION, null, contentValues);
        return true;
    }
    // ------------------------ "IM" table methods ----------------//

    /*
* Creating a im
*/
    public boolean createIm(Im im, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_USER_ID, im.getUserId());
        contentValues.put(KEY_CONVERSATION_ID, im.getConversationId());
        contentValues.put(KEY_SEND, im.getSend());


        db.insert(TABLE_IM, null, contentValues);
        return true;
    }
}
