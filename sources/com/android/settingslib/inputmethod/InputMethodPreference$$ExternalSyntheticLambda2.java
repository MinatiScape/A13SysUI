package com.android.settingslib.inputmethod;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.face.Face;
import android.hardware.face.FaceManager;
import android.util.Log;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.google.android.systemui.face.FaceNotificationDialogFactory$$ExternalSyntheticLambda0;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InputMethodPreference$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ InputMethodPreference$$ExternalSyntheticLambda2(Object obj, int i) {
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
                final Context context = (Context) this.f$0;
                FaceManager faceManager = (FaceManager) context.getSystemService(FaceManager.class);
                if (faceManager == null) {
                    Log.e("FaceNotificationDialogF", "Not launching enrollment. Face manager was null!");
                    SystemUIDialog systemUIDialog = new SystemUIDialog(context);
                    systemUIDialog.setMessage(context.getText(2131952350));
                    systemUIDialog.setPositiveButton(2131952934, FaceNotificationDialogFactory$$ExternalSyntheticLambda0.INSTANCE);
                    systemUIDialog.show();
                    return;
                }
                faceManager.remove(new Face("", 0, 0L), context.getUserId(), new FaceManager.RemovalCallback() { // from class: com.google.android.systemui.face.FaceNotificationDialogFactory$1
                    public boolean mDidShowFailureDialog;

                    public final void onRemovalError(Face face, int i3, CharSequence charSequence) {
                        Log.e("FaceNotificationDialogF", "Not launching enrollment. Failed to remove existing face(s).");
                        if (!this.mDidShowFailureDialog) {
                            this.mDidShowFailureDialog = true;
                            Context context2 = context;
                            SystemUIDialog systemUIDialog2 = new SystemUIDialog(context2);
                            systemUIDialog2.setMessage(context2.getText(2131952350));
                            systemUIDialog2.setPositiveButton(2131952934, FaceNotificationDialogFactory$$ExternalSyntheticLambda0.INSTANCE);
                            systemUIDialog2.show();
                        }
                    }

                    public final void onRemovalSucceeded(Face face, int i3) {
                        if (!this.mDidShowFailureDialog && i3 == 0) {
                            Intent intent = new Intent("android.settings.BIOMETRIC_ENROLL");
                            intent.setPackage(ThemeOverlayApplier.SETTINGS_PACKAGE);
                            intent.setFlags(268435456);
                            context.startActivity(intent);
                        }
                    }
                });
                return;
        }
    }
}
