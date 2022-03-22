package com.android.systemui.statusbar.phone;

import java.util.Objects;
import java.util.function.BiConsumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationPanelViewController$$ExternalSyntheticLambda6 implements BiConsumer {
    public static final /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda6 INSTANCE = new NotificationPanelViewController$$ExternalSyntheticLambda6();

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        NotificationPanelView notificationPanelView = (NotificationPanelView) obj;
        float floatValue = ((Float) obj2).floatValue();
        Objects.requireNonNull(notificationPanelView);
        int i = (int) floatValue;
        notificationPanelView.mCurrentPanelAlpha = i;
        notificationPanelView.mAlphaPaint.setARGB(i, 255, 255, 255);
        notificationPanelView.invalidate();
    }
}
