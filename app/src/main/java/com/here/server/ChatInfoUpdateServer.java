package com.here.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChatInfoUpdateServer extends Service {
    public ChatInfoUpdateServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
