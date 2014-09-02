package com.seyda.st010.chatsample1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends Activity {

    SharedPreferences sharedpreferences;
    String userName;

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, SplashActivity.MODE_PRIVATE);
        new PrefetchData().execute(MyPREFERENCES);

    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            return sharedpreferences.getString("username",null);


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity

             ServerConnection connect = new ServerConnection("217.78.110.158",5222,"localhost");
            //connect.connect();

            if (result == null) {
                Intent startLoginActivity = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(startLoginActivity);
            }else {

                Intent startChatActivity = new Intent(SplashActivity.this, ChatListActivity.class);
                startChatActivity.putExtra("username", sharedpreferences.getString("username", ""));
                startChatActivity.putExtra("userPassword", sharedpreferences.getString("userPassword", ""));

                //check if user already login
                connect.connect(sharedpreferences.getString("username", ""),sharedpreferences.getString("userPassword", ""));
                startActivity(startChatActivity);
            }

            // close this activity
            finish();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
