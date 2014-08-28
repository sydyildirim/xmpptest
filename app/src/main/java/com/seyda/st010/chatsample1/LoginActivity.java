package com.seyda.st010.chatsample1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) this.findViewById(R.id.userName);
        password = (EditText) this.findViewById(R.id.password);



        final Button button = (Button) findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                String username = userName.getText().toString();
                String userPassword = password.getText().toString();

                Intent activityChangeIntent = new Intent(LoginActivity.this, chatActivity.class);
                activityChangeIntent.putExtra("username",username);
                activityChangeIntent.putExtra("userPassword",userPassword);

                LoginActivity.this.startActivity(activityChangeIntent);
            }
        });

    }
}



