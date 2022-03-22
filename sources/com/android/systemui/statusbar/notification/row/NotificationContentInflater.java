package com.android.systemui.statusbar.notification.row;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ImageMessageConsumer;
import com.android.internal.widget.MessagingMessage;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda8;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.InflationTask;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda34;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder;
import com.android.systemui.statusbar.policy.SmartReplyStateInflater;
import com.android.systemui.util.Assert;
import com.android.systemui.util.Utils;
import com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda1;
import com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda2;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda4;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda5;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes.dex */
public class NotificationContentInflater implements NotificationRowContentBinder {
    public final Executor mBgExecutor;
    public final ConversationNotificationProcessor mConversationProcessor;
    public boolean mInflateSynchronously = false;
    public final boolean mIsMediaInQS;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final NotifRemoteViewCache mRemoteViewCache;
    public final SmartReplyStateInflater mSmartReplyStateInflater;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static abstract class ApplyCallback {
        public abstract RemoteViews getRemoteView();

        public abstract void setResultView(View view);
    }

    /* loaded from: classes.dex */
    public static class AsyncInflationTask extends AsyncTask<Void, Void, InflationProgress> implements NotificationRowContentBinder.InflationCallback, InflationTask {
        public final Executor mBgExecutor;
        public final NotificationRowContentBinder.InflationCallback mCallback;
        public CancellationSignal mCancellationSignal;
        public final Context mContext;
        public final ConversationNotificationProcessor mConversationProcessor;
        public final NotificationEntry mEntry;
        public Exception mError;
        public final boolean mInflateSynchronously;
        public final boolean mIsLowPriority;
        public final int mReInflateFlags;
        public final NotifRemoteViewCache mRemoteViewCache;
        public RemoteViews.InteractionHandler mRemoteViewClickHandler;
        public ExpandableNotificationRow mRow;
        public final SmartReplyStateInflater mSmartRepliesInflater;
        public final boolean mUsesIncreasedHeadsUpHeight;
        public final boolean mUsesIncreasedHeight;

        @Override // com.android.systemui.statusbar.InflationTask
        public final void abort() {
            cancel(true);
            CancellationSignal cancellationSignal = this.mCancellationSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
            }
        }

        @Override // android.os.AsyncTask
        public final /* bridge */ /* synthetic */ InflationProgress doInBackground(Void[] voidArr) {
            return doInBackground();
        }

        /* loaded from: classes.dex */
        public static class RtlEnabledContext extends ContextWrapper {
            @Override // android.content.ContextWrapper, android.content.Context
            public final ApplicationInfo getApplicationInfo() {
                ApplicationInfo applicationInfo = super.getApplicationInfo();
                applicationInfo.flags |= 4194304;
                return applicationInfo;
            }

            public RtlEnabledContext(Context context) {
                super(context);
            }
        }

        public final InflationProgress doInBackground() {
            try {
                NotificationEntry notificationEntry = this.mEntry;
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                try {
                    Notification.addFieldsFromContext(this.mContext.getPackageManager().getApplicationInfoAsUser(statusBarNotification.getPackageName(), 8192, UserHandle.getUserId(statusBarNotification.getUid())), statusBarNotification.getNotification());
                } catch (PackageManager.NameNotFoundException unused) {
                }
                Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(this.mContext, statusBarNotification.getNotification());
                Context packageContext = statusBarNotification.getPackageContext(this.mContext);
                if (recoverBuilder.usesTemplate()) {
                    packageContext = new RtlEnabledContext(packageContext);
                }
                NotificationEntry notificationEntry2 = this.mEntry;
                Objects.requireNonNull(notificationEntry2);
                if (notificationEntry2.mRanking.isConversation()) {
                    this.mConversationProcessor.processNotification(this.mEntry, recoverBuilder);
                }
                InflationProgress createRemoteViews = NotificationContentInflater.createRemoteViews(this.mReInflateFlags, recoverBuilder, this.mIsLowPriority, this.mUsesIncreasedHeight, this.mUsesIncreasedHeadsUpHeight, packageContext);
                ExpandableNotificationRow expandableNotificationRow = this.mRow;
                Objects.requireNonNull(expandableNotificationRow);
                NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
                Objects.requireNonNull(notificationContentView);
                NotificationContentInflater.inflateSmartReplyViews(createRemoteViews, this.mReInflateFlags, this.mEntry, this.mContext, packageContext, notificationContentView.mCurrentSmartReplyState, this.mSmartRepliesInflater);
                return createRemoteViews;
            } catch (Exception e) {
                this.mError = e;
                return null;
            }
        }

