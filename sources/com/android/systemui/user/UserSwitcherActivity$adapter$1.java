package com.android.systemui.user;

import android.content.Context;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.Utils;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserSwitcherActivity.kt */
/* loaded from: classes.dex */
public final class UserSwitcherActivity$adapter$1 extends UserSwitcherController.BaseUserAdapter {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$adapter$1(UserSwitcherActivity userSwitcherActivity, UserSwitcherController userSwitcherController) {
        super(userSwitcherController);
        this.this$0 = userSwitcherActivity;
    }

    public final Drawable findUserIcon(UserSwitcherController.UserRecord userRecord) {
        if (Intrinsics.areEqual(userRecord, this.this$0.manageUserRecord)) {
            return this.this$0.getDrawable(2131232035);
        }
        UserInfo userInfo = userRecord.info;
        if (userInfo == null) {
            return UserSwitcherController.BaseUserAdapter.getIconDrawable(this.this$0, userRecord);
        }
        Bitmap userIcon = this.this$0.userManager.getUserIcon(userInfo.id);
        if (userIcon != null) {
            return new BitmapDrawable(userIcon);
        }
        return UserIcons.getDefaultUserIcon(this.this$0.getResources(), userRecord.info.id, false);
    }

    @Override // com.android.systemui.statusbar.policy.UserSwitcherController.BaseUserAdapter
    public final String getName(Context context, UserSwitcherController.UserRecord userRecord) {
        if (Intrinsics.areEqual(userRecord, this.this$0.manageUserRecord)) {
            return this.this$0.getString(2131952709);
        }
        return super.getName(context, userRecord);
    }

    @Override // android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        Drawable drawable;
        UserSwitcherController.UserRecord userRecord;
        float f;
        UserSwitcherController.UserRecord item = getItem(i);
        ViewGroup viewGroup2 = (ViewGroup) view;
        int i2 = 0;
        if (viewGroup2 == null) {
            View inflate = this.this$0.layoutInflater.inflate(2131624646, viewGroup, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
            viewGroup2 = (ViewGroup) inflate;
        }
        View childAt = viewGroup2.getChildAt(0);
        Objects.requireNonNull(childAt, "null cannot be cast to non-null type android.widget.ImageView");
        ImageView imageView = (ImageView) childAt;
        if (!item.isCurrent || !item.isGuest) {
            drawable = findUserIcon(item);
        } else {
            drawable = this.this$0.getDrawable(2131231760);
        }
        drawable.mutate();
        if (!item.isCurrent && !item.isSwitchToEnabled) {
            drawable.setTint(this.this$0.getResources().getColor(2131099894, this.this$0.getTheme()));
        }
        Drawable mutate = this.this$0.getDrawable(2131232753).mutate();
        Objects.requireNonNull(mutate, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        LayerDrawable layerDrawable = (LayerDrawable) mutate;
        UserSwitcherController userSwitcherController = this.this$0.userSwitcherController;
        Objects.requireNonNull(userSwitcherController);
        while (true) {
            if (i2 >= userSwitcherController.mUsers.size()) {
                userRecord = null;
                break;
            }
            userRecord = userSwitcherController.mUsers.get(i2);
            if (userRecord.isCurrent) {
                break;
            }
            i2++;
        }
        if (Intrinsics.areEqual(item, userRecord)) {
            Drawable drawable2 = layerDrawable.getDrawable(1);
            Objects.requireNonNull(drawable2, "null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
            UserSwitcherActivity userSwitcherActivity = this.this$0;
            ((GradientDrawable) drawable2).setStroke(userSwitcherActivity.getResources().getDimensionPixelSize(2131167282), Utils.getColorAttrDefaultColor(userSwitcherActivity, 17956900));
        }
        layerDrawable.addLayer(new InsetDrawable(drawable, this.this$0.getResources().getDimensionPixelSize(2131167281)));
        imageView.setImageDrawable(layerDrawable);
        View childAt2 = viewGroup2.getChildAt(1);
        Objects.requireNonNull(childAt2, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) childAt2;
        textView.setText(getName(textView.getContext(), item));
        viewGroup2.setEnabled(item.isSwitchToEnabled);
        if (viewGroup2.isEnabled()) {
            f = 1.0f;
        } else {
            f = 0.38f;
        }
        viewGroup2.setAlpha(f);
        viewGroup2.setTag("user_view");
        return viewGroup2;
    }

    @Override // android.widget.BaseAdapter
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        UserSwitcherActivity userSwitcherActivity = this.this$0;
        int i = UserSwitcherActivity.$r8$clinit;
        userSwitcherActivity.buildUserViews();
    }
}
