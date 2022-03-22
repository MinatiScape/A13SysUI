package com.android.systemui.qs;

import android.graphics.Rect;
import android.view.View;
import com.android.systemui.screenshot.LongScreenshotActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSPanel$$ExternalSyntheticLambda0 implements View.OnLayoutChangeListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSPanel$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        switch (this.$r8$classId) {
            case 0:
                QSPanel qSPanel = (QSPanel) this.f$0;
                int i9 = QSPanel.$r8$clinit;
                if (i == i5 && i3 == i7 && i4 == i8) {
                    Objects.requireNonNull(qSPanel);
                    return;
                }
                Rect rect = qSPanel.mClippingRect;
                rect.left = i;
                rect.right = i3;
                rect.bottom = i4;
                qSPanel.mHorizontalContentContainer.setClipBounds(rect);
                return;
            default:
                LongScreenshotActivity longScreenshotActivity = (LongScreenshotActivity) this.f$0;
                int i10 = LongScreenshotActivity.$r8$clinit;
                Objects.requireNonNull(longScreenshotActivity);
                longScreenshotActivity.updateImageDimensions();
                return;
        }
    }
}
