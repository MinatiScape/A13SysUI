package com.android.keyguard;

import android.content.ComponentName;
import android.util.Log;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.dreamliner.DockGestureController;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda8(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                Objects.requireNonNull(keyguardUpdateMonitor);
                Log.e("KeyguardUpdateMonitor", "Face cancellation not received, transitioning to STOPPED");
                keyguardUpdateMonitor.mFaceRunningState = 0;
                keyguardUpdateMonitor.updateFaceListeningState(1);
                return;
            case 1:
                LongScreenshotActivity longScreenshotActivity = (LongScreenshotActivity) this.f$0;
                int i = LongScreenshotActivity.$r8$clinit;
                Objects.requireNonNull(longScreenshotActivity);
                try {
                    longScreenshotActivity.mSavedImagePath = (File) longScreenshotActivity.mCacheSaveFuture.get();
                    return;
                } catch (InterruptedException | CancellationException | ExecutionException e) {
                    Log.e("Screenshot", "Error saving temp image file", e);
                    longScreenshotActivity.finishAndRemoveTask();
                    return;
                }
            case 2:
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = (StatusBarKeyguardViewManager) this.f$0;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                statusBarKeyguardViewManager.mStatusBar.hideKeyguard();
                statusBarKeyguardViewManager.onKeyguardFadedAway();
                return;
            case 3:
                BubbleStackView.AnonymousClass5 r5 = (BubbleStackView.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r5);
                BubbleStackView.m154$$Nest$mresetDismissAnimator(BubbleStackView.this);
                BubbleStackView.m152$$Nest$mdismissMagnetizedObject(BubbleStackView.this);
                return;
            case 4:
                LetterboxEduWindowManager letterboxEduWindowManager = (LetterboxEduWindowManager) this.f$0;
                String str = LetterboxEduWindowManager.HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME;
                Objects.requireNonNull(letterboxEduWindowManager);
                letterboxEduWindowManager.release();
                letterboxEduWindowManager.mOnDismissCallback.run();
                return;
            case 5:
                AssistManagerGoogle assistManagerGoogle = (AssistManagerGoogle) this.f$0;
                Objects.requireNonNull(assistManagerGoogle);
                AssistantPresenceHandler assistantPresenceHandler = assistManagerGoogle.mAssistantPresenceHandler;
                Objects.requireNonNull(assistantPresenceHandler);
                ComponentName assistComponentForUser = assistantPresenceHandler.mAssistUtils.getAssistComponentForUser(-2);
                if (assistComponentForUser == null || !"com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString())) {
                    z = false;
                }
                assistantPresenceHandler.updateAssistantPresence(z, assistantPresenceHandler.mNgaIsAssistant, assistantPresenceHandler.mSysUiIsNgaUi);
                assistManagerGoogle.mCheckAssistantStatus = false;
                return;
            default:
                DockGestureController dockGestureController = (DockGestureController) this.f$0;
                int i2 = DockGestureController.$r8$clinit;
                Objects.requireNonNull(dockGestureController);
                dockGestureController.mSettingsGear.setVisibility(4);
                return;
        }
    }
}
