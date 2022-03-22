package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.util.ArraySet;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1;
import java.util.NoSuchElementException;
import java.util.Objects;
/* compiled from: RemoteInputViewController.kt */
/* loaded from: classes.dex */
public final class RemoteInputViewControllerImpl implements RemoteInputViewController {
    public NotificationRemoteInputManager.BouncerChecker bouncerChecker;
    public final NotificationEntry entry;
    public boolean isBound;
    public PendingIntent pendingIntent;
    public RemoteInput remoteInput;
    public final RemoteInputController remoteInputController;
    public final RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler;
    public RemoteInput[] remoteInputs;
    public final ShortcutManager shortcutManager;
    public final UiEventLogger uiEventLogger;
    public final RemoteInputView view;
    public final ArraySet<OnSendRemoteInputListener> onSendListeners = new ArraySet<>();
    public final RemoteInputViewControllerImpl$onFocusChangeListener$1 onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl$onFocusChangeListener$1
        @Override // android.view.View.OnFocusChangeListener
        public final void onFocusChange(View view, boolean z) {
            RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler = RemoteInputViewControllerImpl.this.remoteInputQuickSettingsDisabler;
            Objects.requireNonNull(remoteInputQuickSettingsDisabler);
            if (remoteInputQuickSettingsDisabler.remoteInputActive != z) {
                remoteInputQuickSettingsDisabler.remoteInputActive = z;
                remoteInputQuickSettingsDisabler.commandQueue.recomputeDisableFlags(remoteInputQuickSettingsDisabler.context.getDisplayId(), true);
            }
        }
    };
    public final RemoteInputViewControllerImpl$onSendRemoteInputListener$1 onSendRemoteInputListener = new Runnable() { // from class: com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl$onSendRemoteInputListener$1
        /* JADX WARN: Removed duplicated region for block: B:45:0x0179  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x01ac  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 697
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl$onSendRemoteInputListener$1.run():void");
        }
    };

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final boolean updatePendingIntentFromActions(Notification.Action[] actionArr) {
        Intent intent;
        boolean z;
        RemoteInput[] remoteInputs;
        RemoteInput remoteInput;
        if (actionArr == null) {
            return false;
        }
        PendingIntent pendingIntent = this.pendingIntent;
        if (pendingIntent == null) {
            intent = null;
        } else {
            intent = pendingIntent.getIntent();
        }
        if (intent == null) {
            return false;
        }
        int i = 0;
        while (true) {
            if (i < actionArr.length) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                return false;
            }
            int i2 = i + 1;
            try {
                Notification.Action action = actionArr[i];
                PendingIntent pendingIntent2 = action.actionIntent;
                if (!(pendingIntent2 == null || (remoteInputs = action.getRemoteInputs()) == null || !intent.filterEquals(pendingIntent2.getIntent()))) {
                    int length = remoteInputs.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length) {
                            remoteInput = null;
                            break;
                        }
                        remoteInput = remoteInputs[i3];
                        i3++;
                        if (remoteInput.getAllowFreeFormInput()) {
                            break;
                        }
                    }
                    if (remoteInput != null) {
                        this.pendingIntent = pendingIntent2;
                        this.remoteInput = remoteInput;
                        this.remoteInputs = remoteInputs;
                        RemoteInputView remoteInputView = this.view;
                        Objects.requireNonNull(remoteInputView);
                        remoteInputView.mPendingIntent = pendingIntent2;
                        this.view.setRemoteInput(remoteInputs, remoteInput, null);
                        return true;
                    }
                }
                i = i2;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void addOnSendRemoteInputListener(NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1) {
        this.onSendListeners.add(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void bind() {
        if (!this.isBound) {
            this.isBound = true;
            RemoteInputView remoteInputView = this.view;
            RemoteInputViewControllerImpl$onFocusChangeListener$1 remoteInputViewControllerImpl$onFocusChangeListener$1 = this.onFocusChangeListener;
            Objects.requireNonNull(remoteInputView);
            remoteInputView.mEditTextFocusChangeListeners.add(remoteInputViewControllerImpl$onFocusChangeListener$1);
            RemoteInputView remoteInputView2 = this.view;
            RemoteInputViewControllerImpl$onSendRemoteInputListener$1 remoteInputViewControllerImpl$onSendRemoteInputListener$1 = this.onSendRemoteInputListener;
            Objects.requireNonNull(remoteInputView2);
            remoteInputView2.mOnSendListeners.add(remoteInputViewControllerImpl$onSendRemoteInputListener$1);
        }
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void removeOnSendRemoteInputListener(NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1) {
        this.onSendListeners.remove(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void unbind() {
        if (this.isBound) {
            this.isBound = false;
            RemoteInputView remoteInputView = this.view;
            RemoteInputViewControllerImpl$onFocusChangeListener$1 remoteInputViewControllerImpl$onFocusChangeListener$1 = this.onFocusChangeListener;
            Objects.requireNonNull(remoteInputView);
            remoteInputView.mEditTextFocusChangeListeners.remove(remoteInputViewControllerImpl$onFocusChangeListener$1);
            RemoteInputView remoteInputView2 = this.view;
            RemoteInputViewControllerImpl$onSendRemoteInputListener$1 remoteInputViewControllerImpl$onSendRemoteInputListener$1 = this.onSendRemoteInputListener;
            Objects.requireNonNull(remoteInputView2);
            remoteInputView2.mOnSendListeners.remove(remoteInputViewControllerImpl$onSendRemoteInputListener$1);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl$onFocusChangeListener$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl$onSendRemoteInputListener$1] */
    public RemoteInputViewControllerImpl(RemoteInputView remoteInputView, NotificationEntry notificationEntry, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, RemoteInputController remoteInputController, ShortcutManager shortcutManager, UiEventLogger uiEventLogger) {
        this.view = remoteInputView;
        this.entry = notificationEntry;
        this.remoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.remoteInputController = remoteInputController;
        this.shortcutManager = shortcutManager;
        this.uiEventLogger = uiEventLogger;
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void setBouncerChecker(NotificationRemoteInputManager$$ExternalSyntheticLambda0 notificationRemoteInputManager$$ExternalSyntheticLambda0) {
        this.bouncerChecker = notificationRemoteInputManager$$ExternalSyntheticLambda0;
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void setRemoteInput(RemoteInput remoteInput) {
        this.remoteInput = remoteInput;
    }

    @Override // com.android.systemui.statusbar.policy.RemoteInputViewController
    public final void setRemoteInputs(RemoteInput[] remoteInputArr) {
        this.remoteInputs = remoteInputArr;
    }
}
