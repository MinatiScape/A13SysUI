package com.android.wm.shell.bubbles;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.PinResult;
import android.util.Log;
import com.android.keyguard.KeyguardSimPinView;
import com.android.keyguard.KeyguardSimPinViewController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda20 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda20(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                List list = (List) this.f$1;
                Objects.requireNonNull(bubbleStackView);
                for (int i = 0; i < list.size(); i++) {
                    Bubble bubble = (Bubble) list.get(i);
                    PhysicsAnimationLayout physicsAnimationLayout = bubbleStackView.mBubbleContainer;
                    Objects.requireNonNull(bubble);
                    physicsAnimationLayout.reorderView(bubble.mIconView, i);
                }
                return;
            case 1:
                KeyguardSimPinViewController.AnonymousClass2 r0 = (KeyguardSimPinViewController.AnonymousClass2) this.f$0;
                PinResult pinResult = (PinResult) this.f$1;
                int i2 = KeyguardSimPinViewController.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r0);
                KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                ProgressDialog progressDialog = KeyguardSimPinViewController.this.mSimUnlockProgressDialog;
                if (progressDialog != null) {
                    progressDialog.hide();
                }
                KeyguardSimPinView keyguardSimPinView = (KeyguardSimPinView) KeyguardSimPinViewController.this.mView;
                if (pinResult.getResult() != 0) {
                    z = true;
                } else {
                    z = false;
                }
                keyguardSimPinView.resetPasswordText(true, z);
                if (pinResult.getResult() == 0) {
                    KeyguardSimPinViewController keyguardSimPinViewController = KeyguardSimPinViewController.this;
                    keyguardSimPinViewController.mKeyguardUpdateMonitor.reportSimUnlocked(keyguardSimPinViewController.mSubId);
                    KeyguardSimPinViewController keyguardSimPinViewController2 = KeyguardSimPinViewController.this;
                    keyguardSimPinViewController2.mRemainingAttempts = -1;
                    keyguardSimPinViewController2.mShowDefaultMessage = true;
                    keyguardSimPinViewController2.getKeyguardSecurityCallback().dismiss(KeyguardUpdateMonitor.getCurrentUser());
                } else {
                    KeyguardSimPinViewController.this.mShowDefaultMessage = false;
                    if (pinResult.getResult() != 1) {
                        KeyguardSimPinViewController keyguardSimPinViewController3 = KeyguardSimPinViewController.this;
                        keyguardSimPinViewController3.mMessageAreaController.setMessage(((KeyguardSimPinView) keyguardSimPinViewController3.mView).getResources().getString(2131952582));
                    } else if (pinResult.getAttemptsRemaining() <= 2) {
                        KeyguardSimPinViewController keyguardSimPinViewController4 = KeyguardSimPinViewController.this;
                        int attemptsRemaining = pinResult.getAttemptsRemaining();
                        Objects.requireNonNull(keyguardSimPinViewController4);
                        String pinPasswordErrorMessage = keyguardSimPinViewController4.getPinPasswordErrorMessage(attemptsRemaining);
                        AlertDialog alertDialog = keyguardSimPinViewController4.mRemainingAttemptsDialog;
                        if (alertDialog == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPinView) keyguardSimPinViewController4.mView).getContext());
                            builder.setMessage(pinPasswordErrorMessage);
                            builder.setCancelable(false);
                            builder.setNeutralButton(2131952934, (DialogInterface.OnClickListener) null);
                            AlertDialog create = builder.create();
                            keyguardSimPinViewController4.mRemainingAttemptsDialog = create;
                            create.getWindow().setType(2009);
                        } else {
                            alertDialog.setMessage(pinPasswordErrorMessage);
                        }
                        keyguardSimPinViewController4.mRemainingAttemptsDialog.show();
                    } else {
                        KeyguardSimPinViewController keyguardSimPinViewController5 = KeyguardSimPinViewController.this;
                        keyguardSimPinViewController5.mMessageAreaController.setMessage(keyguardSimPinViewController5.getPinPasswordErrorMessage(pinResult.getAttemptsRemaining()));
                    }
                    Log.d("KeyguardSimPinView", "verifyPasswordAndUnlock  CheckSimPin.onSimCheckResponse: " + pinResult + " attemptsRemaining=" + pinResult.getAttemptsRemaining());
                }
                KeyguardSimPinViewController.this.getKeyguardSecurityCallback().userActivity();
                KeyguardSimPinViewController.this.mCheckSimPinThread = null;
                return;
            case 2:
                ((PendingIntent) this.f$0).registerCancelListener((PendingIntent.CancelListener) this.f$1);
                return;
            default:
                GarbageMonitor.MemoryTile.AnonymousClass1 r02 = (GarbageMonitor.MemoryTile.AnonymousClass1) this.f$0;
                int i3 = GarbageMonitor.MemoryTile.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r02);
                GarbageMonitor.MemoryTile memoryTile = GarbageMonitor.MemoryTile.this;
                memoryTile.dumpInProgress = false;
                memoryTile.refreshState(null);
                GarbageMonitor.MemoryTile memoryTile2 = GarbageMonitor.MemoryTile.this;
                Objects.requireNonNull(memoryTile2);
                memoryTile2.mHost.collapsePanels();
                GarbageMonitor.MemoryTile.this.mActivityStarter.postStartActivityDismissingKeyguard((Intent) this.f$1, 0);
                return;
        }
    }
}
