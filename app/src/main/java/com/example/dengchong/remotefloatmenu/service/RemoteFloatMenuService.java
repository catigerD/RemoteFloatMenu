package com.example.dengchong.remotefloatmenu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.RemoteViews;

import com.example.dengchong.remotefloatmenu.IRemoteFloatMenu;
import com.example.dengchong.remotefloatmenu.rx.RxBus;
import com.example.dengchong.remotefloatmenu.rx.RxEvent;
import com.example.dengchong.remotefloatmenu.util.LogUtil;
import com.example.dengchong.remotefloatmenu.widget.WindowConst;

public class RemoteFloatMenuService extends Service {
    private static final String TAG = "RemoteFloatMenuService";
    private WindowConst windowController;

    public RemoteFloatMenuService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new RemoteFloatMenuManager();
    }

    private class RemoteFloatMenuManager extends IRemoteFloatMenu.Stub {
        @Override
        public void addRemoteFloatMenu(final RemoteViews views) throws RemoteException {
            LogUtil.d(TAG + "--addRemoteFloatMenu--");
            RxBus.getInstance().post(views);
        }

        @Override
        public void changeImage() throws RemoteException {
            RxBus.getInstance().post(new RxEvent(RxEvent.CHANGEIMAGE));
        }
    }

    private Context getContext() {
        return RemoteFloatMenuService.this;
    }
}