        public final void handleError(Exception exc) {
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            notificationEntry.mRunningTask = null;
            NotificationEntry notificationEntry2 = this.mEntry;
            Objects.requireNonNull(notificationEntry2);
            StatusBarNotification statusBarNotification = notificationEntry2.mSbn;
            Log.e("StatusBar", "couldn't inflate view for notification " + (statusBarNotification.getPackageName() + "/0x" + Integer.toHexString(statusBarNotification.getId())), exc);
            NotificationRowContentBinder.InflationCallback inflationCallback = this.mCallback;
            if (inflationCallback != null) {
                ExpandableNotificationRow expandableNotificationRow = this.mRow;
                Objects.requireNonNull(expandableNotificationRow);
                inflationCallback.handleInflationException(expandableNotificationRow.mEntry, new InflationException("Couldn't inflate contentViews" + exc));
            }
        }

        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public final void onAsyncInflationFinished(NotificationEntry notificationEntry) {
            boolean z;
            NotificationEntry notificationEntry2 = this.mEntry;
            Objects.requireNonNull(notificationEntry2);
            notificationEntry2.mRunningTask = null;
            this.mRow.onNotificationUpdated();
            NotificationRowContentBinder.InflationCallback inflationCallback = this.mCallback;
            if (inflationCallback != null) {
                inflationCallback.onAsyncInflationFinished(this.mEntry);
            }
            ExpandableNotificationRow expandableNotificationRow = this.mRow;
            Objects.requireNonNull(expandableNotificationRow);
            NotificationInlineImageResolver notificationInlineImageResolver = expandableNotificationRow.mImageResolver;
            Objects.requireNonNull(notificationInlineImageResolver);
            if (notificationInlineImageResolver.mImageCache == null || ActivityManager.isLowRamDeviceStatic()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                NotificationInlineImageCache notificationInlineImageCache = (NotificationInlineImageCache) notificationInlineImageResolver.mImageCache;
                Objects.requireNonNull(notificationInlineImageCache);
                NotificationInlineImageResolver notificationInlineImageResolver2 = notificationInlineImageCache.mResolver;
                Objects.requireNonNull(notificationInlineImageResolver2);
                final HashSet hashSet = notificationInlineImageResolver2.mWantedUriSet;
                notificationInlineImageCache.mCache.entrySet().removeIf(new Predicate() { // from class: com.android.systemui.statusbar.notification.row.NotificationInlineImageCache$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return !hashSet.contains(((Map.Entry) obj).getKey());
                    }
                });
            }
        }

        public final void onPostExecute(InflationProgress inflationProgress) {
            Exception exc = this.mError;
            if (exc == null) {
                this.mCancellationSignal = NotificationContentInflater.apply(this.mBgExecutor, this.mInflateSynchronously, inflationProgress, this.mReInflateFlags, this.mRemoteViewCache, this.mEntry, this.mRow, this.mRemoteViewClickHandler, this);
            } else {
                handleError(exc);
            }
        }

        public AsyncInflationTask(Executor executor, boolean z, int i, NotifRemoteViewCache notifRemoteViewCache, NotificationEntry notificationEntry, ConversationNotificationProcessor conversationNotificationProcessor, ExpandableNotificationRow expandableNotificationRow, boolean z2, boolean z3, boolean z4, RowContentBindStage.AnonymousClass1 r11, NotificationRemoteInputManager.AnonymousClass1 r12, SmartReplyStateInflater smartReplyStateInflater) {
            this.mEntry = notificationEntry;
            this.mRow = expandableNotificationRow;
            this.mBgExecutor = executor;
            this.mInflateSynchronously = z;
            this.mReInflateFlags = i;
            this.mRemoteViewCache = notifRemoteViewCache;
            this.mSmartRepliesInflater = smartReplyStateInflater;
            this.mContext = expandableNotificationRow.getContext();
            this.mIsLowPriority = z2;
            this.mUsesIncreasedHeight = z3;
            this.mUsesIncreasedHeadsUpHeight = z4;
            this.mRemoteViewClickHandler = r12;
            this.mCallback = r11;
            this.mConversationProcessor = conversationNotificationProcessor;
            notificationEntry.abortTask();
            notificationEntry.mRunningTask = this;
        }

        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public final void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
            handleError(exc);
        }

        @VisibleForTesting
        public int getReInflateFlags() {
            return this.mReInflateFlags;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class InflationProgress {
        public InflatedSmartReplyViewHolder expandedInflatedSmartReplies;
        public InflatedSmartReplyViewHolder headsUpInflatedSmartReplies;
        public CharSequence headsUpStatusBarText;
        public CharSequence headsUpStatusBarTextPublic;
        public View inflatedContentView;
        public View inflatedExpandedView;
        public View inflatedHeadsUpView;
        public View inflatedPublicView;
        public InflatedSmartReplyState inflatedSmartReplyState;
        public RemoteViews newContentView;
        public RemoteViews newExpandedView;
        public RemoteViews newHeadsUpView;
        public RemoteViews newPublicView;
        @VisibleForTesting
        public Context packageContext;
    }

    @VisibleForTesting
    public static boolean canReapplyRemoteView(RemoteViews remoteViews, RemoteViews remoteViews2) {
        if (remoteViews == null && remoteViews2 == null) {
            return true;
        }
        return (remoteViews == null || remoteViews2 == null || remoteViews2.getPackage() == null || remoteViews.getPackage() == null || !remoteViews.getPackage().equals(remoteViews2.getPackage()) || remoteViews.getLayoutId() != remoteViews2.getLayoutId() || remoteViews2.hasFlags(1)) ? false : true;
    }

    public static InflationProgress inflateSmartReplyViews(InflationProgress inflationProgress, int i, NotificationEntry notificationEntry, Context context, Context context2, InflatedSmartReplyState inflatedSmartReplyState, SmartReplyStateInflater smartReplyStateInflater) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if ((i & 1) == 0 || inflationProgress.newContentView == null) {
            z = false;
        } else {
            z = true;
        }
        if ((i & 2) == 0 || inflationProgress.newExpandedView == null) {
            z2 = false;
        } else {
            z2 = true;
        }
        if ((i & 4) == 0 || inflationProgress.newHeadsUpView == null) {
            z3 = false;
        }
        if (z || z2 || z3) {
            inflationProgress.inflatedSmartReplyState = smartReplyStateInflater.inflateSmartReplyState(notificationEntry);
        }
        if (z2) {
            inflationProgress.expandedInflatedSmartReplies = smartReplyStateInflater.inflateSmartReplyViewHolder(context, context2, notificationEntry, inflatedSmartReplyState, inflationProgress.inflatedSmartReplyState);
        }
        if (z3) {
            inflationProgress.headsUpInflatedSmartReplies = smartReplyStateInflater.inflateSmartReplyViewHolder(context, context2, notificationEntry, inflatedSmartReplyState, inflationProgress.inflatedSmartReplyState);
        }
        return inflationProgress;
    }

    @VisibleForTesting
    public InflationProgress inflateNotificationViews(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, NotificationRowContentBinder.BindParams bindParams, boolean z, int i, Notification.Builder builder, Context context, SmartReplyStateInflater smartReplyStateInflater) {
        InflationProgress createRemoteViews = createRemoteViews(i, builder, bindParams.isLowPriority, bindParams.usesIncreasedHeight, bindParams.usesIncreasedHeadsUpHeight, context);
        Context context2 = expandableNotificationRow.getContext();
        NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        inflateSmartReplyViews(createRemoteViews, i, notificationEntry, context2, context, notificationContentView.mCurrentSmartReplyState, smartReplyStateInflater);
        Executor executor = this.mBgExecutor;
        NotifRemoteViewCache notifRemoteViewCache = this.mRemoteViewCache;
        NotificationRemoteInputManager notificationRemoteInputManager = this.mRemoteInputManager;
        Objects.requireNonNull(notificationRemoteInputManager);
        apply(executor, z, createRemoteViews, i, notifRemoteViewCache, notificationEntry, expandableNotificationRow, notificationRemoteInputManager.mInteractionHandler, null);
        return createRemoteViews;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder
    public final void unbindContent(final NotificationEntry notificationEntry, final ExpandableNotificationRow expandableNotificationRow, int i) {
        int i2 = 1;
        while (i != 0) {
            if ((i & i2) != 0) {
                if (i2 == 1) {
                    Objects.requireNonNull(expandableNotificationRow);
                    expandableNotificationRow.mPrivateLayout.performWhenContentInactive(0, new AsyncSensorManager$$ExternalSyntheticLambda1(this, expandableNotificationRow, notificationEntry, 1));
                } else if (i2 == 2) {
                    Objects.requireNonNull(expandableNotificationRow);
                    expandableNotificationRow.mPrivateLayout.performWhenContentInactive(1, new AsyncSensorManager$$ExternalSyntheticLambda2(this, expandableNotificationRow, notificationEntry, 1));
                } else if (i2 == 4) {
                    Objects.requireNonNull(expandableNotificationRow);
                    expandableNotificationRow.mPrivateLayout.performWhenContentInactive(2, new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationContentInflater notificationContentInflater = NotificationContentInflater.this;
                            ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                            NotificationEntry notificationEntry2 = notificationEntry;
                            Objects.requireNonNull(notificationContentInflater);
                            Objects.requireNonNull(expandableNotificationRow2);
                            expandableNotificationRow2.mPrivateLayout.setHeadsUpChild(null);
                            notificationContentInflater.mRemoteViewCache.removeCachedView(notificationEntry2, 4);
                            NotificationContentView notificationContentView = expandableNotificationRow2.mPrivateLayout;
                            Objects.requireNonNull(notificationContentView);
                            notificationContentView.mHeadsUpInflatedSmartReplies = null;
                            notificationContentView.mHeadsUpSmartReplyView = null;
                        }
                    });
                } else if (i2 == 8) {
                    Objects.requireNonNull(expandableNotificationRow);
                    expandableNotificationRow.mPublicLayout.performWhenContentInactive(0, new PipTaskOrganizer$$ExternalSyntheticLambda5(this, expandableNotificationRow, notificationEntry, 4));
                }
            }
            i &= ~i2;
            i2 <<= 1;
        }
    }

    public static CancellationSignal apply(Executor executor, boolean z, final InflationProgress inflationProgress, int i, NotifRemoteViewCache notifRemoteViewCache, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, RemoteViews.InteractionHandler interactionHandler, NotificationRowContentBinder.InflationCallback inflationCallback) {
        NotificationContentView notificationContentView;
        NotificationContentView notificationContentView2;
        final HashMap hashMap;
        RemoteViews remoteViews;
        RemoteViews remoteViews2;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationContentView notificationContentView3 = expandableNotificationRow.mPrivateLayout;
        NotificationContentView notificationContentView4 = expandableNotificationRow.mPublicLayout;
        HashMap hashMap2 = new HashMap();
        if ((i & 1) != 0) {
            ApplyCallback applyCallback = new ApplyCallback() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater.1
                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final RemoteViews getRemoteView() {
                    return InflationProgress.this.newContentView;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final void setResultView(View view) {
                    InflationProgress.this.inflatedContentView = view;
                }
            };
            Objects.requireNonNull(notificationContentView3);
            hashMap = hashMap2;
            notificationContentView2 = notificationContentView4;
            notificationContentView = notificationContentView3;
            applyRemoteView(executor, z, inflationProgress, i, 1, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newContentView, notifRemoteViewCache.getCachedView(notificationEntry, 1)), interactionHandler, inflationCallback, notificationContentView3, notificationContentView3.mContractedChild, notificationContentView3.getVisibleWrapper(0), hashMap, applyCallback);
        } else {
            hashMap = hashMap2;
            notificationContentView2 = notificationContentView4;
            notificationContentView = notificationContentView3;
        }
        if ((i & 2) != 0 && (remoteViews2 = inflationProgress.newExpandedView) != null) {
            ApplyCallback applyCallback2 = new ApplyCallback() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater.2
                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final RemoteViews getRemoteView() {
                    return InflationProgress.this.newExpandedView;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final void setResultView(View view) {
                    InflationProgress.this.inflatedExpandedView = view;
                }
            };
            Objects.requireNonNull(notificationContentView);
            View view = notificationContentView.mExpandedChild;
            NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(1);
            notificationContentView = notificationContentView;
            applyRemoteView(executor, z, inflationProgress, i, 2, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(remoteViews2, notifRemoteViewCache.getCachedView(notificationEntry, 2)), interactionHandler, inflationCallback, notificationContentView, view, visibleWrapper, hashMap, applyCallback2);
        }
        if (!((i & 4) == 0 || (remoteViews = inflationProgress.newHeadsUpView) == null)) {
            ApplyCallback applyCallback3 = new ApplyCallback() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater.3
                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final RemoteViews getRemoteView() {
                    return InflationProgress.this.newHeadsUpView;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final void setResultView(View view2) {
                    InflationProgress.this.inflatedHeadsUpView = view2;
                }
            };
            Objects.requireNonNull(notificationContentView);
            applyRemoteView(executor, z, inflationProgress, i, 4, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(remoteViews, notifRemoteViewCache.getCachedView(notificationEntry, 4)), interactionHandler, inflationCallback, notificationContentView, notificationContentView.mHeadsUpChild, notificationContentView.getVisibleWrapper(2), hashMap, applyCallback3);
        }
        if ((i & 8) != 0) {
            ApplyCallback applyCallback4 = new ApplyCallback() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater.4
                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final RemoteViews getRemoteView() {
                    return InflationProgress.this.newPublicView;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationContentInflater.ApplyCallback
                public final void setResultView(View view2) {
                    InflationProgress.this.inflatedPublicView = view2;
                }
            };
            Objects.requireNonNull(notificationContentView2);
            applyRemoteView(executor, z, inflationProgress, i, 8, notifRemoteViewCache, notificationEntry, expandableNotificationRow, !canReapplyRemoteView(inflationProgress.newPublicView, notifRemoteViewCache.getCachedView(notificationEntry, 8)), interactionHandler, inflationCallback, notificationContentView2, notificationContentView2.mContractedChild, notificationContentView2.getVisibleWrapper(0), hashMap, applyCallback4);
        }
        finishIfDone(inflationProgress, i, notifRemoteViewCache, hashMap, inflationCallback, notificationEntry, expandableNotificationRow);
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater$$ExternalSyntheticLambda0
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                hashMap.values().forEach(ShellInitImpl$$ExternalSyntheticLambda4.INSTANCE$1);
            }
        });
        return cancellationSignal;
    }

    @VisibleForTesting
    public static void applyRemoteView(Executor executor, boolean z, final InflationProgress inflationProgress, final int i, final int i2, final NotifRemoteViewCache notifRemoteViewCache, final NotificationEntry notificationEntry, final ExpandableNotificationRow expandableNotificationRow, final boolean z2, final RemoteViews.InteractionHandler interactionHandler, final NotificationRowContentBinder.InflationCallback inflationCallback, final NotificationContentView notificationContentView, final View view, final NotificationViewWrapper notificationViewWrapper, final HashMap<Integer, CancellationSignal> hashMap, final ApplyCallback applyCallback) {
        CancellationSignal cancellationSignal;
        final RemoteViews remoteView = applyCallback.getRemoteView();
        if (z) {
            try {
                if (z2) {
                    View apply = remoteView.apply(inflationProgress.packageContext, notificationContentView, interactionHandler);
                    apply.setIsRootNamespace(true);
                    applyCallback.setResultView(apply);
                } else {
                    remoteView.reapply(inflationProgress.packageContext, view, interactionHandler);
                    notificationViewWrapper.onReinflated();
                }
            } catch (Exception e) {
                Objects.requireNonNull(expandableNotificationRow);
                NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
                Assert.isMainThread();
                hashMap.values().forEach(DreamOverlayStateController$$ExternalSyntheticLambda8.INSTANCE$1);
                if (inflationCallback != null) {
                    inflationCallback.handleInflationException(notificationEntry2, e);
                }
                hashMap.put(Integer.valueOf(i2), new CancellationSignal());
            }
        } else {
            RemoteViews.OnViewAppliedListener onViewAppliedListener = new RemoteViews.OnViewAppliedListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentInflater.5
                public final void onError(Exception exc) {
                    try {
                        View view2 = view;
                        if (z2) {
                            view2 = remoteView.apply(inflationProgress.packageContext, notificationContentView, interactionHandler);
                        } else {
                            remoteView.reapply(inflationProgress.packageContext, view2, interactionHandler);
                        }
                        Log.wtf("NotifContentInflater", "Async Inflation failed but normal inflation finished normally.", exc);
                        onViewApplied(view2);
                    } catch (Exception unused) {
                        hashMap.remove(Integer.valueOf(i2));
                        HashMap hashMap2 = hashMap;
                        ExpandableNotificationRow expandableNotificationRow2 = ExpandableNotificationRow.this;
                        Objects.requireNonNull(expandableNotificationRow2);
                        NotificationEntry notificationEntry3 = expandableNotificationRow2.mEntry;
                        NotificationRowContentBinder.InflationCallback inflationCallback2 = inflationCallback;
                        Assert.isMainThread();
                        hashMap2.values().forEach(DreamOverlayStateController$$ExternalSyntheticLambda8.INSTANCE$1);
                        if (inflationCallback2 != null) {
                            inflationCallback2.handleInflationException(notificationEntry3, exc);
                        }
                    }
                }

                public final void onViewApplied(View view2) {
                    if (z2) {
                        view2.setIsRootNamespace(true);
                        applyCallback.setResultView(view2);
                    } else {
                        NotificationViewWrapper notificationViewWrapper2 = notificationViewWrapper;
                        if (notificationViewWrapper2 != null) {
                            notificationViewWrapper2.onReinflated();
                        }
                    }
                    hashMap.remove(Integer.valueOf(i2));
                    NotificationContentInflater.finishIfDone(inflationProgress, i, notifRemoteViewCache, hashMap, inflationCallback, notificationEntry, ExpandableNotificationRow.this);
                }

                public final void onViewInflated(View view2) {
                    if (view2 instanceof ImageMessageConsumer) {
                        ExpandableNotificationRow expandableNotificationRow2 = ExpandableNotificationRow.this;
                        Objects.requireNonNull(expandableNotificationRow2);
                        ((ImageMessageConsumer) view2).setImageResolver(expandableNotificationRow2.mImageResolver);
                    }
                }
            };
            if (z2) {
                cancellationSignal = remoteView.applyAsync(inflationProgress.packageContext, notificationContentView, executor, onViewAppliedListener, interactionHandler);
            } else {
                cancellationSignal = remoteView.reapplyAsync(inflationProgress.packageContext, view, executor, onViewAppliedListener, interactionHandler);
            }
            hashMap.put(Integer.valueOf(i2), cancellationSignal);
        }
    }

    public static InflationProgress createRemoteViews(int i, Notification.Builder builder, boolean z, boolean z2, boolean z3, Context context) {
        RemoteViews remoteViews;
        InflationProgress inflationProgress = new InflationProgress();
        if ((i & 1) != 0) {
            if (z) {
                remoteViews = builder.makeLowPriorityContentView(false);
            } else {
                remoteViews = builder.createContentView(z2);
            }
            inflationProgress.newContentView = remoteViews;
        }
        if ((i & 2) != 0) {
            RemoteViews createBigContentView = builder.createBigContentView();
            if (createBigContentView == null) {
                if (z) {
                    createBigContentView = builder.createContentView();
                    Notification.Builder.makeHeaderExpanded(createBigContentView);
                } else {
                    createBigContentView = null;
                }
            }
            inflationProgress.newExpandedView = createBigContentView;
        }
        if ((i & 4) != 0) {
            inflationProgress.newHeadsUpView = builder.createHeadsUpContentView(z3);
        }
        if ((i & 8) != 0) {
            inflationProgress.newPublicView = builder.makePublicContentView(z);
        }
        inflationProgress.packageContext = context;
        inflationProgress.headsUpStatusBarText = builder.getHeadsUpStatusBarText(false);
        inflationProgress.headsUpStatusBarTextPublic = builder.getHeadsUpStatusBarText(true);
        return inflationProgress;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder
    public final void bindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i, NotificationRowContentBinder.BindParams bindParams, boolean z, RowContentBindStage.AnonymousClass1 r23) {
        boolean z2;
        List<Notification.MessagingStyle.Message> list;
        Objects.requireNonNull(expandableNotificationRow);
        if (!expandableNotificationRow.mRemoved) {
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            NotificationInlineImageResolver notificationInlineImageResolver = expandableNotificationRow.mImageResolver;
            Notification notification = statusBarNotification.getNotification();
            Objects.requireNonNull(notificationInlineImageResolver);
            if (notificationInlineImageResolver.mImageCache == null || ActivityManager.isLowRamDeviceStatic()) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                HashSet hashSet = new HashSet();
                Bundle bundle = notification.extras;
                if (bundle != null) {
                    Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
                    List<Notification.MessagingStyle.Message> list2 = null;
                    if (parcelableArray == null) {
                        list = null;
                    } else {
                        list = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray);
                    }
                    if (list != null) {
                        for (Notification.MessagingStyle.Message message : list) {
                            if (MessagingMessage.hasImage(message)) {
                                hashSet.add(message.getDataUri());
                            }
                        }
                    }
                    Parcelable[] parcelableArray2 = bundle.getParcelableArray("android.messages.historic");
                    if (parcelableArray2 != null) {
                        list2 = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray2);
                    }
                    if (list2 != null) {
                        for (Notification.MessagingStyle.Message message2 : list2) {
                            if (MessagingMessage.hasImage(message2)) {
                                hashSet.add(message2.getDataUri());
                            }
                        }
                    }
                    notificationInlineImageResolver.mWantedUriSet = hashSet;
                }
                notificationInlineImageResolver.mWantedUriSet.forEach(new StatusBar$$ExternalSyntheticLambda34(notificationInlineImageResolver, 1));
            }
            if (z) {
                this.mRemoteViewCache.clearCache(notificationEntry);
            }
            if ((i & 1) != 0) {
                expandableNotificationRow.mPrivateLayout.removeContentInactiveRunnable(0);
            }
            if ((i & 2) != 0) {
                expandableNotificationRow.mPrivateLayout.removeContentInactiveRunnable(1);
            }
            if ((i & 4) != 0) {
                expandableNotificationRow.mPrivateLayout.removeContentInactiveRunnable(2);
            }
            if ((i & 8) != 0) {
                expandableNotificationRow.mPublicLayout.removeContentInactiveRunnable(0);
            }
            Executor executor = this.mBgExecutor;
            boolean z3 = this.mInflateSynchronously;
            NotifRemoteViewCache notifRemoteViewCache = this.mRemoteViewCache;
            ConversationNotificationProcessor conversationNotificationProcessor = this.mConversationProcessor;
            boolean z4 = bindParams.isLowPriority;
            boolean z5 = bindParams.usesIncreasedHeight;
            boolean z6 = bindParams.usesIncreasedHeadsUpHeight;
            NotificationRemoteInputManager notificationRemoteInputManager = this.mRemoteInputManager;
            Objects.requireNonNull(notificationRemoteInputManager);
            AsyncInflationTask asyncInflationTask = new AsyncInflationTask(executor, z3, i, notifRemoteViewCache, notificationEntry, conversationNotificationProcessor, expandableNotificationRow, z4, z5, z6, r23, notificationRemoteInputManager.mInteractionHandler, this.mSmartReplyStateInflater);
            if (this.mInflateSynchronously) {
                asyncInflationTask.onPostExecute(asyncInflationTask.doInBackground());
            } else {
                asyncInflationTask.executeOnExecutor(this.mBgExecutor, new Void[0]);
            }
        }
    }

    public NotificationContentInflater(NotifRemoteViewCache notifRemoteViewCache, NotificationRemoteInputManager notificationRemoteInputManager, ConversationNotificationProcessor conversationNotificationProcessor, MediaFeatureFlag mediaFeatureFlag, Executor executor, SmartReplyStateInflater smartReplyStateInflater) {
        this.mRemoteViewCache = notifRemoteViewCache;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mConversationProcessor = conversationNotificationProcessor;
        Objects.requireNonNull(mediaFeatureFlag);
        this.mIsMediaInQS = Utils.useQsMediaPlayer(mediaFeatureFlag.context);
        this.mBgExecutor = executor;
        this.mSmartReplyStateInflater = smartReplyStateInflater;
    }

    public static boolean finishIfDone(InflationProgress inflationProgress, int i, NotifRemoteViewCache notifRemoteViewCache, HashMap<Integer, CancellationSignal> hashMap, NotificationRowContentBinder.InflationCallback inflationCallback, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        boolean z;
        Assert.isMainThread();
        Objects.requireNonNull(expandableNotificationRow);
        NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
        NotificationContentView notificationContentView2 = expandableNotificationRow.mPublicLayout;
        if (!hashMap.isEmpty()) {
            return false;
        }
        if ((i & 1) != 0) {
            View view = inflationProgress.inflatedContentView;
            if (view != null) {
                notificationContentView.setContractedChild(view);
                notifRemoteViewCache.putCachedView(notificationEntry, 1, inflationProgress.newContentView);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 1)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 1, inflationProgress.newContentView);
            }
        }
        if ((i & 2) != 0) {
            View view2 = inflationProgress.inflatedExpandedView;
            if (view2 != null) {
                notificationContentView.setExpandedChild(view2);
                notifRemoteViewCache.putCachedView(notificationEntry, 2, inflationProgress.newExpandedView);
            } else if (inflationProgress.newExpandedView == null) {
                notificationContentView.setExpandedChild(null);
                notifRemoteViewCache.removeCachedView(notificationEntry, 2);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 2)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 2, inflationProgress.newExpandedView);
            }
            if (inflationProgress.newExpandedView != null) {
                InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder = inflationProgress.expandedInflatedSmartReplies;
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mExpandedInflatedSmartReplies = inflatedSmartReplyViewHolder;
                if (inflatedSmartReplyViewHolder == null) {
                    notificationContentView.mExpandedSmartReplyView = null;
                }
            } else {
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mExpandedInflatedSmartReplies = null;
                notificationContentView.mExpandedSmartReplyView = null;
            }
            if (inflationProgress.newExpandedView != null) {
                z = true;
            } else {
                z = false;
            }
            expandableNotificationRow.mExpandable = z;
            NotificationContentView notificationContentView3 = expandableNotificationRow.mPrivateLayout;
            boolean isExpandable = expandableNotificationRow.isExpandable();
            Objects.requireNonNull(notificationContentView3);
            notificationContentView3.updateExpandButtonsDuringLayout(isExpandable, false);
        }
        if ((i & 4) != 0) {
            View view3 = inflationProgress.inflatedHeadsUpView;
            if (view3 != null) {
                notificationContentView.setHeadsUpChild(view3);
                notifRemoteViewCache.putCachedView(notificationEntry, 4, inflationProgress.newHeadsUpView);
            } else if (inflationProgress.newHeadsUpView == null) {
                notificationContentView.setHeadsUpChild(null);
                notifRemoteViewCache.removeCachedView(notificationEntry, 4);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 4)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 4, inflationProgress.newHeadsUpView);
            }
            if (inflationProgress.newHeadsUpView != null) {
                InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder2 = inflationProgress.headsUpInflatedSmartReplies;
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mHeadsUpInflatedSmartReplies = inflatedSmartReplyViewHolder2;
                if (inflatedSmartReplyViewHolder2 == null) {
                    notificationContentView.mHeadsUpSmartReplyView = null;
                }
            } else {
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mHeadsUpInflatedSmartReplies = null;
                notificationContentView.mHeadsUpSmartReplyView = null;
            }
        }
        InflatedSmartReplyState inflatedSmartReplyState = inflationProgress.inflatedSmartReplyState;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mCurrentSmartReplyState = inflatedSmartReplyState;
        if ((i & 8) != 0) {
            View view4 = inflationProgress.inflatedPublicView;
            if (view4 != null) {
                notificationContentView2.setContractedChild(view4);
                notifRemoteViewCache.putCachedView(notificationEntry, 8, inflationProgress.newPublicView);
            } else if (notifRemoteViewCache.hasCachedView(notificationEntry, 8)) {
                notifRemoteViewCache.putCachedView(notificationEntry, 8, inflationProgress.newPublicView);
            }
        }
        notificationEntry.headsUpStatusBarText = inflationProgress.headsUpStatusBarText;
        notificationEntry.headsUpStatusBarTextPublic = inflationProgress.headsUpStatusBarTextPublic;
        if (inflationCallback != null) {
            inflationCallback.onAsyncInflationFinished(notificationEntry);
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder
    public final void cancelBind(NotificationEntry notificationEntry) {
        notificationEntry.abortTask();
    }

    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }
}
