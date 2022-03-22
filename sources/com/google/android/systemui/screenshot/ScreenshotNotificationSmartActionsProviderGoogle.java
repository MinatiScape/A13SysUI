package com.google.android.systemui.screenshot;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$Feedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$QuickShareInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotActionFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOp;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpStatus;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.Utils;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ScreenshotNotificationSmartActionsProviderGoogle extends ScreenshotNotificationSmartActionsProvider {
    public static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType, SuggestParcelables$InteractionType> SCREENSHOT_INTERACTION_TYPE_MAP;
    public static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotOp, FeedbackParcelables$ScreenshotOp> SCREENSHOT_OP_MAP;
    public static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus, FeedbackParcelables$ScreenshotOpStatus> SCREENSHOT_OP_STATUS_MAP;
    public final ContentSuggestionsServiceClient mClient;

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder(4);
        builder.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.RETRIEVE_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.RETRIEVE_SMART_ACTIONS);
        builder.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.REQUEST_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.REQUEST_SMART_ACTIONS);
        builder.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.WAIT_FOR_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.WAIT_FOR_SMART_ACTIONS);
        SCREENSHOT_OP_MAP = builder.buildOrThrow();
        ImmutableMap.Builder builder2 = new ImmutableMap.Builder(4);
        builder2.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.SUCCESS, FeedbackParcelables$ScreenshotOpStatus.SUCCESS);
        builder2.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.ERROR, FeedbackParcelables$ScreenshotOpStatus.ERROR);
        builder2.put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.TIMEOUT, FeedbackParcelables$ScreenshotOpStatus.TIMEOUT);
        SCREENSHOT_OP_STATUS_MAP = builder2.buildOrThrow();
        ImmutableMap.Builder builder3 = new ImmutableMap.Builder(4);
        builder3.put(ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.REGULAR_SMART_ACTIONS, SuggestParcelables$InteractionType.SCREENSHOT_NOTIFICATION);
        builder3.put(ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.QUICK_SHARE_ACTION, SuggestParcelables$InteractionType.QUICK_SHARE);
        SCREENSHOT_INTERACTION_TYPE_MAP = builder3.buildOrThrow();
    }

    public void completeFuture(Bundle bundle, CompletableFuture<List<Notification.Action>> completableFuture) {
        if (bundle.containsKey("ScreenshotNotificationActions")) {
            completableFuture.complete(bundle.getParcelableArrayList("ScreenshotNotificationActions"));
        } else {
            completableFuture.complete(Collections.emptyList());
        }
    }

    /* JADX WARN: Type inference failed for: r15v0, types: [com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.concurrent.CompletableFuture<java.util.List<android.app.Notification.Action>> getActions(final java.lang.String r22, final android.net.Uri r23, android.graphics.Bitmap r24, android.content.ComponentName r25, com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType r26, final android.os.UserHandle r27) {
        /*
            r21 = this;
            java.util.concurrent.CompletableFuture r6 = new java.util.concurrent.CompletableFuture
            r6.<init>()
            android.graphics.Bitmap$Config r0 = r24.getConfig()
            android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.HARDWARE
            r7 = 0
            r8 = 1
            java.lang.String r2 = "ScreenshotActionsGoogle"
            if (r0 == r1) goto L_0x002b
            java.lang.Object[] r0 = new java.lang.Object[r8]
            android.graphics.Bitmap$Config r1 = r24.getConfig()
            r0[r7] = r1
            java.lang.String r1 = "Bitmap expected: Hardware, Bitmap found: %s. Returning empty list."
            java.lang.String r0 = java.lang.String.format(r1, r0)
            android.util.Log.e(r2, r0)
            java.util.List r0 = java.util.Collections.emptyList()
            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.completedFuture(r0)
            return r0
        L_0x002b:
            long r3 = android.os.SystemClock.uptimeMillis()
            java.lang.String r0 = "Calling AiAi to obtain screenshot notification smart actions."
            android.util.Log.d(r2, r0)
            r1 = r21
            com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient r10 = r1.mClient
            java.lang.String r13 = r25.getPackageName()
            java.lang.String r14 = r25.getClassName()
            com.google.common.collect.ImmutableMap<com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider$ScreenshotSmartActionType, com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType> r0 = com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle.SCREENSHOT_INTERACTION_TYPE_MAP
            com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType r2 = com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType.SCREENSHOT_NOTIFICATION
            r5 = r26
            java.lang.Object r0 = r0.getOrDefault(r5, r2)
            r9 = r0
            com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType r9 = (com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType) r9
            com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle$1 r15 = new com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle$1
            r0 = r15
            r2 = r6
            r5 = r22
            r0.<init>()
            java.util.Objects.requireNonNull(r10)
            boolean r0 = r10.isAiAiVersionSupported
            if (r0 != 0) goto L_0x0063
            android.os.Bundle r0 = android.os.Bundle.EMPTY
            r15.onResult(r0)
            goto L_0x00c5
        L_0x0063:
            java.util.Random r0 = com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient.random
            int r11 = r0.nextInt()
            long r0 = java.lang.System.currentTimeMillis()
            com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils r2 = r10.bundleUtils
            java.util.Objects.requireNonNull(r2)
            android.os.Bundle r12 = new android.os.Bundle
            r12.<init>()
            java.lang.String r2 = "CONTEXT_IMAGE_BUNDLE_VERSION_KEY"
            r12.putInt(r2, r8)
            java.lang.String r2 = "CONTEXT_IMAGE_PRIMARY_TASK_KEY"
            r12.putBoolean(r2, r8)
            java.lang.String r2 = "CONTEXT_IMAGE_PACKAGE_NAME_KEY"
            r12.putString(r2, r13)
            java.lang.String r2 = "CONTEXT_IMAGE_ACTIVITY_NAME_KEY"
            r12.putString(r2, r14)
            java.lang.String r2 = "CONTEXT_IMAGE_CAPTURE_TIME_MS_KEY"
            r12.putLong(r2, r0)
            java.lang.String r2 = "CONTEXT_IMAGE_BITMAP_URI_KEY"
            r3 = r23
            r12.putParcelable(r2, r3)
            java.lang.String r2 = "android.contentsuggestions.extra.BITMAP"
            r4 = r24
            r12.putParcelable(r2, r4)
            com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext r2 = new com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext
            r2.<init>()
            r2.interactionType = r9
            r2.disallowCopyPaste = r7
            r2.versionCode = r8
            r2.isPrimaryTask = r8
            com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapperImpl r4 = r10.serviceWrapper
            com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda0 r5 = new com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda0
            r9 = r5
            r7 = r15
            r15 = r0
            r17 = r2
            r18 = r27
            r19 = r23
            r20 = r7
            r9.<init>()
            java.util.Objects.requireNonNull(r4)
            java.util.concurrent.Executor r0 = r4.asyncExecutor
            r0.execute(r5)
        L_0x00c5:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle.getActions(java.lang.String, android.net.Uri, android.graphics.Bitmap, android.content.ComponentName, com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider$ScreenshotSmartActionType, android.os.UserHandle):java.util.concurrent.CompletableFuture");
    }

    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    public final void notifyAction(final String str, final String str2, final boolean z, final Intent intent) {
        final ContentSuggestionsServiceClient contentSuggestionsServiceClient = this.mClient;
        Objects.requireNonNull(contentSuggestionsServiceClient);
        contentSuggestionsServiceClient.serviceWrapper.notifyInteractionAsync(str, new Supplier() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                ContentSuggestionsServiceClient contentSuggestionsServiceClient2 = ContentSuggestionsServiceClient.this;
                String str3 = str;
                String str4 = str2;
                boolean z2 = z;
                Intent intent2 = intent;
                Objects.requireNonNull(contentSuggestionsServiceClient2);
                BundleUtils bundleUtils = contentSuggestionsServiceClient2.bundleUtils;
                ArrayList arrayList = new ArrayList();
                FeedbackParcelables$ScreenshotActionFeedback feedbackParcelables$ScreenshotActionFeedback = new FeedbackParcelables$ScreenshotActionFeedback();
                feedbackParcelables$ScreenshotActionFeedback.actionType = str4;
                feedbackParcelables$ScreenshotActionFeedback.isSmartActions = z2;
                if (!(!"QUICK_SHARE".equals(str4) || intent2 == null || intent2.getComponent() == null)) {
                    FeedbackParcelables$QuickShareInfo feedbackParcelables$QuickShareInfo = new FeedbackParcelables$QuickShareInfo();
                    Uri uri = (Uri) intent2.getParcelableExtra("android.intent.extra.STREAM");
                    if (uri != null) {
                        feedbackParcelables$QuickShareInfo.contentUri = uri.toString();
                    }
                    ComponentName component = intent2.getComponent();
                    int i = Utils.$r8$clinit;
                    Objects.requireNonNull(component);
                    feedbackParcelables$QuickShareInfo.targetPackageName = component.getPackageName();
                    ComponentName component2 = intent2.getComponent();
                    Objects.requireNonNull(component2);
                    feedbackParcelables$QuickShareInfo.targetClassName = component2.getClassName();
                    feedbackParcelables$QuickShareInfo.targetShortcutId = intent2.getStringExtra("android.intent.extra.shortcut.ID");
                    feedbackParcelables$ScreenshotActionFeedback.quickShareInfo = feedbackParcelables$QuickShareInfo;
                }
                FeedbackParcelables$ScreenshotFeedback feedbackParcelables$ScreenshotFeedback = new FeedbackParcelables$ScreenshotFeedback();
                feedbackParcelables$ScreenshotFeedback.screenshotId = str3;
                feedbackParcelables$ScreenshotFeedback.screenshotFeedback = feedbackParcelables$ScreenshotActionFeedback;
                FeedbackParcelables$Feedback feedbackParcelables$Feedback = new FeedbackParcelables$Feedback();
                arrayList.add(feedbackParcelables$Feedback);
                feedbackParcelables$Feedback.feedback = feedbackParcelables$ScreenshotFeedback;
                FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch = new FeedbackParcelables$FeedbackBatch();
                feedbackParcelables$FeedbackBatch.screenSessionId = 0;
                feedbackParcelables$FeedbackBatch.overviewSessionId = str3;
                int i2 = Utils.$r8$clinit;
                feedbackParcelables$FeedbackBatch.feedback = arrayList;
                Objects.requireNonNull(bundleUtils);
                return BundleUtils.createFeedbackRequest(feedbackParcelables$FeedbackBatch);
            }
        }, null, null);
    }

    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    public final void notifyOp(final String str, ScreenshotNotificationSmartActionsProvider.ScreenshotOp screenshotOp, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus screenshotOpStatus, final long j) {
        final ContentSuggestionsServiceClient contentSuggestionsServiceClient = this.mClient;
        final FeedbackParcelables$ScreenshotOp orDefault = SCREENSHOT_OP_MAP.getOrDefault(screenshotOp, FeedbackParcelables$ScreenshotOp.OP_UNKNOWN);
        final FeedbackParcelables$ScreenshotOpStatus orDefault2 = SCREENSHOT_OP_STATUS_MAP.getOrDefault(screenshotOpStatus, FeedbackParcelables$ScreenshotOpStatus.OP_STATUS_UNKNOWN);
        Objects.requireNonNull(contentSuggestionsServiceClient);
        contentSuggestionsServiceClient.serviceWrapper.notifyInteractionAsync(str, new Supplier() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                ContentSuggestionsServiceClient contentSuggestionsServiceClient2 = ContentSuggestionsServiceClient.this;
                String str2 = str;
                FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp = orDefault;
                FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus = orDefault2;
                long j2 = j;
                Objects.requireNonNull(contentSuggestionsServiceClient2);
                BundleUtils bundleUtils = contentSuggestionsServiceClient2.bundleUtils;
                ArrayList arrayList = new ArrayList();
                FeedbackParcelables$ScreenshotOpFeedback feedbackParcelables$ScreenshotOpFeedback = new FeedbackParcelables$ScreenshotOpFeedback();
                feedbackParcelables$ScreenshotOpFeedback.durationMs = j2;
                feedbackParcelables$ScreenshotOpFeedback.op = feedbackParcelables$ScreenshotOp;
                feedbackParcelables$ScreenshotOpFeedback.status = feedbackParcelables$ScreenshotOpStatus;
                FeedbackParcelables$ScreenshotFeedback feedbackParcelables$ScreenshotFeedback = new FeedbackParcelables$ScreenshotFeedback();
                feedbackParcelables$ScreenshotFeedback.screenshotId = str2;
                feedbackParcelables$ScreenshotFeedback.screenshotFeedback = feedbackParcelables$ScreenshotOpFeedback;
                FeedbackParcelables$Feedback feedbackParcelables$Feedback = new FeedbackParcelables$Feedback();
                arrayList.add(feedbackParcelables$Feedback);
                feedbackParcelables$Feedback.feedback = feedbackParcelables$ScreenshotFeedback;
                FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch = new FeedbackParcelables$FeedbackBatch();
                feedbackParcelables$FeedbackBatch.screenSessionId = 0;
                feedbackParcelables$FeedbackBatch.overviewSessionId = str2;
                int i = Utils.$r8$clinit;
                feedbackParcelables$FeedbackBatch.feedback = arrayList;
                Objects.requireNonNull(bundleUtils);
                return BundleUtils.createFeedbackRequest(feedbackParcelables$FeedbackBatch);
            }
        }, null, null);
    }

    public ScreenshotNotificationSmartActionsProviderGoogle(Context context, Executor executor, Handler handler) {
        this.mClient = new ContentSuggestionsServiceClient(context, executor, handler);
    }
}
