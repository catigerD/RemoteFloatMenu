package com.example.dengchong.remotefloatmenu.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.dengchong.remotefloatmenu.util.LogUtil;

public class FloatMenu extends RelativeLayout {
    private static final String TAG = "FloatMenu";

    private float moveRawX;
    private float moveRawY;
    private float downRawX;
    private float downRawY;
    private float downX;
    private float downY;
    private boolean isMove;
    private int floatMenuHeight = 500;
    private int floatMenuWeight = 500;
    private int minMoveInstance;
    private Context context;
    private WindowManager.LayoutParams wmParams;
    private WindowManager windowManager;

    private OnFloatMenuClickedListener listener;

    public FloatMenu(Context context) {
        super(context);
        this.context = context;
        minMoveInstance = ViewConfiguration.get(context).getScaledTouchSlop();
        initWMLayoutParams();
    }

    private void initWMLayoutParams() {
        wmParams = WindowConst.lpFloatMenu;
        /**
         *以下都是WindowManager.LayoutParams的相关属性
         * 具体用途可参考SDK文档
         */
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;   //设置window type
        wmParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

        //设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角，便于调整坐标
        //以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = floatMenuWeight;
        wmParams.height = floatMenuHeight;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveRawX = event.getRawX();
        moveRawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downRawX = moveRawX;
                downRawY = moveRawY;
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float instanceX = Math.abs(moveRawX - downRawX);
                float instanceY = Math.abs(moveRawY - downRawY);
                float instance = (float) Math.sqrt(instanceX * instanceX + instanceY * instanceY);
                if (instance > minMoveInstance) {
                    isMove = true;
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    downX = downY = 0;
                } else {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
                isMove = false;
                break;
        }
        return true;
    }

    private void updateViewPosition() {
        LogUtil.d(TAG + "--updateViewPosition: --wmParams.x : " + wmParams.x
            + "--wmParams.y: " + wmParams.y);
        wmParams.x = (int) (moveRawX - downX);
        wmParams.y = (int) (moveRawY - downY);
        windowManager.updateViewLayout(this, wmParams);
    }

    public void setOnFloatMenuClickedListener(OnFloatMenuClickedListener listener) {
        this.listener = listener;
    }

    public interface OnFloatMenuClickedListener {
        void onClick();
    }
}
