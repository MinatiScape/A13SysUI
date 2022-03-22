package com.android.settingslib.inputmethod;

import android.content.DialogInterface;
import android.content.IntentFilter;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InputMethodPreference$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ InputMethodPreference$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        switch (this.$r8$classId) {
            case 0:
                InputMethodPreference inputMethodPreference = (InputMethodPreference) this.f$0;
                int i2 = InputMethodPreference.$r8$clinit;
                Objects.requireNonNull(inputMethodPreference);
                inputMethodPreference.setCheckedInternal(false);
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_RESTART_CANCELLED);
                return;
        }
    }
}
