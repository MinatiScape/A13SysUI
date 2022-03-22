package com.android.systemui.media;

import android.view.View;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda0(Object obj, int i) {
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
            case 1:
                ScreenshotView.$r8$lambda$b5uTZBQem2CWFC7azy4AO9g0ukw((ScreenshotView) this.f$0);
                return;
            default:
                KeyguardBottomAreaView.m97$r8$lambda$swNNSxHxCV3LqHmDl5jM82UINM((KeyguardBottomAreaView) this.f$0);
                return;
        }
    }
}
