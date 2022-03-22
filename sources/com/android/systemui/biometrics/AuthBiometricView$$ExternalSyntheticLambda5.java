package com.android.systemui.biometrics;

import android.content.ClipData;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.android.keyguard.KeyguardPinBasedInputViewController;
import com.android.keyguard.PasswordTextView;
import com.android.systemui.clipboardoverlay.EditTextActivity;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                authBiometricView.updateState(6);
                return;
            case 1:
                KeyguardPinBasedInputViewController keyguardPinBasedInputViewController = (KeyguardPinBasedInputViewController) this.f$0;
                Objects.requireNonNull(keyguardPinBasedInputViewController);
                if (keyguardPinBasedInputViewController.mPasswordEntry.isEnabled()) {
                    PasswordTextView passwordTextView = keyguardPinBasedInputViewController.mPasswordEntry;
                    Objects.requireNonNull(passwordTextView);
                    int length = passwordTextView.mText.length();
                    StringBuilder transformedText = passwordTextView.getTransformedText();
                    if (length > 0) {
                        int i2 = length - 1;
                        passwordTextView.mText = passwordTextView.mText.substring(0, i2);
                        passwordTextView.mTextChars.get(i2).startRemoveAnimation(0L, 0L);
                        passwordTextView.sendAccessibilityEventTypeViewTextChanged(transformedText, transformedText.length() - 1, 1, 0);
                    }
                    passwordTextView.mPM.userActivity(SystemClock.uptimeMillis(), false);
                    PasswordTextView.UserActivityListener userActivityListener = passwordTextView.mUserActivityListener;
                    if (userActivityListener != null) {
                        ((KeyguardPinBasedInputViewController) ((DozeTriggers$$ExternalSyntheticLambda0) userActivityListener).f$0).onUserInput();
                        return;
                    }
                    return;
                }
                return;
            case 2:
                EditTextActivity editTextActivity = (EditTextActivity) this.f$0;
                int i3 = EditTextActivity.$r8$clinit;
                Objects.requireNonNull(editTextActivity);
                editTextActivity.mClipboardManager.setPrimaryClip(ClipData.newPlainText("text", editTextActivity.mEditText.getText()));
                ((InputMethodManager) editTextActivity.getSystemService(InputMethodManager.class)).hideSoftInputFromWindow(editTextActivity.mEditText.getWindowToken(), 0);
                editTextActivity.finish();
                return;
            case 3:
                LongScreenshotActivity.$r8$lambda$qOUpLbBDnQlAC3CzBI4dsyuYNs4((LongScreenshotActivity) this.f$0, view);
                return;
            default:
                NotificationConversationInfo notificationConversationInfo = (NotificationConversationInfo) this.f$0;
                int i4 = NotificationConversationInfo.$r8$clinit;
                Objects.requireNonNull(notificationConversationInfo);
                notificationConversationInfo.setSelectedAction(2);
                notificationConversationInfo.updateToggleActions(notificationConversationInfo.mSelectedAction, true);
                return;
        }
    }
}
