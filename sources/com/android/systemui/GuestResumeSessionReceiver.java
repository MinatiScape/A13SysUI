package com.android.systemui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes.dex */
public final class GuestResumeSessionReceiver extends BroadcastReceiver {
    @VisibleForTesting
    public static final String SETTING_GUEST_HAS_LOGGED_IN = "systemui.guest_has_logged_in";
    @VisibleForTesting
    public AlertDialog mNewSessionDialog;
    public final SecureSettings mSecureSettings;
    public final UiEventLogger mUiEventLogger;
    public final UserSwitcherController mUserSwitcherController;
    public final UserTracker mUserTracker;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ResetSessionDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        @VisibleForTesting
        public static final int BUTTON_DONTWIPE = -1;
        @VisibleForTesting
        public static final int BUTTON_WIPE = -2;
        public final UiEventLogger mUiEventLogger;
        public final int mUserId;
        public final UserSwitcherController mUserSwitcherController;

        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            if (i == -2) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_WIPE);
                this.mUserSwitcherController.removeGuestUser(this.mUserId, -10000);
                dismiss();
            } else if (i == -1) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_CONTINUE);
                cancel();
            }
        }

        public ResetSessionDialog(Context context, UserSwitcherController userSwitcherController, UiEventLogger uiEventLogger, int i) {
            super(context);
            setTitle(context.getString(2131952423));
            setMessage(context.getString(2131952422));
            setCanceledOnTouchOutside(false);
            setButton(-2, context.getString(2131952424), this);
            setButton(-1, context.getString(2131952421), this);
            this.mUserSwitcherController = userSwitcherController;
            this.mUiEventLogger = uiEventLogger;
            this.mUserId = i;
        }
    }

    public GuestResumeSessionReceiver(UserSwitcherController userSwitcherController, UserTracker userTracker, UiEventLogger uiEventLogger, SecureSettings secureSettings) {
        this.mUserSwitcherController = userSwitcherController;
        this.mUserTracker = userTracker;
        this.mUiEventLogger = uiEventLogger;
        this.mSecureSettings = secureSettings;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
            AlertDialog alertDialog = this.mNewSessionDialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.mNewSessionDialog.cancel();
                this.mNewSessionDialog = null;
            }
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (intExtra == -10000) {
                Log.e("GuestResumeSessionReceiver", intent + " sent to GuestResumeSessionReceiver without EXTRA_USER_HANDLE");
            } else if (this.mUserTracker.getUserInfo().isGuest()) {
                if (this.mSecureSettings.getIntForUser(SETTING_GUEST_HAS_LOGGED_IN, 0, intExtra) != 0) {
                    ResetSessionDialog resetSessionDialog = new ResetSessionDialog(context, this.mUserSwitcherController, this.mUiEventLogger, intExtra);
                    this.mNewSessionDialog = resetSessionDialog;
                    resetSessionDialog.show();
                    return;
                }
                this.mSecureSettings.putIntForUser(SETTING_GUEST_HAS_LOGGED_IN, 1, intExtra);
            }
        }
    }
}
