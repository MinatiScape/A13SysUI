package com.android.systemui.statusbar.phone;

import com.android.systemui.qs.QSFooterView$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public interface ShadeController {
    void addPostCollapseAction(Runnable runnable);

    void animateCollapsePanels();

    void animateCollapsePanels(int i);

    void animateCollapsePanels(int i, boolean z, boolean z2, float f);

    void animateCollapsePanels$1();

    void animateCollapsePanels$1(int i);

    void closeShadeIfOpen();

    void collapsePanel(boolean z);

    boolean collapsePanel();

    void instantExpandNotificationsPanel();

    boolean isShadeOpen();

    void postOnShadeExpanded(QSFooterView$$ExternalSyntheticLambda0 qSFooterView$$ExternalSyntheticLambda0);

    void runPostCollapseRunnables();
}
