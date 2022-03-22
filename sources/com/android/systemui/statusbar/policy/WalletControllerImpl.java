package com.android.systemui.statusbar.policy;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
/* compiled from: WalletControllerImpl.kt */
/* loaded from: classes.dex */
public final class WalletControllerImpl implements WalletController {
    public final QuickAccessWalletClient quickAccessWalletClient;

    @Override // com.android.systemui.statusbar.policy.WalletController
    public final Integer getWalletPosition() {
        if (this.quickAccessWalletClient.isWalletServiceAvailable()) {
            Log.i("WalletControllerImpl", "Setting WalletTile position: 3");
            return 3;
        }
        Log.i("WalletControllerImpl", "Setting WalletTile position: null");
        return null;
    }

    public WalletControllerImpl(QuickAccessWalletClient quickAccessWalletClient) {
        this.quickAccessWalletClient = quickAccessWalletClient;
    }
}
