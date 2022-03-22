package com.google.android.systemui.smartspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda21;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SmartSpaceBroadcastReceiver extends BroadcastReceiver {
    public final SmartSpaceController mController;

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard[] smartspaceCardArr;
        boolean z;
        boolean z2;
        if (SmartSpaceController.DEBUG) {
            Log.d("SmartSpaceReceiver", "receiving update");
        }
        int myUserId = UserHandle.myUserId();
        if (myUserId == 0) {
            if (!intent.hasExtra("uid")) {
                intent.putExtra("uid", myUserId);
            }
            byte[] byteArrayExtra = intent.getByteArrayExtra("com.google.android.apps.nexuslauncher.extra.SMARTSPACE_CARD");
            if (byteArrayExtra != null) {
                SmartspaceProto$SmartspaceUpdate smartspaceProto$SmartspaceUpdate = new SmartspaceProto$SmartspaceUpdate();
                try {
                    MessageNano.mergeFrom(smartspaceProto$SmartspaceUpdate, byteArrayExtra);
                    for (SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard : smartspaceProto$SmartspaceUpdate.card) {
                        int i = smartspaceCard.cardPriority;
                        if (i == 1) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (i == 2) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (!z && !z2) {
                            Log.w("SmartSpaceReceiver", "unrecognized card priority: " + smartspaceCard.cardPriority);
                        }
                        notify(smartspaceCard, context, intent, z);
                    }
                } catch (InvalidProtocolBufferNanoException e) {
                    Log.e("SmartSpaceReceiver", "proto", e);
                }
            } else {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("receiving update with no proto: ");
                m.append(intent.getExtras());
                Log.e("SmartSpaceReceiver", m.toString());
            }
        } else if (!intent.getBooleanExtra("rebroadcast", false)) {
            intent.putExtra("rebroadcast", true);
            intent.putExtra("uid", myUserId);
            context.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    public SmartSpaceBroadcastReceiver(SmartSpaceController smartSpaceController) {
        this.mController = smartSpaceController;
    }

    public final void notify(SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Context context, Intent intent, boolean z) {
        PackageInfo packageInfo;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.google.android.googlequicksearchbox", 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SmartSpaceReceiver", "Cannot find GSA", e);
            packageInfo = null;
        }
        NewCardInfo newCardInfo = new NewCardInfo(smartspaceCard, intent, z, currentTimeMillis, packageInfo);
        SmartSpaceController smartSpaceController = this.mController;
        Objects.requireNonNull(smartSpaceController);
        boolean z2 = SmartSpaceController.DEBUG;
        if (z2) {
            Log.d("SmartSpaceController", "onNewCard: " + newCardInfo);
        }
        if (intent.getIntExtra("uid", -1) == smartSpaceController.mCurrentUserId) {
            smartSpaceController.mBackgroundHandler.post(new BubbleStackView$$ExternalSyntheticLambda21(smartSpaceController, newCardInfo, 7));
        } else if (z2) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Ignore card that belongs to another user target: ");
            m.append(smartSpaceController.mCurrentUserId);
            m.append(" current: ");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, smartSpaceController.mCurrentUserId, "SmartSpaceController");
        }
    }
}
