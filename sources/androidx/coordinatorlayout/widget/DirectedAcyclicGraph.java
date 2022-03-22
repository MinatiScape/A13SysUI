package androidx.coordinatorlayout.widget;

import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools$SimplePool;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DirectedAcyclicGraph {
    public final Object mGraph;
    public final Object mListPool;
    public final Object mSortResult;
    public final Object mSortTmpMarked;

    public /* synthetic */ DirectedAcyclicGraph(NotificationShadeWindowViewController notificationShadeWindowViewController, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, HeadsUpManagerPhone headsUpManagerPhone, InteractionJankMonitor interactionJankMonitor) {
        this.mListPool = notificationShadeWindowViewController;
        this.mGraph = notificationListContainerImpl;
        this.mSortResult = headsUpManagerPhone;
        this.mSortTmpMarked = interactionJankMonitor;
    }

    public final NotificationLaunchAnimatorController getAnimatorController(ExpandableNotificationRow expandableNotificationRow) {
        return new NotificationLaunchAnimatorController((NotificationShadeWindowViewController) this.mListPool, (NotificationListContainer) this.mGraph, (HeadsUpManagerPhone) this.mSortResult, expandableNotificationRow, (InteractionJankMonitor) this.mSortTmpMarked);
    }

    public final void dfs(Object obj, ArrayList arrayList, HashSet hashSet) {
        if (!arrayList.contains(obj)) {
            if (!hashSet.contains(obj)) {
                hashSet.add(obj);
                SimpleArrayMap simpleArrayMap = (SimpleArrayMap) this.mGraph;
                Objects.requireNonNull(simpleArrayMap);
                ArrayList arrayList2 = (ArrayList) simpleArrayMap.getOrDefault(obj, null);
                if (arrayList2 != null) {
                    int size = arrayList2.size();
                    for (int i = 0; i < size; i++) {
                        dfs(arrayList2.get(i), arrayList, hashSet);
                    }
                }
                hashSet.remove(obj);
                arrayList.add(obj);
                return;
            }
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
    }

    public /* synthetic */ DirectedAcyclicGraph() {
        this.mListPool = new Pools$SimplePool(10);
        this.mGraph = new SimpleArrayMap();
        this.mSortResult = new ArrayList();
        this.mSortTmpMarked = new HashSet();
    }
}
