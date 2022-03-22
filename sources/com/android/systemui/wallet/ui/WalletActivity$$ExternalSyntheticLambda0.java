package com.android.systemui.wallet.ui;

import android.app.Activity;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WalletActivity$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Activity f$0;

    public /* synthetic */ WalletActivity$$ExternalSyntheticLambda0(Activity activity, int i) {
        this.$r8$classId = i;
        this.f$0 = activity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                final WalletActivity walletActivity = (WalletActivity) this.f$0;
                int i = WalletActivity.$r8$clinit;
                Objects.requireNonNull(walletActivity);
                if (walletActivity.mWalletClient.createWalletIntent() == null) {
                    Log.w("WalletActivity", "Unable to create wallet app intent.");
                    return;
                } else if (!walletActivity.mKeyguardStateController.isUnlocked() && walletActivity.mFalsingManager.isFalseTap(1)) {
                    return;
                } else {
                    if (walletActivity.mKeyguardStateController.isUnlocked()) {
                        walletActivity.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
                        walletActivity.mActivityStarter.startActivity(walletActivity.mWalletClient.createWalletIntent(), true);
                        walletActivity.finish();
                        return;
                    }
                    walletActivity.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_SHOW_ALL_BUTTON);
                    walletActivity.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda1
                        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                        public final boolean onDismiss() {
                            WalletActivity walletActivity2 = WalletActivity.this;
                            int i2 = WalletActivity.$r8$clinit;
                            Objects.requireNonNull(walletActivity2);
                            walletActivity2.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
                            walletActivity2.mActivityStarter.startActivity(walletActivity2.mWalletClient.createWalletIntent(), true);
                            walletActivity2.finish();
                            return false;
                        }
                    }, false, true);
                    return;
                }
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.navigateToGameModeView();
                return;
        }
    }
}
