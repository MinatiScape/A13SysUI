package com.android.systemui.clipboardoverlay;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                Uri uri = (Uri) this.f$1;
                Objects.requireNonNull(clipboardOverlayController);
                String string = clipboardOverlayController.mContext.getString(2131952146);
                Intent intent = new Intent("android.intent.action.EDIT");
                if (!TextUtils.isEmpty(string)) {
                    intent.setComponent(ComponentName.unflattenFromString(string));
                }
                intent.setDataAndType(uri, "image/*");
                intent.addFlags(1);
                intent.addFlags(268468224);
                clipboardOverlayController.mContext.startActivity(intent);
                clipboardOverlayController.animateOut();
                return;
            default:
                ExpandableNotificationRow.m96$r8$lambda$r5fBwOTn3_BH7IpjuK_7B090Dc((ExpandableNotificationRow) this.f$0, (ExpandableNotificationRow.CoordinateOnClickListener) this.f$1, view);
                return;
        }
    }
}
