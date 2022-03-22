package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.view.View;
import com.android.systemui.screenshot.ScreenshotEvent;
import com.android.systemui.screenshot.ScreenshotView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda6 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda6(Object obj, String str, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = str;
        this.f$2 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                MediaControlPanel mediaControlPanel = (MediaControlPanel) this.f$0;
                String str = this.f$1;
                MediaData mediaData = (MediaData) this.f$2;
                Objects.requireNonNull(mediaControlPanel);
                if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                    mediaControlPanel.logSmartspaceCardReported(761, false, 0, 0);
                    if (mediaControlPanel.mKey != null) {
                        mediaControlPanel.closeGuts(false);
                        if (!mediaControlPanel.mMediaDataManagerLazy.get().dismissMediaData(mediaControlPanel.mKey, 600L)) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Manager failed to dismiss media ");
                            m.append(mediaControlPanel.mKey);
                            Log.w("MediaControlPanel", m.toString());
                            mediaControlPanel.mMediaCarouselController.removePlayer(str, false, false);
                            return;
                        }
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Dismiss media with null notification. Token uid=");
                    Objects.requireNonNull(mediaData);
                    sb.append(mediaData.token.getUid());
                    Log.w("MediaControlPanel", sb.toString());
                    return;
                }
                return;
            default:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                String str2 = this.f$1;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_REQUESTED, 0, str2);
                ((Runnable) this.f$2).run();
                return;
        }
    }
}
