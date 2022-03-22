package com.android.systemui.qs;

import androidx.recyclerview.widget.DiffUtil;
import com.android.systemui.qs.FgsManagerController;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: FgsManagerController.kt */
/* loaded from: classes.dex */
public final class FgsManagerController$AppListAdapter$setData$1 extends DiffUtil.Callback {
    public final /* synthetic */ List<FgsManagerController.RunningApp> $newData;
    public final /* synthetic */ Ref$ObjectRef<List<FgsManagerController.RunningApp>> $oldData;

    public FgsManagerController$AppListAdapter$setData$1(Ref$ObjectRef<List<FgsManagerController.RunningApp>> ref$ObjectRef, List<FgsManagerController.RunningApp> list) {
        this.$oldData = ref$ObjectRef;
        this.$newData = list;
    }

    @Override // androidx.recyclerview.widget.DiffUtil.Callback
    public final boolean areContentsTheSame(int i, int i2) {
        FgsManagerController.RunningApp runningApp = this.$oldData.element.get(i);
        Objects.requireNonNull(runningApp);
        boolean z = runningApp.stopped;
        FgsManagerController.RunningApp runningApp2 = this.$newData.get(i2);
        Objects.requireNonNull(runningApp2);
        if (z == runningApp2.stopped) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.DiffUtil.Callback
    public final boolean areItemsTheSame(int i, int i2) {
        return Intrinsics.areEqual(this.$oldData.element.get(i), this.$newData.get(i2));
    }

    public final int getNewListSize() {
        return this.$newData.size();
    }

    public final int getOldListSize() {
        return this.$oldData.element.size();
    }
}
