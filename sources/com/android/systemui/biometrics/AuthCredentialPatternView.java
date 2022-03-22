package com.android.systemui.biometrics;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.biometrics.AuthCredentialPatternView;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class AuthCredentialPatternView extends AuthCredentialView {
    public LockPatternView mLockPatternView;

    /* loaded from: classes.dex */
    public class UnlockPatternListener implements LockPatternView.OnPatternListener {
        public final void onPatternCellAdded(List<LockPatternView.Cell> list) {
        }

        public final void onPatternCleared() {
        }

        public final void onPatternStart() {
        }

        public UnlockPatternListener() {
        }

        public final void onPatternDetected(List<LockPatternView.Cell> list) {
            AsyncTask<?, ?, ?> asyncTask = AuthCredentialPatternView.this.mPendingLockCheck;
            if (asyncTask != null) {
                asyncTask.cancel(false);
            }
            AuthCredentialPatternView.this.mLockPatternView.setEnabled(false);
            if (list.size() < 4) {
                AuthCredentialPatternView.this.onCredentialVerified(VerifyCredentialResponse.ERROR, 0);
                AuthCredentialPatternView.this.mLockPatternView.setEnabled(true);
                return;
            }
            LockscreenCredential createPattern = LockscreenCredential.createPattern(list);
            try {
                AuthCredentialPatternView authCredentialPatternView = AuthCredentialPatternView.this;
                authCredentialPatternView.mPendingLockCheck = LockPatternChecker.verifyCredential(authCredentialPatternView.mLockPatternUtils, createPattern, authCredentialPatternView.mEffectiveUserId, 1, new LockPatternChecker.OnVerifyCallback() { // from class: com.android.systemui.biometrics.AuthCredentialPatternView$UnlockPatternListener$$ExternalSyntheticLambda0
                    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
                        AuthCredentialPatternView.UnlockPatternListener unlockPatternListener = AuthCredentialPatternView.UnlockPatternListener.this;
                        Objects.requireNonNull(unlockPatternListener);
                        AuthCredentialPatternView.this.onCredentialVerified(verifyCredentialResponse, i);
                        if (i > 0) {
                            AuthCredentialPatternView.this.mLockPatternView.setEnabled(false);
                        } else {
                            AuthCredentialPatternView.this.mLockPatternView.setEnabled(true);
                        }
                    }
                });
                if (createPattern != null) {
                    createPattern.close();
                }
            } catch (Throwable th) {
                if (createPattern != null) {
                    try {
                        createPattern.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    @Override // com.android.systemui.biometrics.AuthCredentialView
    public final void onErrorTimeoutFinish() {
        this.mLockPatternView.setEnabled(true);
    }

    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        LockPatternView findViewById = findViewById(2131428272);
        this.mLockPatternView = findViewById;
        findViewById.setOnPatternListener(new UnlockPatternListener());
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(this.mUserId));
    }

    public AuthCredentialPatternView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
