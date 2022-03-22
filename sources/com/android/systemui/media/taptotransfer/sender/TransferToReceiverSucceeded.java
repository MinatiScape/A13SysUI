package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.view.View;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import java.util.Objects;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public final class TransferToReceiverSucceeded extends ChipStateSender {
    public final String otherDeviceName;
    public final IUndoMediaTransferCallback undoCallback;

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final String getChipTextString(Context context) {
        return context.getString(2131952750, this.otherDeviceName);
    }

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final View.OnClickListener undoClickListener(final MediaTttChipControllerSender mediaTttChipControllerSender) {
        if (this.undoCallback == null) {
            return null;
        }
        return new View.OnClickListener() { // from class: com.android.systemui.media.taptotransfer.sender.TransferToReceiverSucceeded$undoClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TransferToReceiverSucceeded transferToReceiverSucceeded = TransferToReceiverSucceeded.this;
                Objects.requireNonNull(transferToReceiverSucceeded);
                transferToReceiverSucceeded.undoCallback.onUndoTriggered();
                MediaTttChipControllerSender mediaTttChipControllerSender2 = mediaTttChipControllerSender;
                TransferToReceiverSucceeded transferToReceiverSucceeded2 = TransferToReceiverSucceeded.this;
                Objects.requireNonNull(transferToReceiverSucceeded2);
                mediaTttChipControllerSender2.displayChip(new TransferToThisDeviceTriggered(transferToReceiverSucceeded2.appPackageName));
            }
        };
    }

    public TransferToReceiverSucceeded(String str, String str2, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        super(str);
        this.otherDeviceName = str2;
        this.undoCallback = iUndoMediaTransferCallback;
    }
}
