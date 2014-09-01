package com.seyda.st010.chatsample1;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;



import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by st010 on 01.09.2014.
 */
public class ServerConnection {

    public String HOST = "217.78.110.158";
    public int PORT = 5222;
    public String SERVICE = "localhost";
    public String USERNAME;
    public String PASSWORD;


    public ServerConnection(String host, int port, String service ){
        this.HOST = host;
        this.PORT = port;
        this.SERVICE = service;
    }

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static XMPPConnection connection;

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    public void connect() {

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
                setConnection(connection);
               if(USERNAME!=null){
                   try {
                       connection.login(USERNAME, PASSWORD);
                   } catch (XMPPException e) {
                       e.printStackTrace();
                   }
               }
            }
        });
        t.start();

    }
    public void Xmpplogin(String username, String password){

        USERNAME = username;
        PASSWORD = password;
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
    }
}