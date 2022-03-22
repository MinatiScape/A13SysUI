package com.google.android.systemui.statusbar.notification.voicereplies;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.util.ConvenienceExtensionsKt;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$extractCandidate$2 extends Lambda implements Function1<Notification.Action, VoiceReplyTarget> {
    public final /* synthetic */ Lazy<Notification.Builder> $builderLazy;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ List<NotificationVoiceReplyHandler> $handlers;
    public final /* synthetic */ long $postTime;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationVoiceReplyController$extractCandidate$2(NotificationVoiceReplyController notificationVoiceReplyController, NotificationEntry notificationEntry, long j, Lazy<? extends Notification.Builder> lazy, List<NotificationVoiceReplyHandler> list) {
        super(1);
        this.this$0 = notificationVoiceReplyController;
        this.$entry = notificationEntry;
        this.$postTime = j;
        this.$builderLazy = lazy;
        this.$handlers = list;
    }

    @Override // kotlin.jvm.functions.Function1
    public final VoiceReplyTarget invoke(Notification.Action action) {
        Object obj;
        NotificationContentView notificationContentView;
        Button button;
        Button button2;
        Object obj2;
        Object[] objArr;
        Notification.Action action2 = action;
        NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
        NotificationEntry notificationEntry = this.$entry;
        long j = this.$postTime;
        Lazy<Notification.Builder> lazy = this.$builderLazy;
        List<NotificationVoiceReplyHandler> list = this.$handlers;
        Objects.requireNonNull(notificationVoiceReplyController);
        PendingIntent pendingIntent = action2.actionIntent;
        if (pendingIntent == null) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController.logger;
            Objects.requireNonNull(notificationEntry);
            notificationVoiceReplyLogger.logRejectCandidate(notificationEntry.mKey, "no action intent");
            return null;
        }
        RemoteInput[] remoteInputs = action2.getRemoteInputs();
        if (remoteInputs == null) {
            return null;
        }
        Iterator it = ArraysKt___ArraysKt.asSequence(remoteInputs).iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((RemoteInput) obj).getAllowFreeFormInput()) {
                break;
            }
        }
        RemoteInput remoteInput = (RemoteInput) obj;
        if (remoteInput == null) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger2 = notificationVoiceReplyController.logger;
            Objects.requireNonNull(notificationEntry);
            notificationVoiceReplyLogger2.logRejectCandidate(notificationEntry.mKey, "no freeform input");
            return null;
        }
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        if (expandableNotificationRow == null) {
            notificationContentView = null;
        } else {
            notificationContentView = expandableNotificationRow.getShowingLayout();
        }
        if (notificationContentView == null) {
            return null;
        }
        View view = notificationContentView.mExpandedChild;
        if (view == null) {
            button = null;
        } else {
            ViewGroup viewGroup = (ViewGroup) view.findViewById(16908741);
            if (viewGroup != null) {
                FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.filter(ConvenienceExtensionsKt.getChildren(viewGroup), NotificationVoiceReplyManagerKt$getReplyButton$1.INSTANCE));
                while (true) {
                    if (!filteringSequence$iterator$1.hasNext()) {
                        obj2 = null;
                        break;
                    }
                    obj2 = filteringSequence$iterator$1.next();
                    Object tag = ((View) obj2).getTag(16909390);
                    boolean z = false;
                    if (tag != null) {
                        if (tag instanceof Object[]) {
                            objArr = (Object[]) tag;
                        } else {
                            objArr = null;
                        }
                        if (objArr == null) {
                            continue;
                        } else {
                            Iterator it2 = ArraysKt___ArraysKt.asSequence(objArr).iterator();
                            int i = 0;
                            while (true) {
                                if (!it2.hasNext()) {
                                    i = -1;
                                    break;
                                }
                                Object next = it2.next();
                                if (i < 0) {
                                    SetsKt__SetsKt.throwIndexOverflow();
                                    throw null;
                                } else if (Intrinsics.areEqual(remoteInput, next)) {
                                    break;
                                } else {
                                    i++;
                                }
                            }
                            if (i >= 0) {
                                z = true;
                                continue;
                            } else {
                                continue;
                            }
                        }
                    }
                    if (z) {
                        break;
                    }
                }
                View view2 = (View) obj2;
                if (view2 != null && (view2 instanceof Button)) {
                    button2 = (Button) view2;
                    button = button2;
                }
            }
            button2 = null;
            button = button2;
        }
        if (button != null) {
            return new VoiceReplyTarget(notificationEntry, lazy.getValue(), j, list, pendingIntent, remoteInputs, remoteInput, button, notificationVoiceReplyController.notificationRemoteInputManager, notificationVoiceReplyController.shadeTransitionController, notificationVoiceReplyController.statusBar, notificationVoiceReplyController.statusBarStateController, notificationVoiceReplyController.logger, notificationVoiceReplyController.notifShadeWindowController, notificationVoiceReplyController.statusBarKeyguardViewManager, notificationVoiceReplyController.powerManager);
        }
        notificationVoiceReplyController.logger.logRejectCandidate(notificationEntry.mKey, "no reply button in expanded view");
        return null;
    }
}
