package com.google.android.systemui.elmyra.gates;

import com.android.systemui.Dependency;
import com.android.systemui.assist.AssistManager;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.elmyra.actions.Action;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NavigationBarVisibility extends Gate {
    public final AnonymousClass1 mCommandQueueCallbacks;
    public final int mDisplayId;
    public final ArrayList mExceptions;
    public final AnonymousClass2 mGateListener;
    public boolean mIsKeyguardShowing;
    public boolean mIsNavigationGestural;
    public final KeyguardVisibility mKeyguardGate;
    public final NonGesturalNavigation mNavigationModeGate;
    public boolean mIsNavigationHidden = false;
    public final AssistManagerGoogle mAssistManager = (AssistManagerGoogle) Dependency.get(AssistManager.class);

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        if (this.mIsKeyguardShowing) {
            return false;
        }
        if (this.mIsNavigationGestural) {
            AssistManagerGoogle assistManagerGoogle = this.mAssistManager;
            Objects.requireNonNull(assistManagerGoogle);
            if (assistManagerGoogle.mNgaIsAssistant) {
                return false;
            }
        }
        for (int i = 0; i < this.mExceptions.size(); i++) {
            if (((Action) this.mExceptions.get(i)).isAvailable()) {
                return false;
            }
        }
        return this.mIsNavigationHidden;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        this.mKeyguardGate.activate();
        KeyguardVisibility keyguardVisibility = this.mKeyguardGate;
        Objects.requireNonNull(keyguardVisibility);
        this.mIsKeyguardShowing = keyguardVisibility.mKeyguardStateController.isShowing();
        this.mNavigationModeGate.activate();
        NonGesturalNavigation nonGesturalNavigation = this.mNavigationModeGate;
        Objects.requireNonNull(nonGesturalNavigation);
        this.mIsNavigationGestural = nonGesturalNavigation.mCurrentModeIsGestural;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mNavigationModeGate.deactivate();
        NonGesturalNavigation nonGesturalNavigation = this.mNavigationModeGate;
        Objects.requireNonNull(nonGesturalNavigation);
        this.mIsNavigationGestural = nonGesturalNavigation.mCurrentModeIsGestural;
        this.mKeyguardGate.deactivate();
        KeyguardVisibility keyguardVisibility = this.mKeyguardGate;
        Objects.requireNonNull(keyguardVisibility);
        this.mIsKeyguardShowing = keyguardVisibility.mKeyguardStateController.isShowing();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [mIsNavigationHidden -> ");
        sb.append(this.mIsNavigationHidden);
        sb.append("; mExceptions -> ");
        sb.append(this.mExceptions);
        sb.append("; mIsNavigationGestural -> ");
        sb.append(this.mIsNavigationGestural);
        sb.append("; isActiveAssistantNga() -> ");
        AssistManagerGoogle assistManagerGoogle = this.mAssistManager;
        Objects.requireNonNull(assistManagerGoogle);
        sb.append(assistManagerGoogle.mNgaIsAssistant);
        sb.append("]");
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.CommandQueue$Callbacks, com.google.android.systemui.elmyra.gates.NavigationBarVisibility$1] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.google.android.systemui.elmyra.gates.Gate$Listener, com.google.android.systemui.elmyra.gates.NavigationBarVisibility$2] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NavigationBarVisibility(android.content.Context r4, java.util.List<com.google.android.systemui.elmyra.actions.Action> r5) {
        /*
            r3 = this;
            r3.<init>(r4)
            com.google.android.systemui.elmyra.gates.NavigationBarVisibility$1 r0 = new com.google.android.systemui.elmyra.gates.NavigationBarVisibility$1
            r0.<init>()
            r3.mCommandQueueCallbacks = r0
            com.google.android.systemui.elmyra.gates.NavigationBarVisibility$2 r1 = new com.google.android.systemui.elmyra.gates.NavigationBarVisibility$2
            r1.<init>()
            r3.mGateListener = r1
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r5)
            r3.mExceptions = r2
            r5 = 0
            r3.mIsNavigationHidden = r5
            java.lang.Class<com.android.systemui.statusbar.CommandQueue> r5 = com.android.systemui.statusbar.CommandQueue.class
            java.lang.Object r5 = com.android.systemui.Dependency.get(r5)
            com.android.systemui.statusbar.CommandQueue r5 = (com.android.systemui.statusbar.CommandQueue) r5
            r5.addCallback(r0)
            int r5 = r4.getDisplayId()
            r3.mDisplayId = r5
            java.lang.Class<com.android.systemui.assist.AssistManager> r5 = com.android.systemui.assist.AssistManager.class
            java.lang.Object r5 = com.android.systemui.Dependency.get(r5)
            com.google.android.systemui.assist.AssistManagerGoogle r5 = (com.google.android.systemui.assist.AssistManagerGoogle) r5
            r3.mAssistManager = r5
            com.google.android.systemui.elmyra.gates.KeyguardVisibility r5 = new com.google.android.systemui.elmyra.gates.KeyguardVisibility
            r5.<init>(r4)
            r3.mKeyguardGate = r5
            r5.mListener = r1
            com.google.android.systemui.elmyra.gates.NonGesturalNavigation r5 = new com.google.android.systemui.elmyra.gates.NonGesturalNavigation
            r5.<init>(r4)
            r3.mNavigationModeGate = r5
            r5.mListener = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.gates.NavigationBarVisibility.<init>(android.content.Context, java.util.List):void");
    }
}
