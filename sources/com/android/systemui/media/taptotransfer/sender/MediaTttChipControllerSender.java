package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaTttChipControllerSender.kt */
/* loaded from: classes.dex */
public final class MediaTttChipControllerSender extends MediaTttChipControllerCommon<ChipStateSender> {
    public final DelayableExecutor mainExecutor;

    @Override // com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon
    public final void updateChipView(ChipStateSender chipStateSender, ViewGroup viewGroup) {
        int i;
        int i2;
        ChipStateSender chipStateSender2 = chipStateSender;
        setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(chipStateSender2, viewGroup);
        TextView textView = (TextView) viewGroup.requireViewById(2131429024);
        textView.setText(chipStateSender2.getChipTextString(textView.getContext()));
        View requireViewById = viewGroup.requireViewById(2131428269);
        int i3 = 0;
        if (chipStateSender2.showLoading()) {
            i = 0;
        } else {
            i = 8;
        }
        requireViewById.setVisibility(i);
        View requireViewById2 = viewGroup.requireViewById(2131429146);
        View.OnClickListener undoClickListener = chipStateSender2.undoClickListener(this);
        requireViewById2.setOnClickListener(undoClickListener);
        if (undoClickListener != null) {
            i2 = 0;
        } else {
            i2 = 8;
        }
        requireViewById2.setVisibility(i2);
        boolean z = chipStateSender2 instanceof TransferFailed;
        View requireViewById3 = viewGroup.requireViewById(2131427956);
        if (!z) {
            i3 = 8;
        }
        requireViewById3.setVisibility(i3);
    }

    public MediaTttChipControllerSender(CommandQueue commandQueue, Context context, WindowManager windowManager, DelayableExecutor delayableExecutor) {
        super(context, windowManager, delayableExecutor, 2131624266);
        this.mainExecutor = delayableExecutor;
        commandQueue.addCallback(new CommandQueue.Callbacks() { // from class: com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender$commandQueueCallbacks$1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
                MediaTttChipControllerSender mediaTttChipControllerSender = MediaTttChipControllerSender.this;
                Objects.requireNonNull(mediaTttChipControllerSender);
                String packageName = mediaRoute2Info.getPackageName();
                String obj = mediaRoute2Info.getName().toString();
                ChipStateSender chipStateSender = null;
                switch (i) {
                    case 0:
                        chipStateSender = new AlmostCloseToStartCast(packageName, obj);
                        break;
                    case 1:
                        chipStateSender = new AlmostCloseToEndCast(packageName, obj);
                        break;
                    case 2:
                        chipStateSender = new TransferToReceiverTriggered(packageName, obj);
                        break;
                    case 3:
                        chipStateSender = new TransferToThisDeviceTriggered(packageName);
                        break;
                    case 4:
                        chipStateSender = new TransferToReceiverSucceeded(packageName, obj, iUndoMediaTransferCallback);
                        break;
                    case 5:
                        chipStateSender = new TransferToThisDeviceSucceeded(packageName, obj, iUndoMediaTransferCallback);
                        break;
                    case FalsingManager.VERSION /* 6 */:
                    case 7:
                        chipStateSender = new TransferFailed(packageName);
                        break;
                    case 8:
                        ViewGroup viewGroup = mediaTttChipControllerSender.chipView;
                        if (viewGroup != null) {
                            mediaTttChipControllerSender.windowManager.removeView(viewGroup);
                            mediaTttChipControllerSender.chipView = null;
                            break;
                        }
                        break;
                    default:
                        Log.e("MediaTapToTransferSender", Intrinsics.stringPlus("Unhandled MediaTransferSenderState ", Integer.valueOf(i)));
                        break;
                }
                if (chipStateSender != null) {
                    mediaTttChipControllerSender.displayChip(chipStateSender);
                }
            }
        });
    }
}
