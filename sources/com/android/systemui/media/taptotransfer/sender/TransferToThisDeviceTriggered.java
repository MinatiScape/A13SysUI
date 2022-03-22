package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public final class TransferToThisDeviceTriggered extends ChipStateSender {
    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final boolean showLoading() {
        return true;
    }

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final String getChipTextString(Context context) {
        return context.getString(2131952751);
    }

    public TransferToThisDeviceTriggered(String str) {
        super(str);
    }
}
