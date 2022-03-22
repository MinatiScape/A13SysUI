package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.view.View;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import java.util.Objects;
/* compiled from: ChipStateSender.kt */
/* loaded from: classes.dex */
public final class TransferToThisDeviceSucceeded extends ChipStateSender {
    public final String otherDeviceName;
    public final IUndoMediaTransferCallback undoCallback;

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final View.OnClickListener undoClickListener(final MediaTttChipControllerSender mediaTttChipControllerSender) {
        if (this.undoCallback == null) {
            return null;
        }
        return new View.OnClickListener() { // from class: com.android.systemui.media.taptotransfer.sender.TransferToThisDeviceSucceeded$undoClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TransferToThisDeviceSucceeded transferToThisDeviceSucceeded = TransferToThisDeviceSucceeded.this;
                Objects.requireNonNull(transferToThisDeviceSucceeded);
                transferToThisDeviceSucceeded.undoCallback.onUndoTriggered();
                MediaTttChipControllerSender mediaTttChipControllerSender2 = mediaTttChipControllerSender;
                TransferToThisDeviceSucceeded transferToThisDeviceSucceeded2 = TransferToThisDeviceSucceeded.this;
                Objects.requireNonNull(transferToThisDeviceSucceeded2);
                mediaTttChipControllerSender2.displayChip(new TransferToReceiverTriggered(transferToThisDeviceSucceeded2.appPackageName, TransferToThisDeviceSucceeded.this.otherDeviceName));
            }
        };
    }

    public TransferToThisDeviceSucceeded(String str, String str2, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        super(str);
        this.otherDeviceName = str2;
        this.undoCallback = iUndoMediaTransferCallback;
    }

    @Override // com.android.systemui.media.taptotransfer.sender.ChipStateSender
    public final String getChipTextString(Context context) {
        return context.getString(2131952751);
    }
}
