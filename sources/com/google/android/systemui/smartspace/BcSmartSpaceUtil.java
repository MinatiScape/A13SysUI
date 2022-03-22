package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BcSmartSpaceUtil {
    public static FalsingManager sFalsingManager;
    public static BcSmartspaceDataPlugin.IntentStarter sIntentStarter;

    public static void setOnClickListener(View view, final SmartspaceTarget smartspaceTarget, final SmartspaceAction smartspaceAction, final String str, final CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0, final BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        final boolean z;
        if (view == null || smartspaceAction == null) {
            Log.e(str, "No tap action can be set up");
            return;
        }
        final boolean z2 = false;
        if (smartspaceAction.getExtras() == null || !smartspaceAction.getExtras().getBoolean("show_on_lockscreen")) {
            z = false;
        } else {
            z = true;
        }
        if (smartspaceAction.getIntent() == null && smartspaceAction.getPendingIntent() == null) {
            z2 = true;
        }
        final BcSmartspaceDataPlugin.IntentStarter intentStarter = sIntentStarter;
        if (intentStarter == null) {
            intentStarter = new BcSmartspaceDataPlugin.IntentStarter() { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil.1
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                public final void startIntent(View view2, Intent intent, boolean z3) {
                    try {
                        view2.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException | NullPointerException | SecurityException e) {
                        Log.e(str, "Cannot invoke smartspace intent", e);
                    }
                }

                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                public final void startPendingIntent(PendingIntent pendingIntent, boolean z3) {
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        Log.e(str, "Cannot invoke canceled smartspace intent", e);
                    }
                }
            };
        }
        view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil$$ExternalSyntheticLambda0
            public final /* synthetic */ View.OnClickListener f$5 = null;

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo2 = BcSmartspaceCardLoggingInfo.this;
                boolean z3 = z2;
                BcSmartspaceDataPlugin.IntentStarter intentStarter2 = intentStarter;
                SmartspaceAction smartspaceAction2 = smartspaceAction;
                boolean z4 = z;
                View.OnClickListener onClickListener = this.f$5;
                BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier = cardPagerAdapter$$ExternalSyntheticLambda0;
                String str2 = str;
                SmartspaceTarget smartspaceTarget2 = smartspaceTarget;
                FalsingManager falsingManager = BcSmartSpaceUtil.sFalsingManager;
                if (falsingManager == null || !falsingManager.isFalseTap(1)) {
                    if (bcSmartspaceCardLoggingInfo2 != null) {
                        BcSmartspaceLogger.log(BcSmartspaceEvent.SMARTSPACE_CARD_CLICK, bcSmartspaceCardLoggingInfo2);
                    }
                    if (!z3) {
                        intentStarter2.startFromAction(smartspaceAction2, view2, z4);
                    }
                    if (onClickListener != null) {
                        onClickListener.onClick(view2);
                    }
                    if (smartspaceEventNotifier == null) {
                        Log.w(str2, "Cannot notify target interaction smartspace event: event notifier null.");
                    } else {
                        smartspaceEventNotifier.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(1).setSmartspaceTarget(smartspaceTarget2).setSmartspaceActionId(smartspaceAction2.getId()).build());
                    }
                }
            }
        });
    }

    public static Drawable getIconDrawable(Icon icon, Context context) {
        Drawable drawable;
        if (icon == null) {
            return null;
        }
        if (icon.getType() == 1 || icon.getType() == 5) {
            drawable = new BitmapDrawable(context.getResources(), icon.getBitmap());
        } else {
            drawable = icon.loadDrawable(context);
        }
        if (drawable != null) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131165711);
            drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        }
        return drawable;
    }

    public static int getLoggingDisplaySurface(String str, float f) {
        Objects.requireNonNull(str);
        if (str.equals("com.google.android.apps.nexuslauncher")) {
            return 1;
        }
        if (!str.equals(ThemeOverlayApplier.SYSUI_PACKAGE)) {
            return 0;
        }
        if (f == 1.0f) {
            return 3;
        }
        if (f == 0.0f) {
            return 2;
        }
        return -1;
    }
}
