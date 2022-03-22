package com.android.systemui.media;

import android.view.View;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda1(Object obj, int i) {
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
                    mediaControlPanel.mActivityStarter.startActivity(MediaControlPanel.SETTINGS_INTENT, true);
                    return;
                }
                return;
            default:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.dismiss();
                return;
        }
    }
}
