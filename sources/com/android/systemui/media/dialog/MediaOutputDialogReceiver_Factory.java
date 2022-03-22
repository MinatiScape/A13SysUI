package com.android.systemui.media.dialog;

import android.content.Context;
import android.os.Looper;
import android.telecom.TelecomManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.util.concurrency.ExecutorImpl;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaOutputDialogReceiver_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider mediaOutputDialogFactoryProvider;

    public /* synthetic */ MediaOutputDialogReceiver_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.mediaOutputDialogFactoryProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new MediaOutputDialogReceiver((MediaOutputDialogFactory) this.mediaOutputDialogFactoryProvider.mo144get());
            case 1:
                Optional ofNullable = Optional.ofNullable((TelecomManager) ((Context) this.mediaOutputDialogFactoryProvider.mo144get()).getSystemService(TelecomManager.class));
                Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
                return ofNullable;
            case 2:
                return new OngoingCallLogger((UiEventLogger) this.mediaOutputDialogFactoryProvider.mo144get());
            default:
                return new ExecutorImpl((Looper) this.mediaOutputDialogFactoryProvider.mo144get());
        }
    }
}
