package com.android.keyguard;

import android.view.View;
import com.android.systemui.statusbar.notification.row.PartialConversationInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardPatternViewController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardPatternViewController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                KeyguardPatternViewController keyguardPatternViewController = (KeyguardPatternViewController) this.f$0;
                Objects.requireNonNull(keyguardPatternViewController);
                keyguardPatternViewController.getKeyguardSecurityCallback().reset();
                keyguardPatternViewController.getKeyguardSecurityCallback().onCancelClicked();
                return;
            default:
                PartialConversationInfo partialConversationInfo = (PartialConversationInfo) this.f$0;
                int i = PartialConversationInfo.$r8$clinit;
                Objects.requireNonNull(partialConversationInfo);
                partialConversationInfo.mPressedApply = true;
                partialConversationInfo.mGutsContainer.closeControls(view, true);
                return;
        }
    }
}
