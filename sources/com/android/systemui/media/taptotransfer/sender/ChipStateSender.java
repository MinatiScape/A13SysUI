package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.view.View;
import com.android.systemui.media.taptotransfer.common.MediaTttChipState;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public abstract class ChipStateSender extends MediaTttChipState {
    public abstract String getChipTextString(Context context);

    public boolean showLoading() {
        return this instanceof TransferToReceiverTriggered;
    }

    public View.OnClickListener undoClickListener(MediaTttChipControllerSender mediaTttChipControllerSender) {
        return null;
    }

    public ChipStateSender(String str) {
        super(str);
    }
}
