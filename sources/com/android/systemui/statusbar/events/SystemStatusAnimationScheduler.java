package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public final class SystemStatusAnimationScheduler implements CallbackController<SystemStatusAnimationCallback>, Dumpable {
    public int animationState;
    public final SystemEventChipAnimationController chipAnimationController;
    public final SystemEventCoordinator coordinator;
    public final DelayableExecutor executor;
    public boolean hasPersistentDot;
    public StatusEvent scheduledEvent;
    public final StatusBarWindowController statusBarWindowController;
    public final SystemClock systemClock;
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final SystemStatusAnimationScheduler$systemUpdateListener$1 systemUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemUpdateListener$1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            for (SystemStatusAnimationCallback systemStatusAnimationCallback : systemStatusAnimationScheduler.listeners) {
                systemStatusAnimationCallback.onSystemChromeAnimationUpdate(valueAnimator);
            }
        }
    };
    public final SystemStatusAnimationScheduler$systemAnimatorAdapter$1 systemAnimatorAdapter = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemAnimatorAdapter$1
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            for (SystemStatusAnimationCallback systemStatusAnimationCallback : systemStatusAnimationScheduler.listeners) {
                systemStatusAnimationCallback.onSystemChromeAnimationEnd();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            for (SystemStatusAnimationCallback systemStatusAnimationCallback : systemStatusAnimationScheduler.listeners) {
                systemStatusAnimationCallback.onSystemChromeAnimationStart();
            }
        }
    };
    public final SystemStatusAnimationScheduler$chipUpdateListener$1 chipUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$chipUpdateListener$1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            SystemEventChipAnimationController systemEventChipAnimationController = systemStatusAnimationScheduler.chipAnimationController;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            Objects.requireNonNull(systemEventChipAnimationController);
            View view = systemEventChipAnimationController.currentAnimatedView;
            if (view != null) {
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                view.setAlpha(floatValue);
                float width = (1 - floatValue) * view.getWidth();
                if (view.isLayoutRtl()) {
                    width = -width;
                }
                view.setTranslationX(width);
            }
        }
    };

    /* compiled from: SystemStatusAnimationScheduler.kt */
    /* loaded from: classes.dex */
    public final class ChipAnimatorAdapter extends AnimatorListenerAdapter {
        public final int endState;
        public final Function1<Context, View> viewCreator;

        /* JADX WARN: Multi-variable type inference failed */
        public ChipAnimatorAdapter(int i, Function1<? super Context, ? extends View> function1) {
            this.endState = i;
            this.viewCreator = function1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            int i;
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            SystemEventChipAnimationController systemEventChipAnimationController = systemStatusAnimationScheduler.chipAnimationController;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            int i2 = systemStatusAnimationScheduler.animationState;
            Objects.requireNonNull(systemEventChipAnimationController);
            if (i2 == 1) {
                View view = systemEventChipAnimationController.currentAnimatedView;
                if (view != null) {
                    view.setTranslationX(0.0f);
                    view.setAlpha(1.0f);
                }
            } else {
                View view2 = systemEventChipAnimationController.currentAnimatedView;
                if (view2 != null) {
                    view2.setVisibility(4);
                }
                FrameLayout frameLayout = systemEventChipAnimationController.animationWindowView;
                if (frameLayout == null) {
                    frameLayout = null;
                }
                frameLayout.removeView(systemEventChipAnimationController.currentAnimatedView);
            }
            SystemStatusAnimationScheduler systemStatusAnimationScheduler2 = SystemStatusAnimationScheduler.this;
            if (this.endState == 4) {
                Objects.requireNonNull(systemStatusAnimationScheduler2);
                if (!systemStatusAnimationScheduler2.hasPersistentDot) {
                    i = 0;
                    systemStatusAnimationScheduler2.animationState = i;
                }
            }
            i = this.endState;
            systemStatusAnimationScheduler2.animationState = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            int i;
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            SystemEventChipAnimationController systemEventChipAnimationController = systemStatusAnimationScheduler.chipAnimationController;
            Function1<Context, View> function1 = this.viewCreator;
            int i2 = systemStatusAnimationScheduler.animationState;
            Objects.requireNonNull(systemEventChipAnimationController);
            FrameLayout frameLayout = null;
            if (!systemEventChipAnimationController.initialized) {
                systemEventChipAnimationController.initialized = true;
                View inflate = LayoutInflater.from(systemEventChipAnimationController.context).inflate(2131624605, (ViewGroup) null);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.FrameLayout");
                FrameLayout frameLayout2 = (FrameLayout) inflate;
                systemEventChipAnimationController.animationWindowView = frameLayout2;
                systemEventChipAnimationController.animationDotView = frameLayout2.findViewById(2131427867);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
                layoutParams.gravity = 8388629;
                StatusBarWindowController statusBarWindowController = systemEventChipAnimationController.statusBarWindowController;
                FrameLayout frameLayout3 = systemEventChipAnimationController.animationWindowView;
                if (frameLayout3 == null) {
                    frameLayout3 = null;
                }
                Objects.requireNonNull(statusBarWindowController);
                statusBarWindowController.mStatusBarWindowView.addView(frameLayout3, layoutParams);
            }
            if (i2 == 1) {
                View invoke = function1.invoke(systemEventChipAnimationController.context);
                systemEventChipAnimationController.currentAnimatedView = invoke;
                FrameLayout frameLayout4 = systemEventChipAnimationController.animationWindowView;
                if (frameLayout4 == null) {
                    frameLayout4 = null;
                }
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
                layoutParams2.gravity = 8388629;
                FrameLayout frameLayout5 = systemEventChipAnimationController.animationWindowView;
                if (frameLayout5 != null) {
                    frameLayout = frameLayout5;
                }
                if (frameLayout.isLayoutRtl()) {
                    StatusBarLocationPublisher statusBarLocationPublisher = systemEventChipAnimationController.locationPublisher;
                    Objects.requireNonNull(statusBarLocationPublisher);
                    i = statusBarLocationPublisher.marginRight;
                } else {
                    StatusBarLocationPublisher statusBarLocationPublisher2 = systemEventChipAnimationController.locationPublisher;
                    Objects.requireNonNull(statusBarLocationPublisher2);
                    i = statusBarLocationPublisher2.marginLeft;
                }
                layoutParams2.setMarginStart(i);
                frameLayout4.addView(invoke, layoutParams2);
                View view = systemEventChipAnimationController.currentAnimatedView;
                if (view != null) {
                    float width = view.getWidth();
                    if (view.isLayoutRtl()) {
                        width = -width;
                    }
                    view.setTranslationX(width);
                    view.setAlpha(0.0f);
                    view.setVisibility(0);
                    StatusBarLocationPublisher statusBarLocationPublisher3 = systemEventChipAnimationController.locationPublisher;
                    Objects.requireNonNull(statusBarLocationPublisher3);
                    int i3 = statusBarLocationPublisher3.marginLeft;
                    StatusBarLocationPublisher statusBarLocationPublisher4 = systemEventChipAnimationController.locationPublisher;
                    Objects.requireNonNull(statusBarLocationPublisher4);
                    view.setPadding(i3, 0, statusBarLocationPublisher4.marginRight, 0);
                    return;
                }
                return;
            }
            View view2 = systemEventChipAnimationController.currentAnimatedView;
            if (view2 != null) {
                view2.setTranslationX(0.0f);
                view2.setAlpha(1.0f);
            }
        }
    }

    public final void addCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Assert.isMainThread();
        if (this.listeners.isEmpty()) {
            SystemEventCoordinator systemEventCoordinator = this.coordinator;
            Objects.requireNonNull(systemEventCoordinator);
            systemEventCoordinator.privacyController.addCallback(systemEventCoordinator.privacyStateListener);
        }
        this.listeners.add(systemStatusAnimationCallback);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Scheduled event: ", this.scheduledEvent));
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.hasPersistentDot, "Has persistent privacy dot: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("Animation state: ", Integer.valueOf(this.animationState)));
        printWriter.println("Listeners:");
        if (this.listeners.isEmpty()) {
            printWriter.println("(none)");
            return;
        }
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            printWriter.println(Intrinsics.stringPlus("  ", systemStatusAnimationCallback));
        }
    }

    public final AnimatorSet notifyTransitionToPersistentDot() {
        LinkedHashSet linkedHashSet = this.listeners;
        ArrayList arrayList = new ArrayList();
        Iterator it = linkedHashSet.iterator();
        while (true) {
            String str = null;
            if (!it.hasNext()) {
                break;
            }
            SystemStatusAnimationCallback systemStatusAnimationCallback = (SystemStatusAnimationCallback) it.next();
            StatusEvent statusEvent = this.scheduledEvent;
            if (statusEvent != null) {
                str = statusEvent.getContentDescription();
            }
            systemStatusAnimationCallback.onSystemStatusAnimationTransitionToPersistentDot(str);
        }
        if (!(!arrayList.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    public final void removeCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Assert.isMainThread();
        this.listeners.remove(systemStatusAnimationCallback);
        if (this.listeners.isEmpty()) {
            SystemEventCoordinator systemEventCoordinator = this.coordinator;
            Objects.requireNonNull(systemEventCoordinator);
            systemEventCoordinator.privacyController.removeCallback(systemEventCoordinator.privacyStateListener);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemUpdateListener$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemAnimatorAdapter$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$chipUpdateListener$1] */
    public SystemStatusAnimationScheduler(SystemEventCoordinator systemEventCoordinator, SystemEventChipAnimationController systemEventChipAnimationController, StatusBarWindowController statusBarWindowController, DumpManager dumpManager, SystemClock systemClock, DelayableExecutor delayableExecutor) {
        this.coordinator = systemEventCoordinator;
        this.chipAnimationController = systemEventChipAnimationController;
        this.statusBarWindowController = statusBarWindowController;
        this.systemClock = systemClock;
        this.executor = delayableExecutor;
        Objects.requireNonNull(systemEventCoordinator);
        systemEventCoordinator.scheduler = this;
        dumpManager.registerDumpable("SystemStatusAnimationScheduler", this);
    }
}
