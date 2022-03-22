package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.LocusId;
import android.content.pm.LauncherApps;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobImpl;
import kotlinx.coroutines.StandaloneCoroutine;
import kotlinx.coroutines.internal.ContextScope;
/* compiled from: BubbleDataRepository.kt */
/* loaded from: classes.dex */
public final class BubbleDataRepository {
    public final ContextScope ioScope;
    public StandaloneCoroutine job;
    public final LauncherApps launcherApps;
    public final ShellExecutor mainExecutor;
    public final BubblePersistentRepository persistentRepository;
    public final BubbleVolatileRepository volatileRepository;

    public static ArrayList transform(List list) {
        String str;
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Bubble bubble = (Bubble) it.next();
            Objects.requireNonNull(bubble);
            int identifier = bubble.mUser.getIdentifier();
            String str2 = bubble.mPackageName;
            String str3 = bubble.mMetadataShortcutId;
            BubbleEntity bubbleEntity = null;
            if (str3 != null) {
                String str4 = bubble.mKey;
                int i = bubble.mDesiredHeight;
                int i2 = bubble.mDesiredHeightResId;
                String str5 = bubble.mTitle;
                int taskId = bubble.getTaskId();
                LocusId locusId = bubble.mLocusId;
                if (locusId == null) {
                    str = null;
                } else {
                    str = locusId.getId();
                }
                bubbleEntity = new BubbleEntity(identifier, str2, str3, str4, i, i2, str5, taskId, str);
            }
            if (bubbleEntity != null) {
                arrayList.add(bubbleEntity);
            }
        }
        return arrayList;
    }

    public BubbleDataRepository(Context context, LauncherApps launcherApps, ShellExecutor shellExecutor) {
        this.launcherApps = launcherApps;
        this.mainExecutor = shellExecutor;
        this.volatileRepository = new BubbleVolatileRepository(launcherApps);
        this.persistentRepository = new BubblePersistentRepository(context);
        CoroutineContext coroutineContext = Dispatchers.IO;
        this.ioScope = new ContextScope(coroutineContext.get(Job.Key.$$INSTANCE) == null ? coroutineContext.plus(new JobImpl(null)) : coroutineContext);
    }
}
