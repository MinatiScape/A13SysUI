package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.text.TextUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.EntitiesData;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Action;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$ActionGroup;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Entity;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$IntentInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$IntentType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.LogUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.Utils;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ContentSuggestionsServiceClient {
    public static final Random random = new Random();
    public final BundleUtils bundleUtils;
    public final Context context;
    public final boolean isAiAiVersionSupported;
    public final ContentSuggestionsServiceWrapperImpl serviceWrapper;
    public final UserManager userManager;

    /* JADX WARN: Type inference failed for: r3v0, types: [com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda2] */
    public ContentSuggestionsServiceClient(Context context, Executor executor, final Handler uiHandler) {
        this.context = context;
        SuggestController suggestController = new SuggestController(context, context, new Executor() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                uiHandler.post(runnable);
            }
        }, executor, executor);
        ContentSuggestionsServiceWrapperImpl contentSuggestionsServiceWrapperImpl = suggestController.wrapper;
        boolean z = false;
        SuggestController$$ExternalSyntheticLambda1 suggestController$$ExternalSyntheticLambda1 = new SuggestController$$ExternalSyntheticLambda1(suggestController, 0);
        Objects.requireNonNull(contentSuggestionsServiceWrapperImpl);
        contentSuggestionsServiceWrapperImpl.asyncExecutor.execute(suggestController$$ExternalSyntheticLambda1);
        this.serviceWrapper = suggestController.wrapper;
        try {
            if (context.getPackageManager().getPackageInfo("com.google.android.as", 0).getLongVersionCode() >= 660780) {
                z = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("Error obtaining package info: ", e);
        }
        this.isAiAiVersionSupported = z;
        this.bundleUtils = new BundleUtils();
        this.userManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public static Notification.Action access$200(Context context, SuggestParcelables$Entity suggestParcelables$Entity, EntitiesData entitiesData, Uri uri) {
        String str;
        String str2;
        Objects.requireNonNull(suggestParcelables$Entity);
        ArrayList arrayList = suggestParcelables$Entity.actions;
        Icon icon = null;
        if (arrayList == null) {
            return null;
        }
        int i = Utils.$r8$clinit;
        if (arrayList.isEmpty()) {
            return null;
        }
        ArrayList arrayList2 = suggestParcelables$Entity.actions;
        Objects.requireNonNull(arrayList2);
        int i2 = 0;
        SuggestParcelables$ActionGroup suggestParcelables$ActionGroup = (SuggestParcelables$ActionGroup) arrayList2.get(0);
        Objects.requireNonNull(suggestParcelables$ActionGroup);
        SuggestParcelables$Action suggestParcelables$Action = suggestParcelables$ActionGroup.mainAction;
        if (suggestParcelables$Action == null || suggestParcelables$Action.id == null) {
            return null;
        }
        if (uri != null && suggestParcelables$Action.hasProxiedIntentInfo) {
            SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = suggestParcelables$Action.proxiedIntentInfo;
            Objects.requireNonNull(suggestParcelables$IntentInfo);
            if (suggestParcelables$IntentInfo.intentType == SuggestParcelables$IntentType.LENS) {
                context.grantUriPermission("com.google.android.googlequicksearchbox", uri, 1);
            }
        }
        String str3 = suggestParcelables$Action.id;
        Objects.requireNonNull(str3);
        Bitmap bitmap = entitiesData.bitmapMap.get(str3);
        String str4 = suggestParcelables$Action.id;
        Objects.requireNonNull(str4);
        PendingIntent pendingIntent = entitiesData.pendingIntentMap.get(str4);
        if (pendingIntent == null || bitmap == null) {
            return null;
        }
        String str5 = suggestParcelables$Action.displayName;
        Objects.requireNonNull(str5);
        String str6 = suggestParcelables$Action.fullDisplayName;
        Objects.requireNonNull(str6);
        String str7 = suggestParcelables$Entity.searchQueryHint;
        Objects.requireNonNull(str7);
        String[] strArr = {str5, str6, str7};
        while (true) {
            if (i2 >= 3) {
                str = null;
                break;
            }
            str = strArr[i2];
            if (!TextUtils.isEmpty(str)) {
                break;
            }
            i2++;
        }
        if (str == null) {
            return null;
        }
        RemoteAction remoteAction = new RemoteAction(Icon.createWithBitmap(bitmap), str, str, pendingIntent);
        if (TextUtils.isEmpty(suggestParcelables$Entity.searchQueryHint)) {
            str2 = "Smart Action";
        } else {
            str2 = suggestParcelables$Entity.searchQueryHint;
        }
        Objects.requireNonNull(str2);
        if (remoteAction.shouldShowIcon()) {
            icon = remoteAction.getIcon();
        }
        Bundle bundle = new Bundle();
        bundle.putString("action_type", str2);
        bundle.putFloat("action_score", 1.0f);
        return new Notification.Action.Builder(icon, remoteAction.getTitle(), remoteAction.getActionIntent()).setContextual(true).addExtras(bundle).build();
    }
}
