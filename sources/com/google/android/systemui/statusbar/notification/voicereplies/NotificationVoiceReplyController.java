package com.google.android.systemui.statusbar.notification.voicereplies;

import android.app.Notification;
import android.content.Context;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$anim;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.ConvenienceExtensionsKt;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequenceBuilderIterator;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.AbstractChannel;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharedFlowImpl;
import kotlinx.coroutines.flow.SharedFlowKt;
import kotlinx.coroutines.flow.StateFlowImpl;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController implements NotificationVoiceReplyManager.Initializer {
    public final BindEventManager bindEventManager;
    public final Context context;
    public final HeadsUpManager headsUpManager;
    public final NotificationLockscreenUserManager lockscreenUserManager;
    public final NotificationVoiceReplyLogger logger;
    public final CommonNotifCollection notifCollection;
    public final NotifLiveDataStore notifLiveDataStore;
    public final NotificationShadeWindowController notifShadeWindowController;
    public final NotificationRemoteInputManager notificationRemoteInputManager;
    public final PowerManager powerManager;
    public final LockscreenShadeTransitionController shadeTransitionController;
    public final StatusBar statusBar;
    public final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    public final SysuiStatusBarStateController statusBarStateController;

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* loaded from: classes.dex */
    public static final class Connection {
        public final Map<Integer, List<NotificationVoiceReplyHandler>> activeHandlersByUser;
        public final MutableSharedFlow<Pair<NotificationEntry, String>> entryReinflations;
        public final MutableSharedFlow<String> entryRemovals;
        public final MutableSharedFlow<Unit> hideVisibleQuickPhraseCtaRequests;
        public final Channel<StartSessionRequest> startSessionRequests;
        public final MutableStateFlow<VoiceReplyState> stateFlow;

        public Connection() {
            this(null);
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Connection)) {
                return false;
            }
            Connection connection = (Connection) obj;
            return Intrinsics.areEqual(this.entryReinflations, connection.entryReinflations) && Intrinsics.areEqual(this.entryRemovals, connection.entryRemovals) && Intrinsics.areEqual(this.startSessionRequests, connection.startSessionRequests) && Intrinsics.areEqual(this.activeHandlersByUser, connection.activeHandlersByUser) && Intrinsics.areEqual(this.stateFlow, connection.stateFlow) && Intrinsics.areEqual(this.hideVisibleQuickPhraseCtaRequests, connection.hideVisibleQuickPhraseCtaRequests);
        }

        public final int hashCode() {
            int hashCode = this.entryRemovals.hashCode();
            int hashCode2 = this.startSessionRequests.hashCode();
            int hashCode3 = this.activeHandlersByUser.hashCode();
            int hashCode4 = this.stateFlow.hashCode();
            return this.hideVisibleQuickPhraseCtaRequests.hashCode() + ((hashCode4 + ((hashCode3 + ((hashCode2 + ((hashCode + (this.entryReinflations.hashCode() * 31)) * 31)) * 31)) * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Connection(entryReinflations=");
            m.append(this.entryReinflations);
            m.append(", entryRemovals=");
            m.append(this.entryRemovals);
            m.append(", startSessionRequests=");
            m.append(this.startSessionRequests);
            m.append(", activeHandlersByUser=");
            m.append(this.activeHandlersByUser);
            m.append(", stateFlow=");
            m.append(this.stateFlow);
            m.append(", hideVisibleQuickPhraseCtaRequests=");
            m.append(this.hideVisibleQuickPhraseCtaRequests);
            m.append(')');
            return m.toString();
        }

        public Connection(Object obj) {
            SharedFlowImpl MutableSharedFlow$default = SharedFlowKt.MutableSharedFlow$default(Integer.MAX_VALUE, 5);
            SharedFlowImpl MutableSharedFlow$default2 = SharedFlowKt.MutableSharedFlow$default(Integer.MAX_VALUE, 5);
            AbstractChannel Channel$default = R$anim.Channel$default(0, 7);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            StateFlowImpl stateFlowImpl = new StateFlowImpl(NoCandidate.INSTANCE);
            SharedFlowImpl MutableSharedFlow$default3 = SharedFlowKt.MutableSharedFlow$default(0, 7);
            this.entryReinflations = MutableSharedFlow$default;
            this.entryRemovals = MutableSharedFlow$default2;
            this.startSessionRequests = Channel$default;
            this.activeHandlersByUser = linkedHashMap;
            this.stateFlow = stateFlowImpl;
            this.hideVisibleQuickPhraseCtaRequests = MutableSharedFlow$default3;
        }
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager.Initializer
    public final NotificationVoiceReplyController$connect$1 connect(CoroutineScope coroutineScope) {
        Connection connection = new Connection(null);
        return new NotificationVoiceReplyController$connect$1(BuildersKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyController$connect$job$1(this, connection, null), 3), this, connection);
    }

    public final VoiceReplyState queryInitialState(Connection connection) {
        Object obj;
        FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1((Iterable) this.notifLiveDataStore.getActiveNotifList().getValue()), new NotificationVoiceReplyController$queryInitialState$1(this, connection)));
        HasCandidate hasCandidate = null;
        if (!filteringSequence$iterator$1.hasNext()) {
            obj = null;
        } else {
            obj = filteringSequence$iterator$1.next();
        }
        VoiceReplyTarget voiceReplyTarget = (VoiceReplyTarget) obj;
        if (voiceReplyTarget != null) {
            hasCandidate = new HasCandidate(voiceReplyTarget);
        }
        if (hasCandidate == null) {
            return NoCandidate.INSTANCE;
        }
        return hasCandidate;
    }

    public NotificationVoiceReplyController(CommonNotifCollection commonNotifCollection, BindEventManager bindEventManager, NotifLiveDataStore notifLiveDataStore, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBar statusBar, SysuiStatusBarStateController sysuiStatusBarStateController, HeadsUpManager headsUpManager, PowerManager powerManager, Context context, NotificationVoiceReplyLogger notificationVoiceReplyLogger) {
        this.notifCollection = commonNotifCollection;
        this.bindEventManager = bindEventManager;
        this.notifLiveDataStore = notifLiveDataStore;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.notificationRemoteInputManager = notificationRemoteInputManager;
        this.shadeTransitionController = lockscreenShadeTransitionController;
        this.notifShadeWindowController = notificationShadeWindowController;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.statusBar = statusBar;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.headsUpManager = headsUpManager;
        this.powerManager = powerManager;
        this.context = context;
        this.logger = notificationVoiceReplyLogger;
    }

    public static final void access$addCallToAction(NotificationVoiceReplyController notificationVoiceReplyController, View view, int i) {
        ViewGroup viewGroup;
        ColorStateList textColors;
        Objects.requireNonNull(notificationVoiceReplyController);
        View findViewById = view.findViewById(16909500);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
            viewGroup = (ViewGroup) findViewById;
        } else {
            viewGroup = null;
        }
        if (viewGroup != null) {
            viewGroup.setVisibility(0);
            Iterator<Object> it = ConvenienceExtensionsKt.getChildren(viewGroup).iterator();
            while (true) {
                SequenceBuilderIterator sequenceBuilderIterator = (SequenceBuilderIterator) it;
                if (!sequenceBuilderIterator.hasNext()) {
                    break;
                }
                ((View) sequenceBuilderIterator.next()).setVisibility(8);
            }
            View inflate = LayoutInflater.from(notificationVoiceReplyController.context).inflate(2131624001, viewGroup, true);
            TextView textView = (TextView) view.findViewById(16909597);
            int i2 = -7829368;
            if (!(textView == null || (textColors = textView.getTextColors()) == null)) {
                i2 = textColors.getDefaultColor();
            }
            ((ImageView) inflate.requireViewById(2131429204)).setColorFilter(i2);
            TextView textView2 = (TextView) inflate.requireViewById(2131429205);
            textView2.setTextColor(i2);
            textView2.setText(i);
        }
    }

    public static final void access$removeCallToAction(NotificationVoiceReplyController notificationVoiceReplyController, View view) {
        LinearLayout linearLayout;
        View findViewById = view.findViewById(16909500);
        if (findViewById != null && (findViewById instanceof LinearLayout)) {
            linearLayout = (LinearLayout) findViewById;
        } else {
            linearLayout = null;
        }
        if (linearLayout != null) {
            linearLayout.removeView((ViewGroup) linearLayout.findViewById(2131429203));
            Iterator<Object> it = ConvenienceExtensionsKt.getChildren(linearLayout).iterator();
            while (true) {
                SequenceBuilderIterator sequenceBuilderIterator = (SequenceBuilderIterator) it;
                if (!sequenceBuilderIterator.hasNext()) {
                    break;
                }
                ((View) sequenceBuilderIterator.next()).setVisibility(0);
            }
            if (linearLayout.getChildCount() == 0) {
                linearLayout.setVisibility(8);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003e  */
    /* JADX WARN: Type inference failed for: r6v6, types: [com.android.systemui.statusbar.NotificationLockscreenUserManager] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6, types: [com.android.systemui.statusbar.NotificationLockscreenUserManager$UserChangedListener, java.lang.Object, com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1] */
    /* JADX WARN: Type inference failed for: r7v8 */
    /* JADX WARN: Type inference failed for: r7v9 */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void access$resetStateOnUserChange(final com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5, final com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.Connection r6, kotlin.coroutines.Continuation r7) {
        /*
            java.util.Objects.requireNonNull(r5)
            boolean r0 = r7 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1
            if (r0 == 0) goto L_0x0016
            r0 = r7
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0016
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x001b
        L_0x0016:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1
            r0.<init>(r5, r7)
        L_0x001b:
            java.lang.Object r7 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x003e
            if (r2 == r3) goto L_0x002e
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x002e:
            java.lang.Object r5 = r0.L$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1 r5 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1) r5
            java.lang.Object r6 = r0.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r6
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: all -> 0x003c
            r7 = r5
            r5 = r6
            goto L_0x0064
        L_0x003c:
            r7 = move-exception
            goto L_0x006f
        L_0x003e:
            kotlin.ResultKt.throwOnFailure(r7)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1
            r7.<init>()
            com.android.systemui.statusbar.NotificationLockscreenUserManager r6 = r5.lockscreenUserManager
            r6.addUserChangedListener(r7)
            r0.L$0 = r5     // Catch: all -> 0x006a
            r0.L$1 = r7     // Catch: all -> 0x006a
            r0.label = r3     // Catch: all -> 0x006a
            kotlinx.coroutines.CancellableContinuationImpl r6 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x006a
            kotlin.coroutines.Continuation r0 = androidx.preference.R$color.intercepted(r0)     // Catch: all -> 0x006a
            r6.<init>(r0, r3)     // Catch: all -> 0x006a
            r6.initCancellability()     // Catch: all -> 0x006a
            java.lang.Object r6 = r6.getResult()     // Catch: all -> 0x006a
            if (r6 != r1) goto L_0x0064
            return
        L_0x0064:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x006a
            r6.<init>()     // Catch: all -> 0x006a
            throw r6     // Catch: all -> 0x006a
        L_0x006a:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r7
            r7 = r4
        L_0x006f:
            com.android.systemui.statusbar.NotificationLockscreenUserManager r6 = r6.lockscreenUserManager
            r6.removeUserChangedListener(r5)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$resetStateOnUserChange(com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController, com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection, kotlin.coroutines.Continuation):void");
    }

    public final VoiceReplyTarget extractCandidate(Connection connection, NotificationEntry notificationEntry, long j, Lazy<? extends Notification.Builder> lazy) {
        Objects.requireNonNull(notificationEntry);
        Object obj = null;
        if (!Intrinsics.areEqual(notificationEntry.mSbn.getNotification().getNotificationStyle(), Notification.MessagingStyle.class)) {
            return null;
        }
        int identifier = notificationEntry.mSbn.getUser().getIdentifier();
        Objects.requireNonNull(connection);
        List<NotificationVoiceReplyHandler> list = connection.activeHandlersByUser.get(Integer.valueOf(identifier));
        if (list == null) {
            this.logger.logRejectCandidate(notificationEntry.mKey, Intrinsics.stringPlus("no handlers for user=", Integer.valueOf(identifier)));
            return null;
        }
        Notification.Action[] actionArr = notificationEntry.mSbn.getNotification().actions;
        if (actionArr == null) {
            this.logger.logRejectCandidate(notificationEntry.mKey, "no actions");
            return null;
        }
        FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(ArraysKt___ArraysKt.asSequence(actionArr), new NotificationVoiceReplyController$extractCandidate$2(this, notificationEntry, j, lazy, list)));
        if (filteringSequence$iterator$1.hasNext()) {
            obj = filteringSequence$iterator$1.next();
        }
        return (VoiceReplyTarget) obj;
    }
}
