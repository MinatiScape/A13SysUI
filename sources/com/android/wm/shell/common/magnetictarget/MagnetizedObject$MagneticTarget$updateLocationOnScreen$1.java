package com.android.wm.shell.common.magnetictarget;

import android.graphics.PointF;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.util.Objects;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes.dex */
public final class MagnetizedObject$MagneticTarget$updateLocationOnScreen$1 implements Runnable {
    public final /* synthetic */ MagnetizedObject.MagneticTarget this$0;

    public MagnetizedObject$MagneticTarget$updateLocationOnScreen$1(MagnetizedObject.MagneticTarget magneticTarget) {
        this.this$0 = magneticTarget;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MagnetizedObject.MagneticTarget magneticTarget = this.this$0;
        Objects.requireNonNull(magneticTarget);
        magneticTarget.targetView.getLocationOnScreen(this.this$0.tempLoc);
        MagnetizedObject.MagneticTarget magneticTarget2 = this.this$0;
        Objects.requireNonNull(magneticTarget2);
        PointF pointF = magneticTarget2.centerOnScreen;
        MagnetizedObject.MagneticTarget magneticTarget3 = this.this$0;
        Objects.requireNonNull(magneticTarget3);
        float width = (magneticTarget3.targetView.getWidth() / 2.0f) + magneticTarget3.tempLoc[0];
        MagnetizedObject.MagneticTarget magneticTarget4 = this.this$0;
        Objects.requireNonNull(magneticTarget4);
        float translationX = width - magneticTarget4.targetView.getTranslationX();
        MagnetizedObject.MagneticTarget magneticTarget5 = this.this$0;
        Objects.requireNonNull(magneticTarget5);
        float height = magneticTarget5.targetView.getHeight() / 2.0f;
        MagnetizedObject.MagneticTarget magneticTarget6 = this.this$0;
        Objects.requireNonNull(magneticTarget6);
        pointF.set(translationX, (height + magneticTarget5.tempLoc[1]) - magneticTarget6.targetView.getTranslationY());
    }
}
