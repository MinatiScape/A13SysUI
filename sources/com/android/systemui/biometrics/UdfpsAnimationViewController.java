package com.android.systemui.biometrics;

import android.graphics.PointF;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.UdfpsAnimationView;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.util.ViewController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UdfpsAnimationViewController.kt */
/* loaded from: classes.dex */
public abstract class UdfpsAnimationViewController<T extends UdfpsAnimationView> extends ViewController<T> implements Dumpable {
    public final SystemUIDialogManager dialogManager;
    public final DumpManager dumpManager;
    public boolean notificationShadeVisible;
    public final UdfpsAnimationViewController$panelExpansionListener$1 panelExpansionListener;
    public final PanelExpansionStateManager panelExpansionStateManager;
    public final StatusBarStateController statusBarStateController;
    public final UdfpsAnimationViewController$dialogListener$1 dialogListener = new SystemUIDialogManager.Listener(this) { // from class: com.android.systemui.biometrics.UdfpsAnimationViewController$dialogListener$1
        public final /* synthetic */ UdfpsAnimationViewController<T> this$0;

        {
            this.this$0 = this;
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialogManager.Listener
        public final void shouldHideAffordances() {
            this.this$0.updatePauseAuth();
        }
    };
    public final PointF touchTranslation = new PointF(0.0f, 0.0f);
    public final String dumpTag = getTag() + " (" + this + ')';

    public int getPaddingX() {
        return 0;
    }

    public int getPaddingY() {
        return 0;
    }

    public abstract String getTag();

    public void onTouchOutsideView() {
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("mNotificationShadeVisible=", Boolean.valueOf(this.notificationShadeVisible)));
        printWriter.println(Intrinsics.stringPlus("shouldPauseAuth()=", Boolean.valueOf(shouldPauseAuth())));
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(getView().mPauseAuth, "isPauseAuth=", printWriter);
    }

    public final T getView() {
        T t = this.mView;
        Intrinsics.checkNotNull(t);
        return (T) ((UdfpsAnimationView) t);
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.panelExpansionStateManager.addExpansionListener(this.panelExpansionListener);
        SystemUIDialogManager systemUIDialogManager = this.dialogManager;
        UdfpsAnimationViewController$dialogListener$1 udfpsAnimationViewController$dialogListener$1 = this.dialogListener;
        Objects.requireNonNull(systemUIDialogManager);
        systemUIDialogManager.mListeners.add(udfpsAnimationViewController$dialogListener$1);
        this.dumpManager.registerDumpable(this.dumpTag, this);
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        PanelExpansionStateManager panelExpansionStateManager = this.panelExpansionStateManager;
        UdfpsAnimationViewController$panelExpansionListener$1 udfpsAnimationViewController$panelExpansionListener$1 = this.panelExpansionListener;
        Objects.requireNonNull(panelExpansionStateManager);
        panelExpansionStateManager.expansionListeners.remove(udfpsAnimationViewController$panelExpansionListener$1);
        SystemUIDialogManager systemUIDialogManager = this.dialogManager;
        UdfpsAnimationViewController$dialogListener$1 udfpsAnimationViewController$dialogListener$1 = this.dialogListener;
        Objects.requireNonNull(systemUIDialogManager);
        systemUIDialogManager.mListeners.remove(udfpsAnimationViewController$dialogListener$1);
        this.dumpManager.unregisterDumpable(this.dumpTag);
    }

    public boolean shouldPauseAuth() {
        if (this.notificationShadeVisible || this.dialogManager.shouldHideAffordance()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.biometrics.UdfpsAnimationViewController$dialogListener$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.biometrics.UdfpsAnimationViewController$panelExpansionListener$1] */
    public UdfpsAnimationViewController(final T t, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager) {
        super(t);
        this.statusBarStateController = statusBarStateController;
        this.panelExpansionStateManager = panelExpansionStateManager;
        this.dialogManager = systemUIDialogManager;
        this.dumpManager = dumpManager;
        this.panelExpansionListener = new PanelExpansionListener() { // from class: com.android.systemui.biometrics.UdfpsAnimationViewController$panelExpansionListener$1
            @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
            public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
                boolean z3;
                UdfpsAnimationViewController<T> udfpsAnimationViewController = UdfpsAnimationViewController.this;
                int i = 0;
                if (!z || f <= 0.0f) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                Objects.requireNonNull(udfpsAnimationViewController);
                udfpsAnimationViewController.notificationShadeVisible = z3;
                UdfpsAnimationView udfpsAnimationView = t;
                Objects.requireNonNull(udfpsAnimationView);
                if (f < 0.4f) {
                    i = (int) ((1.0f - (f / 0.4f)) * 255.0f);
                }
                udfpsAnimationView.mAlpha = i;
                udfpsAnimationView.updateAlpha();
                UdfpsAnimationViewController.this.updatePauseAuth();
            }
        };
    }

    public final void updatePauseAuth() {
        boolean z;
        T view = getView();
        boolean shouldPauseAuth = shouldPauseAuth();
        if (shouldPauseAuth != view.mPauseAuth) {
            view.mPauseAuth = shouldPauseAuth;
            view.updateAlpha();
            z = true;
        } else {
            z = false;
        }
        if (z) {
            getView().postInvalidate();
        }
    }

    public PointF getTouchTranslation() {
        return this.touchTranslation;
    }
}
