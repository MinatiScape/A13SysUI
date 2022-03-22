package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.format.DateFormat;
import android.util.FloatProperty;
import android.util.Log;
import android.view.Choreographer;
import android.view.InsetsFlags;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.PathInterpolator;
import androidx.preference.R$id;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.policy.CallbackController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class StatusBarStateControllerImpl implements SysuiStatusBarStateController, CallbackController<StatusBarStateController.StateListener>, Dumpable {
    public ObjectAnimator mDarkAnimator;
    public float mDozeAmount;
    public float mDozeAmountTarget;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public boolean mIsDozing;
    public boolean mIsExpanded;
    public boolean mKeyguardRequested;
    public int mLastState;
    public boolean mLeaveOpenOnKeyguardHide;
    public boolean mPulsing;
    public int mState;
    public final UiEventLogger mUiEventLogger;
    public int mUpcomingState;
    public View mView;
    public static final boolean DEBUG_IMMERSIVE_APPS = SystemProperties.getBoolean("persist.debug.immersive_apps", false);
    public static final Comparator<SysuiStatusBarStateController.RankedListener> sComparator = Comparator.comparingInt(StatusBarStateControllerImpl$$ExternalSyntheticLambda1.INSTANCE);
    public static final AnonymousClass1 SET_DARK_AMOUNT_PROPERTY = new FloatProperty<StatusBarStateControllerImpl>() { // from class: com.android.systemui.statusbar.StatusBarStateControllerImpl.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            return Float.valueOf(((StatusBarStateControllerImpl) obj).mDozeAmount);
        }

        @Override // android.util.FloatProperty
        public final void setValue(StatusBarStateControllerImpl statusBarStateControllerImpl, float f) {
            statusBarStateControllerImpl.setDozeAmountInternal(f);
        }
    };
    public final ArrayList<SysuiStatusBarStateController.RankedListener> mListeners = new ArrayList<>();
    public int mHistoryIndex = 0;
    public HistoricalState[] mHistoricalRecords = new HistoricalState[32];
    public boolean mIsFullscreen = false;
    public PathInterpolator mDozeInterpolator = Interpolators.FAST_OUT_SLOW_IN;

    /* loaded from: classes.dex */
    public static class HistoricalState {
        public int mLastState;
        public int mNewState;
        public long mTimestamp;
        public boolean mUpcoming;

        public final String toString() {
            if (this.mTimestamp != 0) {
                StringBuilder sb = new StringBuilder();
                if (this.mUpcoming) {
                    sb.append("upcoming-");
                }
                sb.append("newState=");
                sb.append(this.mNewState);
                sb.append("(");
                sb.append(R$id.toShortString(this.mNewState));
                sb.append(")");
                sb.append(" lastState=");
                sb.append(this.mLastState);
                sb.append("(");
                sb.append(R$id.toShortString(this.mLastState));
                sb.append(")");
                sb.append(" timestamp=");
                sb.append(DateFormat.format("MM-dd HH:mm:ss", this.mTimestamp));
                return sb.toString();
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Empty ");
            m.append(HistoricalState.class.getSimpleName());
            return m.toString();
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean setState(int i, boolean z) {
        StatusBarStateEvent statusBarStateEvent;
        if (i > 2 || i < 0) {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid state ", i));
        } else if (!z && i == this.mState) {
            return false;
        } else {
            recordHistoricalState(i, this.mState, false);
            if (this.mState == 0 && i == 2) {
                Log.e("SbStateController", "Invalid state transition: SHADE -> SHADE_LOCKED", new Throwable());
            }
            synchronized (this.mListeners) {
                String str = "StatusBarStateControllerImpl#setState(" + i + ")";
                DejankUtils.startDetectingBlockingIpcs(str);
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onStatePreChange(this.mState, i);
                }
                this.mLastState = this.mState;
                this.mState = i;
                this.mUpcomingState = i;
                UiEventLogger uiEventLogger = this.mUiEventLogger;
                if (i == 0) {
                    statusBarStateEvent = StatusBarStateEvent.STATUS_BAR_STATE_SHADE;
                } else if (i == 1) {
                    statusBarStateEvent = StatusBarStateEvent.STATUS_BAR_STATE_KEYGUARD;
                } else if (i != 2) {
                    statusBarStateEvent = StatusBarStateEvent.STATUS_BAR_STATE_UNKNOWN;
                } else {
                    statusBarStateEvent = StatusBarStateEvent.STATUS_BAR_STATE_SHADE_LOCKED;
                }
                uiEventLogger.log(statusBarStateEvent);
                Trace.instantForTrack(4096L, "UI Events", "StatusBarState " + str);
                Iterator it2 = new ArrayList(this.mListeners).iterator();
                while (it2.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it2.next()).mListener.onStateChanged(this.mState);
                }
                Iterator it3 = new ArrayList(this.mListeners).iterator();
                while (it3.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it3.next()).mListener.onStatePostChange();
                }
                DejankUtils.stopDetectingBlockingIpcs(str);
            }
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setSystemBarAttributes(int i, int i2, InsetsVisibilities insetsVisibilities, String str) {
        boolean z;
        boolean z2 = false;
        if (!insetsVisibilities.getVisibility(0) || !insetsVisibilities.getVisibility(1)) {
            z = true;
        } else {
            z = false;
        }
        if (this.mIsFullscreen != z) {
            this.mIsFullscreen = z;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onFullscreenStateChanged(z);
                }
            }
        }
        if (DEBUG_IMMERSIVE_APPS) {
            if ((i & 4) != 0) {
                z2 = true;
            }
            String flagsToString = ViewDebug.flagsToString(InsetsFlags.class, "behavior", i2);
            String insetsVisibilities2 = insetsVisibilities.toString();
            if (insetsVisibilities2.isEmpty()) {
                insetsVisibilities2 = "none";
            }
            Log.d("SbStateController", str + " dim=" + z2 + " behavior=" + flagsToString + " requested visibilities: " + insetsVisibilities2);
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setUpcomingState() {
        this.mUpcomingState = 1;
        recordHistoricalState(1, this.mState, true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final void addCallback(StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, Integer.MAX_VALUE);
        }
    }

    @GuardedBy({"mListeners"})
    public final void addListenerInternalLocked(StatusBarStateController.StateListener stateListener, int i) {
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (it.next().mListener.equals(stateListener)) {
                return;
            }
        }
        this.mListeners.add(new SysuiStatusBarStateController.RankedListener(stateListener, i));
        this.mListeners.sort(sComparator);
    }

    public final void beginInteractionJankMonitor() {
        boolean z;
        View view;
        int i;
        boolean z2 = this.mIsDozing;
        if ((!z2 || this.mDozeAmount != 0.0f) && (z2 || this.mDozeAmount != 1.0f)) {
            z = false;
        } else {
            z = true;
        }
        if (this.mInteractionJankMonitor != null && (view = this.mView) != null && view.isAttachedToWindow()) {
            if (z) {
                Choreographer.getInstance().postCallback(1, new StatusBar$$ExternalSyntheticLambda18(this, 7), null);
                return;
            }
            if (this.mIsDozing) {
                i = 24;
            } else {
                i = 23;
            }
            this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(i, this.mView).setDeferMonitorForAnimationStart(false));
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "StatusBarStateController: ", " mState=");
        m.append(this.mState);
        m.append(" (");
        m.append(R$id.toShortString(this.mState));
        m.append(")");
        printWriter.println(m.toString());
        printWriter.println(" mLastState=" + this.mLastState + " (" + R$id.toShortString(this.mLastState) + ")");
        StringBuilder sb = new StringBuilder();
        sb.append(" mLeaveOpenOnKeyguardHide=");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mLeaveOpenOnKeyguardHide, printWriter, " mKeyguardRequested="), this.mKeyguardRequested, printWriter, " mIsDozing="), this.mIsDozing, printWriter, " mListeners{");
        m2.append(this.mListeners.size());
        m2.append("}=");
        printWriter.println(m2.toString());
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            m3.append(it.next().mListener);
            printWriter.println(m3.toString());
        }
        printWriter.println(" Historical states:");
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            if (this.mHistoricalRecords[i2].mTimestamp != 0) {
                i++;
            }
        }
        for (int i3 = this.mHistoryIndex + 32; i3 >= ((this.mHistoryIndex + 32) - i) + 1; i3--) {
            StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("  (");
            m4.append(((this.mHistoryIndex + 32) - i3) + 1);
            m4.append(")");
            m4.append(this.mHistoricalRecords[i3 & 31]);
            printWriter.println(m4.toString());
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean fromShadeLocked() {
        if (this.mLastState == 2) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final float getInterpolatedDozeAmount() {
        return this.mDozeInterpolator.getInterpolation(this.mDozeAmount);
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean goingToFullShade() {
        if (this.mState != 0 || !this.mLeaveOpenOnKeyguardHide) {
            return false;
        }
        return true;
    }

    public final void recordHistoricalState(int i, int i2, boolean z) {
        Trace.traceCounter(4096L, "statusBarState", i);
        int i3 = (this.mHistoryIndex + 1) % 32;
        this.mHistoryIndex = i3;
        HistoricalState historicalState = this.mHistoricalRecords[i3];
        historicalState.mNewState = i;
        historicalState.mLastState = i2;
        historicalState.mTimestamp = System.currentTimeMillis();
        historicalState.mUpcoming = z;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final void removeCallback(final StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            this.mListeners.removeIf(new Predicate() { // from class: com.android.systemui.statusbar.StatusBarStateControllerImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((SysuiStatusBarStateController.RankedListener) obj).mListener.equals(StatusBarStateController.StateListener.this);
                }
            });
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setAndInstrumentDozeAmount(NotificationPanelView notificationPanelView, float f, boolean z) {
        PathInterpolator pathInterpolator;
        ObjectAnimator objectAnimator = this.mDarkAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            if (!z || this.mDozeAmountTarget != f) {
                this.mDarkAnimator.cancel();
            } else {
                return;
            }
        }
        View view = this.mView;
        if ((view == null || !view.isAttachedToWindow()) && notificationPanelView != null && notificationPanelView.isAttachedToWindow()) {
            this.mView = notificationPanelView;
        }
        this.mDozeAmountTarget = f;
        if (z) {
            float f2 = this.mDozeAmount;
            if (f2 == 0.0f || f2 == 1.0f) {
                if (this.mIsDozing) {
                    pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
                } else {
                    pathInterpolator = Interpolators.TOUCH_RESPONSE_REVERSE;
                }
                this.mDozeInterpolator = pathInterpolator;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, SET_DARK_AMOUNT_PROPERTY, f);
            this.mDarkAnimator = ofFloat;
            ofFloat.setInterpolator(Interpolators.LINEAR);
            this.mDarkAnimator.setDuration(500L);
            this.mDarkAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarStateControllerImpl.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    int i;
                    StatusBarStateControllerImpl statusBarStateControllerImpl = StatusBarStateControllerImpl.this;
                    Objects.requireNonNull(statusBarStateControllerImpl);
                    InteractionJankMonitor interactionJankMonitor = statusBarStateControllerImpl.mInteractionJankMonitor;
                    if (interactionJankMonitor != null) {
                        if (statusBarStateControllerImpl.mIsDozing) {
                            i = 24;
                        } else {
                            i = 23;
                        }
                        interactionJankMonitor.cancel(i);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    int i;
                    StatusBarStateControllerImpl statusBarStateControllerImpl = StatusBarStateControllerImpl.this;
                    Objects.requireNonNull(statusBarStateControllerImpl);
                    InteractionJankMonitor interactionJankMonitor = statusBarStateControllerImpl.mInteractionJankMonitor;
                    if (interactionJankMonitor != null) {
                        if (statusBarStateControllerImpl.mIsDozing) {
                            i = 24;
                        } else {
                            i = 23;
                        }
                        interactionJankMonitor.end(i);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    StatusBarStateControllerImpl.this.beginInteractionJankMonitor();
                }
            });
            this.mDarkAnimator.start();
            return;
        }
        setDozeAmountInternal(f);
    }

    public final void setDozeAmountInternal(float f) {
        if (Float.compare(f, this.mDozeAmount) != 0) {
            this.mDozeAmount = f;
            float interpolation = this.mDozeInterpolator.getInterpolation(f);
            synchronized (this.mListeners) {
                DejankUtils.startDetectingBlockingIpcs("StatusBarStateControllerImpl#setDozeAmount");
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozeAmountChanged(this.mDozeAmount, interpolation);
                }
                DejankUtils.stopDetectingBlockingIpcs("StatusBarStateControllerImpl#setDozeAmount");
            }
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean setIsDozing(boolean z) {
        if (this.mIsDozing == z) {
            return false;
        }
        this.mIsDozing = z;
        synchronized (this.mListeners) {
            DejankUtils.startDetectingBlockingIpcs("StatusBarStateControllerImpl#setIsDozing");
            Iterator it = new ArrayList(this.mListeners).iterator();
            while (it.hasNext()) {
                ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozingChanged(z);
            }
            DejankUtils.stopDetectingBlockingIpcs("StatusBarStateControllerImpl#setIsDozing");
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean setPanelExpanded(boolean z) {
        if (this.mIsExpanded == z) {
            return false;
        }
        this.mIsExpanded = z;
        DejankUtils.startDetectingBlockingIpcs("StatusBarStateControllerImpl#setIsExpanded");
        Iterator it = new ArrayList(this.mListeners).iterator();
        while (it.hasNext()) {
            ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onExpandedChanged(this.mIsExpanded);
        }
        DejankUtils.stopDetectingBlockingIpcs("StatusBarStateControllerImpl#setIsExpanded");
        return true;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onPulsingChanged(z);
                }
            }
        }
    }

    public StatusBarStateControllerImpl(UiEventLogger uiEventLogger, DumpManager dumpManager, InteractionJankMonitor interactionJankMonitor) {
        this.mUiEventLogger = uiEventLogger;
        this.mInteractionJankMonitor = interactionJankMonitor;
        for (int i = 0; i < 32; i++) {
            this.mHistoricalRecords[i] = new HistoricalState();
        }
        dumpManager.registerDumpable(this);
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    @Deprecated
    public final void addCallback(StatusBarStateController.StateListener stateListener, int i) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, i);
        }
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setKeyguardRequested(boolean z) {
        this.mKeyguardRequested = z;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final void setLeaveOpenOnKeyguardHide(boolean z) {
        this.mLeaveOpenOnKeyguardHide = z;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final int getCurrentOrUpcomingState() {
        return this.mUpcomingState;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final float getDozeAmount() {
        return this.mDozeAmount;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final int getState() {
        return this.mState;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final boolean isDozing() {
        return this.mIsDozing;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final boolean isExpanded() {
        return this.mIsExpanded;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean isKeyguardRequested() {
        return this.mKeyguardRequested;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController
    public final boolean isPulsing() {
        return this.mPulsing;
    }

    @Override // com.android.systemui.statusbar.SysuiStatusBarStateController
    public final boolean leaveOpenOnKeyguardHide() {
        return this.mLeaveOpenOnKeyguardHide;
    }
}
