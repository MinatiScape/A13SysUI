package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FalsingDataProvider {
    public BatteryController mBatteryController;
    public final DockManager mDockManager;
    public MotionEvent mFirstRecentMotionEvent;
    public final int mHeightPixels;
    public boolean mJustUnlockedWithFace;
    public MotionEvent mLastMotionEvent;
    public final int mWidthPixels;
    public final float mXdpi;
    public final float mYdpi;
    public final ArrayList mSessionListeners = new ArrayList();
    public final ArrayList mMotionEventListeners = new ArrayList();
    public final ArrayList mGestureFinalizedListeners = new ArrayList();
    public TimeLimitedMotionEventBuffer mRecentMotionEvents = new TimeLimitedMotionEventBuffer();
    public List<MotionEvent> mPriorMotionEvents = new ArrayList();
    public boolean mDirty = true;
    public float mAngle = 0.0f;

    /* loaded from: classes.dex */
    public interface GestureFinalizedListener {
        void onGestureFinalized(long j);
    }

    /* loaded from: classes.dex */
    public interface MotionEventListener {
        void onMotionEvent(MotionEvent motionEvent);
    }

    /* loaded from: classes.dex */
    public interface SessionListener {
        void onSessionEnded();

        void onSessionStarted();
    }

    public final void completePriorGesture() {
        if (!this.mRecentMotionEvents.isEmpty()) {
            this.mGestureFinalizedListeners.forEach(new DozeTriggers$$ExternalSyntheticLambda3(this, 1));
            this.mPriorMotionEvents = this.mRecentMotionEvents;
            this.mRecentMotionEvents = new TimeLimitedMotionEventBuffer();
        }
    }

    public final void onMotionEvent(MotionEvent motionEvent) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int pointerCount = motionEvent.getPointerCount();
        int i = 0;
        for (int i2 = 0; i2 < pointerCount; i2++) {
            MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
            motionEvent.getPointerProperties(i2, pointerProperties);
            arrayList2.add(pointerProperties);
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = new MotionEvent.PointerProperties[arrayList2.size()];
        arrayList2.toArray(pointerPropertiesArr);
        int historySize = motionEvent.getHistorySize();
        int i3 = 0;
        while (i3 < historySize) {
            ArrayList arrayList3 = new ArrayList();
            for (int i4 = i; i4 < pointerCount; i4++) {
                MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
                motionEvent.getHistoricalPointerCoords(i4, i3, pointerCoords);
                arrayList3.add(pointerCoords);
            }
            arrayList.add(MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getHistoricalEventTime(i3), motionEvent.getAction(), pointerCount, pointerPropertiesArr, (MotionEvent.PointerCoords[]) arrayList3.toArray(new MotionEvent.PointerCoords[i]), motionEvent.getMetaState(), motionEvent.getButtonState(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getFlags()));
            i3++;
            pointerPropertiesArr = pointerPropertiesArr;
            i = i;
            pointerCount = pointerCount;
        }
        arrayList.add(MotionEvent.obtainNoHistory(motionEvent));
        BrightLineFalsingManager.logDebug("Unpacked into: " + arrayList.size());
        if (BrightLineFalsingManager.DEBUG) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                MotionEvent motionEvent2 = (MotionEvent) it.next();
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("x,y,t: ");
                m.append(motionEvent2.getX());
                m.append(",");
                m.append(motionEvent2.getY());
                m.append(",");
                m.append(motionEvent2.getEventTime());
                BrightLineFalsingManager.logDebug(m.toString());
            }
        }
        if (motionEvent.getActionMasked() == 0) {
            completePriorGesture();
        }
        this.mRecentMotionEvents.addAll(arrayList);
        BrightLineFalsingManager.logDebug("Size: " + this.mRecentMotionEvents.size());
        this.mMotionEventListeners.forEach(new Monitor$$ExternalSyntheticLambda2(motionEvent, 1));
        this.mDirty = true;
    }

    public final void recalculateData() {
        if (this.mDirty) {
            if (this.mRecentMotionEvents.isEmpty()) {
                this.mFirstRecentMotionEvent = null;
                this.mLastMotionEvent = null;
            } else {
                TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = this.mRecentMotionEvents;
                Objects.requireNonNull(timeLimitedMotionEventBuffer);
                this.mFirstRecentMotionEvent = timeLimitedMotionEventBuffer.mMotionEvents.get(0);
                TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer2 = this.mRecentMotionEvents;
                this.mLastMotionEvent = timeLimitedMotionEventBuffer2.mMotionEvents.get(timeLimitedMotionEventBuffer2.size() - 1);
            }
            if (this.mRecentMotionEvents.size() >= 2) {
                this.mAngle = (float) Math.atan2(this.mLastMotionEvent.getY() - this.mFirstRecentMotionEvent.getY(), this.mLastMotionEvent.getX() - this.mFirstRecentMotionEvent.getX());
                while (true) {
                    float f = this.mAngle;
                    if (f < 0.0f) {
                        this.mAngle = f + 6.2831855f;
                    }
                }
                while (true) {
                    float f2 = this.mAngle;
                    if (f2 <= 6.2831855f) {
                        break;
                    }
                    this.mAngle = f2 - 6.2831855f;
                }
            } else {
                this.mAngle = Float.MAX_VALUE;
            }
            this.mDirty = false;
        }
    }

    public FalsingDataProvider(DisplayMetrics displayMetrics, BatteryController batteryController, DockManager dockManager) {
        float f = displayMetrics.xdpi;
        this.mXdpi = f;
        float f2 = displayMetrics.ydpi;
        this.mYdpi = f2;
        int i = displayMetrics.widthPixels;
        this.mWidthPixels = i;
        int i2 = displayMetrics.heightPixels;
        this.mHeightPixels = i2;
        this.mBatteryController = batteryController;
        this.mDockManager = dockManager;
        BrightLineFalsingManager.logInfo("xdpi, ydpi: " + f + ", " + f2);
        BrightLineFalsingManager.logInfo("width, height: " + i + ", " + i2);
    }

    public final boolean isHorizontal() {
        recalculateData();
        if (!this.mRecentMotionEvents.isEmpty() && Math.abs(this.mFirstRecentMotionEvent.getX() - this.mLastMotionEvent.getX()) > Math.abs(this.mFirstRecentMotionEvent.getY() - this.mLastMotionEvent.getY())) {
            return true;
        }
        return false;
    }
}
