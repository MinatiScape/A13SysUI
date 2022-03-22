package com.android.systemui.biometrics;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.PromptInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class AuthCredentialView extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Callback mCallback;
    public AuthContainerView mContainerView;
    public int mCredentialType;
    public TextView mDescriptionView;
    public int mEffectiveUserId;
    public AnonymousClass2 mErrorTimer;
    public TextView mErrorView;
    public ImageView mIconView;
    public long mOperationId;
    public AuthPanelController mPanelController;
    public AsyncTask<?, ?, ?> mPendingLockCheck;
    public PromptInfo mPromptInfo;
    public boolean mShouldAnimateContents;
    public boolean mShouldAnimatePanel;
    public TextView mSubtitleView;
    public TextView mTitleView;
    public int mUserId;
    public final AnonymousClass1 mClearErrorRunnable = new AnonymousClass1();
    public final LockPatternUtils mLockPatternUtils = new LockPatternUtils(((LinearLayout) this).mContext);
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final AccessibilityManager mAccessibilityManager = (AccessibilityManager) ((LinearLayout) this).mContext.getSystemService(AccessibilityManager.class);
    public final UserManager mUserManager = (UserManager) ((LinearLayout) this).mContext.getSystemService(UserManager.class);
    public final DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) ((LinearLayout) this).mContext.getSystemService(DevicePolicyManager.class);

    /* renamed from: com.android.systemui.biometrics.AuthCredentialView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            TextView textView = AuthCredentialView.this.mErrorView;
            if (textView != null) {
                textView.setText("");
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* loaded from: classes.dex */
    public static class ErrorTimer extends CountDownTimer {
        public final Context mContext;
        public final TextView mErrorView;

        public ErrorTimer(Context context, long j, TextView textView) {
            super(j, 1000L);
            this.mErrorView = textView;
            this.mContext = context;
        }

        @Override // android.os.CountDownTimer
        public final void onTick(long j) {
            this.mErrorView.setText(this.mContext.getString(2131951927, Integer.valueOf((int) (j / 1000))));
        }
    }

    /* renamed from: $r8$lambda$Gx6uVWVgpTY74-hc4P38xpLn6pE  reason: not valid java name */
    public static String m30$r8$lambda$Gx6uVWVgpTY74hc4P38xpLn6pE(AuthCredentialView authCredentialView, int i) {
        int i2;
        if (i == 1) {
            i2 = 2131951933;
        } else if (i == 2) {
            i2 = 2131951934;
        } else if (i == 3) {
            i2 = 2131951935;
        } else {
            Objects.requireNonNull(authCredentialView);
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unrecognized user type:", i));
        }
        return ((LinearLayout) authCredentialView).mContext.getString(i2);
    }

    public void onErrorTimeoutFinish() {
    }

    public final int getUserTypeForWipe() {
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mDevicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(this.mEffectiveUserId));
        if (userInfo == null || userInfo.isPrimary()) {
            return 1;
        }
        if (userInfo.isManagedProfile()) {
            return 2;
        }
        return 3;
    }

    public static String $r8$lambda$eIDslPxXQOSGDO4GIa22aIM6I0o(AuthCredentialView authCredentialView, int i) {
        int i2;
        Objects.requireNonNull(authCredentialView);
        if (i == 1) {
            i2 = 2131951944;
        } else if (i != 2) {
            i2 = 2131951938;
        } else {
            i2 = 2131951941;
        }
        return ((LinearLayout) authCredentialView).mContext.getString(i2);
    }

    public AuthCredentialView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        Drawable drawable;
        super.onAttachedToWindow();
        PromptInfo promptInfo = this.mPromptInfo;
        CharSequence deviceCredentialTitle = promptInfo.getDeviceCredentialTitle();
        if (deviceCredentialTitle == null) {
            deviceCredentialTitle = promptInfo.getTitle();
        }
        this.mTitleView.setText(deviceCredentialTitle);
        TextView textView = this.mSubtitleView;
        PromptInfo promptInfo2 = this.mPromptInfo;
        CharSequence deviceCredentialSubtitle = promptInfo2.getDeviceCredentialSubtitle();
        if (deviceCredentialSubtitle == null) {
            deviceCredentialSubtitle = promptInfo2.getSubtitle();
        }
        if (TextUtils.isEmpty(deviceCredentialSubtitle)) {
            textView.setVisibility(8);
        } else {
            textView.setText(deviceCredentialSubtitle);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
        TextView textView2 = this.mDescriptionView;
        PromptInfo promptInfo3 = this.mPromptInfo;
        CharSequence deviceCredentialDescription = promptInfo3.getDeviceCredentialDescription();
        if (deviceCredentialDescription == null) {
            deviceCredentialDescription = promptInfo3.getDescription();
        }
        if (TextUtils.isEmpty(deviceCredentialDescription)) {
            textView2.setVisibility(8);
        } else {
            textView2.setText(deviceCredentialDescription);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
        announceForAccessibility(deviceCredentialTitle);
        if (this.mIconView != null) {
            Context context = ((LinearLayout) this).mContext;
            if (((UserManager) context.getSystemService(UserManager.class)).isManagedProfile(this.mEffectiveUserId)) {
                drawable = getResources().getDrawable(2131231601, ((LinearLayout) this).mContext.getTheme());
            } else {
                drawable = getResources().getDrawable(2131231602, ((LinearLayout) this).mContext.getTheme());
            }
            this.mIconView.setImageDrawable(drawable);
        }
        if (this.mShouldAnimateContents) {
            setTranslationY(getResources().getDimension(2131165373));
            setAlpha(0.0f);
            postOnAnimation(new StatusBar$$ExternalSyntheticLambda18(this, 2));
        }
    }

    /* JADX WARN: Type inference failed for: r6v9, types: [com.android.systemui.biometrics.AuthCredentialView$2, android.os.CountDownTimer] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onCredentialVerified(com.android.internal.widget.VerifyCredentialResponse r10, int r11) {
        /*
            Method dump skipped, instructions count: 454
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthCredentialView.onCredentialVerified(com.android.internal.widget.VerifyCredentialResponse, int):void");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnonymousClass2 r0 = this.mErrorTimer;
        if (r0 != null) {
            r0.cancel();
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(2131429057);
        this.mSubtitleView = (TextView) findViewById(2131428947);
        this.mDescriptionView = (TextView) findViewById(2131427815);
        this.mIconView = (ImageView) findViewById(2131428102);
        this.mErrorView = (TextView) findViewById(2131427937);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mShouldAnimatePanel) {
            AuthPanelController authPanelController = this.mPanelController;
            Objects.requireNonNull(authPanelController);
            authPanelController.mUseFullScreen = true;
            AuthPanelController authPanelController2 = this.mPanelController;
            Objects.requireNonNull(authPanelController2);
            int i5 = authPanelController2.mContainerWidth;
            AuthPanelController authPanelController3 = this.mPanelController;
            Objects.requireNonNull(authPanelController3);
            authPanelController2.updateForContentDimensions(i5, authPanelController3.mContainerHeight, 0);
            this.mShouldAnimatePanel = false;
        }
    }
}
