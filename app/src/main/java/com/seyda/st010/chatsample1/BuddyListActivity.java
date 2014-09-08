package com.seyda.st010.chatsample1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import java.util.ArrayList;
import java.util.Collection;


public class BuddyListActivity extends Activity {
    // Database Helper
    DbHelper db;
    ListView list;
    Roster roster;
    static Buddy[] buddyList;
    User users;
    Conversation conversation;
    Group group;
    ArrayList<Group> groups;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_buddy_list);

        Roster roster = ServerConnection.connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        //roster = AsmackTestingActivity.xmpp.getRoster();
        ArrayList<Buddy> buddies = new ArrayList<Buddy>();
        //Collection<RosterEntry> entries = roster.getEntries();

        buddyList = new Buddy[entries.size()];
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
        Buddy bud = null;
        VCard card = null;
        int i = 0;
        for (RosterEntry entry:entries) {
            bud = new Buddy();
            card = new VCard();
            try {
                card.load(ServerConnection.connection, entry.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }

            bud.jid = entry.getUser();
            bud.name = card.getField("FN");
            if (bud.name == null)
                bud.name = bud.jid;
            buddies.add(bud);
            byte[] img = card.getAvatar();
            if (img != null) {
                int len = img.length;
                bud.img = BitmapFactory.decodeByteArray(img, 0, len);
            }

            //ADD BUDDY TO DB
            users = new User();
            users.setUsername(bud.jid);
            Log.e("DB username=", users.getUsername());
            Presence entryPresence = roster.getPresence(bud.jid);
          //  users.setStatus(entryPresence.getStatus());

         //   Log.e("DB status=", users.getStatus());
             db = new DbHelper(BuddyListActivity.this);
            if(db.createUser(users))
                Log.e("DB", "database kayıt oldu");

            buddyList[i++] = bud;
        }

        User u= new User();
        u = db.getUser(2);

        Log.e("2. kayıtlı user username=", u.getUsername());
        ArrayList<User> allusers = new ArrayList<User>();
        allusers= db.getAllUsers();

        for ( i = 0; i < allusers.size(); i++) {
            String item = allusers.get(i).getUsername();
        Log.e("Item ", item);
    }
        db.closeDB();

        final BuddyAdapter adapter = new BuddyAdapter(this, buddies);
        list = (ListView) findViewById(R.id.buddiesList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //if conversation does not exist for
                conversation = new Conversation();
                conversation.setConversationId(db.getLastConversationId()+1);
                db.createConversation(conversation);
                groups = new ArrayList<Group>();
                group = new Group();
                group.setConversationId(conversation.getConversationId());
                String recipient = adapter.getItem(position).jid;
                group.setGroupMemberUserId(db.getUserId(recipient));
                groups.add(group);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, SplashActivity.MODE_PRIVATE);

                group.setGroupMemberUserId(db.getUserId(sharedpreferences.getString("username","")));
                groups.add(group);
                for(int i=0; i<groups.size(); i++)
                    db.createGroup(groups.get(i));

                Intent intent = new Intent(BuddyListActivity.this, chatActivity.class);
                intent.putExtra("recipient", adapter.getItem(position).jid); //jid
                intent.putExtra("conversationId", conversation.getConversationId());
                startActivity(intent);
            }
        });
    }
      //buraya bak
    class BuddyAdapter extends ArrayAdapter<Buddy> {
        public BuddyAdapter(Context context, ArrayList<Buddy> items) {
            super(context, R.layout.buddy, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentView) {
            Buddy bud = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.buddy, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.buddyName);
                holder.thumb = (ImageView) convertView.findViewById(R.id.buddyThumb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(bud.name);
            holder.thumb.setImageBitmap(bud.img);

            return convertView;
        }
    }
    class Buddy {
        String jid;
        String name;
        int status;
        Bitmap img;

    }
    class ViewHolder {
        ImageView thumb;
        TextView name;
    }
}
