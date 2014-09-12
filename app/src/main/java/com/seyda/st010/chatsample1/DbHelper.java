package com.seyda.st010.chatsample1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.ArrayList;
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
    private static final String TABLE_GROUP = "groupTable";
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
    //private static final String KEY_SEND = "im_send"; //if sended msg, true
    private static final String KEY_MSG_TEXT = "im_msg_text";


    // GROUP Table - column names

    private static final String KEY_GROUP_MEMBER_USER_ID = "group_member_userId"; //if sended msg, true


    // Table Create Statements
    // user table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"+ KEY_USERNAME
            + " TEXT," + KEY_USER_PASSWORD + " TEXT," + KEY_STATUS + " TEXT," + KEY_USER_TYPE
            + " TEXT" + ")";

    // conversation table create statement
    private static final String CREATE_TABLE_CONVERSATION = "CREATE TABLE " + TABLE_CONVERSATION
            + "(" + KEY_CONVERSATION_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
            + KEY_CONVERSATION_LAST_SEEN + " DATETIME,"
            + KEY_CONVERSATION_NAME + " TEXT" + ")";

    // im table create statement
    private static final String CREATE_TABLE_IM = "CREATE TABLE "
            + TABLE_IM + "(" + KEY_IM_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " INTEGER," + KEY_CONVERSATION_ID +
            " INTEGER," + KEY_MSG_TEXT + " TEXT"
            + ")";

    // GROUP table create statement
    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP
            + "(" + KEY_CONVERSATION_ID + " INTEGER," + KEY_GROUP_MEMBER_USER_ID + " INTEGER" + ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CONVERSATION);
        db.execSQL(CREATE_TABLE_IM);
        db.execSQL(CREATE_TABLE_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);

        // create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
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

    /**
     * get ALL USERS
     */
    public ArrayList <User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                User user = new User();
                user.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                user.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));

                // adding to todo list
                users.add(user);
            } while (c.moveToNext());
        }

        return users;
    }

    public User getUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_USER_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User user = new User();
        user.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        user.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));


        return user;
    }

    public long getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_USERNAME + " = " + "'"+ username+ "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c.getInt(c.getColumnIndex(KEY_USER_ID));
    }

    // ------------------------ "CONVERSATION" table methods ----------------//

    /*
* Creating a conversation
*/
    public boolean createConversation(Conversation conversation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_CONVERSATION_NAME, conversation.getConversationName());
        contentValues.put(KEY_CONVERSATION_LAST_SEEN, conversation.getLast_seen());
        contentValues.put(KEY_USER_ID, conversation.getUserId());


        db.insert(TABLE_CONVERSATION, null, contentValues);
        return true;
    }

    //get user's all conversations
    public ArrayList <Conversation> getUserConversations(long userId) {
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP + " WHERE "
                + KEY_GROUP_MEMBER_USER_ID + " = " + userId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Conversation conversation = new Conversation();
                conversation.setConversationId(c.getInt(c.getColumnIndex(KEY_CONVERSATION_ID)));
                //kontrol et
                String selectQuery1 = "SELECT  * FROM " + TABLE_CONVERSATION + " WHERE "
                        + KEY_CONVERSATION_ID + " = " + conversation.getConversationId();

                Cursor c1 = db.rawQuery(selectQuery1, null);
                String conName;
                if (c1.moveToFirst()) {


                        if(c1.getCount()!=0) {
                            if (c1.getString(c1.getColumnIndex(KEY_CONVERSATION_NAME)) == null)
                                conName = "noName";
                            else
                                conName = c1.getString(c1.getColumnIndex(KEY_CONVERSATION_NAME));
                        }
                        else
                            conName = "noName";
                        conversation.setConversationName(conName);
                        //conversation.setLast_seen(c1.getDate(c1.getColumnIndex(KEY_CONVERSATION_LAST_SEEN)));

                        // adding to im list


                }
                conversations.add(conversation);
            } while (c.moveToNext());
        }

        return conversations;
    }
//if conversation already exists return its conversation_id
 /*   public long ifConversationExists(long[] userIds) {
        SQLiteDatabase db = this.getReadableDatabase();
        long conId;
        for (long user_id : userIds) {

            String selectQuery = "SELECT  * FROM " + TABLE_GROUP + " WHERE "
                    + KEY_GROUP_MEMBER_USER_ID + " = " + user_id;

            Log.e(LOG, selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null)
                c.moveToFirst();
                conId=c.getInt(c.getColumnIndex(KEY_CONVERSATION_ID));
            else

        }

        return c.getInt(c.getColumnIndex(KEY_USER_ID));

    }*/
    public long getLastConversationId(){
        SQLiteDatabase db = this.getReadableDatabase();
        long conId;

        String selectQuery = "SELECT  * FROM " + TABLE_CONVERSATION + " ORDER BY "
                + KEY_CONVERSATION_ID + " DESC limit 1";

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.getCount()!=0)
        if (c != null) {
            c.moveToFirst();
            conId = c.getInt(c.getColumnIndex(KEY_CONVERSATION_ID));
            return conId;
        }
        return 0;
    }

    // ------------------------ "IM" table methods ----------------//

    /*
* Creating a im
*/
    public boolean createIm(Im im) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_USER_ID, im.getUserId());
        contentValues.put(KEY_CONVERSATION_ID, im.getConversationId());

        contentValues.put(KEY_MSG_TEXT, im.getMsgText());

        db.insert(TABLE_IM, null, contentValues);
        return true;
    }

//get conversation's all ims
    public ArrayList <Im> getConversationIms(long conversationId) {
        ArrayList<Im> ims = new ArrayList<Im>();
        String selectQuery = "SELECT  * FROM " + TABLE_IM + " WHERE "
                + KEY_CONVERSATION_ID + " = " + conversationId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Im im = new Im();
                im.setMsgText(c.getString(c.getColumnIndex(KEY_MSG_TEXT)));
                im.setSender_userId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                // adding to im list
                ims.add(im);
            } while (c.moveToNext());
        }

        return ims;
    }

    // ------------------------ "GROUP" table methods ----------------//

    /*
* Creating a GROUP
*/
    public boolean createGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_CONVERSATION_ID, group.getConversationId());
        contentValues.put(KEY_GROUP_MEMBER_USER_ID, group.getGroupMemberUserId());

        db.insert(TABLE_GROUP, null, contentValues);
        return true;
    }
    public ArrayList <User> getMessageReceivers(long conversationId) {
        ArrayList<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP + " WHERE "
                + KEY_CONVERSATION_ID + " = " + conversationId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                User user = new User();
                user.setUser_id(c.getInt(c.getColumnIndex(KEY_GROUP_MEMBER_USER_ID)));

                // adding to im list
                users.add(user);
            } while (c.moveToNext());
        }

        return users;
    }

}
