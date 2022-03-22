package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.columbus.actions.Action;
import dagger.Lazy;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
/* compiled from: SetupWizard.kt */
/* loaded from: classes.dex */
public final class SetupWizard extends Gate {
    public boolean exceptionActive;
    public final Set<Action> exceptions;
    public final Lazy<DeviceProvisionedController> provisionedController;
    public boolean setupComplete;
    public final SetupWizard$provisionedListener$1 provisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.google.android.systemui.columbus.gates.SetupWizard$provisionedListener$1
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onDeviceProvisionedChanged() {
            SetupWizard setupWizard = SetupWizard.this;
            setupWizard.setupComplete = setupWizard.isSetupComplete();
            SetupWizard.this.updateBlocking();
        }

        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onUserSetupChanged() {
            SetupWizard setupWizard = SetupWizard.this;
            setupWizard.setupComplete = setupWizard.isSetupComplete();
            SetupWizard.this.updateBlocking();
        }
    };
    public final SetupWizard$actionListener$1 actionListener = new Action.Listener() { // from class: com.google.android.systemui.columbus.gates.SetupWizard$actionListener$1
        @Override // com.google.android.systemui.columbus.actions.Action.Listener
        public final void onActionAvailabilityChanged(Action action) {
            Object obj;
            boolean z;
            SetupWizard setupWizard = SetupWizard.this;
            Iterator<T> it = setupWizard.exceptions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                Action action2 = (Action) obj;
                Objects.requireNonNull(action2);
                if (action2.isAvailable) {
                    break;
                }
            }
            if (obj != null) {
                z = true;
            } else {
                z = false;
            }
            setupWizard.exceptionActive = z;
            SetupWizard.this.updateBlocking();
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.exceptionActive = false;
        for (Action action : this.exceptions) {
            SetupWizard$actionListener$1 setupWizard$actionListener$1 = this.actionListener;
            Objects.requireNonNull(action);
            action.listeners.add(setupWizard$actionListener$1);
            this.exceptionActive = action.isAvailable | this.exceptionActive;
        }
        this.setupComplete = isSetupComplete();
        this.provisionedController.get().addCallback(this.provisionedListener);
        updateBlocking();
    }

    public final boolean isSetupComplete() {
        if (!this.provisionedController.get().isDeviceProvisioned() || !this.provisionedController.get().isCurrentUserSetup()) {
            return false;
        }
        return true;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.provisionedController.get().removeCallback(this.provisionedListener);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final String toString() {
        return super.toString() + " [isDeviceProvisioned -> " + this.provisionedController.get().isDeviceProvisioned() + "; isCurrentUserSetup -> " + this.provisionedController.get().isCurrentUserSetup() + ']';
    }

    public final void updateBlocking() {
        boolean z;
        if (this.exceptionActive || this.setupComplete) {
            z = false;
        } else {
            z = true;
        }
        setBlocking(z);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.SetupWizard$provisionedListener$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.systemui.columbus.gates.SetupWizard$actionListener$1] */
    public SetupWizard(Context context, Set<Action> set, Lazy<DeviceProvisionedController> lazy) {
        super(context);
        this.exceptions = set;
        this.provisionedController = lazy;
    }
}
