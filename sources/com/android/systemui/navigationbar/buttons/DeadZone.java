package com.android.systemui.navigationbar.buttons;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.FloatProperty;
import android.util.Slog;
import android.view.MotionEvent;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DeadZone {
    public static final AnonymousClass1 FLASH_PROPERTY = new FloatProperty<DeadZone>() { // from class: com.android.systemui.navigationbar.buttons.DeadZone.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            DeadZone deadZone = (DeadZone) obj;
            Objects.requireNonNull(deadZone);
            return Float.valueOf(deadZone.mFlashFrac);
        }

        @Override // android.util.FloatProperty
        public final void setValue(DeadZone deadZone, float f) {
            DeadZone deadZone2 = deadZone;
            Objects.requireNonNull(deadZone2);
            deadZone2.mFlashFrac = f;
            deadZone2.mNavigationBarView.postInvalidate();
        }
    };
    public int mDecay;
    public final int mDisplayId;
    public int mDisplayRotation;
    public int mHold;
    public long mLastPokeTime;
    public final NavigationBarView mNavigationBarView;
    public boolean mShouldFlash;
    public int mSizeMax;
    public int mSizeMin;
    public boolean mVertical;
    public float mFlashFrac = 0.0f;
    public final AnonymousClass2 mDebugFlash = new Runnable() { // from class: com.android.systemui.navigationbar.buttons.DeadZone.2
        @Override // java.lang.Runnable
        public final void run() {
            ObjectAnimator.ofFloat(DeadZone.this, DeadZone.FLASH_PROPERTY, 1.0f, 0.0f).setDuration(150L).start();
        }
    };
    public final NavigationBarController mNavBarController = (NavigationBarController) Dependency.get(NavigationBarController.class);

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (motionEvent.getToolType(0) == 3) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 4) {
            this.mLastPokeTime = motionEvent.getEventTime();
            if (this.mShouldFlash) {
                this.mNavigationBarView.postInvalidate();
            }
            return true;
        }
        if (action == 0) {
            this.mNavBarController.touchAutoDim(this.mDisplayId);
            int size = (int) getSize(motionEvent.getEventTime());
            if (!this.mVertical ? motionEvent.getY() >= size : this.mDisplayRotation != 3 ? motionEvent.getX() >= size : motionEvent.getX() <= this.mNavigationBarView.getWidth() - size) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("consuming errant click: (");
                m.append(motionEvent.getX());
                m.append(",");
                m.append(motionEvent.getY());
                m.append(")");
                Slog.v("DeadZone", m.toString());
                if (this.mShouldFlash) {
                    this.mNavigationBarView.post(this.mDebugFlash);
                    this.mNavigationBarView.postInvalidate();
                }
                return true;
            }
        }
        return false;
    }

    public final float getSize(long j) {
        int i;
        int i2 = this.mSizeMax;
        if (i2 == 0) {
            return 0.0f;
        }
        long j2 = j - this.mLastPokeTime;
        int i3 = this.mHold;
        int i4 = this.mDecay;
        if (j2 > i3 + i4) {
            i = this.mSizeMin;
        } else if (j2 < i3) {
            return i2;
        } else {
            float f = i2;
            i = (int) MotionController$$ExternalSyntheticOutline0.m(this.mSizeMin, f, ((float) (j2 - i3)) / i4, f);
        }
        return i;
    }

    public final void onConfigurationChanged(int i) {
        this.mDisplayRotation = i;
        Resources resources = this.mNavigationBarView.getResources();
        this.mHold = resources.getInteger(2131493020);
        this.mDecay = resources.getInteger(2131493019);
        this.mSizeMin = resources.getDimensionPixelSize(2131166591);
        this.mSizeMax = resources.getDimensionPixelSize(2131166592);
        boolean z = true;
        if (resources.getInteger(2131493021) != 1) {
            z = false;
        }
        this.mVertical = z;
        this.mShouldFlash = resources.getBoolean(2131034122);
        this.mFlashFrac = 0.0f;
        this.mNavigationBarView.postInvalidate();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.navigationbar.buttons.DeadZone$2] */
    public DeadZone(NavigationBarView navigationBarView) {
        this.mNavigationBarView = navigationBarView;
        this.mDisplayId = navigationBarView.getContext().getDisplayId();
        onConfigurationChanged(0);
    }
}
