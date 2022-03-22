package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.util.Comparator;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationStackScrollLayout$$ExternalSyntheticLambda3 implements Comparator {
    public static final /* synthetic */ NotificationStackScrollLayout$$ExternalSyntheticLambda3 INSTANCE = new NotificationStackScrollLayout$$ExternalSyntheticLambda3();

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        ExpandableView expandableView = (ExpandableView) obj;
        ExpandableView expandableView2 = (ExpandableView) obj2;
        boolean z = NotificationStackScrollLayout.SPEW;
        float translationY = expandableView.getTranslationY() + expandableView.mActualHeight;
        float translationY2 = expandableView2.getTranslationY() + expandableView2.mActualHeight;
        if (translationY < translationY2) {
            return -1;
        }
        if (translationY > translationY2) {
            return 1;
        }
        return 0;
    }
}
