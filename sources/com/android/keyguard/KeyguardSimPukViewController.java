package com.android.keyguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.telephony.PinResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardSimPukViewController extends KeyguardPinBasedInputViewController<KeyguardSimPukView> {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public CheckSimPuk mCheckSimPukThread;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public String mPinText;
    public String mPukText;
    public int mRemainingAttempts;
    public AlertDialog mRemainingAttemptsDialog;
    public boolean mShowDefaultMessage;
    public ImageView mSimImageView;
    public ProgressDialog mSimUnlockProgressDialog;
    public final TelephonyManager mTelephonyManager;
    public StateMachine mStateMachine = new StateMachine();
    public int mSubId = -1;
    public AnonymousClass1 mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardSimPukViewController.1
        {
            KeyguardSimPukViewController.this = this;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onSimStateChanged(int i, int i2, int i3) {
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "onSimStateChanged(subId=" + i + ",state=" + i3 + ")");
            }
            if (i3 == 5) {
                KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                keyguardSimPukViewController.mRemainingAttempts = -1;
                keyguardSimPukViewController.mShowDefaultMessage = true;
                keyguardSimPukViewController.getKeyguardSecurityCallback().dismiss(KeyguardUpdateMonitor.getCurrentUser());
                return;
            }
            KeyguardSimPukViewController.this.resetState();
        }
    };

    /* renamed from: com.android.keyguard.KeyguardSimPukViewController$3 */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 extends CheckSimPuk {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass3(String str, String str2, int i) {
            super(str, str2, i);
            KeyguardSimPukViewController.this = r1;
        }

        @Override // com.android.keyguard.KeyguardSimPukViewController.CheckSimPuk
        public final void onSimLockChangedResponse(PinResult pinResult) {
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new ShellExecutor$$ExternalSyntheticLambda0(this, pinResult, 1));
        }

        public static void $r8$lambda$D4YXQJ16o3cMhvtpU1r8aJ4UUVw(AnonymousClass3 r7, PinResult pinResult) {
            boolean z;
            Objects.requireNonNull(r7);
            ProgressDialog progressDialog = KeyguardSimPukViewController.this.mSimUnlockProgressDialog;
            if (progressDialog != null) {
                progressDialog.hide();
            }
            KeyguardSimPukView keyguardSimPukView = (KeyguardSimPukView) KeyguardSimPukViewController.this.mView;
            if (pinResult.getResult() != 0) {
                z = true;
            } else {
                z = false;
            }
            keyguardSimPukView.resetPasswordText(true, z);
            if (pinResult.getResult() == 0) {
                KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                keyguardSimPukViewController.mKeyguardUpdateMonitor.reportSimUnlocked(keyguardSimPukViewController.mSubId);
                KeyguardSimPukViewController keyguardSimPukViewController2 = KeyguardSimPukViewController.this;
                keyguardSimPukViewController2.mRemainingAttempts = -1;
                keyguardSimPukViewController2.mShowDefaultMessage = true;
                keyguardSimPukViewController2.getKeyguardSecurityCallback().dismiss(KeyguardUpdateMonitor.getCurrentUser());
            } else {
                KeyguardSimPukViewController.this.mShowDefaultMessage = false;
                if (pinResult.getResult() == 1) {
                    KeyguardSimPukViewController keyguardSimPukViewController3 = KeyguardSimPukViewController.this;
                    keyguardSimPukViewController3.mMessageAreaController.setMessage(((KeyguardSimPukView) keyguardSimPukViewController3.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId)));
                    if (pinResult.getAttemptsRemaining() <= 2) {
                        KeyguardSimPukViewController keyguardSimPukViewController4 = KeyguardSimPukViewController.this;
                        int attemptsRemaining = pinResult.getAttemptsRemaining();
                        Objects.requireNonNull(keyguardSimPukViewController4);
                        KeyguardSimPukView keyguardSimPukView2 = (KeyguardSimPukView) keyguardSimPukViewController4.mView;
                        String pukPasswordErrorMessage = keyguardSimPukView2.getPukPasswordErrorMessage(attemptsRemaining, false, KeyguardEsimArea.isEsimLocked(keyguardSimPukView2.getContext(), keyguardSimPukViewController4.mSubId));
                        AlertDialog alertDialog = keyguardSimPukViewController4.mRemainingAttemptsDialog;
                        if (alertDialog == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPukView) keyguardSimPukViewController4.mView).getContext());
                            builder.setMessage(pukPasswordErrorMessage);
                            builder.setCancelable(false);
                            builder.setNeutralButton(2131952934, (DialogInterface.OnClickListener) null);
                            AlertDialog create = builder.create();
                            keyguardSimPukViewController4.mRemainingAttemptsDialog = create;
                            create.getWindow().setType(2009);
                        } else {
                            alertDialog.setMessage(pukPasswordErrorMessage);
                        }
                        keyguardSimPukViewController4.mRemainingAttemptsDialog.show();
                    } else {
                        KeyguardSimPukViewController keyguardSimPukViewController5 = KeyguardSimPukViewController.this;
                        keyguardSimPukViewController5.mMessageAreaController.setMessage(((KeyguardSimPukView) keyguardSimPukViewController5.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId)));
                    }
                } else {
                    KeyguardSimPukViewController keyguardSimPukViewController6 = KeyguardSimPukViewController.this;
                    keyguardSimPukViewController6.mMessageAreaController.setMessage(((KeyguardSimPukView) keyguardSimPukViewController6.mView).getResources().getString(2131952583));
                }
                if (KeyguardSimPukViewController.DEBUG) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("verifyPasswordAndUnlock  UpdateSim.onSimCheckResponse:  attemptsRemaining=");
                    m.append(pinResult.getAttemptsRemaining());
                    Log.d("KeyguardSimPukView", m.toString());
                }
            }
            KeyguardSimPukViewController.this.mStateMachine.reset();
            KeyguardSimPukViewController.this.mCheckSimPukThread = null;
        }
    }

    /* loaded from: classes.dex */
    public abstract class CheckSimPuk extends Thread {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final String mPin;
        public final String mPuk;
        public final int mSubId;

        public abstract void onSimLockChangedResponse(PinResult pinResult);

        public CheckSimPuk(String str, String str2, int i) {
            KeyguardSimPukViewController.this = r1;
            this.mPuk = str;
            this.mPin = str2;
            this.mSubId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            boolean z = KeyguardSimPukViewController.DEBUG;
            if (z) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("call supplyIccLockPuk(subid=");
                m.append(this.mSubId);
                m.append(")");
                Log.v("KeyguardSimPukView", m.toString());
            }
            PinResult supplyIccLockPuk = KeyguardSimPukViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPuk(this.mPuk, this.mPin);
            if (z) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("supplyIccLockPuk returned: ");
                m2.append(supplyIccLockPuk.toString());
                Log.v("KeyguardSimPukView", m2.toString());
            }
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new BubblesManager$5$$ExternalSyntheticLambda0(this, supplyIccLockPuk, 1));
        }
    }

    /* loaded from: classes.dex */
    public class StateMachine {
        public int mState = 0;

        public StateMachine() {
            KeyguardSimPukViewController.this = r1;
        }

        public final void reset() {
            int i;
            String str;
            KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
            String str2 = "";
            keyguardSimPukViewController.mPinText = str2;
            keyguardSimPukViewController.mPukText = str2;
            int i2 = 0;
            this.mState = 0;
            int nextSubIdForState = keyguardSimPukViewController.mKeyguardUpdateMonitor.getNextSubIdForState(3);
            if (nextSubIdForState != keyguardSimPukViewController.mSubId && SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
                keyguardSimPukViewController.mSubId = nextSubIdForState;
                keyguardSimPukViewController.mShowDefaultMessage = true;
                keyguardSimPukViewController.mRemainingAttempts = -1;
            }
            KeyguardSimPukViewController keyguardSimPukViewController2 = KeyguardSimPukViewController.this;
            if (keyguardSimPukViewController2.mShowDefaultMessage) {
                int i3 = keyguardSimPukViewController2.mRemainingAttempts;
                if (i3 >= 0) {
                    KeyguardMessageAreaController keyguardMessageAreaController = keyguardSimPukViewController2.mMessageAreaController;
                    KeyguardSimPukView keyguardSimPukView = (KeyguardSimPukView) keyguardSimPukViewController2.mView;
                    keyguardMessageAreaController.setMessage(keyguardSimPukView.getPukPasswordErrorMessage(i3, true, KeyguardEsimArea.isEsimLocked(keyguardSimPukView.getContext(), keyguardSimPukViewController2.mSubId)));
                } else {
                    boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) keyguardSimPukViewController2.mView).getContext(), keyguardSimPukViewController2.mSubId);
                    TelephonyManager telephonyManager = keyguardSimPukViewController2.mTelephonyManager;
                    if (telephonyManager != null) {
                        i = telephonyManager.getActiveModemCount();
                    } else {
                        i = 1;
                    }
                    Resources resources = ((KeyguardSimPukView) keyguardSimPukViewController2.mView).getResources();
                    TypedArray obtainStyledAttributes = ((KeyguardSimPukView) keyguardSimPukViewController2.mView).getContext().obtainStyledAttributes(new int[]{16842904});
                    int color = obtainStyledAttributes.getColor(0, -1);
                    obtainStyledAttributes.recycle();
                    if (i < 2) {
                        str = resources.getString(2131952595);
                    } else {
                        SubscriptionInfo subscriptionInfoForSubId = keyguardSimPukViewController2.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(keyguardSimPukViewController2.mSubId);
                        String str3 = str2;
                        if (subscriptionInfoForSubId != null) {
                            str3 = subscriptionInfoForSubId.getDisplayName();
                        }
                        str = resources.getString(2131952596, str3);
                        if (subscriptionInfoForSubId != null) {
                            color = subscriptionInfoForSubId.getIconTint();
                        }
                    }
                    if (isEsimLocked) {
                        str = resources.getString(2131952597, str);
                    }
                    keyguardSimPukViewController2.mMessageAreaController.setMessage(str);
                    keyguardSimPukViewController2.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
                    new CheckSimPuk(keyguardSimPukViewController2.mSubId) { // from class: com.android.keyguard.KeyguardSimPukViewController.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super("", "", r3);
                            KeyguardSimPukViewController.this = keyguardSimPukViewController2;
                        }

                        @Override // com.android.keyguard.KeyguardSimPukViewController.CheckSimPuk
                        public final void onSimLockChangedResponse(PinResult pinResult) {
                            if (pinResult == null) {
                                Log.e("KeyguardSimPukView", "onSimCheckResponse, pin result is NULL");
                                return;
                            }
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onSimCheckResponse  empty One result ");
                            m.append(pinResult.toString());
                            Log.d("KeyguardSimPukView", m.toString());
                            if (pinResult.getAttemptsRemaining() >= 0) {
                                KeyguardSimPukViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                                KeyguardSimPukViewController keyguardSimPukViewController3 = KeyguardSimPukViewController.this;
                                keyguardSimPukViewController3.mMessageAreaController.setMessage(((KeyguardSimPukView) keyguardSimPukViewController3.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId)));
                            }
                        }
                    }.start();
                }
            }
            boolean isEsimLocked2 = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId);
            KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).findViewById(2131428172);
            int i4 = KeyguardSimPukViewController.this.mSubId;
            Objects.requireNonNull(keyguardEsimArea);
            keyguardEsimArea.mSubscriptionId = i4;
            if (!isEsimLocked2) {
                i2 = 8;
            }
            keyguardEsimArea.setVisibility(i2);
            KeyguardSimPukViewController.this.mPasswordEntry.requestFocus();
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.keyguard.KeyguardSimPukViewController$1] */
    public KeyguardSimPukViewController(KeyguardSimPukView keyguardSimPukView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPukView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mSimImageView = (ImageView) keyguardSimPukView.findViewById(2131428190);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public final boolean shouldLockout(long j) {
        return false;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public final void onPause() {
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public final void resetState() {
        ((KeyguardPinBasedInputView) this.mView).setPasswordEntryEnabled(true);
        this.mStateMachine.reset();
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public final void verifyPasswordAndUnlock() {
        StateMachine stateMachine = this.mStateMachine;
        Objects.requireNonNull(stateMachine);
        int i = stateMachine.mState;
        int i2 = 0;
        if (i == 0) {
            KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
            Objects.requireNonNull(keyguardSimPukViewController);
            PasswordTextView passwordTextView = keyguardSimPukViewController.mPasswordEntry;
            Objects.requireNonNull(passwordTextView);
            if (passwordTextView.mText.length() == 8) {
                PasswordTextView passwordTextView2 = keyguardSimPukViewController.mPasswordEntry;
                Objects.requireNonNull(passwordTextView2);
                keyguardSimPukViewController.mPukText = passwordTextView2.mText;
                i2 = 1;
            }
            if (i2 != 0) {
                stateMachine.mState = 1;
                i2 = 2131952594;
            } else {
                i2 = 2131952581;
            }
        } else if (i == 1) {
            KeyguardSimPukViewController keyguardSimPukViewController2 = KeyguardSimPukViewController.this;
            Objects.requireNonNull(keyguardSimPukViewController2);
            PasswordTextView passwordTextView3 = keyguardSimPukViewController2.mPasswordEntry;
            Objects.requireNonNull(passwordTextView3);
            int length = passwordTextView3.mText.length();
            if (length >= 4 && length <= 8) {
                PasswordTextView passwordTextView4 = keyguardSimPukViewController2.mPasswordEntry;
                Objects.requireNonNull(passwordTextView4);
                keyguardSimPukViewController2.mPinText = passwordTextView4.mText;
                i2 = 1;
            }
            if (i2 != 0) {
                stateMachine.mState = 2;
                i2 = 2131952569;
            } else {
                i2 = 2131952580;
            }
        } else if (i == 2) {
            KeyguardSimPukViewController keyguardSimPukViewController3 = KeyguardSimPukViewController.this;
            Objects.requireNonNull(keyguardSimPukViewController3);
            String str = keyguardSimPukViewController3.mPinText;
            PasswordTextView passwordTextView5 = keyguardSimPukViewController3.mPasswordEntry;
            Objects.requireNonNull(passwordTextView5);
            if (str.equals(passwordTextView5.mText)) {
                stateMachine.mState = 3;
                KeyguardSimPukViewController keyguardSimPukViewController4 = KeyguardSimPukViewController.this;
                Objects.requireNonNull(keyguardSimPukViewController4);
                if (keyguardSimPukViewController4.mSimUnlockProgressDialog == null) {
                    ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPukView) keyguardSimPukViewController4.mView).getContext());
                    keyguardSimPukViewController4.mSimUnlockProgressDialog = progressDialog;
                    progressDialog.setMessage(((KeyguardSimPukView) keyguardSimPukViewController4.mView).getResources().getString(2131952600));
                    keyguardSimPukViewController4.mSimUnlockProgressDialog.setIndeterminate(true);
                    keyguardSimPukViewController4.mSimUnlockProgressDialog.setCancelable(false);
                    if (!(((KeyguardSimPukView) keyguardSimPukViewController4.mView).getContext() instanceof Activity)) {
                        keyguardSimPukViewController4.mSimUnlockProgressDialog.getWindow().setType(2009);
                    }
                }
                keyguardSimPukViewController4.mSimUnlockProgressDialog.show();
                if (keyguardSimPukViewController4.mCheckSimPukThread == null) {
                    AnonymousClass3 r2 = new AnonymousClass3(keyguardSimPukViewController4.mPukText, keyguardSimPukViewController4.mPinText, keyguardSimPukViewController4.mSubId);
                    keyguardSimPukViewController4.mCheckSimPukThread = r2;
                    r2.start();
                }
                i2 = 2131952561;
            } else {
                stateMachine.mState = 1;
                i2 = 2131952579;
            }
        }
        ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).resetPasswordText(true, true);
        if (i2 != 0) {
            KeyguardSimPukViewController.this.mMessageAreaController.setMessage(i2);
        }
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        super.onViewAttached();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        super.onViewDetached();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
    }
}
