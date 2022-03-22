package com.android.systemui.controls.controller;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.backup.BackupHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AuxiliaryPersistenceWrapper.kt */
/* loaded from: classes.dex */
public final class AuxiliaryPersistenceWrapper {
    public List<StructureInfo> favorites = EmptyList.INSTANCE;
    public ControlsFavoritePersistenceWrapper persistenceWrapper;

    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class DeletionJobService extends JobService {
        public static final /* synthetic */ int $r8$clinit = 0;
        public static final long WEEK_IN_MILLIS = TimeUnit.DAYS.toMillis(7);

        @Override // android.app.job.JobService
        public final boolean onStopJob(JobParameters jobParameters) {
            return true;
        }

        @Override // android.app.job.JobService
        public final boolean onStartJob(JobParameters jobParameters) {
            Object obj = BackupHelper.controlsDataLock;
            synchronized (BackupHelper.controlsDataLock) {
                getBaseContext().deleteFile("aux_controls_favorites.xml");
            }
            return false;
        }

        @VisibleForTesting
        public final void attachContext(Context context) {
            attachBaseContext(context);
        }
    }

    public final List<StructureInfo> getCachedFavoritesAndRemoveFor(ComponentName componentName) {
        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper = this.persistenceWrapper;
        Objects.requireNonNull(controlsFavoritePersistenceWrapper);
        if (!controlsFavoritePersistenceWrapper.file.exists()) {
            return EmptyList.INSTANCE;
        }
        List<StructureInfo> list = this.favorites;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : list) {
            StructureInfo structureInfo = (StructureInfo) obj;
            Objects.requireNonNull(structureInfo);
            if (Intrinsics.areEqual(structureInfo.componentName, componentName)) {
                arrayList.add(obj);
            } else {
                arrayList2.add(obj);
            }
        }
        Pair pair = new Pair(arrayList, arrayList2);
        List<StructureInfo> list2 = (List) pair.component1();
        List<StructureInfo> list3 = (List) pair.component2();
        this.favorites = list3;
        if (!list3.isEmpty()) {
            this.persistenceWrapper.storeFavorites(list3);
        } else {
            ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper2 = this.persistenceWrapper;
            Objects.requireNonNull(controlsFavoritePersistenceWrapper2);
            controlsFavoritePersistenceWrapper2.file.delete();
        }
        return list2;
    }

    public final void initialize() {
        List<StructureInfo> list;
        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper = this.persistenceWrapper;
        Objects.requireNonNull(controlsFavoritePersistenceWrapper);
        if (controlsFavoritePersistenceWrapper.file.exists()) {
            list = this.persistenceWrapper.readFavorites();
        } else {
            list = EmptyList.INSTANCE;
        }
        this.favorites = list;
    }

    @VisibleForTesting
    public AuxiliaryPersistenceWrapper(ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper) {
        this.persistenceWrapper = controlsFavoritePersistenceWrapper;
        initialize();
    }
}
