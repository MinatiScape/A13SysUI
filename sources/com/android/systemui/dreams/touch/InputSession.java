package com.android.systemui.dreams.touch;

import android.os.Looper;
import android.view.Choreographer;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.google.android.setupcompat.util.Logger;
import java.util.Objects;
/* loaded from: classes.dex */
public final class InputSession {
    public final GestureDetector mGestureDetector;
    public final InputChannelCompat$InputEventReceiver mInputEventReceiver;
    public final Logger mInputMonitor;

    public InputSession(String str, final InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, GestureDetector.OnGestureListener onGestureListener, final boolean z) {
        Logger logger = new Logger(str, 0);
        this.mInputMonitor = logger;
        this.mGestureDetector = new GestureDetector(onGestureListener);
        this.mInputEventReceiver = logger.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.dreams.touch.InputSession$$ExternalSyntheticLambda0
            @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
            public final void onInputEvent(InputEvent inputEvent) {
                InputSession inputSession = InputSession.this;
                InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener2 = inputChannelCompat$InputEventListener;
                boolean z2 = z;
                Objects.requireNonNull(inputSession);
                inputChannelCompat$InputEventListener2.onInputEvent(inputEvent);
                if ((inputEvent instanceof MotionEvent) && inputSession.mGestureDetector.onTouchEvent((MotionEvent) inputEvent) && z2) {
                    Logger logger2 = inputSession.mInputMonitor;
                    Objects.requireNonNull(logger2);
                    ((InputMonitor) logger2.prefix).pilferPointers();
                }
            }
        });
    }
}
