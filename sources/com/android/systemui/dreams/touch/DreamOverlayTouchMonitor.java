package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.MotionEvent;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda2;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda9;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda3;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda4;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda5;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda27;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9;
import com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda4;
import com.android.wm.shell.legacysplitscreen.WindowManagerProxy$$ExternalSyntheticLambda0;
import com.google.android.setupcompat.util.Logger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class DreamOverlayTouchMonitor {
    public InputSession mCurrentInputSession;
    public final Executor mExecutor;
    public final Set mHandlers;
    public InputSessionComponent.Factory mInputSessionFactory;
    public final Lifecycle mLifecycle;
    public final LifecycleObserver mLifecycleObserver = new DefaultLifecycleObserver() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.1
        @Override // androidx.lifecycle.FullLifecycleObserver
        public final void onPause$1() {
            DreamOverlayTouchMonitor dreamOverlayTouchMonitor = DreamOverlayTouchMonitor.this;
            Objects.requireNonNull(dreamOverlayTouchMonitor);
            InputSession inputSession = dreamOverlayTouchMonitor.mCurrentInputSession;
            if (inputSession != null) {
                InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = inputSession.mInputEventReceiver;
                if (inputChannelCompat$InputEventReceiver != null) {
                    inputChannelCompat$InputEventReceiver.mReceiver.dispose();
                }
                Logger logger = inputSession.mInputMonitor;
                if (logger != null) {
                    ((InputMonitor) logger.prefix).dispose();
                }
                dreamOverlayTouchMonitor.mCurrentInputSession = null;
            }
        }

        @Override // androidx.lifecycle.FullLifecycleObserver
        public final void onResume$1() {
            DreamOverlayTouchMonitor dreamOverlayTouchMonitor = DreamOverlayTouchMonitor.this;
            Objects.requireNonNull(dreamOverlayTouchMonitor);
            InputSession inputSession = dreamOverlayTouchMonitor.mCurrentInputSession;
            if (inputSession != null) {
                InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = inputSession.mInputEventReceiver;
                if (inputChannelCompat$InputEventReceiver != null) {
                    inputChannelCompat$InputEventReceiver.mReceiver.dispose();
                }
                Logger logger = inputSession.mInputMonitor;
                if (logger != null) {
                    ((InputMonitor) logger.prefix).dispose();
                }
                dreamOverlayTouchMonitor.mCurrentInputSession = null;
            }
            dreamOverlayTouchMonitor.mCurrentInputSession = dreamOverlayTouchMonitor.mInputSessionFactory.create("dreamOverlay", dreamOverlayTouchMonitor.mInputEventListener, dreamOverlayTouchMonitor.mOnGestureListener, true).getInputSession();
        }
    };
    public final HashSet<TouchSessionImpl> mActiveTouchSessions = new HashSet<>();
    public AnonymousClass2 mInputEventListener = new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.2
        @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
        public final void onInputEvent(InputEvent inputEvent) {
            if (DreamOverlayTouchMonitor.this.mActiveTouchSessions.isEmpty()) {
                for (DreamTouchHandler dreamTouchHandler : DreamOverlayTouchMonitor.this.mHandlers) {
                    TouchSessionImpl touchSessionImpl = new TouchSessionImpl(DreamOverlayTouchMonitor.this);
                    DreamOverlayTouchMonitor.this.mActiveTouchSessions.add(touchSessionImpl);
                    dreamTouchHandler.onSessionStart(touchSessionImpl);
                }
            }
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(BubbleData$$ExternalSyntheticLambda4.INSTANCE$2).flatMap(DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda0.INSTANCE).forEach(new WMShell$$ExternalSyntheticLambda3(inputEvent, 1));
        }
    };
    public AnonymousClass3 mOnGestureListener = new GestureDetector.OnGestureListener() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.3
        public final boolean evaluate(final Evaluator evaluator) {
            final HashSet hashSet = new HashSet();
            boolean anyMatch = DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new Function() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    final DreamOverlayTouchMonitor.Evaluator evaluator2 = DreamOverlayTouchMonitor.Evaluator.this;
                    Set set = hashSet;
                    DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl = (DreamOverlayTouchMonitor.TouchSessionImpl) obj;
                    Objects.requireNonNull(touchSessionImpl);
                    boolean anyMatch2 = touchSessionImpl.mGestureListeners.stream().map(new Function() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda4
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Boolean.valueOf(DreamOverlayTouchMonitor.Evaluator.this.evaluate((GestureDetector.OnGestureListener) obj2));
                        }
                    }).anyMatch(WifiPickerTracker$$ExternalSyntheticLambda27.INSTANCE$1);
                    if (anyMatch2) {
                        set.add(touchSessionImpl);
                    }
                    return Boolean.valueOf(anyMatch2);
                }
            }).anyMatch(DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda7.INSTANCE);
            if (anyMatch) {
                DreamOverlayTouchMonitor dreamOverlayTouchMonitor = DreamOverlayTouchMonitor.this;
                Objects.requireNonNull(dreamOverlayTouchMonitor);
                Collection<?> collection = (Collection) dreamOverlayTouchMonitor.mActiveTouchSessions.stream().filter(new WindowManagerProxy$$ExternalSyntheticLambda0(hashSet, 1)).collect(Collectors.toCollection(DreamOverlayStateController$$ExternalSyntheticLambda9.INSTANCE));
                collection.forEach(SystemActions$$ExternalSyntheticLambda2.INSTANCE$1);
                dreamOverlayTouchMonitor.mActiveTouchSessions.removeAll(collection);
            }
            return anyMatch;
        }

        public final void observe(Consumer<GestureDetector.OnGestureListener> consumer) {
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(WifiPickerTracker$$ExternalSyntheticLambda5.INSTANCE$1).flatMap(DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6.INSTANCE).forEach(new BubbleController$$ExternalSyntheticLambda9(consumer, 1));
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onDown(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2(motionEvent));
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float f, final float f2) {
            return evaluate(new Evaluator() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda0
                @Override // com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.Evaluator
                public final boolean evaluate(GestureDetector.OnGestureListener onGestureListener) {
                    return onGestureListener.onFling(motionEvent, motionEvent2, f, f2);
                }
            });
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final void onLongPress(MotionEvent motionEvent) {
            observe(new WMShell$$ExternalSyntheticLambda5(motionEvent, 1));
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float f, final float f2) {
            return evaluate(new Evaluator() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda1
                @Override // com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.Evaluator
                public final boolean evaluate(GestureDetector.OnGestureListener onGestureListener) {
                    return onGestureListener.onScroll(motionEvent, motionEvent2, f, f2);
                }
            });
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final void onShowPress(MotionEvent motionEvent) {
            observe(new WMShell$$ExternalSyntheticLambda4(motionEvent, 1));
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onSingleTapUp(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(motionEvent));
        }
    };

    /* loaded from: classes.dex */
    public interface Evaluator {
        boolean evaluate(GestureDetector.OnGestureListener onGestureListener);
    }

    /* loaded from: classes.dex */
    public static class TouchSessionImpl implements DreamTouchHandler.TouchSession {
        public final DreamOverlayTouchMonitor mTouchMonitor;
        public final HashSet<InputChannelCompat$InputEventListener> mEventListeners = new HashSet<>();
        public final HashSet<GestureDetector.OnGestureListener> mGestureListeners = new HashSet<>();
        public final HashSet<DreamTouchHandler.TouchSession.Callback> mCallbacks = new HashSet<>();
        public final TouchSessionImpl mPredecessor = null;

        public TouchSessionImpl(DreamOverlayTouchMonitor dreamOverlayTouchMonitor) {
            this.mTouchMonitor = dreamOverlayTouchMonitor;
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$2] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$3] */
    public DreamOverlayTouchMonitor(Executor executor, Lifecycle lifecycle, InputSessionComponent.Factory factory, Set<DreamTouchHandler> set) {
        this.mHandlers = set;
        this.mInputSessionFactory = factory;
        this.mExecutor = executor;
        this.mLifecycle = lifecycle;
    }
}
