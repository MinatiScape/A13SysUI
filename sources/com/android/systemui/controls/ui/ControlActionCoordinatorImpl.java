package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.service.controls.Control;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskViewFactory;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl implements ControlActionCoordinator {
    public LinkedHashSet actionsInProgress = new LinkedHashSet();
    public Context activityContext;
    public final ActivityStarter activityStarter;
    public final DelayableExecutor bgExecutor;
    public final Context context;
    public final ControlsMetricsLogger controlsMetricsLogger;
    public Dialog dialog;
    public final KeyguardStateController keyguardStateController;
    public Action pendingAction;
    public final Optional<TaskViewFactory> taskViewFactory;
    public final DelayableExecutor uiExecutor;
    public final VibratorHelper vibrator;

    /* compiled from: ControlActionCoordinatorImpl.kt */
    /* loaded from: classes.dex */
    public final class Action {
        public final boolean blockable;
        public final String controlId;
        public final Function0<Unit> f;

        public Action(String str, Function0<Unit> function0, boolean z) {
            this.controlId = str;
            this.f = function0;
            this.blockable = z;
        }

        public final void invoke() {
            boolean z;
            if (this.blockable) {
                final ControlActionCoordinatorImpl controlActionCoordinatorImpl = ControlActionCoordinatorImpl.this;
                final String str = this.controlId;
                Objects.requireNonNull(controlActionCoordinatorImpl);
                if (controlActionCoordinatorImpl.actionsInProgress.add(str)) {
                    controlActionCoordinatorImpl.uiExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$shouldRunAction$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ControlActionCoordinatorImpl.this.actionsInProgress.remove(str);
                        }
                    }, 3000L);
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    return;
                }
            }
            this.f.invoke();
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void drag(boolean z) {
        if (z) {
            VibrationEffect vibrationEffect = Vibrations.rangeEdgeEffect;
            VibratorHelper vibratorHelper = this.vibrator;
            Objects.requireNonNull(vibratorHelper);
            if (vibratorHelper.hasVibrator()) {
                vibratorHelper.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda0(vibratorHelper, vibrationEffect, 0));
                return;
            }
            return;
        }
        VibrationEffect vibrationEffect2 = Vibrations.rangeMiddleEffect;
        VibratorHelper vibratorHelper2 = this.vibrator;
        Objects.requireNonNull(vibratorHelper2);
        if (vibratorHelper2.hasVibrator()) {
            vibratorHelper2.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda0(vibratorHelper2, vibrationEffect2, 0));
        }
    }

    @VisibleForTesting
    public final void bouncerOrRun(final Action action, boolean z) {
        if (!this.keyguardStateController.isShowing() || !z) {
            action.invoke();
            return;
        }
        if (isLocked()) {
            this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            this.pendingAction = action;
        }
        this.activityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$1
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                Log.d("ControlsUiController", "Device unlocked, invoking controls action");
                ControlActionCoordinatorImpl.Action.this.invoke();
                return true;
            }
        }, new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$2
            @Override // java.lang.Runnable
            public final void run() {
                ControlActionCoordinatorImpl.this.pendingAction = null;
            }
        }, true);
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void closeDialogs() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        this.dialog = null;
    }

    @VisibleForTesting
    public final Action createAction(String str, Function0<Unit> function0, boolean z) {
        return new Action(str, function0, z);
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void enableActionOnTouch(String str) {
        this.actionsInProgress.remove(str);
    }

    public final boolean isLocked() {
        return !this.keyguardStateController.isUnlocked();
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void longPress(ControlViewHolder controlViewHolder) {
        this.controlsMetricsLogger.longPress(controlViewHolder, isLocked());
        ControlWithState cws = controlViewHolder.getCws();
        Objects.requireNonNull(cws);
        ControlInfo controlInfo = cws.ci;
        Objects.requireNonNull(controlInfo);
        bouncerOrRun(createAction(controlInfo.controlId, new ControlActionCoordinatorImpl$longPress$1(controlViewHolder, this), false), isAuthRequired(controlViewHolder));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void setValue(ControlViewHolder controlViewHolder, String str, float f) {
        this.controlsMetricsLogger.drag(controlViewHolder, isLocked());
        ControlWithState cws = controlViewHolder.getCws();
        Objects.requireNonNull(cws);
        ControlInfo controlInfo = cws.ci;
        Objects.requireNonNull(controlInfo);
        bouncerOrRun(createAction(controlInfo.controlId, new ControlActionCoordinatorImpl$setValue$1(controlViewHolder, str, f), false), isAuthRequired(controlViewHolder));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void toggle(ControlViewHolder controlViewHolder, String str, boolean z) {
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        ControlWithState cws = controlViewHolder.getCws();
        Objects.requireNonNull(cws);
        ControlInfo controlInfo = cws.ci;
        Objects.requireNonNull(controlInfo);
        bouncerOrRun(createAction(controlInfo.controlId, new ControlActionCoordinatorImpl$toggle$1(controlViewHolder, str, z), true), isAuthRequired(controlViewHolder));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void touch(ControlViewHolder controlViewHolder, String str, Control control) {
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        boolean usePanel = controlViewHolder.usePanel();
        ControlWithState cws = controlViewHolder.getCws();
        Objects.requireNonNull(cws);
        ControlInfo controlInfo = cws.ci;
        Objects.requireNonNull(controlInfo);
        bouncerOrRun(createAction(controlInfo.controlId, new ControlActionCoordinatorImpl$touch$1(controlViewHolder, this, control, str), usePanel), isAuthRequired(controlViewHolder));
    }

    public ControlActionCoordinatorImpl(Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ActivityStarter activityStarter, KeyguardStateController keyguardStateController, Optional<TaskViewFactory> optional, ControlsMetricsLogger controlsMetricsLogger, VibratorHelper vibratorHelper) {
        this.context = context;
        this.bgExecutor = delayableExecutor;
        this.uiExecutor = delayableExecutor2;
        this.activityStarter = activityStarter;
        this.keyguardStateController = keyguardStateController;
        this.taskViewFactory = optional;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.vibrator = vibratorHelper;
    }

    public static boolean isAuthRequired(ControlViewHolder controlViewHolder) {
        ControlWithState cws = controlViewHolder.getCws();
        Objects.requireNonNull(cws);
        Control control = cws.control;
        if (control == null) {
            return true;
        }
        return control.isAuthRequired();
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void runPendingAction(String str) {
        String str2;
        if (!isLocked()) {
            Action action = this.pendingAction;
            if (action == null) {
                str2 = null;
            } else {
                str2 = action.controlId;
            }
            if (Intrinsics.areEqual(str2, str)) {
                Action action2 = this.pendingAction;
                if (action2 != null) {
                    action2.invoke();
                }
                this.pendingAction = null;
            }
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public final void setActivityContext(Context context) {
        this.activityContext = context;
    }
}
