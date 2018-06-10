package com.example.dengchong.remotefloatmenu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dengchong.remotefloatmenu.R;
import com.example.dengchong.remotefloatmenu.rx.RxBus;
import com.example.dengchong.remotefloatmenu.rx.RxEvent;
import com.example.dengchong.remotefloatmenu.util.LogUtil;
import com.example.dengchong.remotefloatmenu.widget.FloatMenu;
import com.example.dengchong.remotefloatmenu.widget.WindowConst;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static WindowManager windowManager;
    private View remoteView;
    private Disposable addRemoteDisposable;
    private Disposable changeImgDisposable;
    private FloatMenu floatMenu;
    private boolean isAddtoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Button btn2SecActivity = findViewById(R.id.btn_to_second);
        btn2SecActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start2SecActivity();
            }
        });
        addRemoteDisposable = RxBus.getInstance().doSubscribe(RemoteViews.class,
                new Consumer<RemoteViews>() {
                    @Override
                    public void accept(RemoteViews views) throws Exception {
                        LogUtil.d(TAG + "RxBus : next");
                        if (isAddtoWindow) {
                            return;
                        }
                        floatMenu = new FloatMenu(MainActivity.this);
                        floatMenu.setOnFloatMenuClickedListener(new FloatMenu.OnFloatMenuClickedListener() {
                            @Override
                            public void onClick() {
                                Toast.makeText(MainActivity.this,
                                        "clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                        remoteView = views.apply(MainActivity.this, floatMenu);
                        floatMenu.addView(remoteView);
                        windowManager.addView(floatMenu, WindowConst.lpFloatMenu);
                        isAddtoWindow = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        changeImgDisposable = RxBus.getInstance().doSubscribe(RxEvent.class, new Consumer<RxEvent>() {
            @Override
            public void accept(RxEvent rxEvent) throws Exception {
                if (RxEvent.CHANGEIMAGE.equals(rxEvent.getTag())) {
                    LogUtil.d(TAG + "change_img");
                    if (remoteView != null) {
                        ((ImageView) remoteView).setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void start2SecActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isAddtoWindow) {
            windowManager.removeView(floatMenu);
        }
        addRemoteDisposable.dispose();
        changeImgDisposable.dispose();
    }
}
