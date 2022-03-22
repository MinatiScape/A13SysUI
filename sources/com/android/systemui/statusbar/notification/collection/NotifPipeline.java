package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.battery.BatteryMeterViewController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2;
import com.android.systemui.util.Assert;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor$$ExternalSyntheticLambda0;
import java.util.Collection;
import java.util.Objects;
/* compiled from: NotifPipeline.kt */
/* loaded from: classes.dex */
public final class NotifPipeline implements CommonNotifCollection {
    public final boolean isNewPipelineEnabled;
    public final NotifCollection mNotifCollection;
    public final RenderStageManager mRenderStageManager;
    public final ShadeListBuilder mShadeListBuilder;

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        notifCollection.mNotifCollectionListeners.add(notifCollectionListener);
    }

    public final void addFinalizeFilter(NotifFilter notifFilter) {
        ShadeListBuilder shadeListBuilder = this.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mNotifFinalizeFilters.add(notifFilter);
        notifFilter.mListener = new StatusBar$$ExternalSyntheticLambda2(shadeListBuilder);
    }

    public final void addNotificationLifetimeExtender(NotifLifetimeExtender notifLifetimeExtender) {
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        notifCollection.checkForReentrantCall();
        if (!notifCollection.mLifetimeExtenders.contains(notifLifetimeExtender)) {
            notifCollection.mLifetimeExtenders.add(notifLifetimeExtender);
            notifLifetimeExtender.setCallback(new NotifCollection$$ExternalSyntheticLambda2(notifCollection));
            return;
        }
        throw new IllegalArgumentException("Extender " + notifLifetimeExtender + " already added.");
    }

    public final void addOnBeforeFinalizeFilterListener(OnBeforeFinalizeFilterListener onBeforeFinalizeFilterListener) {
        ShadeListBuilder shadeListBuilder = this.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mOnBeforeFinalizeFilterListeners.add(onBeforeFinalizeFilterListener);
    }

    public final void addOnBeforeRenderListListener(OnBeforeRenderListListener onBeforeRenderListListener) {
        ShadeListBuilder shadeListBuilder = this.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mOnBeforeRenderListListeners.add(onBeforeRenderListListener);
    }

    public final void addPreGroupFilter(NotifFilter notifFilter) {
        ShadeListBuilder shadeListBuilder = this.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mNotifPreGroupFilters.add(notifFilter);
        notifFilter.mListener = new BatteryMeterViewController$$ExternalSyntheticLambda0(shadeListBuilder);
    }

    public final void addPromoter(NotifPromoter notifPromoter) {
        ShadeListBuilder shadeListBuilder = this.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        shadeListBuilder.mNotifPromoters.add(notifPromoter);
        notifPromoter.mListener = new CHREGestureSensor$$ExternalSyntheticLambda0(shadeListBuilder);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final Collection<NotificationEntry> getAllNotifs() {
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        return notifCollection.mReadOnlyNotificationSet;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final NotificationEntry getEntry(String str) {
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        return (NotificationEntry) notifCollection.mNotificationSet.get(str);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public final void removeCollectionListener(NotifCollectionListener notifCollectionListener) {
        NotifCollection notifCollection = this.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        notifCollection.mNotifCollectionListeners.remove(notifCollectionListener);
    }

    public NotifPipeline(NotifPipelineFlags notifPipelineFlags, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, RenderStageManager renderStageManager) {
        this.mNotifCollection = notifCollection;
        this.mShadeListBuilder = shadeListBuilder;
        this.mRenderStageManager = renderStageManager;
        this.isNewPipelineEnabled = notifPipelineFlags.isNewPipelineEnabled();
    }
}
