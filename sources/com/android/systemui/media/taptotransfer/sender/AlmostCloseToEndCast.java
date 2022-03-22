package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public final class AlmostCloseToEndCast extends ChipStateSender {
    public final String otherDeviceName;

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final String getChipTextString(Context context) {
        return context.getString(2131952732, this.otherDeviceName);
    }

    public AlmostCloseToEndCast(String str, String str2) {
        super(str);
        this.otherDeviceName = str2;
    }
}
