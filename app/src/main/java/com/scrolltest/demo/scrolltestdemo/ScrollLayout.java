package com.scrolltest.demo.scrolltestdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @ Creator     :     chenchao
 * @ CreateDate  :     2018/5/14 0014 15:15
 * @ Description :     外部拦截滑动冲突
 */

public class ScrollLayout extends ViewGroup {

    private Scroller mScroller;
    private int      mLeftBorder;
    private int      mRightBorder;
    private float    mXDown;
    private float    mXLastMove=0;
    private int      mLastXIntercept;
    private int      mLastYIntercept;
    private float mXMove;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //创建一个scroll对象
        mScroller = new Scroller(context);
    }

    //重写viewGroup的拦截方法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercept = true;
                }
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动事件
                mXMove = ev.getRawX();
                mXLastMove = mXDown;
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                //判断如果是左右滑动则拦截事件
                if (Math.abs(deltaX) > Math.abs(deltaY)) {//X方向滑动
                    intercept = true;
                } else {
                    intercept = false;
                }
                //每次move滑动完成后都重新赋值，达到view跟随手指滑动
                mLastXIntercept = x;
                mLastYIntercept = y;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
//        //每次move滑动完成后都重新赋值，达到view跟随手指滑动
//        mLastXIntercept = x;
//        mLastYIntercept = y;
        return intercept;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                //摆放每一个子view的布局
                childAt.layout(i * childAt.getMeasuredWidth(), 0, (i + 1) * childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
            //初始化左右边界值
            mLeftBorder = getChildAt(0).getLeft();
            mRightBorder = getChildAt(getChildCount() - 1).getRight();
        }
    }
    //重写viewGroup的ontouchEvent

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                Log.d("xxx","mXLastMove=="+mXLastMove);
                int scrolledX  = (int) (mXLastMove - mXMove);
                //判断边界
                if(getScrollX()+scrolledX<mLeftBorder){
                    scrollTo(mLeftBorder,0);
                    return true;
                }else if(getScrollX()+scrolledX+getWidth()>mRightBorder){
                    scrollTo(mRightBorder-getWidth(),0);
                    return true;
                }
                scrollBy(scrolledX,0);
                mXLastMove=mXMove;
                break;
            case MotionEvent.ACTION_UP:
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(),0,dx,0);
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量每一个子view的大小
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
    }
}
