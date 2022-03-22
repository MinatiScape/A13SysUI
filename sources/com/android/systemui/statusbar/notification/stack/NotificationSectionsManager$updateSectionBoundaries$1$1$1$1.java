package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager$updateSectionBoundaries$1$1$1$1 extends Lambda implements Function1<NotificationSectionsManager.SectionUpdateState<? extends ExpandableView>, Boolean> {
    public final /* synthetic */ NotificationSectionsManager.SectionUpdateState<ExpandableView> $state;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationSectionsManager$updateSectionBoundaries$1$1$1$1(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        super(1);
        this.$state = sectionUpdateState;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        boolean z;
        if (sectionUpdateState == this.$state) {
            z = true;
        } else {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
