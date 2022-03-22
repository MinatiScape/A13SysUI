package com.android.systemui.user;

import android.graphics.drawable.Drawable;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: UserSwitcherActivity.kt */
/* loaded from: classes.dex */
final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2 extends Lambda implements Function1<UserSwitcherController.UserRecord, Drawable> {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Drawable invoke(UserSwitcherController.UserRecord userRecord) {
        Drawable mutate = this.this$0.adapter.findUserIcon(userRecord).mutate();
        UserSwitcherActivity userSwitcherActivity = this.this$0;
        mutate.setTint(userSwitcherActivity.getResources().getColor(2131100782, userSwitcherActivity.getTheme()));
        return mutate;
    }
}
