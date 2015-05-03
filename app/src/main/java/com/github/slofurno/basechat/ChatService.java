package com.github.slofurno.basechat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ChatService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private NotificationManager mNotificationManager;
    Firebase root;
    Firebase messages;
    List<ChatMessage> messageStore = new ArrayList<ChatMessage>();
    List<String> recentMessages = new ArrayList<String>();
    int numMessages;

    NotificationCompat.Builder mNotifyBuilder;

    @Override
    public void onCreate() {
        // The service is being created
        Firebase.setAndroidContext(this);
        root = new Firebase("https://messagequeue.firebaseio.com/");

        messages = root.child("messages");
        Firebase users = root.child("users");

        final Bus bus = OttoBus.getInstance().getBus();
        numMessages = 0;

        mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        mNotifyBuilder  = new NotificationCompat.Builder(this)
                .setContentTitle("Base Chat")
                .setContentText("Connected")
                .setSmallIcon(R.drawable.ni_basechat)
                .setAutoCancel(true);


        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mNotifyBuilder.setContentIntent(resultPendingIntent);

        startForeground(555,mNotifyBuilder.build());



        messages.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                ChatMessage message = new ChatMessage("name", snapshot.getValue().toString());
                List<ChatMessage> m = new ArrayList<ChatMessage>();

                messageStore.add(message);
                m.add(message);

                bus.post(new ReceiveMessageEvent(m));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });
        bus.register(this);
    }

    @Produce
    public ReceiveMessageEvent getExistingMessages() {

        numMessages = 0;

        List<ChatMessage> m = new ArrayList<ChatMessage>();
        for(ChatMessage message : messageStore){
            m.add(message);
        }

        return new ReceiveMessageEvent(m);
    }

    @Subscribe
    public void sendChatMessage(SendMessageEvent event){

        Firebase push = messages.push();

        push.setValue(event.getMessage().Message);

    }

    @Subscribe
    public void updateNotification(DeadEvent event){

        numMessages++;
        NotificationCompat.InboxStyle nextInboxStyle =
                new NotificationCompat.InboxStyle(mNotifyBuilder);

        nextInboxStyle.setBigContentTitle("Base Chat");
        nextInboxStyle.setSummaryText(numMessages + " new messages");

        int lastMessage = messageStore.size()-1;

        for(int i = 0; i<5 && i<numMessages;i++ ){
            nextInboxStyle.addLine(messageStore.get(lastMessage-i).Message);
        }

        mNotificationManager.notify(
                555,
                nextInboxStyle.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
       // connect();

        return(START_NOT_STICKY);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }

    public void connect(){

    }

}
