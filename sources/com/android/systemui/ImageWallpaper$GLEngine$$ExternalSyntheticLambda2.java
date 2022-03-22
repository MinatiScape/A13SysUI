package com.android.systemui;

import android.graphics.Bitmap;
import android.graphics.Region;
import android.view.View;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                Bitmap bitmap = (Bitmap) obj;
                Objects.requireNonNull(gLEngine);
                ImageWallpaper imageWallpaper = ImageWallpaper.this;
                imageWallpaper.mLocalColorsToAdd.addAll(imageWallpaper.mColorAreas);
                if (ImageWallpaper.this.mLocalColorsToAdd.size() > 0) {
                    gLEngine.updateMiniBitmapAndNotify(bitmap);
                    return;
                }
                return;
            case 1:
                ((HeadsUpAppearanceController) this.f$0).show((View) obj);
                return;
            case 2:
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = (NotificationStackScrollLayoutController) this.f$0;
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) obj;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
                Objects.requireNonNull(ambientState);
                ambientState.mTrackedHeadsUpRow = expandableNotificationRow;
                NotificationRoundnessManager notificationRoundnessManager = notificationStackScrollLayoutController.mNotificationRoundnessManager;
                Objects.requireNonNull(notificationRoundnessManager);
                ExpandableNotificationRow expandableNotificationRow2 = notificationRoundnessManager.mTrackedHeadsUp;
                notificationRoundnessManager.mTrackedHeadsUp = expandableNotificationRow;
                if (expandableNotificationRow2 != null) {
                    notificationRoundnessManager.updateView(expandableNotificationRow2, true);
                    return;
                }
                return;
            default:
                ((Region) this.f$0).op((Region) obj, Region.Op.UNION);
                return;
        }
    }
}
