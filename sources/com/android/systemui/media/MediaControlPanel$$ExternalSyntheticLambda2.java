package com.android.systemui.media;

import android.view.View;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.volume.Events;
import com.android.systemui.volume.VolumeDialogImpl;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                MediaControlPanel mediaControlPanel = (MediaControlPanel) this.f$0;
                Objects.requireNonNull(mediaControlPanel);
                if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                    mediaControlPanel.closeGuts(false);
                    return;
                }
                return;
            case 1:
                TvUnblockSensorActivity tvUnblockSensorActivity = (TvUnblockSensorActivity) this.f$0;
                int i = TvUnblockSensorActivity.$r8$clinit;
                Objects.requireNonNull(tvUnblockSensorActivity);
                tvUnblockSensorActivity.finish();
                return;
            default:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                volumeDialogImpl.hideCaptionsTooltip();
                Events.writeEvent(22, new Object[0]);
                return;
        }
    }
}
