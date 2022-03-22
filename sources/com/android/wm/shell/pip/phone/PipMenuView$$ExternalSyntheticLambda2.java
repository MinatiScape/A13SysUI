package com.android.wm.shell.pip.phone;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                PipMenuView.$r8$lambda$ZIdnozLD4vi0K38Zv_I0w2iOV1U((PipMenuView) this.f$0, view);
                return;
            case 1:
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.f$0;
                int i = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull(mediaOutputBaseDialog);
                mediaOutputBaseDialog.dismiss();
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.startActivityForResult(new Intent("com.google.android.settings.games.GAME_SETTINGS").setPackage(ThemeOverlayApplier.SETTINGS_PACKAGE), 0);
                return;
        }
    }
}
