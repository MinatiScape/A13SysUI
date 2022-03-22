package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.collections.EmptyList;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public final class PrivacyEvent implements StatusEvent {
    public String contentDescription;
    public final boolean forceVisible;
    public final int priority;
    public OngoingPrivacyChip privacyChip;
    public List<PrivacyItem> privacyItems;
    public final boolean showAnimation;
    public final Function1<Context, View> viewCreator;

    public PrivacyEvent() {
        this(true);
    }

    public final String toString() {
        return "PrivacyEvent";
    }

    public PrivacyEvent(boolean z) {
        this.showAnimation = z;
        this.priority = 100;
        this.forceVisible = true;
        this.privacyItems = EmptyList.INSTANCE;
        this.viewCreator = new PrivacyEvent$viewCreator$1(this);
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public final boolean shouldUpdateFromEvent(StatusEvent statusEvent) {
        if (statusEvent instanceof PrivacyEvent) {
            PrivacyEvent privacyEvent = (PrivacyEvent) statusEvent;
            if (!Intrinsics.areEqual(privacyEvent.privacyItems, this.privacyItems) || !Intrinsics.areEqual(privacyEvent.contentDescription, this.contentDescription)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public final void updateFromEvent(StatusEvent statusEvent) {
        if (statusEvent instanceof PrivacyEvent) {
            PrivacyEvent privacyEvent = (PrivacyEvent) statusEvent;
            this.privacyItems = privacyEvent.privacyItems;
            this.contentDescription = privacyEvent.contentDescription;
            OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
            if (ongoingPrivacyChip != null) {
                ongoingPrivacyChip.setContentDescription(privacyEvent.contentDescription);
            }
            OngoingPrivacyChip ongoingPrivacyChip2 = this.privacyChip;
            if (ongoingPrivacyChip2 != null) {
                ongoingPrivacyChip2.setPrivacyList(privacyEvent.privacyItems);
            }
        }
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public final String getContentDescription() {
        return this.contentDescription;
    }

    public final boolean getForceVisible() {
        return this.forceVisible;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public final int getPriority() {
        return this.priority;
    }

    public final boolean getShowAnimation() {
        return this.showAnimation;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public final Function1<Context, View> getViewCreator() {
        return this.viewCreator;
    }
}
