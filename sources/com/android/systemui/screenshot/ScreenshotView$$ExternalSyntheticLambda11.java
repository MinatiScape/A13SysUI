package com.android.systemui.screenshot;

import android.content.Intent;
import android.view.View;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.accessibility.floatingmenu.MigrationTooltipView;
import com.android.systemui.media.dialog.MediaOutputAdapter;
import com.android.systemui.screenshot.ScreenshotController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda11 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda11(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_EDIT_TAPPED, 0, screenshotView.mPackageName);
                screenshotView.startSharedTransition(((ScreenshotController.SavedImageData) this.f$1).editTransition.get());
                return;
            case 1:
                MigrationTooltipView migrationTooltipView = (MigrationTooltipView) this.f$0;
                int i2 = MigrationTooltipView.$r8$clinit;
                Objects.requireNonNull(migrationTooltipView);
                migrationTooltipView.getContext().startActivity((Intent) this.f$1);
                migrationTooltipView.hide();
                return;
            default:
                MediaOutputAdapter.MediaDeviceViewHolder mediaDeviceViewHolder = (MediaOutputAdapter.MediaDeviceViewHolder) this.f$0;
                int i3 = MediaOutputAdapter.MediaDeviceViewHolder.$r8$clinit;
                Objects.requireNonNull(mediaDeviceViewHolder);
                mediaDeviceViewHolder.onItemClick((MediaDevice) this.f$1);
                return;
        }
    }
}
