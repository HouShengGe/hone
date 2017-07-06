package com.miaolian.facead.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

/**
 * Created by gaofeng on 2017-03-16.
 */

public class MyExpandableView extends LinearLayout {
    private static final String TAG = MyExpandableView.class.getSimpleName();

    private View subView;
    private int originalHeight = -1;
    private int subViewMeasuredHeight = -1;
    private boolean collapsed;
    volatile private boolean animating;

    public MyExpandableView(Context context) {
        super(context);
    }

    public MyExpandableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        subView = getChildAt(1);
        subView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subViewMeasuredHeight = subView.getMeasuredHeight();
        collapsed = true;
        animating = false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (originalHeight == -1) {
            originalHeight = getMeasuredHeight()-(subView.getVisibility()==GONE?0:subViewMeasuredHeight);
        }
    }

    public void expand(boolean animated) {
        if (animating) return;
        collapsed = false;
        if (animated == false) {
            subView.setVisibility(VISIBLE);
            return;
        }


        animating = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + subViewMeasuredHeight).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().height = (Integer) animation.getAnimatedValue();
                requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                subView.setAlpha(0);
                subView.setVisibility(VISIBLE);
                ViewPropertyAnimator viewPropertyAnimator = subView.animate().alpha(1).setDuration(300);
                viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                viewPropertyAnimator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();
    }

    public void collapse(boolean animated) {
        if (animating) return;
        collapsed = true;
        if (animated == false) {
            subView.setVisibility(GONE);
            return;
        }
        animating = true;
        subView.setAlpha(1);
        ViewPropertyAnimator viewPropertyAnimator = subView.animate().alpha(0).setDuration(300);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(originalHeight + subViewMeasuredHeight, originalHeight).setDuration(300);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        getLayoutParams().height = (Integer) animation.getAnimatedValue();
                        requestLayout();
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        subView.setVisibility(GONE);
                        animating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        viewPropertyAnimator.start();

    }

    public boolean isCollpased() {
        return collapsed;
    }


}
