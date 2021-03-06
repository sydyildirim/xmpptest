package com.seyda.st010.chatsample1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

public class chatActivity extends Activity {

    public static final String HOST = "217.78.110.158";
    public static final int PORT = 5222;
    public static final String SERVICE = "localhost";

    public String USERNAME;
    public String PASSWORD;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private long conversationId;

    public static XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();

    //private EditText recipient;
    private EditText textMessage;
    private ListView listview;
    private ArrayList<Im> ims;
    // Database Helper
    DbHelper db;
    Im im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        connection = ServerConnection.connection;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, SplashActivity.MODE_PRIVATE);

        USERNAME = sharedpreferences.getString("username","");
        PASSWORD = sharedpreferences.getString("userPassword","");
       // USERNAME = getIntent().getStringExtra("username");
       // PASSWORD = getIntent().getStringExtra("userPassword");
        conversationId= getIntent().getLongExtra("conversationId",0);
        Log.e("chat act con_id=", ""+conversationId);
        //recipient = (EditText) this.findViewById(R.id.toET);
        textMessage = (EditText) this.findViewById(R.id.editText);
        listview = (ListView) this.findViewById(R.id.listView);
        ims = new ArrayList<Im>();
        db = new DbHelper(chatActivity.this);
        ims = db.getConversationIms(conversationId);

        if(!ims.isEmpty()){

            for(int i=0; i<ims.size(); i++) {
                User sender= db.getUser(ims.get(i).getSender_userId());
                messages.add(sender.getUsername()+ ":"); //sender id
                messages.add(ims.get(i).getMsgText());
            }
        }
        setListAdapter();

        // Set a listener to send a chat text message
        Button send = (Button) this.findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String to;
                ArrayList<String> toS = new ArrayList<String>();
                //conversationId den im, im den rescipient çek
                ArrayList<User> resceivers =db.getMessageReceivers(conversationId);
                if(!resceivers.isEmpty()){ //şuan group yok
                    for(int i=0; i<resceivers.size() ; i++) {
                    String tempoTo = db.getUser(resceivers.get(i).getUser_id()).getUsername();
                        if(tempoTo.compareTo(USERNAME)!=0)
                            toS.add(tempoTo);
                    }
                }

                to = getIntent().getStringExtra("recipient"); //BUDDYLİST DEN SEÇERSE
                if(to==null)
                    to=toS.get(0);// şuan grup chat yok tek alıcı deneme

                Log.e("rescipient= ", to);

                String text = textMessage.getText().toString();

                textMessage.setText(null);

                Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + to);
                Message msg = new Message(to, Message.Type.chat);
                msg.setBody(text);
                if (connection != null) {
                    connection.sendPacket(msg);
                    messages.add(connection.getUser() + ":");
                    messages.add(text);
                    setListAdapter();
                }
              //ADD IM TO DB
                im = new Im();
                im.setSender_userId(db.getUserId(USERNAME));
                im.setUserId(db.getUserId(USERNAME));
                im.setReceiver_userId(db.getUserId(to));
                im.setConversationId(conversationId);
                im.setMsgText(text);


                if(db.createIm(im))
                    Log.e("DB", "database im kayıt oldu");
                db.closeDB();

              //  Log.e("DB username=", d);



            }
        });

        connect();

    }
    /**
     * Called by Settings dialog when a connection is establised with the XMPP
     * server
     *
     * @param connection
     */
    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message
                                .getFrom());
                        Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
                                + " from " + fromName );
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
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

    private void setListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.list, messages);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connection != null)
                connection.disconnect();
        } catch (Exception e) {

        }
    }

    public void connect() {

        final ProgressDialog mDiaog = new ProgressDialog(chatActivity.this);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a connection
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        HOST, PORT, SERVICE);
                XMPPConnection connection = new XMPPConnection(connConfig);

                try {
                    connection.connect();
                    Log.i("XMPPChatDemoActivity",
                            "Connected to " + connection.getHost());
                } catch (XMPPException ex) {
                    Log.e("XMPPChatDemoActivity", "Failed to connect to "
                            + connection.getHost());
                    Log.e("XMPPChatDemoActivity", ex.toString());
                    setConnection(null);
                }
                Log.e("Service Name", connConfig.getServiceName());
                try {
                    //SASLAuthentication.supportSASLMechanism("PLAIN", 0);

                    connection.login(USERNAME, PASSWORD);
                    Log.i("XMPPChatDemoActivity",
                            "Logged in as " + connection.getUser());

                    // Set the status to available
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnection(connection);

                    Roster roster = connection.getRoster();
                    Collection<RosterEntry> entries = roster.getEntries();
                    for (RosterEntry entry : entries) {
                        Log.d("XMPPChatDemoActivity",
                                "--------------------------------------");
                        Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
                        Log.d("XMPPChatDemoActivity",
                                "User: " + entry.getUser());
                        Log.d("XMPPChatDemoActivity",
                                "Name: " + entry.getName());
                        Log.d("XMPPChatDemoActivity",
                                "Status: " + entry.getStatus());
                        Log.d("XMPPChatDemoActivity",
                                "Type: " + entry.getType());
                        Presence entryPresence = roster.getPresence(entry
                                .getUser());

                        Log.d("XMPPChatDemoActivity", "Presence Status: "
                                + entryPresence.getStatus());
                        Log.d("XMPPChatDemoActivity", "Presence Type: "
                                + entryPresence.getType());
                        Presence.Type type = entryPresence.getType();
                        if (type == Presence.Type.available)
                            Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                        Log.d("XMPPChatDemoActivity", "Presence : "
                                + entryPresence);

                    }
                } catch (XMPPException ex) {
                    Log.e("XMPPChatDemoActivity", "Failed to log in as "
                            + USERNAME);
                    Log.e("XMPPChatDemoActivity", ex.toString());
                    setConnection(null);
                }

                mDiaog.dismiss();
            }
        });
        t.start();
        mDiaog.show();
    }

}
