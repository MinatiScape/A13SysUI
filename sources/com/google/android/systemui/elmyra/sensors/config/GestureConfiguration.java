package com.google.android.systemui.elmyra.sensors.config;

import android.content.Context;
import android.provider.Settings;
import android.util.Range;
import com.android.keyguard.clock.ClockManager$AvailableClocks$$ExternalSyntheticLambda0;
import com.android.systemui.DejankUtils;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda10;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1;
import com.google.android.systemui.elmyra.UserContentObserver;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class GestureConfiguration {
    public static final Range<Float> SENSITIVITY_RANGE = Range.create(Float.valueOf(0.0f), Float.valueOf(1.0f));
    public final ArrayList mAdjustments;
    public final Context mContext;
    public Listener mListener;
    public final BubbleController$$ExternalSyntheticLambda9 mAdjustmentCallback = new BubbleController$$ExternalSyntheticLambda9(this, 5);
    public float mSensitivity = getUserSensitivity();

    /* loaded from: classes.dex */
    public interface Listener {
        void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration);
    }

    public final float getSensitivity() {
        float f = this.mSensitivity;
        for (int i = 0; i < this.mAdjustments.size(); i++) {
            f = SENSITIVITY_RANGE.clamp(Float.valueOf(((Adjustment) this.mAdjustments.get(i)).adjustSensitivity(f))).floatValue();
        }
        return f;
    }

    public final float getUserSensitivity() {
        float floatValue = ((Float) DejankUtils.whitelistIpcs(new ClockManager$AvailableClocks$$ExternalSyntheticLambda0(this, 1))).floatValue();
        if (!SENSITIVITY_RANGE.contains((Range<Float>) Float.valueOf(floatValue))) {
            return 0.5f;
        }
        return floatValue;
    }

    public GestureConfiguration(Context context, ArrayList arrayList) {
        this.mContext = context;
        ArrayList arrayList2 = new ArrayList(arrayList);
        this.mAdjustments = arrayList2;
        arrayList2.forEach(new PipMotionHelper$$ExternalSyntheticLambda1(this, 6));
        new UserContentObserver(context, Settings.Secure.getUriFor("assist_gesture_sensitivity"), new BubbleController$$ExternalSyntheticLambda10(this, 4), true);
    }
}
