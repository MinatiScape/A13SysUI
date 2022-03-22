package com.android.systemui.statusbar.gesture;

import android.content.Context;
import android.os.Looper;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.google.android.setupcompat.util.Logger;
import java.util.LinkedHashMap;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
/* compiled from: SwipeStatusBarAwayGestureHandler.kt */
/* loaded from: classes.dex */
public final class SwipeStatusBarAwayGestureHandler {
    public final LinkedHashMap callbacks = new LinkedHashMap();
    public Logger inputMonitor;
    public InputChannelCompat$InputEventReceiver inputReceiver;
    public final SwipeStatusBarAwayGestureLogger logger;
    public boolean monitoringCurrentTouch;
    public long startTime;
    public float startY;
    public final StatusBarWindowController statusBarWindowController;
    public int swipeDistanceThreshold;

    public final void addOnGestureDetectedCallback(Function0 function0) {
        boolean isEmpty = this.callbacks.isEmpty();
        this.callbacks.put("OngoingCallController", function0);
        if (isEmpty) {
            stopGestureListening();
            SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger = this.logger;
            Objects.requireNonNull(swipeStatusBarAwayGestureLogger);
            LogBuffer logBuffer = swipeStatusBarAwayGestureLogger.buffer;
            LogLevel logLevel = LogLevel.VERBOSE;
            SwipeStatusBarAwayGestureLogger$logInputListeningStarted$2 swipeStatusBarAwayGestureLogger$logInputListeningStarted$2 = SwipeStatusBarAwayGestureLogger$logInputListeningStarted$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", logLevel, swipeStatusBarAwayGestureLogger$logInputListeningStarted$2));
            }
            Logger logger = new Logger(SwipeStatusBarAwayGestureHandlerKt.TAG, 0);
            this.inputReceiver = logger.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler$startGestureListening$1$1
                @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
                public final void onInputEvent(InputEvent inputEvent) {
                    SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler = SwipeStatusBarAwayGestureHandler.this;
                    Objects.requireNonNull(swipeStatusBarAwayGestureHandler);
                    LogLevel logLevel2 = LogLevel.DEBUG;
                    if (inputEvent instanceof MotionEvent) {
                        MotionEvent motionEvent = (MotionEvent) inputEvent;
                        int actionMasked = motionEvent.getActionMasked();
                        if (actionMasked != 0) {
                            if (actionMasked != 1) {
                                if (actionMasked != 2) {
                                    if (actionMasked != 3) {
                                        return;
                                    }
                                } else if (swipeStatusBarAwayGestureHandler.monitoringCurrentTouch) {
                                    float y = motionEvent.getY();
                                    float f = swipeStatusBarAwayGestureHandler.startY;
                                    if (y < f && f - motionEvent.getY() >= swipeStatusBarAwayGestureHandler.swipeDistanceThreshold && motionEvent.getEventTime() - swipeStatusBarAwayGestureHandler.startTime < 500) {
                                        swipeStatusBarAwayGestureHandler.monitoringCurrentTouch = false;
                                        SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger2 = swipeStatusBarAwayGestureHandler.logger;
                                        int y2 = (int) motionEvent.getY();
                                        Objects.requireNonNull(swipeStatusBarAwayGestureLogger2);
                                        LogBuffer logBuffer2 = swipeStatusBarAwayGestureLogger2.buffer;
                                        LogLevel logLevel3 = LogLevel.INFO;
                                        SwipeStatusBarAwayGestureLogger$logGestureDetected$2 swipeStatusBarAwayGestureLogger$logGestureDetected$2 = SwipeStatusBarAwayGestureLogger$logGestureDetected$2.INSTANCE;
                                        Objects.requireNonNull(logBuffer2);
                                        if (!logBuffer2.frozen) {
                                            LogMessageImpl obtain = logBuffer2.obtain("SwipeStatusBarAwayGestureHandler", logLevel3, swipeStatusBarAwayGestureLogger$logGestureDetected$2);
                                            obtain.int1 = y2;
                                            logBuffer2.push(obtain);
                                        }
                                        for (Function0 function02 : swipeStatusBarAwayGestureHandler.callbacks.values()) {
                                            function02.invoke();
                                        }
                                        return;
                                    }
                                    return;
                                } else {
                                    return;
                                }
                            }
                            if (swipeStatusBarAwayGestureHandler.monitoringCurrentTouch) {
                                SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger3 = swipeStatusBarAwayGestureHandler.logger;
                                int y3 = (int) motionEvent.getY();
                                Objects.requireNonNull(swipeStatusBarAwayGestureLogger3);
                                LogBuffer logBuffer3 = swipeStatusBarAwayGestureLogger3.buffer;
                                SwipeStatusBarAwayGestureLogger$logGestureDetectionEndedWithoutTriggering$2 swipeStatusBarAwayGestureLogger$logGestureDetectionEndedWithoutTriggering$2 = SwipeStatusBarAwayGestureLogger$logGestureDetectionEndedWithoutTriggering$2.INSTANCE;
                                Objects.requireNonNull(logBuffer3);
                                if (!logBuffer3.frozen) {
                                    LogMessageImpl obtain2 = logBuffer3.obtain("SwipeStatusBarAwayGestureHandler", logLevel2, swipeStatusBarAwayGestureLogger$logGestureDetectionEndedWithoutTriggering$2);
                                    obtain2.int1 = y3;
                                    logBuffer3.push(obtain2);
                                }
                            }
                            swipeStatusBarAwayGestureHandler.monitoringCurrentTouch = false;
                            return;
                        }
                        float y4 = motionEvent.getY();
                        StatusBarWindowController statusBarWindowController = swipeStatusBarAwayGestureHandler.statusBarWindowController;
                        Objects.requireNonNull(statusBarWindowController);
                        if (y4 >= statusBarWindowController.mBarHeight) {
                            float y5 = motionEvent.getY();
                            StatusBarWindowController statusBarWindowController2 = swipeStatusBarAwayGestureHandler.statusBarWindowController;
                            Objects.requireNonNull(statusBarWindowController2);
                            if (y5 <= statusBarWindowController2.mBarHeight * 3) {
                                SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger4 = swipeStatusBarAwayGestureHandler.logger;
                                int y6 = (int) motionEvent.getY();
                                Objects.requireNonNull(swipeStatusBarAwayGestureLogger4);
                                LogBuffer logBuffer4 = swipeStatusBarAwayGestureLogger4.buffer;
                                SwipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2 swipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2 = SwipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2.INSTANCE;
                                Objects.requireNonNull(logBuffer4);
                                if (!logBuffer4.frozen) {
                                    LogMessageImpl obtain3 = logBuffer4.obtain("SwipeStatusBarAwayGestureHandler", logLevel2, swipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2);
                                    obtain3.int1 = y6;
                                    logBuffer4.push(obtain3);
                                }
                                swipeStatusBarAwayGestureHandler.startY = motionEvent.getY();
                                swipeStatusBarAwayGestureHandler.startTime = motionEvent.getEventTime();
                                swipeStatusBarAwayGestureHandler.monitoringCurrentTouch = true;
                                return;
                            }
                        }
                        swipeStatusBarAwayGestureHandler.monitoringCurrentTouch = false;
                    }
                }
            });
            this.inputMonitor = logger;
        }
    }

    public final void removeOnGestureDetectedCallback() {
        this.callbacks.remove("OngoingCallController");
        if (this.callbacks.isEmpty()) {
            stopGestureListening();
        }
    }

    public final void stopGestureListening() {
        Logger logger = this.inputMonitor;
        if (logger != null) {
            SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger = this.logger;
            Objects.requireNonNull(swipeStatusBarAwayGestureLogger);
            LogBuffer logBuffer = swipeStatusBarAwayGestureLogger.buffer;
            LogLevel logLevel = LogLevel.VERBOSE;
            SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2 swipeStatusBarAwayGestureLogger$logInputListeningStopped$2 = SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", logLevel, swipeStatusBarAwayGestureLogger$logInputListeningStopped$2));
            }
            this.inputMonitor = null;
            ((InputMonitor) logger.prefix).dispose();
        }
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.inputReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            this.inputReceiver = null;
            inputChannelCompat$InputEventReceiver.mReceiver.dispose();
        }
    }

    public SwipeStatusBarAwayGestureHandler(Context context, StatusBarWindowController statusBarWindowController, SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger) {
        this.statusBarWindowController = statusBarWindowController;
        this.logger = swipeStatusBarAwayGestureLogger;
        this.swipeDistanceThreshold = context.getResources().getDimensionPixelSize(17105561);
    }
}
