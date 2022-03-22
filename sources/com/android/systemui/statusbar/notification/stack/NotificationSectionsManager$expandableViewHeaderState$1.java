package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager$expandableViewHeaderState$1 implements NotificationSectionsManager.SectionUpdateState<Object> {
    public final /* synthetic */ Object $header;
    public Integer currentPosition;
    public Integer targetPosition;
    public final /* synthetic */ NotificationSectionsManager this$0;

    public NotificationSectionsManager$expandableViewHeaderState$1(Object obj, NotificationSectionsManager notificationSectionsManager) {
        this.$header = obj;
        this.this$0 = notificationSectionsManager;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.lang.Object, com.android.systemui.statusbar.notification.row.ExpandableView] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout] */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v5, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.lang.Object, com.android.systemui.statusbar.notification.row.ExpandableView] */
    /* JADX WARN: Type inference failed for: r3v2, types: [android.view.View, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v3, types: [android.view.View, java.lang.Object] */
    @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
    public final void adjustViewPosition() {
        this.this$0.notifPipelineFlags.checkLegacyPipelineEnabled();
        Integer num = this.targetPosition;
        Integer num2 = this.currentPosition;
        NotificationStackScrollLayout notificationStackScrollLayout = 0;
        NotificationStackScrollLayout notificationStackScrollLayout2 = 0;
        NotificationStackScrollLayout notificationStackScrollLayout3 = 0;
        if (num == null) {
            if (num2 != null) {
                NotificationStackScrollLayout notificationStackScrollLayout4 = this.this$0.parent;
                if (notificationStackScrollLayout4 != null) {
                    notificationStackScrollLayout2 = notificationStackScrollLayout4;
                }
                notificationStackScrollLayout2.removeView(this.$header);
            }
        } else if (num2 == null) {
            this.$header.removeFromTransientContainer();
            NotificationStackScrollLayout notificationStackScrollLayout5 = this.this$0.parent;
            if (notificationStackScrollLayout5 != null) {
                notificationStackScrollLayout3 = notificationStackScrollLayout5;
            }
            notificationStackScrollLayout3.addView(this.$header, num.intValue());
        } else {
            NotificationStackScrollLayout notificationStackScrollLayout6 = this.this$0.parent;
            if (notificationStackScrollLayout6 != null) {
                notificationStackScrollLayout = notificationStackScrollLayout6;
            }
            notificationStackScrollLayout.changeViewPosition(this.$header, num.intValue());
        }
    }

    @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
    public final void setCurrentPosition(Integer num) {
        this.currentPosition = num;
    }

    @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
    public final void setTargetPosition(Integer num) {
        this.targetPosition = num;
    }

    @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
    public final Integer getCurrentPosition() {
        return this.currentPosition;
    }

    @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
    public final Integer getTargetPosition() {
        return this.targetPosition;
    }
}
