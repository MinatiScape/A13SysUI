package com.android.systemui.toast;

import android.content.ClipboardManager;
import android.content.Context;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger;
import com.android.wm.shell.compatui.CompatUIController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ToastLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bufferProvider;

    public /* synthetic */ ToastLogger_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ToastLogger((LogBuffer) this.bufferProvider.mo144get());
            case 1:
                ClipboardManager clipboardManager = (ClipboardManager) ((Context) this.bufferProvider.mo144get()).getSystemService(ClipboardManager.class);
                Objects.requireNonNull(clipboardManager, "Cannot return null from a non-@Nullable @Provides method");
                return clipboardManager;
            case 2:
                return new ShadeViewDifferLogger((LogBuffer) this.bufferProvider.mo144get());
            default:
                CompatUIController compatUIController = (CompatUIController) this.bufferProvider.mo144get();
                Objects.requireNonNull(compatUIController);
                Optional of = Optional.of(compatUIController.mImpl);
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
