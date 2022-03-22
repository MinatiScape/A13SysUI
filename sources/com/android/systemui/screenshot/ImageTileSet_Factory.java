package com.android.systemui.screenshot;

import android.content.Context;
import android.content.pm.ShortcutManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.RepeatableExecutorImpl;
import com.android.wm.shell.ShellInitImpl;
import com.google.android.systemui.controls.GoogleControlsTileResourceConfigurationImpl;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ImageTileSet_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object handlerProvider;

    public /* synthetic */ ImageTileSet_Factory(Object obj, int i) {
        this.$r8$classId = i;
        this.handlerProvider = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Optional optional;
        switch (this.$r8$classId) {
            case 0:
                return new ImageTileSet((Handler) ((Provider) this.handlerProvider).mo144get());
            case 1:
                ShortcutManager shortcutManager = (ShortcutManager) ((Context) ((Provider) this.handlerProvider).mo144get()).getSystemService(ShortcutManager.class);
                Objects.requireNonNull(shortcutManager, "Cannot return null from a non-@Nullable @Provides method");
                return shortcutManager;
            case 2:
                return new SystemUIAuxiliaryDumpService((DumpHandler) ((Provider) this.handlerProvider).mo144get());
            case 3:
                return ((LogBufferFactory) ((Provider) this.handlerProvider).mo144get()).create("DozeLog", 100);
            case 4:
                return new PeopleSpaceActivity((PeopleSpaceWidgetManager) ((Provider) this.handlerProvider).mo144get());
            case 5:
                return new AccessibilityManagerWrapper((AccessibilityManager) ((Provider) this.handlerProvider).mo144get());
            case FalsingManager.VERSION /* 6 */:
                return new RepeatableExecutorImpl((DelayableExecutor) ((Provider) this.handlerProvider).mo144get());
            case 7:
                ShellInitImpl shellInitImpl = (ShellInitImpl) ((Provider) this.handlerProvider).mo144get();
                Objects.requireNonNull(shellInitImpl);
                ShellInitImpl.InitImpl initImpl = shellInitImpl.mImpl;
                Objects.requireNonNull(initImpl, "Cannot return null from a non-@Nullable @Provides method");
                return initImpl;
            case 8:
                return new GoogleControlsTileResourceConfigurationImpl((ControlsController) ((Provider) this.handlerProvider).mo144get());
            case 9:
                if (((Context) ((Provider) this.handlerProvider).mo144get()).getResources().getBoolean(2131034182)) {
                    optional = Optional.of(new ReverseWirelessCharger());
                } else {
                    optional = Optional.empty();
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
            default:
                Objects.requireNonNull((UnfoldTransitionModule) this.handlerProvider);
                return "systemui";
        }
    }
}
