package com.android.systemui.classifier;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.displayareahelper.DisplayAreaHelperController;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.columbus.feedback.UserActivity;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.inject.Provider;
import kotlin.collections.SetsKt__SetsKt;
/* loaded from: classes.dex */
public final class SingleTapClassifier_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dataProvider;
    public final Provider touchSlopProvider;

    public /* synthetic */ SingleTapClassifier_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.dataProvider = provider;
        this.touchSlopProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SingleTapClassifier((FalsingDataProvider) this.dataProvider.mo144get(), ((Float) this.touchSlopProvider.mo144get()).floatValue());
            case 1:
                Optional of = Optional.of(new DisplayAreaHelperController((ShellExecutor) this.dataProvider.mo144get(), (RootDisplayAreaOrganizer) this.touchSlopProvider.mo144get()));
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
            case 2:
                return new AssistantPresenceHandler((Context) this.dataProvider.mo144get(), (AssistUtils) this.touchSlopProvider.mo144get());
            default:
                Set of2 = SetsKt__SetsKt.setOf((HapticClick) this.dataProvider.mo144get(), (UserActivity) this.touchSlopProvider.mo144get());
                Objects.requireNonNull(of2, "Cannot return null from a non-@Nullable @Provides method");
                return of2;
        }
    }
}
