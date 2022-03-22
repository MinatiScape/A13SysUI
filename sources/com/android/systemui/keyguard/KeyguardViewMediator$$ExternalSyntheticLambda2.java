package com.android.systemui.keyguard;

import android.content.ContentResolver;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedSettingsUtil;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda2(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardViewMediator keyguardViewMediator = (KeyguardViewMediator) this.f$0;
                int i = this.f$1;
                boolean z = KeyguardViewMediator.DEBUG;
                Objects.requireNonNull(keyguardViewMediator);
                if (keyguardViewMediator.mLockPatternUtils.isSecure(i)) {
                    keyguardViewMediator.mLockPatternUtils.getDevicePolicyManager().reportKeyguardDismissed(i);
                    return;
                }
                return;
            default:
                OneHandedController.OneHandedImpl oneHandedImpl = (OneHandedController.OneHandedImpl) this.f$0;
                int i2 = this.f$1;
                int i3 = OneHandedController.OneHandedImpl.$r8$clinit;
                Objects.requireNonNull(oneHandedImpl);
                OneHandedController oneHandedController = OneHandedController.this;
                Objects.requireNonNull(oneHandedController);
                OneHandedSettingsUtil oneHandedSettingsUtil = oneHandedController.mOneHandedSettingsUtil;
                ContentResolver contentResolver = oneHandedController.mContext.getContentResolver();
                OneHandedController.AnonymousClass5 r3 = oneHandedController.mEnabledObserver;
                Objects.requireNonNull(oneHandedSettingsUtil);
                if (contentResolver != null) {
                    contentResolver.unregisterContentObserver(r3);
                }
                OneHandedSettingsUtil oneHandedSettingsUtil2 = oneHandedController.mOneHandedSettingsUtil;
                ContentResolver contentResolver2 = oneHandedController.mContext.getContentResolver();
                OneHandedController.AnonymousClass5 r32 = oneHandedController.mSwipeToNotificationEnabledObserver;
                Objects.requireNonNull(oneHandedSettingsUtil2);
                if (contentResolver2 != null) {
                    contentResolver2.unregisterContentObserver(r32);
                }
                OneHandedSettingsUtil oneHandedSettingsUtil3 = oneHandedController.mOneHandedSettingsUtil;
                ContentResolver contentResolver3 = oneHandedController.mContext.getContentResolver();
                OneHandedController.AnonymousClass5 r33 = oneHandedController.mShortcutEnabledObserver;
                Objects.requireNonNull(oneHandedSettingsUtil3);
                if (contentResolver3 != null) {
                    contentResolver3.unregisterContentObserver(r33);
                }
                oneHandedController.mUserId = i2;
                oneHandedController.registerSettingObservers(i2);
                oneHandedController.updateSettings();
                oneHandedController.updateOneHandedEnabled();
                return;
        }
    }
}
