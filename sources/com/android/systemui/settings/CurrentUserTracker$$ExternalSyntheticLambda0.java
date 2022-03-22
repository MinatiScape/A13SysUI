package com.android.systemui.settings;

import android.app.ActivityManager;
import android.content.Intent;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.wm.shell.recents.RecentTasksController;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.actions.Action;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CurrentUserTracker$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CurrentUserTracker$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((CurrentUserTracker) this.f$0).onUserSwitched(((Integer) obj).intValue());
                return;
            case 1:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                IntentButtonProvider.IntentButton intentButton = (IntentButtonProvider.IntentButton) obj;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                keyguardBottomAreaView.mLeftButton = intentButton;
                if (!(intentButton instanceof KeyguardBottomAreaView.DefaultLeftButton)) {
                    keyguardBottomAreaView.mLeftIsVoiceAssist = false;
                }
                keyguardBottomAreaView.updateLeftAffordance();
                return;
            case 2:
                ((RecentTasksController) obj).removeSplitPair(((ActivityManager.RunningTaskInfo) this.f$0).taskId);
                return;
            default:
                ElmyraService elmyraService = (ElmyraService) this.f$0;
                Action action = (Action) obj;
                Objects.requireNonNull(elmyraService);
                ElmyraService.AnonymousClass1 r1 = elmyraService.mActionListener;
                Objects.requireNonNull(action);
                action.mListener = r1;
                return;
        }
    }
}
