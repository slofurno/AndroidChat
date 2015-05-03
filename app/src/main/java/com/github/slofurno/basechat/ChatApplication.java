package com.github.slofurno.basechat;

import android.app.Application;
import android.content.Intent;

/**
 * Created by slofurno on 5/3/2015.
 */
public class ChatApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Intent intent=new Intent(this, ChatService.class);

        startService(intent);

    }

}
