package com.android.wm.shell.bubbles;

import android.app.NotificationChannel;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.util.Pair;
import android.util.SparseArray;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda1;
import com.android.systemui.wmshell.BubblesManager$8$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface Bubbles {

    /* loaded from: classes.dex */
    public interface BubbleExpandListener {
        void onBubbleExpandChanged(boolean z, String str);
    }

    /* loaded from: classes.dex */
    public interface PendingIntentCanceledListener {
    }

    /* loaded from: classes.dex */
    public interface SuppressionChangedListener {
    }

    /* loaded from: classes.dex */
    public interface SysuiProxy {
    }

    void collapseStack();

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    void expandStackAndSelectBubble(Bubble bubble);

    void expandStackAndSelectBubble(BubbleEntry bubbleEntry);

    Bubble getBubbleWithShortcutId(String str);

    boolean handleDismissalInterception(BubbleEntry bubbleEntry, ArrayList arrayList, BubblesManager$$ExternalSyntheticLambda1 bubblesManager$$ExternalSyntheticLambda1, Executor executor);

    boolean isBubbleExpanded(String str);

    boolean isBubbleNotificationSuppressedFromShade(String str, String str2);

    boolean isStackExpanded();

    void onConfigChanged(Configuration configuration);

    void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray);

    void onEntryAdded(BubbleEntry bubbleEntry);

    void onEntryRemoved(BubbleEntry bubbleEntry);

    void onEntryUpdated(BubbleEntry bubbleEntry, boolean z);

    void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i);

    void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap);

    void onStatusBarStateChanged(boolean z);

    void onStatusBarVisibilityChanged(boolean z);

    void onUserChanged(int i);

    void onZenStateChanged();

    void removeSuppressedSummaryIfNecessary(String str, BubblesManager$8$$ExternalSyntheticLambda0 bubblesManager$8$$ExternalSyntheticLambda0, Executor executor);

    void setExpandListener(StatusBar$$ExternalSyntheticLambda2 statusBar$$ExternalSyntheticLambda2);

    void setSysuiProxy(BubblesManager.AnonymousClass5 r1);

    void updateForThemeChanges();
}
