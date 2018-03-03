package com.clb.school.magicedit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MagicView1 extends View{

    private final String TAG = MagicView1.class.getSimpleName();

    private float downX;
    private float downY;
    private float firstX;
    private float firstY;
    private View.OnClickListener listener;
    private boolean clickable = true;
    private float whRatio;
    private int minWidth;
    private int minHeight;
    private int maxWidth;
    private int maxHeight;

    private float lastDis;//上一次两指的距离
    private float coreX;
    private float coreY;
    private boolean doubleMove = false;//双指滑动

    public MagicView1(Context context){
        this(context,null);
    }

    public MagicView1(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public MagicView1(Context context, AttributeSet attributeSet, int def){
        super(context,attributeSet,def);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    //当调用到这个方法的时候，说明子view和父view已经测量完毕，这时候就可以获取到宽高
    @Override
    public void onLayout(boolean changed,int left,int top,int right,int bottom){
        super.onLayout(changed,left,top,right,bottom);
        if(minWidth == 0){
            //算出宽高比
            whRatio = getWidth()*1f / getHeight();
            minWidth = getWidth() / 2;
            ViewGroup parent = (ViewGroup)getParent();
            maxWidth = parent.getWidth();//最大宽度为父view的宽度
            minHeight = getHeight() / 2;
            maxHeight = (int)(maxWidth / whRatio);
        }
    }

    //View copyView;//存储当前状态下的子view;
    float lastRota;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取触摸点相对于屏幕的坐标
                firstX = event.getRawX();
                firstY = event.getRawY();
                downX = event.getRawX();
                downY = event.getRawY();
                //获取view的中心点的坐标（相对于屏幕坐标系）
                coreX = getWidth() / 2 + getX();
                coreY = getHeight() / 2 + getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取手指触摸点的数量
                int pointerCount = event.getPointerCount();
                //两个手指触摸滑动事件
                if(pointerCount >= 2){
                    doubleMove = true;
                    //当前两指距离
                    float curDistance = getSlideDistance(event);
                    //当前角度
                    float curRotation = getRotation(event);

                    //放大缩小逻辑
                    int slide = (int)(lastDis - curDistance);
                    if(lastDis != 0){
                        slide = (int)((lastDis - curDistance)/whRatio);
                        //将view的宽高放大缩小
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.width = layoutParams.width - slide;
                        layoutParams.height = layoutParams.height - slide;
                        if(layoutParams.width > maxWidth || layoutParams.height > maxHeight){
                            layoutParams.width = maxWidth;
                            layoutParams.height = maxHeight;
                        }else if(layoutParams.width < minWidth || layoutParams.height < minHeight){
                            layoutParams.width = minWidth;
                            layoutParams.height = minHeight;
                        }
                        setLayoutParams(layoutParams);
                        float x = coreX - getWidth() / 2;
                        float y = coreY - getHeight() / 2;
                        setX(x);
                        setY(y);
                    }
                    if(lastRota != 0){
                        Log.i(TAG,"当前view的角度"+getRotation());
                        Log.i(TAG,"curRotation的角度"+curRotation);
                        Log.i(TAG,"lastRotation的角度"+lastRota);
                        float r = lastRota - curRotation;
                        setRotation(getRotation() - r);
                    }
                    //保存本次双指旋转的角度
                    lastRota = curRotation;
                    //保存本次双指之间的距离
                    lastDis = curDistance;
                }else if(!doubleMove && pointerCount == 1){//单击事件，移动view
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();
                    float slideX = moveX - downX;
                    float slideY = moveY - downY;
                    //设置view坐标
                    setX(getX() + slideX);
                    setY(getY() + slideY);
                    //保存移动后的位置
                    downX = moveX;
                    downY = moveY;
                }
                break;
            case MotionEvent.ACTION_UP:
                //恢复数据
                lastRota = 0;
                lastDis = 0;
                doubleMove = false;
                float upX = event.getRawX();
                float upY = event.getRawY();
                if(Math.abs(upX - firstX) < 10 && Math.abs(upY - firstY) < 10 && clickable){
                    if(listener != null){
                        listener.onClick(this);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 获取手指间的距离
     * @param event
     * @return
     */
    private float getSlideDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(( x * x ) + (y * y));
    }

    /**
     * 获取手指间的旋转角度
     * @param event
     * @return
     */
    private float getRotation(MotionEvent event){
        double deltaX = event.getX(0) - event.getX(1);
        double deltaY = event.getY(0) - event.getY(1);
        double radians = Math.atan2(deltaY,deltaX);
        return (float)Math.toDegrees(radians);
    }
}
