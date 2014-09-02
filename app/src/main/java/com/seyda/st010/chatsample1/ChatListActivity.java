package com.seyda.st010.chatsample1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.jivesoftware.smack.XMPPException;


public class ChatListActivity extends Activity {
    public String USERNAME;
    public String PASSWORD;
    public ServerConnection connect;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, SplashActivity.MODE_PRIVATE);
        USERNAME = sharedpreferences.getString("username","");
        PASSWORD =  sharedpreferences.getString("userPassword","");
        Log.e("USERNAME CHATLIST", USERNAME);
        Log.e("PASSWORD CHATLIST", PASSWORD);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                    connect = new ServerConnection("217.78.110.158",5222,"localhost");

                    connect.connect(USERNAME, PASSWORD);

            }
            });
            t.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_about:
                // About option clicked.
                return true;
            case R.id.logout:

                Intent activityChangeIntent = new Intent(ChatListActivity.this, LoginActivity.class);
                //logout
                sharedpreferences = getSharedPreferences(MyPREFERENCES, SplashActivity.MODE_PRIVATE); //buraya bak
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("username",null);
                editor.putString("userPassword",null);
                editor.commit();

                ServerConnection.connection.disconnect();
                startActivity(activityChangeIntent);
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;
            case R.id.buddies:
                Intent activityIntent = new Intent(ChatListActivity.this, BuddyListActivity.class);
                startActivity(activityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
