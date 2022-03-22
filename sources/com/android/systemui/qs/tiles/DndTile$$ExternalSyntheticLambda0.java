package com.android.systemui.qs.tiles;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.UserHandle;
import android.service.notification.Condition;
import android.util.Slog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.settingslib.notification.EnableZenModeDialog;
import com.android.systemui.qs.QSDndEvent;
import com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.volume.VolumeDialogImpl;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DndTile$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ DndTile$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                DndTile dndTile = (DndTile) this.f$0;
                View view = (View) this.f$1;
                Objects.requireNonNull(dndTile);
                final EnableZenModeDialog enableZenModeDialog = new EnableZenModeDialog(dndTile.mContext, dndTile.mQSZenDialogMetricsLogger);
                enableZenModeDialog.mNotificationManager = (NotificationManager) enableZenModeDialog.mContext.getSystemService("notification");
                enableZenModeDialog.mForeverId = Condition.newId(enableZenModeDialog.mContext).appendPath("forever").build();
                enableZenModeDialog.mAlarmManager = (AlarmManager) enableZenModeDialog.mContext.getSystemService("alarm");
                enableZenModeDialog.mUserId = enableZenModeDialog.mContext.getUserId();
                AlertDialog.Builder positiveButton = new AlertDialog.Builder(enableZenModeDialog.mContext, 2132018183).setTitle(2131953683).setPositiveButton(2131953680, new DialogInterface.OnClickListener() { // from class: com.android.settingslib.notification.EnableZenModeDialog.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        ConditionTag conditionTagAt = enableZenModeDialog.getConditionTagAt(enableZenModeDialog.mZenRadioGroup.getCheckedRadioButtonId());
                        if (enableZenModeDialog.isForever(conditionTagAt.condition)) {
                            QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger = (QSZenModeDialogMetricsLogger) enableZenModeDialog.mMetricsLogger;
                            Objects.requireNonNull(qSZenModeDialogMetricsLogger);
                            MetricsLogger.action(qSZenModeDialogMetricsLogger.mContext, 1259);
                            qSZenModeDialogMetricsLogger.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_FOREVER);
                        } else if (enableZenModeDialog.isAlarm(conditionTagAt.condition)) {
                            QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger2 = (QSZenModeDialogMetricsLogger) enableZenModeDialog.mMetricsLogger;
                            Objects.requireNonNull(qSZenModeDialogMetricsLogger2);
                            MetricsLogger.action(qSZenModeDialogMetricsLogger2.mContext, 1261);
                            qSZenModeDialogMetricsLogger2.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_UNTIL_ALARM);
                        } else if (enableZenModeDialog.isCountdown(conditionTagAt.condition)) {
                            QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger3 = (QSZenModeDialogMetricsLogger) enableZenModeDialog.mMetricsLogger;
                            Objects.requireNonNull(qSZenModeDialogMetricsLogger3);
                            MetricsLogger.action(qSZenModeDialogMetricsLogger3.mContext, 1260);
                            qSZenModeDialogMetricsLogger3.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_UNTIL_COUNTDOWN);
                        } else {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid manual condition: ");
                            m.append(conditionTagAt.condition);
                            Slog.d("EnableZenModeDialog", m.toString());
                        }
                        EnableZenModeDialog enableZenModeDialog2 = enableZenModeDialog;
                        NotificationManager notificationManager = enableZenModeDialog2.mNotificationManager;
                        Condition condition = conditionTagAt.condition;
                        Uri uri = null;
                        if (!enableZenModeDialog2.isForever(condition) && condition != null) {
                            uri = condition.id;
                        }
                        notificationManager.setZenMode(1, uri, "EnableZenModeDialog");
                    }
                });
                positiveButton.setNeutralButton(2131952094, (DialogInterface.OnClickListener) null);
                if (enableZenModeDialog.mLayoutInflater == null) {
                    enableZenModeDialog.mLayoutInflater = new PhoneWindow(enableZenModeDialog.mContext).getLayoutInflater();
                }
                View inflate = enableZenModeDialog.mLayoutInflater.inflate(2131624663, (ViewGroup) null);
                ScrollView scrollView = (ScrollView) inflate.findViewById(2131427734);
                enableZenModeDialog.mZenRadioGroup = (RadioGroup) scrollView.findViewById(2131429295);
                enableZenModeDialog.mZenRadioGroupContent = (LinearLayout) scrollView.findViewById(2131429296);
                enableZenModeDialog.mZenAlarmWarning = (TextView) scrollView.findViewById(2131429290);
                for (int i = 0; i < 3; i++) {
                    View inflate2 = enableZenModeDialog.mLayoutInflater.inflate(2131624662, (ViewGroup) enableZenModeDialog.mZenRadioGroup, false);
                    enableZenModeDialog.mZenRadioGroup.addView(inflate2);
                    inflate2.setId(i);
                    View inflate3 = enableZenModeDialog.mLayoutInflater.inflate(2131624660, (ViewGroup) enableZenModeDialog.mZenRadioGroupContent, false);
                    inflate3.setId(i + 3);
                    enableZenModeDialog.mZenRadioGroupContent.addView(inflate3);
                }
                int childCount = enableZenModeDialog.mZenRadioGroupContent.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    enableZenModeDialog.mZenRadioGroupContent.getChildAt(i2).setVisibility(8);
                }
                enableZenModeDialog.mZenAlarmWarning.setVisibility(8);
                enableZenModeDialog.bindConditions(enableZenModeDialog.forever());
                positiveButton.setView(inflate);
                AlertDialog create = positiveButton.create();
                SystemUIDialog.applyFlags(create);
                SystemUIDialog.setShowForAllUsers(create);
                SystemUIDialog.registerDismissListener(create);
                create.create();
                create.getWindow().setLayout(SystemUIDialog.getDefaultDialogWidth(create), -2);
                if (view != null) {
                    dndTile.mDialogLaunchAnimator.showFromView(create, view, false);
                    return;
                } else {
                    create.show();
                    return;
                }
            case 1:
                AutoTileManager autoTileManager = (AutoTileManager) this.f$0;
                Objects.requireNonNull(autoTileManager);
                autoTileManager.changeUser((UserHandle) this.f$1);
                return;
            default:
                ImageButton imageButton = (ImageButton) this.f$1;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull((VolumeDialogImpl) this.f$0);
                if (imageButton != null) {
                    imageButton.setPressed(true);
                    imageButton.postOnAnimationDelayed(new LockIconViewController$$ExternalSyntheticLambda1(imageButton, 7), 200L);
                    return;
                }
                return;
        }
    }
}
