package com.android.systemui.qs;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.SecurityController;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class QSSecurityFooter implements View.OnClickListener, DialogInterface.OnClickListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ActivityStarter mActivityStarter;
    public final Context mContext;
    public SystemUIDialog mDialog;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final DevicePolicyManager mDpm;
    public final TextView mFooterText;
    public H mHandler;
    public boolean mIsVisible;
    public final Handler mMainHandler;
    public final ImageView mPrimaryFooterIcon;
    public Drawable mPrimaryFooterIconDrawable;
    public final View mRootView;
    public final SecurityController mSecurityController;
    public final UserTracker mUserTracker;
    public VisibilityChangedDispatcher$OnVisibilityChangedListener mVisibilityChangedListener;
    public final Callback mCallback = new Callback();
    public final AtomicBoolean mShouldUseSettingsButton = new AtomicBoolean(false);
    public boolean mIsMovable = true;
    public String mFooterTextContent = null;
    public final AnonymousClass1 mUpdatePrimaryIcon = new Runnable() { // from class: com.android.systemui.qs.QSSecurityFooter.1
        @Override // java.lang.Runnable
        public final void run() {
            QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
            Drawable drawable = qSSecurityFooter.mPrimaryFooterIconDrawable;
            if (drawable != null) {
                qSSecurityFooter.mPrimaryFooterIcon.setImageDrawable(drawable);
            } else {
                qSSecurityFooter.mPrimaryFooterIcon.setImageResource(qSSecurityFooter.mFooterIconId);
            }
        }
    };
    public final AnonymousClass2 mUpdateDisplayState = new Runnable() { // from class: com.android.systemui.qs.QSSecurityFooter.2
        @Override // java.lang.Runnable
        public final void run() {
            int i;
            QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
            String str = qSSecurityFooter.mFooterTextContent;
            if (str != null) {
                qSSecurityFooter.mFooterText.setText(str);
            }
            QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
            View view = qSSecurityFooter2.mRootView;
            if (!qSSecurityFooter2.mIsVisible) {
                i = 8;
            } else {
                i = 0;
            }
            view.setVisibility(i);
            QSSecurityFooter qSSecurityFooter3 = QSSecurityFooter.this;
            VisibilityChangedDispatcher$OnVisibilityChangedListener visibilityChangedDispatcher$OnVisibilityChangedListener = qSSecurityFooter3.mVisibilityChangedListener;
            if (visibilityChangedDispatcher$OnVisibilityChangedListener != null) {
                visibilityChangedDispatcher$OnVisibilityChangedListener.onVisibilityChanged(qSSecurityFooter3.mRootView.getVisibility());
            }
        }
    };
    public int mFooterIconId = 2131232010;

    /* loaded from: classes.dex */
    public class Callback implements SecurityController.SecurityControllerCallback {
        public Callback() {
        }

        @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
        public final void onStateChanged() {
            QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
            Objects.requireNonNull(qSSecurityFooter);
            qSSecurityFooter.mHandler.sendEmptyMessage(1);
        }
    }

    /* loaded from: classes.dex */
    public class H extends Handler {
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            String str = null;
            try {
                int i = message.what;
                if (i == 1) {
                    str = "handleRefreshState";
                    QSSecurityFooter.m74$$Nest$mhandleRefreshState(QSSecurityFooter.this);
                } else if (i == 0) {
                    str = "handleClick";
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    qSSecurityFooter.mShouldUseSettingsButton.set(false);
                    qSSecurityFooter.mMainHandler.post(new KeyguardPatternView$$ExternalSyntheticLambda0(qSSecurityFooter, qSSecurityFooter.createDialogView(), 2));
                    DevicePolicyEventLogger.createEvent(57).write();
                }
            } catch (Throwable th) {
                Log.w("QSSecurityFooter", "Error in " + str, th);
            }
        }

        public H(Looper looper) {
            super(looper);
        }
    }

    /* loaded from: classes.dex */
    public class VpnSpan extends ClickableSpan {
        public final int hashCode() {
            return 314159257;
        }

        public VpnSpan() {
        }

        @Override // android.text.style.ClickableSpan
        public final void onClick(View view) {
            Intent intent = new Intent("android.settings.VPN_SETTINGS");
            QSSecurityFooter.this.mDialog.dismiss();
            QSSecurityFooter.this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }

        public final boolean equals(Object obj) {
            return obj instanceof VpnSpan;
        }
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (this.mRootView.getVisibility() != 8) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    /* renamed from: -$$Nest$mhandleRefreshState  reason: not valid java name */
    public static void m74$$Nest$mhandleRefreshState(final QSSecurityFooter qSSecurityFooter) {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        final String str2;
        Objects.requireNonNull(qSSecurityFooter);
        boolean isDeviceManaged = qSSecurityFooter.mSecurityController.isDeviceManaged();
        UserInfo userInfo = qSSecurityFooter.mUserTracker.getUserInfo();
        if (!UserManager.isDeviceInDemoMode(qSSecurityFooter.mContext) || userInfo == null || !userInfo.isDemo()) {
            z = false;
        } else {
            z = true;
        }
        boolean hasWorkProfile = qSSecurityFooter.mSecurityController.hasWorkProfile();
        boolean hasCACertInCurrentUser = qSSecurityFooter.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = qSSecurityFooter.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = qSSecurityFooter.mSecurityController.isNetworkLoggingEnabled();
        final String primaryVpnName = qSSecurityFooter.mSecurityController.getPrimaryVpnName();
        final String workProfileVpnName = qSSecurityFooter.mSecurityController.getWorkProfileVpnName();
        final CharSequence deviceOwnerOrganizationName = qSSecurityFooter.mSecurityController.getDeviceOwnerOrganizationName();
        final CharSequence workProfileOrganizationName = qSSecurityFooter.mSecurityController.getWorkProfileOrganizationName();
        boolean isProfileOwnerOfOrganizationOwnedDevice = qSSecurityFooter.mSecurityController.isProfileOwnerOfOrganizationOwnedDevice();
        boolean isParentalControlsEnabled = qSSecurityFooter.mSecurityController.isParentalControlsEnabled();
        boolean isWorkProfileOn = qSSecurityFooter.mSecurityController.isWorkProfileOn();
        if (hasCACertInWorkProfile || workProfileVpnName != null || (hasWorkProfile && isNetworkLoggingEnabled)) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((!isDeviceManaged || z) && !hasCACertInCurrentUser && primaryVpnName == null && !isProfileOwnerOfOrganizationOwnedDevice && !isParentalControlsEnabled && (!z2 || !isWorkProfileOn)) {
            z3 = false;
        } else {
            z3 = true;
        }
        qSSecurityFooter.mIsVisible = z3;
        if (!z3 || !isProfileOwnerOfOrganizationOwnedDevice || (z2 && isWorkProfileOn)) {
            qSSecurityFooter.mRootView.setClickable(true);
            qSSecurityFooter.mRootView.findViewById(2131427983).setVisibility(0);
        } else {
            qSSecurityFooter.mRootView.setClickable(false);
            qSSecurityFooter.mRootView.findViewById(2131427983).setVisibility(8);
        }
        if (isParentalControlsEnabled) {
            str = qSSecurityFooter.mContext.getString(2131953104);
        } else if (!isDeviceManaged) {
            if (hasCACertInCurrentUser || (hasCACertInWorkProfile && isWorkProfileOn)) {
                if (!hasCACertInWorkProfile || !isWorkProfileOn) {
                    if (hasCACertInCurrentUser) {
                        str = qSSecurityFooter.mContext.getString(2131953097);
                    }
                    str = null;
                } else if (workProfileOrganizationName == null) {
                    str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_WORK_PROFILE_MONITORING", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda4
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                            Objects.requireNonNull(qSSecurityFooter2);
                            return qSSecurityFooter2.mContext.getString(2131953090);
                        }
                    });
                } else {
                    str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_NAMED_WORK_PROFILE_MONITORING", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda15
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                            CharSequence charSequence = workProfileOrganizationName;
                            Objects.requireNonNull(qSSecurityFooter2);
                            return qSSecurityFooter2.mContext.getString(2131953098, charSequence);
                        }
                    }, new Object[]{workProfileOrganizationName});
                }
            } else if (primaryVpnName != null || (workProfileVpnName != null && isWorkProfileOn)) {
                if (primaryVpnName != null && workProfileVpnName != null) {
                    str = qSSecurityFooter.mContext.getString(2131953106);
                } else if (workProfileVpnName == null || !isWorkProfileOn) {
                    if (primaryVpnName != null) {
                        if (hasWorkProfile) {
                            str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_PERSONAL_PROFILE_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda20
                                @Override // java.util.concurrent.Callable
                                public final Object call() {
                                    QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                                    String str3 = primaryVpnName;
                                    Objects.requireNonNull(qSSecurityFooter2);
                                    return qSSecurityFooter2.mContext.getString(2131953105, str3);
                                }
                            }, new Object[]{primaryVpnName});
                        } else {
                            str = qSSecurityFooter.mContext.getString(2131953103, primaryVpnName);
                        }
                    }
                    str = null;
                } else {
                    str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_WORK_PROFILE_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda19
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                            String str3 = workProfileVpnName;
                            Objects.requireNonNull(qSSecurityFooter2);
                            return qSSecurityFooter2.mContext.getString(2131953091, str3);
                        }
                    }, new Object[]{workProfileVpnName});
                }
            } else if (!hasWorkProfile || !isNetworkLoggingEnabled || !isWorkProfileOn) {
                if (isProfileOwnerOfOrganizationOwnedDevice) {
                    str = qSSecurityFooter.getMangedDeviceGeneralText(workProfileOrganizationName);
                }
                str = null;
            } else {
                str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_WORK_PROFILE_NETWORK", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda5
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                        Objects.requireNonNull(qSSecurityFooter2);
                        return qSSecurityFooter2.mContext.getString(2131953092);
                    }
                });
            }
        } else if (hasCACertInCurrentUser || hasCACertInWorkProfile || isNetworkLoggingEnabled) {
            if (deviceOwnerOrganizationName == null) {
                str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_MANAGEMENT_MONITORING", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                        Objects.requireNonNull(qSSecurityFooter2);
                        return qSSecurityFooter2.mContext.getString(2131953094);
                    }
                });
            } else {
                str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MONITORING", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda12
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                        CharSequence charSequence = deviceOwnerOrganizationName;
                        Objects.requireNonNull(qSSecurityFooter2);
                        return qSSecurityFooter2.mContext.getString(2131953100, charSequence);
                    }
                }, new Object[]{deviceOwnerOrganizationName});
            }
        } else if (primaryVpnName == null && workProfileVpnName == null) {
            str = qSSecurityFooter.getMangedDeviceGeneralText(deviceOwnerOrganizationName);
        } else if (primaryVpnName == null || workProfileVpnName == null) {
            if (primaryVpnName != null) {
                str2 = primaryVpnName;
            } else {
                str2 = workProfileVpnName;
            }
            if (deviceOwnerOrganizationName == null) {
                str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_MANAGEMENT_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda18
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                        String str3 = str2;
                        Objects.requireNonNull(qSSecurityFooter2);
                        return qSSecurityFooter2.mContext.getString(2131953095, str3);
                    }
                }, new Object[]{str2});
            } else {
                str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda17
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                        CharSequence charSequence = deviceOwnerOrganizationName;
                        String str3 = str2;
                        Objects.requireNonNull(qSSecurityFooter2);
                        return qSSecurityFooter2.mContext.getString(2131953101, charSequence, str3);
                    }
                }, new Object[]{deviceOwnerOrganizationName, str2});
            }
        } else if (deviceOwnerOrganizationName == null) {
            str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_MANAGEMENT_MULTIPLE_VPNS", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda2
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter2);
                    return qSSecurityFooter2.mContext.getString(2131953096);
                }
            });
        } else {
            str = qSSecurityFooter.mDpm.getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MULTIPLE_VPNS", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda13
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter2 = QSSecurityFooter.this;
                    CharSequence charSequence = deviceOwnerOrganizationName;
                    Objects.requireNonNull(qSSecurityFooter2);
                    return qSSecurityFooter2.mContext.getString(2131953102, charSequence);
                }
            }, new Object[]{deviceOwnerOrganizationName});
        }
        qSSecurityFooter.mFooterTextContent = str;
        int i = 2131232010;
        if (!(primaryVpnName == null && workProfileVpnName == null)) {
            if (qSSecurityFooter.mSecurityController.isVpnBranded()) {
                i = 2131232674;
            } else {
                i = 2131232697;
            }
        }
        if (qSSecurityFooter.mFooterIconId != i) {
            qSSecurityFooter.mFooterIconId = i;
        }
        if (!isParentalControlsEnabled) {
            qSSecurityFooter.mPrimaryFooterIconDrawable = null;
        } else if (qSSecurityFooter.mPrimaryFooterIconDrawable == null) {
            qSSecurityFooter.mPrimaryFooterIconDrawable = qSSecurityFooter.mSecurityController.getIcon(qSSecurityFooter.mSecurityController.getDeviceAdminInfo());
        }
        qSSecurityFooter.mMainHandler.post(qSSecurityFooter.mUpdatePrimaryIcon);
        qSSecurityFooter.mMainHandler.post(qSSecurityFooter.mUpdateDisplayState);
    }

    static {
        Log.isLoggable("QSSecurityFooter", 3);
    }

    public View createDialogView() {
        String str;
        String str2;
        SpannableStringBuilder spannableStringBuilder;
        boolean z;
        int i;
        boolean z2;
        int i2;
        boolean z3 = false;
        String str3 = null;
        if (this.mSecurityController.isParentalControlsEnabled()) {
            View inflate = LayoutInflater.from(this.mContext).inflate(2131624440, (ViewGroup) null, false);
            DeviceAdminInfo deviceAdminInfo = this.mSecurityController.getDeviceAdminInfo();
            Drawable icon = this.mSecurityController.getIcon(deviceAdminInfo);
            if (icon != null) {
                ((ImageView) inflate.findViewById(2131428567)).setImageDrawable(icon);
            }
            ((TextView) inflate.findViewById(2131428568)).setText(this.mSecurityController.getLabel(deviceAdminInfo));
            return inflate;
        }
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        final CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        final String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        final String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        View inflate2 = LayoutInflater.from(this.mContext).inflate(2131624439, (ViewGroup) null, false);
        ((TextView) inflate2.findViewById(2131427837)).setText(getManagementTitle(deviceOwnerOrganizationName));
        if (isDeviceManaged) {
            if (deviceOwnerOrganizationName == null) {
                str3 = this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda7
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                        Objects.requireNonNull(qSSecurityFooter);
                        return qSSecurityFooter.mContext.getString(2131952784);
                    }
                });
            } else if (isFinancedDevice()) {
                str3 = this.mContext.getString(2131952794, deviceOwnerOrganizationName, deviceOwnerOrganizationName);
            } else {
                str3 = this.mDpm.getString("SystemUi.QS_DIALOG_NAMED_MANAGEMENT", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda16
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                        CharSequence charSequence = deviceOwnerOrganizationName;
                        Objects.requireNonNull(qSSecurityFooter);
                        return qSSecurityFooter.mContext.getString(2131952787, charSequence);
                    }
                }, new Object[]{deviceOwnerOrganizationName});
            }
        }
        if (str3 == null) {
            inflate2.findViewById(2131427836).setVisibility(8);
        } else {
            inflate2.findViewById(2131427836).setVisibility(0);
            ((TextView) inflate2.findViewById(2131427838)).setText(str3);
            this.mShouldUseSettingsButton.set(true);
        }
        if (!hasCACertInCurrentUser && !hasCACertInWorkProfile) {
            str = null;
        } else if (isDeviceManaged) {
            str = this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_CA_CERT", new QSSecurityFooter$$ExternalSyntheticLambda0(this, 0));
        } else if (hasCACertInWorkProfile) {
            str = this.mDpm.getString("SystemUi.QS_DIALOG_WORK_PROFILE_CA_CERT", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda8
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    return qSSecurityFooter.mContext.getString(2131952781);
                }
            });
        } else {
            str = this.mContext.getString(2131952780);
        }
        if (str == null) {
            inflate2.findViewById(2131427651).setVisibility(8);
        } else {
            inflate2.findViewById(2131427651).setVisibility(0);
            TextView textView = (TextView) inflate2.findViewById(2131427653);
            textView.setText(str);
            textView.setMovementMethod(new LinkMovementMethod());
        }
        if (!isNetworkLoggingEnabled) {
            str2 = null;
        } else if (isDeviceManaged) {
            str2 = this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_NETWORK", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda9
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    return qSSecurityFooter.mContext.getString(2131952786);
                }
            });
        } else {
            str2 = this.mDpm.getString("SystemUi.QS_DIALOG_WORK_PROFILE_NETWORK", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda10
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    return qSSecurityFooter.mContext.getString(2131952783);
                }
            });
        }
        if (str2 == null) {
            inflate2.findViewById(2131428491).setVisibility(8);
        } else {
            inflate2.findViewById(2131428491).setVisibility(0);
            ((TextView) inflate2.findViewById(2131428493)).setText(str2);
        }
        if (primaryVpnName == null && workProfileVpnName == null) {
            spannableStringBuilder = null;
        } else {
            spannableStringBuilder = new SpannableStringBuilder();
            if (isDeviceManaged) {
                if (primaryVpnName == null || workProfileVpnName == null) {
                    if (primaryVpnName == null) {
                        primaryVpnName = workProfileVpnName;
                    }
                    spannableStringBuilder.append((CharSequence) this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda21
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                            String str4 = primaryVpnName;
                            Objects.requireNonNull(qSSecurityFooter);
                            return qSSecurityFooter.mContext.getString(2131952788, str4);
                        }
                    }, new Object[]{primaryVpnName}));
                } else {
                    spannableStringBuilder.append((CharSequence) this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda24
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                            String str4 = primaryVpnName;
                            String str5 = workProfileVpnName;
                            Objects.requireNonNull(qSSecurityFooter);
                            return qSSecurityFooter.mContext.getString(2131952791, str4, str5);
                        }
                    }, new Object[]{primaryVpnName, workProfileVpnName}));
                }
            } else if (primaryVpnName != null && workProfileVpnName != null) {
                spannableStringBuilder.append((CharSequence) this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda25
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                        String str4 = primaryVpnName;
                        String str5 = workProfileVpnName;
                        Objects.requireNonNull(qSSecurityFooter);
                        return qSSecurityFooter.mContext.getString(2131952791, str4, str5);
                    }
                }, new Object[]{primaryVpnName, workProfileVpnName}));
            } else if (workProfileVpnName != null) {
                spannableStringBuilder.append((CharSequence) this.mDpm.getString("SystemUi.QS_DIALOG_WORK_PROFILE_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda22
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                        String str4 = workProfileVpnName;
                        Objects.requireNonNull(qSSecurityFooter);
                        return qSSecurityFooter.mContext.getString(2131952782, str4);
                    }
                }, new Object[]{workProfileVpnName}));
            } else if (hasWorkProfile) {
                spannableStringBuilder.append((CharSequence) this.mDpm.getString("SystemUi.QS_DIALOG_PERSONAL_PROFILE_NAMED_VPN", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda23
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                        String str4 = primaryVpnName;
                        Objects.requireNonNull(qSSecurityFooter);
                        return qSSecurityFooter.mContext.getString(2131952790, str4);
                    }
                }, new Object[]{primaryVpnName}));
            } else {
                spannableStringBuilder.append((CharSequence) this.mContext.getString(2131952788, primaryVpnName));
            }
            spannableStringBuilder.append((CharSequence) this.mContext.getString(2131952793));
            spannableStringBuilder.append(this.mContext.getString(2131952792), new VpnSpan(), 0);
        }
        if (spannableStringBuilder == null) {
            inflate2.findViewById(2131429234).setVisibility(8);
        } else {
            inflate2.findViewById(2131429234).setVisibility(0);
            TextView textView2 = (TextView) inflate2.findViewById(2131429236);
            textView2.setText(spannableStringBuilder);
            textView2.setMovementMethod(new LinkMovementMethod());
        }
        if (str3 != null) {
            z = true;
        } else {
            z = false;
        }
        if (str != null) {
            i = 1;
        } else {
            i = 0;
        }
        if (str2 != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (spannableStringBuilder != null) {
            z3 = true;
        }
        if (!z) {
            if (z2) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            if (z3) {
                i2++;
            }
            if (i2 == 1) {
                if (i != 0) {
                    inflate2.findViewById(2131427652).setVisibility(8);
                }
                if (z2) {
                    inflate2.findViewById(2131428492).setVisibility(8);
                }
                if (z3) {
                    inflate2.findViewById(2131429235).setVisibility(8);
                }
            }
        }
        return inflate2;
    }

    public CharSequence getManagementTitle(CharSequence charSequence) {
        if (charSequence == null || !isFinancedDevice()) {
            return this.mDpm.getString("SystemUi.QS_DIALOG_MANAGEMENT_TITLE", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda11
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    return qSSecurityFooter.mContext.getString(2131952798);
                }
            });
        }
        return this.mContext.getString(2131952799, charSequence);
    }

    public final String getMangedDeviceGeneralText(final CharSequence charSequence) {
        if (charSequence == null) {
            return this.mDpm.getString("SystemUi.QS_MSG_MANAGEMENT", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                    Objects.requireNonNull(qSSecurityFooter);
                    return qSSecurityFooter.mContext.getString(2131953093);
                }
            });
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(2131953109, charSequence);
        }
        return this.mDpm.getString("SystemUi.QS_MSG_NAMED_MANAGEMENT", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda14
            @Override // java.util.concurrent.Callable
            public final Object call() {
                QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                CharSequence charSequence2 = charSequence;
                Objects.requireNonNull(qSSecurityFooter);
                return qSSecurityFooter.mContext.getString(2131953099, charSequence2);
            }
        }, new Object[]{charSequence});
    }

    public String getSettingsButton() {
        return this.mDpm.getString("SystemUi.QS_DIALOG_VIEW_POLICIES", new Callable() { // from class: com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda6
            @Override // java.util.concurrent.Callable
            public final Object call() {
                QSSecurityFooter qSSecurityFooter = QSSecurityFooter.this;
                Objects.requireNonNull(qSSecurityFooter);
                return qSSecurityFooter.mContext.getString(2131952779);
            }
        });
    }

    public final boolean isFinancedDevice() {
        if (this.mSecurityController.isDeviceManaged()) {
            SecurityController securityController = this.mSecurityController;
            if (securityController.getDeviceOwnerType(securityController.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.qs.QSSecurityFooter$1] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.qs.QSSecurityFooter$2] */
    public QSSecurityFooter(View view, UserTracker userTracker, Handler handler, ActivityStarter activityStarter, SecurityController securityController, DialogLaunchAnimator dialogLaunchAnimator, Looper looper) {
        this.mRootView = view;
        view.setOnClickListener(this);
        this.mFooterText = (TextView) view.findViewById(2131427986);
        this.mPrimaryFooterIcon = (ImageView) view.findViewById(2131428610);
        this.mContext = view.getContext();
        this.mDpm = (DevicePolicyManager) view.getContext().getSystemService(DevicePolicyManager.class);
        this.mMainHandler = handler;
        this.mActivityStarter = activityStarter;
        this.mSecurityController = securityController;
        this.mHandler = new H(looper);
        this.mUserTracker = userTracker;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            Intent intent = new Intent("android.settings.ENTERPRISE_PRIVACY_SETTINGS");
            dialogInterface.dismiss();
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }
    }
}
