package com.google.android.systemui.assist.uihints.input;

import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.util.Log;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda2;
import com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda2;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class NgaInputHandler implements NgaMessageHandler.EdgeLightsInfoListener {
    public NgaInputEventReceiver mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public final Set<TouchActionRegion> mTouchActionRegions;
    public final TouchInsideHandler mTouchInsideHandler;
    public final Set<TouchInsideRegion> mTouchInsideRegions;

    /* loaded from: classes.dex */
    public class NgaInputEventReceiver extends InputEventReceiver {
        public NgaInputEventReceiver(InputChannel inputChannel) {
            super(inputChannel, Looper.getMainLooper());
        }

        public final void onInputEvent(InputEvent inputEvent) {
            if (inputEvent instanceof MotionEvent) {
                NgaInputHandler ngaInputHandler = NgaInputHandler.this;
                MotionEvent motionEvent = (MotionEvent) inputEvent;
                Objects.requireNonNull(ngaInputHandler);
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                Region region = new Region();
                for (TouchInsideRegion touchInsideRegion : ngaInputHandler.mTouchInsideRegions) {
                    touchInsideRegion.getTouchInsideRegion().ifPresent(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda2(region, 3));
                }
                for (TouchActionRegion touchActionRegion : ngaInputHandler.mTouchActionRegions) {
                    touchActionRegion.getTouchActionRegion().ifPresent(new PipMediaController$$ExternalSyntheticLambda2(region, 2));
                }
                if (region.contains(rawX, rawY)) {
                    ngaInputHandler.mTouchInsideHandler.onTouchInside();
                }
            }
            finishInputEvent(inputEvent, false);
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.EdgeLightsInfoListener
    public final void onEdgeLightsInfo(String str, boolean z) {
        if (!"HALF_LISTENING".equals(str)) {
            NgaInputEventReceiver ngaInputEventReceiver = this.mInputEventReceiver;
            if (ngaInputEventReceiver != null) {
                ngaInputEventReceiver.dispose();
                this.mInputEventReceiver = null;
            }
            InputMonitor inputMonitor = this.mInputMonitor;
            if (inputMonitor != null) {
                inputMonitor.dispose();
                this.mInputMonitor = null;
            }
        } else if (this.mInputEventReceiver == null && this.mInputMonitor == null) {
            this.mInputMonitor = InputManager.getInstance().monitorGestureInput("NgaInputHandler", 0);
            this.mInputEventReceiver = new NgaInputEventReceiver(this.mInputMonitor.getInputChannel());
        } else {
            Log.w("NgaInputHandler", "Already monitoring");
        }
    }

    public NgaInputHandler(TouchInsideHandler touchInsideHandler, Set<TouchActionRegion> set, Set<TouchInsideRegion> set2) {
        this.mTouchInsideHandler = touchInsideHandler;
        this.mTouchActionRegions = set;
        this.mTouchInsideRegions = set2;
    }
}
