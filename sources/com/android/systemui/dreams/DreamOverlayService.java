package com.android.systemui.dreams;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.policy.PhoneWindow;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda21;
import com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class DreamOverlayService extends android.service.dreams.DreamOverlayService {
    public static final boolean DEBUG = Log.isLoggable("DreamOverlayService", 3);
    public final Context mContext;
    public final DreamOverlayContainerViewController mDreamOverlayContainerViewController;
    public final Executor mExecutor;
    public final AnonymousClass1 mHost;
    public final AnonymousClass2 mKeyguardCallback;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LifecycleRegistry mLifecycleRegistry;
    public DreamOverlayStateController mStateController;
    public ViewModelStore mViewModelStore = new ViewModelStore();
    public PhoneWindow mWindow;

    /* renamed from: com.android.systemui.dreams.DreamOverlayService$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Complication.Host {
        public AnonymousClass1() {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void onDestroy() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardCallback);
        this.mExecutor.execute(new Bubble$$ExternalSyntheticLambda1(this, Lifecycle.State.DESTROYED, 1));
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        PhoneWindow phoneWindow = this.mWindow;
        if (phoneWindow != null) {
            windowManager.removeView(phoneWindow.getDecorView());
        }
        this.mStateController.setOverlayActive(false);
        DreamOverlayService.super.onDestroy();
    }

    public final void onStartDream(WindowManager.LayoutParams layoutParams) {
        this.mExecutor.execute(new Bubble$$ExternalSyntheticLambda1(this, Lifecycle.State.STARTED, 1));
        this.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda21(this, layoutParams, 1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.android.systemui.dreams.DreamOverlayService$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DreamOverlayService(android.content.Context r3, java.util.concurrent.Executor r4, com.android.systemui.dreams.dagger.DreamOverlayComponent.Factory r5, com.android.systemui.dreams.DreamOverlayStateController r6, com.android.keyguard.KeyguardUpdateMonitor r7) {
        /*
            r2 = this;
            r2.<init>()
            com.android.systemui.dreams.DreamOverlayService$1 r0 = new com.android.systemui.dreams.DreamOverlayService$1
            r0.<init>()
            r2.mHost = r0
            androidx.lifecycle.ViewModelStore r1 = new androidx.lifecycle.ViewModelStore
            r1.<init>()
            r2.mViewModelStore = r1
            com.android.systemui.dreams.DreamOverlayService$2 r1 = new com.android.systemui.dreams.DreamOverlayService$2
            r1.<init>()
            r2.mKeyguardCallback = r1
            r2.mContext = r3
            r2.mExecutor = r4
            r2.mKeyguardUpdateMonitor = r7
            r7.registerCallback(r1)
            r2.mStateController = r6
            androidx.lifecycle.ViewModelStore r3 = r2.mViewModelStore
            com.android.systemui.dreams.dagger.DreamOverlayComponent r3 = r5.create(r3, r0)
            com.android.systemui.dreams.DreamOverlayContainerViewController r5 = r3.getDreamOverlayContainerViewController()
            r2.mDreamOverlayContainerViewController = r5
            androidx.lifecycle.Lifecycle$State r5 = androidx.lifecycle.Lifecycle.State.CREATED
            com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1 r6 = new com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1
            r7 = 1
            r6.<init>(r2, r5, r7)
            r4.execute(r6)
            androidx.lifecycle.LifecycleRegistry r4 = r3.getLifecycleRegistry()
            r2.mLifecycleRegistry = r4
            com.android.systemui.dreams.touch.DreamOverlayTouchMonitor r2 = r3.getDreamOverlayTouchMonitor()
            java.util.Objects.requireNonNull(r2)
            androidx.lifecycle.Lifecycle r3 = r2.mLifecycle
            androidx.lifecycle.LifecycleObserver r2 = r2.mLifecycleObserver
            r3.addObserver(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dreams.DreamOverlayService.<init>(android.content.Context, java.util.concurrent.Executor, com.android.systemui.dreams.dagger.DreamOverlayComponent$Factory, com.android.systemui.dreams.DreamOverlayStateController, com.android.keyguard.KeyguardUpdateMonitor):void");
    }
}
