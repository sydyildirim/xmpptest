package com.seyda.st010.chatsample1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import java.util.ArrayList;
import java.util.Collection;


public class BuddyListActivity extends Activity {
    ListView list;
    Roster roster;
    static Buddy[] buddyList;

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
            buddyList[i++] = bud;
        }


        final BuddyAdapter adapter = new BuddyAdapter(this, buddies);
        list = (ListView) findViewById(R.id.buddiesList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BuddyListActivity.this, chatActivity.class);
                intent.putExtra("recipient", adapter.getItem(position).jid); //jid
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
