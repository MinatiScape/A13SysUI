package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Map;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserSelectedAction.kt */
/* loaded from: classes.dex */
public final class UserSelectedAction extends Action {
    public UserAction currentAction;
    public final KeyguardStateController keyguardStateController;
    public final PowerManagerWrapper powerManager;
    public final TakeScreenshot takeScreenshot;
    public final Map<String, UserAction> userSelectedActions;
    public final UserSelectedAction$keyguardMonitorCallback$1 keyguardMonitorCallback = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$keyguardMonitorCallback$1
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            UserSelectedAction.this.updateAvailable();
        }
    };
    public final UserSelectedAction$wakefulnessLifecycleObserver$1 wakefulnessLifecycleObserver = new WakefulnessLifecycle.Observer() { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$wakefulnessLifecycleObserver$1
        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onFinishedGoingToSleep() {
            UserSelectedAction.this.updateAvailable();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onStartedWakingUp() {
            UserSelectedAction.this.updateAvailable();
        }
    };

    /* JADX WARN: Type inference failed for: r6v1, types: [com.google.android.systemui.columbus.actions.UserSelectedAction$keyguardMonitorCallback$1] */
    /* JADX WARN: Type inference failed for: r6v2, types: [com.google.android.systemui.columbus.actions.UserSelectedAction$wakefulnessLifecycleObserver$1] */
    public UserSelectedAction(Context context, ColumbusSettings columbusSettings, Map<String, UserAction> map, TakeScreenshot takeScreenshot, KeyguardStateController keyguardStateController, PowerManagerWrapper powerManagerWrapper, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context, null);
        this.userSelectedActions = map;
        this.takeScreenshot = takeScreenshot;
        this.keyguardStateController = keyguardStateController;
        this.powerManager = powerManagerWrapper;
        ColumbusSettings.ColumbusSettingsChangeListener userSelectedAction$settingsChangeListener$1 = new ColumbusSettings.ColumbusSettingsChangeListener() { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$settingsChangeListener$1
            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onAlertSilenceEnabledChange(boolean z) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onColumbusEnabledChange(boolean z) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onLowSensitivityChange(boolean z) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedAppChange(String str) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedAppShortcutChange(String str) {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onUseApSensorChange() {
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public final void onSelectedActionChange(String str) {
                UserSelectedAction userSelectedAction = UserSelectedAction.this;
                Objects.requireNonNull(userSelectedAction);
                UserAction orDefault = userSelectedAction.userSelectedActions.getOrDefault(str, userSelectedAction.takeScreenshot);
                if (!Intrinsics.areEqual(orDefault, UserSelectedAction.this.currentAction)) {
                    UserSelectedAction.this.currentAction.onGestureDetected(0, null);
                    UserSelectedAction.this.currentAction = orDefault;
                    Log.i("Columbus/SelectedAction", Intrinsics.stringPlus("User Action selected: ", orDefault));
                    UserSelectedAction.this.updateAvailable();
                }
            }
        };
        UserAction orDefault = map.getOrDefault(columbusSettings.selectedAction(), takeScreenshot);
        this.currentAction = orDefault;
        Log.i("Columbus/SelectedAction", Intrinsics.stringPlus("User Action selected: ", orDefault));
        columbusSettings.registerColumbusSettingsChangeListener(userSelectedAction$settingsChangeListener$1);
        Action.Listener userSelectedAction$sublistener$1 = new Action.Listener() { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$sublistener$1
            @Override // com.google.android.systemui.columbus.actions.Action.Listener
            public final void onActionAvailabilityChanged(Action action) {
                if (Intrinsics.areEqual(UserSelectedAction.this.currentAction, action)) {
                    UserSelectedAction.this.updateAvailable();
                }
            }
        };
        for (UserAction userAction : map.values()) {
            Objects.requireNonNull(userAction);
            userAction.listeners.add(userSelectedAction$sublistener$1);
        }
        this.keyguardStateController.addCallback(this.keyguardMonitorCallback);
        wakefulnessLifecycle.addObserver(this.wakefulnessLifecycleObserver);
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.currentAction.getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.onGestureDetected(i, detectionProperties);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.onTrigger(detectionProperties);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String toString() {
        return super.toString() + " [currentAction -> " + this.currentAction + ']';
    }

    public final void updateAvailable() {
        Boolean bool;
        UserAction userAction = this.currentAction;
        Objects.requireNonNull(userAction);
        if (!userAction.isAvailable) {
            setAvailable(false);
            return;
        }
        if (!this.currentAction.availableOnScreenOff()) {
            PowerManagerWrapper powerManagerWrapper = this.powerManager;
            Objects.requireNonNull(powerManagerWrapper);
            PowerManager powerManager = powerManagerWrapper.powerManager;
            if (powerManager == null) {
                bool = null;
            } else {
                bool = Boolean.valueOf(powerManager.isInteractive());
            }
            if (!Intrinsics.areEqual(bool, Boolean.TRUE)) {
                setAvailable(false);
                return;
            }
        }
        if (this.currentAction.availableOnLockscreen() || !this.keyguardStateController.isShowing()) {
            setAvailable(true);
        } else {
            setAvailable(false);
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void updateFeedbackEffects(int i, GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.updateFeedbackEffects(i, detectionProperties);
    }
}
