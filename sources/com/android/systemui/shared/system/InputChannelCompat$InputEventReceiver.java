package com.android.systemui.shared.system;

import android.os.Looper;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
/* loaded from: classes.dex */
public final class InputChannelCompat$InputEventReceiver {
    public final AnonymousClass1 mReceiver;

    public InputChannelCompat$InputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer, final InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener) {
        this.mReceiver = new BatchedInputEventReceiver(inputChannel, looper, choreographer) { // from class: com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver.1
            public final void onInputEvent(InputEvent inputEvent) {
                inputChannelCompat$InputEventListener.onInputEvent(inputEvent);
                finishInputEvent(inputEvent, true);
            }
        };
    }
}
