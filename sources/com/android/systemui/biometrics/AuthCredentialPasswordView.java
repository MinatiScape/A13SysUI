package com.android.systemui.biometrics;

import android.content.Context;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImeAwareEditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import java.util.Objects;
/* loaded from: classes.dex */
public class AuthCredentialPasswordView extends AuthCredentialView implements TextView.OnEditorActionListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final InputMethodManager mImm = (InputMethodManager) ((LinearLayout) this).mContext.getSystemService(InputMethodManager.class);
    public ImeAwareEditText mPasswordField;

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean z;
        boolean z2;
        LockscreenCredential lockscreenCredential;
        if (keyEvent == null && (i == 0 || i == 6 || i == 5)) {
            z = true;
        } else {
            z = false;
        }
        if (keyEvent == null || !KeyEvent.isConfirmKey(keyEvent.getKeyCode()) || keyEvent.getAction() != 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (!z && !z2) {
            return false;
        }
        if (this.mCredentialType == 1) {
            lockscreenCredential = LockscreenCredential.createPinOrNone(this.mPasswordField.getText());
        } else {
            lockscreenCredential = LockscreenCredential.createPasswordOrNone(this.mPasswordField.getText());
        }
        try {
            if (!lockscreenCredential.isNone()) {
                this.mPendingLockCheck = LockPatternChecker.verifyCredential(this.mLockPatternUtils, lockscreenCredential, this.mEffectiveUserId, 1, new LockPatternChecker.OnVerifyCallback() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda1
                    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i2) {
                        AuthCredentialPasswordView.this.onCredentialVerified(verifyCredentialResponse, i2);
                    }
                });
            }
            lockscreenCredential.close();
            return true;
        } catch (Throwable th) {
            if (lockscreenCredential != null) {
                try {
                    lockscreenCredential.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public AuthCredentialPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPasswordField.setTextOperationUser(UserHandle.of(this.mUserId));
        if (this.mCredentialType == 1) {
            this.mPasswordField.setInputType(18);
        }
        this.mPasswordField.requestFocus();
        this.mPasswordField.scheduleShowSoftInput();
    }

    @Override // com.android.systemui.biometrics.AuthCredentialView
    public final void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        super.onCredentialVerified(verifyCredentialResponse, i);
        if (verifyCredentialResponse.isMatched()) {
            this.mImm.hideSoftInputFromWindow(getWindowToken(), 0);
        } else {
            this.mPasswordField.setText("");
        }
    }

    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        ImeAwareEditText findViewById = findViewById(2131428271);
        this.mPasswordField = findViewById;
        findViewById.setOnEditorActionListener(this);
        this.mPasswordField.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                AuthCredentialPasswordView authCredentialPasswordView = AuthCredentialPasswordView.this;
                int i2 = AuthCredentialPasswordView.$r8$clinit;
                Objects.requireNonNull(authCredentialPasswordView);
                if (i != 4) {
                    return false;
                }
                if (keyEvent.getAction() == 1) {
                    authCredentialPasswordView.mContainerView.sendEarlyUserCanceled();
                    authCredentialPasswordView.mContainerView.animateAway(1);
                }
                return true;
            }
        });
    }
}
