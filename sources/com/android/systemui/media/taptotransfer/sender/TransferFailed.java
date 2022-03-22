package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public final class TransferFailed extends ChipStateSender {
    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final String getChipTextString(Context context) {
        return context.getString(2131952749);
    }

    public TransferFailed(String str) {
        super(str);
    }
}
