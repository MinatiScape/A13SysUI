package com.android.systemui.statusbar.notification.collection.render;

import android.view.ViewGroup;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import com.google.android.systemui.assist.uihints.IconController$$ExternalSyntheticLambda0;
/* compiled from: SectionHeaderController.kt */
/* loaded from: classes.dex */
public interface SectionHeaderController {
    SectionHeaderView getHeaderView();

    void reinflateView(ViewGroup viewGroup);

    void setClearSectionEnabled(boolean z);

    void setOnClearSectionClickListener(IconController$$ExternalSyntheticLambda0 iconController$$ExternalSyntheticLambda0);
}
