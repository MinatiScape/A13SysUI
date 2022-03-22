package com.android.systemui.statusbar.phone;

import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.TranscriptionView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BiometricUnlockController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BiometricUnlockController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BiometricUnlockController biometricUnlockController = (BiometricUnlockController) this.f$0;
                BiometricUnlockController.PendingAuthenticated pendingAuthenticated = (BiometricUnlockController.PendingAuthenticated) this.f$1;
                Objects.requireNonNull(biometricUnlockController);
                biometricUnlockController.onBiometricAuthenticated(pendingAuthenticated.userId, pendingAuthenticated.biometricSourceType, pendingAuthenticated.isStrongBiometric);
                return;
            case 1:
                NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender = (NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) this.f$1;
                Objects.requireNonNull(legacyRemoteInputLifetimeExtender);
                if (legacyRemoteInputLifetimeExtender.mEntriesKeptForRemoteInputActive.remove(notificationEntry)) {
                    NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = legacyRemoteInputLifetimeExtender.mNotificationLifetimeFinishedCallback;
                    Objects.requireNonNull(notificationEntry);
                    ((ScreenshotController$$ExternalSyntheticLambda3) notificationSafeToRemoveCallback).onSafeToRemove(notificationEntry.mKey);
                    return;
                }
                return;
            default:
                TranscriptionController transcriptionController = (TranscriptionController) this.f$0;
                Objects.requireNonNull(transcriptionController);
                ((TranscriptionView) transcriptionController.mViewMap.get(TranscriptionController.State.TRANSCRIPTION)).setTranscription((String) this.f$1);
                return;
        }
    }
}
