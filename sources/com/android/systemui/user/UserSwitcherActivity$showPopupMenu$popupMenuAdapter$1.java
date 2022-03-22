package com.android.systemui.user;

import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: UserSwitcherActivity.kt */
/* loaded from: classes.dex */
final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1 extends Lambda implements Function1<UserSwitcherController.UserRecord, String> {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(UserSwitcherController.UserRecord userRecord) {
        UserSwitcherActivity userSwitcherActivity = this.this$0;
        return userSwitcherActivity.adapter.getName(userSwitcherActivity, userRecord);
    }
}
