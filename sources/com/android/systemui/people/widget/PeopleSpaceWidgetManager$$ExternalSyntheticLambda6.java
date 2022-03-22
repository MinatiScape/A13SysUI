package com.android.systemui.people.widget;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 INSTANCE = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6(0);
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 INSTANCE$1 = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6(1);
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 INSTANCE$2 = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6(2);
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6 INSTANCE$3 = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6(3);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda6(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return new PeopleTileKey((NotificationEntry) obj);
            case 1:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                StatusBarNotificationPresenter statusBarNotificationPresenter = statusBar.mPresenter;
                Objects.requireNonNull(statusBarNotificationPresenter);
                return Boolean.valueOf(statusBarNotificationPresenter.mVrMode);
            case 2:
                NotificationPanelView notificationPanelView = (NotificationPanelView) obj;
                Objects.requireNonNull(notificationPanelView);
                return Float.valueOf(notificationPanelView.mCurrentPanelAlpha);
            default:
                return ((SysUIUnfoldComponent) obj).getFoldAodAnimationController();
        }
    }
}
