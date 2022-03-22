package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PseudoGridView;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.Objects;
/* loaded from: classes.dex */
public class UserDetailView extends PseudoGridView {

    /* loaded from: classes.dex */
    public static class Adapter extends UserSwitcherController.BaseUserAdapter implements View.OnClickListener {
        public final Context mContext;
        public UserSwitcherController mController;
        public UserDetailItemView mCurrentUserView;
        public UserSwitchDialogController.DialogShower mDialogShower;
        public final FalsingManager mFalsingManager;
        public final UiEventLogger mUiEventLogger;

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (!this.mFalsingManager.isFalseTap(1)) {
                Trace.beginSection("UserDetailView.Adapter#onClick");
                UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) view.getTag();
                if (userRecord.isDisabledByAdmin) {
                    Intent showAdminSupportDetailsIntent = RestrictedLockUtils.getShowAdminSupportDetailsIntent(userRecord.enforcedAdmin);
                    UserSwitcherController userSwitcherController = this.mController;
                    Objects.requireNonNull(userSwitcherController);
                    userSwitcherController.mActivityStarter.startActivity(showAdminSupportDetailsIntent, true);
                } else if (userRecord.isSwitchToEnabled) {
                    MetricsLogger.action(this.mContext, 156);
                    this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_SWITCH);
                    if (!userRecord.isAddUser && !userRecord.isRestricted && !userRecord.isDisabledByAdmin) {
                        UserDetailItemView userDetailItemView = this.mCurrentUserView;
                        if (userDetailItemView != null) {
                            userDetailItemView.setActivated(false);
                        }
                        view.setActivated(true);
                    }
                    onUserListItemClicked(userRecord, this.mDialogShower);
                }
                Trace.endSection();
            }
        }

        public Adapter(Context context, UserSwitcherController userSwitcherController, UiEventLogger uiEventLogger, FalsingManager falsingManager) {
            super(userSwitcherController);
            this.mContext = context;
            this.mController = userSwitcherController;
            this.mUiEventLogger = uiEventLogger;
            this.mFalsingManager = falsingManager;
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            float f;
            int i2;
            int i3;
            UserSwitcherController.UserRecord item = getItem(i);
            Context context = viewGroup.getContext();
            int i4 = UserDetailItemView.$r8$clinit;
            int i5 = 0;
            if (!(view instanceof UserDetailItemView)) {
                view = LayoutInflater.from(context).inflate(2131624434, viewGroup, false);
            }
            UserDetailItemView userDetailItemView = (UserDetailItemView) view;
            ColorMatrixColorFilter colorMatrixColorFilter = null;
            if (!item.isCurrent || item.isGuest) {
                userDetailItemView.setOnClickListener(this);
            } else {
                userDetailItemView.setOnClickListener(null);
                userDetailItemView.setClickable(false);
            }
            String name = getName(this.mContext, item);
            if (item.picture == null) {
                Context context2 = this.mContext;
                Drawable iconDrawable = UserSwitcherController.BaseUserAdapter.getIconDrawable(context2, item);
                if (item.isCurrent) {
                    i2 = 2131100515;
                } else if (!item.isSwitchToEnabled) {
                    i2 = 2131099655;
                } else {
                    i2 = 2131100514;
                }
                iconDrawable.setTint(context2.getResources().getColor(i2, context2.getTheme()));
                if (item.isCurrent) {
                    i3 = 2131231609;
                } else {
                    i3 = 2131232579;
                }
                Drawable mutate = new LayerDrawable(new Drawable[]{context2.getDrawable(i3), iconDrawable}).mutate();
                int resolveId = item.resolveId();
                Objects.requireNonNull(userDetailItemView);
                userDetailItemView.mName.setText(name);
                userDetailItemView.mAvatar.setDrawableWithBadge(mutate, resolveId);
            } else {
                CircleFramedDrawable circleFramedDrawable = new CircleFramedDrawable(item.picture, (int) this.mContext.getResources().getDimension(2131166868));
                if (!item.isSwitchToEnabled) {
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0.0f);
                    colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                }
                circleFramedDrawable.setColorFilter(colorMatrixColorFilter);
                int i6 = item.info.id;
                Objects.requireNonNull(userDetailItemView);
                userDetailItemView.mName.setText(name);
                userDetailItemView.mAvatar.setDrawableWithBadge(circleFramedDrawable, i6);
            }
            userDetailItemView.setActivated(item.isCurrent);
            boolean z = item.isDisabledByAdmin;
            View view2 = userDetailItemView.mRestrictedPadlock;
            if (view2 != null) {
                if (!z) {
                    i5 = 8;
                }
                view2.setVisibility(i5);
            }
            userDetailItemView.setEnabled(!z);
            userDetailItemView.setEnabled(item.isSwitchToEnabled);
            if (userDetailItemView.isEnabled()) {
                f = 1.0f;
            } else {
                f = 0.38f;
            }
            userDetailItemView.setAlpha(f);
            if (item.isCurrent) {
                this.mCurrentUserView = userDetailItemView;
            }
            userDetailItemView.setTag(item);
            return userDetailItemView;
        }
    }

    public UserDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
