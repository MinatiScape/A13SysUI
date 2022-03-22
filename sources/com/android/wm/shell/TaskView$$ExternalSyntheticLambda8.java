package com.android.wm.shell;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.pm.ShortcutInfo;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda8(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                TaskView.$r8$lambda$GN2DCVjyacAWE0TrNN18t7khmiQ((TaskView) this.f$0, (ShortcutInfo) this.f$1, (ActivityOptions) this.f$2);
                return;
            case 1:
                CastTile castTile = (CastTile) this.f$0;
                View view = (View) this.f$1;
                Dialog dialog = (Dialog) this.f$2;
                int i = CastTile.$r8$clinit;
                if (view != null) {
                    DialogLaunchAnimator dialogLaunchAnimator = castTile.mDialogLaunchAnimator;
                    Objects.requireNonNull(dialogLaunchAnimator);
                    dialogLaunchAnimator.showFromView(dialog, view, false);
                    return;
                }
                Objects.requireNonNull(castTile);
                dialog.show();
                return;
            default:
                NotificationLogger.ExpansionStateLogger expansionStateLogger = (NotificationLogger.ExpansionStateLogger) this.f$0;
                String str = (String) this.f$1;
                NotificationLogger.ExpansionStateLogger.State state = (NotificationLogger.ExpansionStateLogger.State) this.f$2;
                Objects.requireNonNull(expansionStateLogger);
                try {
                    expansionStateLogger.mBarService.onNotificationExpansionChanged(str, state.mIsUserAction.booleanValue(), state.mIsExpanded.booleanValue(), state.mLocation.ordinal());
                    return;
                } catch (RemoteException e) {
                    Log.e("NotificationLogger", "Failed to call onNotificationExpansionChanged: ", e);
                    return;
                }
        }
    }
}
