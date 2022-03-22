package com.android.systemui.qs.tiles.dialog;

import android.view.View;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                Objects.requireNonNull(internetDialog);
                InternetDialogController internetDialogController = internetDialog.mInternetDialogController;
                Objects.requireNonNull(internetDialogController);
                internetDialogController.startActivity(internetDialogController.getSettingsIntent(), view);
                return;
            default:
                TvUnblockSensorActivity tvUnblockSensorActivity = (TvUnblockSensorActivity) this.f$0;
                int i = TvUnblockSensorActivity.$r8$clinit;
                Objects.requireNonNull(tvUnblockSensorActivity);
                int i2 = tvUnblockSensorActivity.mSensor;
                if (i2 == Integer.MAX_VALUE) {
                    tvUnblockSensorActivity.mSensorPrivacyController.setSensorBlocked(5, 2, false);
                    tvUnblockSensorActivity.mSensorPrivacyController.setSensorBlocked(5, 1, false);
                    return;
                }
                tvUnblockSensorActivity.mSensorPrivacyController.setSensorBlocked(5, i2, false);
                return;
        }
    }
}
