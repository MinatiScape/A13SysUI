package com.android.systemui.clipboardoverlay;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.screenshot.SwipeDismissHandler;
/* loaded from: classes.dex */
public class DraggableConstraintLayout extends ConstraintLayout {
    public Runnable mOnDismiss;
    public Runnable mOnInteraction;
    public final GestureDetector mSwipeDetector;
    public final SwipeDismissHandler mSwipeDismissHandler;

    public DraggableConstraintLayout(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        SwipeDismissHandler swipeDismissHandler = new SwipeDismissHandler(((ViewGroup) this).mContext, this, new SwipeDismissHandler.SwipeDismissCallbacks() { // from class: com.android.systemui.clipboardoverlay.DraggableConstraintLayout.1
            @Override // com.android.systemui.screenshot.SwipeDismissHandler.SwipeDismissCallbacks
            public final void onDismiss() {
                Runnable runnable = DraggableConstraintLayout.this.mOnDismiss;
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override // com.android.systemui.screenshot.SwipeDismissHandler.SwipeDismissCallbacks
            public final void onInteraction() {
                Runnable runnable = DraggableConstraintLayout.this.mOnInteraction;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        this.mSwipeDismissHandler = swipeDismissHandler;
        setOnTouchListener(swipeDismissHandler);
        GestureDetector gestureDetector = new GestureDetector(((ViewGroup) this).mContext, new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.clipboardoverlay.DraggableConstraintLayout.2
            public final Rect mActionsRect = new Rect();

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                View findViewById = DraggableConstraintLayout.this.findViewById(2131427456);
                findViewById.getBoundsOnScreen(this.mActionsRect);
                if (!this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !findViewById.canScrollHorizontally((int) f)) {
                    return true;
                }
                return false;
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mSwipeDismissHandler.onTouch(this, motionEvent);
        }
        return this.mSwipeDetector.onTouchEvent(motionEvent);
    }
}
