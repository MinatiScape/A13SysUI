package com.google.android.systemui.assist.uihints;

import android.content.ContentResolver;
import android.content.Context;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantWarmer_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ AssistantWarmer_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new AssistantWarmer((Context) this.contextProvider.mo144get());
            case 1:
                ContentResolver contentResolver = ((Context) this.contextProvider.mo144get()).getContentResolver();
                Objects.requireNonNull(contentResolver, "Cannot return null from a non-@Nullable @Provides method");
                return contentResolver;
            case 2:
                NodeController nodeController = ((SectionHeaderControllerSubcomponent) this.contextProvider.mo144get()).getNodeController();
                Objects.requireNonNull(nodeController, "Cannot return null from a non-@Nullable @Provides method");
                return nodeController;
            case 3:
                return new PipAnimationController((PipSurfaceTransactionHelper) this.contextProvider.mo144get());
            default:
                return new TakeScreenshotHandler((Context) this.contextProvider.mo144get());
        }
    }
}
