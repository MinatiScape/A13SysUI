package androidx.slice.view;

import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.CommandAction;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.FloatAction;
import android.service.controls.actions.ModeAction;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.Symbol;
/* loaded from: classes.dex */
public class R$id {
    public static final R$id INSTANCE = new R$id();
    public static final Symbol NO_DECISION = new Symbol("NO_DECISION");

    /* JADX WARN: Type inference failed for: r4v0, types: [android.app.AlertDialog, android.app.Dialog, com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1 createPinDialog(final com.android.systemui.controls.ui.ControlViewHolder r7, final boolean r8, boolean r9, final kotlin.jvm.functions.Function0 r10) {
        /*
            android.service.controls.actions.ControlAction r0 = r7.lastAction
            r1 = 0
            if (r0 != 0) goto L_0x000d
            java.lang.String r7 = "ControlsUiController"
            java.lang.String r8 = "PIN Dialog attempted but no last action is set. Will not show"
            android.util.Log.e(r7, r8)
            return r1
        L_0x000d:
            android.content.Context r2 = r7.context
            android.content.res.Resources r2 = r2.getResources()
            if (r9 == 0) goto L_0x0029
            kotlin.Pair r9 = new kotlin.Pair
            r3 = 2131952201(0x7f130249, float:1.9540838E38)
            java.lang.String r2 = r2.getString(r3)
            r3 = 2131952198(0x7f130246, float:1.9540832E38)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r9.<init>(r2, r3)
            goto L_0x0048
        L_0x0029:
            kotlin.Pair r9 = new kotlin.Pair
            r3 = 2131952200(0x7f130248, float:1.9540836E38)
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            android.widget.TextView r6 = r7.title
            java.lang.CharSequence r6 = r6.getText()
            r4[r5] = r6
            java.lang.String r2 = r2.getString(r3, r4)
            r3 = 2131952197(0x7f130245, float:1.954083E38)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r9.<init>(r2, r3)
        L_0x0048:
            java.lang.Object r2 = r9.component1()
            java.lang.String r2 = (java.lang.String) r2
            java.lang.Object r9 = r9.component2()
            java.lang.Number r9 = (java.lang.Number) r9
            int r9 = r9.intValue()
            android.content.Context r3 = r7.context
            com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1 r4 = new com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1
            r4.<init>(r3)
            r4.setTitle(r2)
            android.content.Context r2 = r4.getContext()
            android.view.LayoutInflater r2 = android.view.LayoutInflater.from(r2)
            r3 = 2131624047(0x7f0e006f, float:1.8875263E38)
            android.view.View r1 = r2.inflate(r3, r1)
            r4.setView(r1)
            r1 = -1
            android.content.Context r2 = r4.getContext()
            r3 = 17039370(0x104000a, float:2.42446E-38)
            java.lang.CharSequence r2 = r2.getText(r3)
            com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$1 r3 = new com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$1
            r3.<init>()
            r4.setButton(r1, r2, r3)
            r7 = -2
            android.content.Context r0 = r4.getContext()
            r1 = 17039360(0x1040000, float:2.424457E-38)
            java.lang.CharSequence r0 = r0.getText(r1)
            com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$2 r1 = new com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$2
            r1.<init>()
            r4.setButton(r7, r0, r1)
            android.view.Window r7 = r4.getWindow()
            r10 = 2020(0x7e4, float:2.83E-42)
            r7.setType(r10)
            r10 = 4
            r7.setSoftInputMode(r10)
            com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4 r7 = new com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4
            r7.<init>()
            r4.setOnShowListener(r7)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.view.R$id.createPinDialog(com.android.systemui.controls.ui.ControlViewHolder, boolean, boolean, kotlin.jvm.functions.Function0):com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1");
    }

    public static final ControlAction access$addChallengeValue(ControlAction controlAction, String str) {
        String templateId = controlAction.getTemplateId();
        if (controlAction instanceof BooleanAction) {
            return new BooleanAction(templateId, ((BooleanAction) controlAction).getNewState(), str);
        }
        if (controlAction instanceof FloatAction) {
            return new FloatAction(templateId, ((FloatAction) controlAction).getNewValue(), str);
        }
        if (controlAction instanceof CommandAction) {
            return new CommandAction(templateId, str);
        }
        if (controlAction instanceof ModeAction) {
            return new ModeAction(templateId, ((ModeAction) controlAction).getNewMode(), str);
        }
        throw new IllegalStateException(Intrinsics.stringPlus("'action' is not a known type: ", controlAction));
    }
}
