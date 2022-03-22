package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.statusbar.phone.KeyguardPreviewContainer;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PreviewInflater {
    public final ActivityIntentHelper mActivityIntentHelper;
    public Context mContext;

    /* loaded from: classes.dex */
    public static class WidgetInfo {
        public String contextPackage;
        public int layoutId;
    }

    public final KeyguardPreviewContainer inflatePreview(WidgetInfo widgetInfo) {
        View view;
        if (widgetInfo == null) {
            return null;
        }
        try {
            Context createPackageContext = this.mContext.createPackageContext(widgetInfo.contextPackage, 4);
            view = ((LayoutInflater) createPackageContext.getSystemService("layout_inflater")).cloneInContext(createPackageContext).inflate(widgetInfo.layoutId, (ViewGroup) null, false);
        } catch (PackageManager.NameNotFoundException | RuntimeException e) {
            Log.w("PreviewInflater", "Error creating widget view", e);
            view = null;
        }
        if (view == null) {
            return null;
        }
        KeyguardPreviewContainer keyguardPreviewContainer = new KeyguardPreviewContainer(this.mContext, null);
        keyguardPreviewContainer.addView(view);
        return keyguardPreviewContainer;
    }

    public PreviewInflater(Context context, ActivityIntentHelper activityIntentHelper) {
        this.mContext = context;
        this.mActivityIntentHelper = activityIntentHelper;
    }

    public final KeyguardPreviewContainer inflatePreview(Intent intent) {
        ActivityInfo activityInfo;
        int i;
        PackageManager packageManager = this.mContext.getPackageManager();
        List queryIntentActivitiesAsUser = packageManager.queryIntentActivitiesAsUser(intent, 851968, KeyguardUpdateMonitor.getCurrentUser());
        WidgetInfo widgetInfo = null;
        if (queryIntentActivitiesAsUser.size() != 0) {
            ResolveInfo resolveActivityAsUser = packageManager.resolveActivityAsUser(intent, 852096, KeyguardUpdateMonitor.getCurrentUser());
            Objects.requireNonNull(this.mActivityIntentHelper);
            if (!(ActivityIntentHelper.wouldLaunchResolverActivity(resolveActivityAsUser, queryIntentActivitiesAsUser) || resolveActivityAsUser == null || (activityInfo = resolveActivityAsUser.activityInfo) == null)) {
                String str = activityInfo.packageName;
                Bundle bundle = activityInfo.metaData;
                if (!(bundle == null || (i = bundle.getInt("com.android.keyguard.layout")) == 0)) {
                    WidgetInfo widgetInfo2 = new WidgetInfo();
                    widgetInfo2.contextPackage = str;
                    widgetInfo2.layoutId = i;
                    widgetInfo = widgetInfo2;
                }
            }
        }
        return inflatePreview(widgetInfo);
    }
}
