package com.example.dengchong.remotefloatmenu.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dengchong.remotefloatmenu.IRemoteFloatMenu;
import com.example.dengchong.remotefloatmenu.R;
import com.example.dengchong.remotefloatmenu.service.RemoteFloatMenuService;
import com.example.dengchong.remotefloatmenu.util.LogUtil;

// ：remote进程
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemoteFloatMenu = IRemoteFloatMenu.Stub.asInterface(service);
            Toast.makeText(SecondActivity.this, "bindsuc", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IRemoteFloatMenu iRemoteFloatMenu;
    private boolean isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btnAddRemote = findViewById(R.id.btn_add_remote);
        btnAddRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRemoteFloatMenu != null) {
                    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.win_float_menu);
                    LogUtil.d(TAG + "--remoteViews : " + remoteViews);
                    try {
                        iRemoteFloatMenu.addRemoteFloatMenu(remoteViews);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button btnBindService = findViewById(R.id.btn_bind_remote);
        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });
        Button btnChangeImg = findViewById(R.id.btn_change_img);
        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRemoteFloatMenu != null) {
                    try {
                        LogUtil.d(TAG + "--changeImage");
                        iRemoteFloatMenu.changeImage();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void bindService() {
        Intent intent = new Intent(this, RemoteFloatMenuService.class);
        isBind = bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(conn);
        }
    }
}
