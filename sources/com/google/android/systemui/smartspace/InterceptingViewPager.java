package com.google.android.systemui.smartspace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.viewpager.widget.ViewPager;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class InterceptingViewPager extends ViewPager {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mHasPerformedLongPress;
    public boolean mHasPostedLongPress;
    public final Runnable mLongPressCallback;
    public final EventProxy mSuperOnIntercept;
    public final EventProxy mSuperOnTouch;

    /* loaded from: classes.dex */
    public interface EventProxy {
        boolean delegateEvent(MotionEvent motionEvent);
    }

    public InterceptingViewPager(Context context) {
        super(context);
        this.mSuperOnTouch = new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(this);
        this.mSuperOnIntercept = new CHREGestureSensor$$ExternalSyntheticLambda0(this);
        this.mLongPressCallback = new WMShell$7$$ExternalSyntheticLambda2(this, 8);
    }

    public final void cancelScheduledLongPress() {
        if (this.mHasPostedLongPress) {
            this.mHasPostedLongPress = false;
            removeCallbacks(this.mLongPressCallback);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnIntercept);
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnTouch);
    }

    public static /* synthetic */ boolean $r8$lambda$3SWsnYuFjnqtbymqfR4U1UuMdzc(InterceptingViewPager interceptingViewPager, MotionEvent motionEvent) {
        Objects.requireNonNull(interceptingViewPager);
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* renamed from: $r8$lambda$Kinb8UkpjhBhKntC-OQxRM-Ndiw */
    public static /* synthetic */ boolean m173$r8$lambda$Kinb8UkpjhBhKntCOQxRMNdiw(InterceptingViewPager interceptingViewPager, MotionEvent motionEvent) {
        Objects.requireNonNull(interceptingViewPager);
        return super.onTouchEvent(motionEvent);
    }

    public final boolean handleTouchOverride(MotionEvent motionEvent, EventProxy eventProxy) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mHasPerformedLongPress = false;
            if (isLongClickable()) {
                cancelScheduledLongPress();
                this.mHasPostedLongPress = true;
                postDelayed(this.mLongPressCallback, ViewConfiguration.getLongPressTimeout());
            }
        } else if (action == 1 || action == 3) {
            cancelScheduledLongPress();
        }
        if (this.mHasPerformedLongPress) {
            cancelScheduledLongPress();
            return true;
        } else if (!eventProxy.delegateEvent(motionEvent)) {
            return false;
        } else {
            cancelScheduledLongPress();
            return true;
        }
    }

    public InterceptingViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSuperOnTouch = new InterceptingViewPager$$ExternalSyntheticLambda0(this);
        this.mSuperOnIntercept = new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2(this);
        this.mLongPressCallback = new PipTaskOrganizer$$ExternalSyntheticLambda3(this, 7);
    }
}
