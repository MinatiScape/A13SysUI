package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.google.android.setupcompat.util.Logger;
import com.google.android.systemui.columbus.gates.Gate;
import java.util.Objects;
/* compiled from: ScreenTouch.kt */
/* loaded from: classes.dex */
public final class ScreenTouch extends Gate {
    public final Handler handler;
    public InputChannelCompat$InputEventReceiver inputEventReceiver;
    public Logger inputMonitor;
    public final PowerState powerState;
    public final RectF touchRegion;
    public final ScreenTouch$gateListener$1 gateListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.gates.ScreenTouch$gateListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            if (ScreenTouch.this.powerState.isBlocking()) {
                ScreenTouch screenTouch = ScreenTouch.this;
                Objects.requireNonNull(screenTouch);
                InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = screenTouch.inputEventReceiver;
                if (inputChannelCompat$InputEventReceiver != null) {
                    inputChannelCompat$InputEventReceiver.mReceiver.dispose();
                }
                screenTouch.inputEventReceiver = null;
                Logger logger = screenTouch.inputMonitor;
                if (logger != null) {
                    ((InputMonitor) logger.prefix).dispose();
                }
                screenTouch.inputMonitor = null;
                return;
            }
            ScreenTouch.this.startListeningForTouch();
        }
    };
    public final ScreenTouch$inputEventListener$1 inputEventListener = new InputChannelCompat$InputEventListener() { // from class: com.google.android.systemui.columbus.gates.ScreenTouch$inputEventListener$1
        @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
        public final void onInputEvent(InputEvent inputEvent) {
            MotionEvent motionEvent;
            boolean z;
            if (inputEvent != null) {
                ScreenTouch screenTouch = ScreenTouch.this;
                Objects.requireNonNull(screenTouch);
                if (inputEvent instanceof MotionEvent) {
                    motionEvent = (MotionEvent) inputEvent;
                } else {
                    motionEvent = null;
                }
                if (motionEvent != null) {
                    if (motionEvent.getAction() != 0 || !screenTouch.touchRegion.contains(motionEvent.getRawX(), motionEvent.getRawY())) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z) {
                        screenTouch.handler.removeCallbacks(screenTouch.clearBlocking);
                        screenTouch.setBlocking(true);
                    } else if (motionEvent.getAction() == 1 && screenTouch.isBlocking()) {
                        screenTouch.handler.postDelayed(screenTouch.clearBlocking, 500L);
                    }
                }
            }
        }
    };
    public final ScreenTouch$clearBlocking$1 clearBlocking = new Runnable() { // from class: com.google.android.systemui.columbus.gates.ScreenTouch$clearBlocking$1
        @Override // java.lang.Runnable
        public final void run() {
            ScreenTouch.this.setBlocking(false);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.powerState.registerListener(this.gateListener);
        if (!this.powerState.isBlocking()) {
            startListeningForTouch();
        }
        setBlocking(false);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.powerState.unregisterListener(this.gateListener);
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.inputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.mReceiver.dispose();
        }
        this.inputEventReceiver = null;
        Logger logger = this.inputMonitor;
        if (logger != null) {
            ((InputMonitor) logger.prefix).dispose();
        }
        this.inputMonitor = null;
    }

    public final void startListeningForTouch() {
        if (this.inputEventReceiver == null) {
            Logger logger = new Logger("Quick Tap", 0);
            this.inputMonitor = logger;
            this.inputEventReceiver = logger.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), this.inputEventListener);
        }
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.columbus.gates.ScreenTouch$gateListener$1] */
    /* JADX WARN: Type inference failed for: r3v2, types: [com.google.android.systemui.columbus.gates.ScreenTouch$inputEventListener$1] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.google.android.systemui.columbus.gates.ScreenTouch$clearBlocking$1] */
    public ScreenTouch(Context context, PowerState powerState, Handler handler) {
        super(context);
        this.powerState = powerState;
        this.handler = handler;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float f = displayMetrics.density * 32;
        this.touchRegion = new RectF(f, f, displayMetrics.widthPixels - f, displayMetrics.heightPixels - f);
    }
}
