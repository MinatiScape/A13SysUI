package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController$onDraggedDown$animationHandler$1 extends Lambda implements Function1<Long, Unit> {
    public final /* synthetic */ View $startingChild;
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$onDraggedDown$animationHandler$1(ExpandableView expandableView, LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(1);
        this.$startingChild = expandableView;
        this.this$0 = lockscreenShadeTransitionController;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Long l) {
        long longValue = l.longValue();
        View view = this.$startingChild;
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).onExpandedByGesture(true);
        }
        this.this$0.getNotificationPanelController().animateToFullShade(longValue);
        this.this$0.getNotificationPanelController().setTransitionToFullShadeAmount(0.0f, true, longValue);
        LockscreenShadeTransitionController lockscreenShadeTransitionController = this.this$0;
        lockscreenShadeTransitionController.forceApplyAmount = true;
        lockscreenShadeTransitionController.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
        this.this$0.forceApplyAmount = false;
        return Unit.INSTANCE;
    }
}
