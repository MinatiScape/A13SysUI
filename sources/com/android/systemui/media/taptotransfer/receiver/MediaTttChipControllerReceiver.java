package com.android.systemui.media.taptotransfer.receiver;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaTttChipControllerReceiver.kt */
/* loaded from: classes.dex */
public final class MediaTttChipControllerReceiver extends MediaTttChipControllerCommon<ChipStateReceiver> {
    public final Handler mainHandler;

    @Override // com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon
    public final void updateChipView(ChipStateReceiver chipStateReceiver, ViewGroup viewGroup) {
        setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(chipStateReceiver, viewGroup);
    }

    public MediaTttChipControllerReceiver(CommandQueue commandQueue, Context context, WindowManager windowManager, DelayableExecutor delayableExecutor, Handler handler) {
        super(context, windowManager, delayableExecutor, 2131624267);
        this.mainHandler = handler;
        commandQueue.addCallback(new CommandQueue.Callbacks() { // from class: com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver$commandQueueCallbacks$1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, final CharSequence charSequence) {
                final MediaTttChipControllerReceiver mediaTttChipControllerReceiver = MediaTttChipControllerReceiver.this;
                Objects.requireNonNull(mediaTttChipControllerReceiver);
                if (i == 0) {
                    final String packageName = mediaRoute2Info.getPackageName();
                    if (icon == null) {
                        mediaTttChipControllerReceiver.displayChip(new ChipStateReceiver(packageName, null, charSequence));
                    } else {
                        icon.loadDrawableAsync(mediaTttChipControllerReceiver.context, new Icon.OnDrawableLoadedListener() { // from class: com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver$updateMediaTapToTransferReceiverDisplay$1
                            @Override // android.graphics.drawable.Icon.OnDrawableLoadedListener
                            public final void onDrawableLoaded(Drawable drawable) {
                                MediaTttChipControllerReceiver.this.displayChip(new ChipStateReceiver(packageName, drawable, charSequence));
                            }
                        }, mediaTttChipControllerReceiver.mainHandler);
                    }
                } else if (i != 1) {
                    Log.e("MediaTapToTransferRcvr", Intrinsics.stringPlus("Unhandled MediaTransferReceiverState ", Integer.valueOf(i)));
                } else {
                    ViewGroup viewGroup = mediaTttChipControllerReceiver.chipView;
                    if (viewGroup != null) {
                        mediaTttChipControllerReceiver.windowManager.removeView(viewGroup);
                        mediaTttChipControllerReceiver.chipView = null;
                    }
                }
            }
        });
    }
}
