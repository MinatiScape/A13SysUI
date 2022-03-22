package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WalletView$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WalletView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                WalletCardViewInfo walletCardViewInfo = (WalletCardViewInfo) this.f$0;
                int i = WalletView.$r8$clinit;
                try {
                    walletCardViewInfo.getPendingIntent().send();
                    return;
                } catch (PendingIntent.CanceledException unused) {
                    Log.w("WalletView", "Error sending pending intent for wallet card.");
                    return;
                }
            case 1:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i2 = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                authBiometricView.startTransitionToCredentialUI();
                return;
            case 2:
                LongScreenshotActivity.$r8$lambda$qOUpLbBDnQlAC3CzBI4dsyuYNs4((LongScreenshotActivity) this.f$0, view);
                return;
            default:
                AmbientIndicationContainer ambientIndicationContainer = (AmbientIndicationContainer) this.f$0;
                int i3 = AmbientIndicationContainer.$r8$clinit;
                Objects.requireNonNull(ambientIndicationContainer);
                if (ambientIndicationContainer.mFavoritingIntent != null) {
                    ambientIndicationContainer.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), view, "AMBIENT_MUSIC_CLICK");
                    AmbientIndicationContainer.sendBroadcastWithoutDismissingKeyguard(ambientIndicationContainer.mFavoritingIntent);
                    return;
                }
                ambientIndicationContainer.onTextClick(view);
                return;
        }
    }
}
