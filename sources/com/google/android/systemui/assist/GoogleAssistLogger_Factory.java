package com.google.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GoogleAssistLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider assistUtilsProvider;
    public final Provider assistantPresenceHandlerProvider;
    public final Provider contextProvider;
    public final Provider phoneStateMonitorProvider;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ GoogleAssistLogger_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.phoneStateMonitorProvider = provider4;
        this.assistantPresenceHandlerProvider = provider5;
    }

    public static GoogleAssistLogger_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new GoogleAssistLogger_Factory(provider, provider2, provider3, provider4, provider5, 1);
    }

    public static GoogleAssistLogger_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new GoogleAssistLogger_Factory(provider, provider2, provider3, provider4, provider5, 2);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new GoogleAssistLogger((Context) this.contextProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get(), (AssistUtils) this.assistUtilsProvider.mo144get(), (PhoneStateMonitor) this.phoneStateMonitorProvider.mo144get(), (AssistantPresenceHandler) this.assistantPresenceHandlerProvider.mo144get());
            case 1:
                return new LongScreenshotActivity((UiEventLogger) this.contextProvider.mo144get(), (ImageExporter) this.uiEventLoggerProvider.mo144get(), (Executor) this.assistUtilsProvider.mo144get(), (Executor) this.phoneStateMonitorProvider.mo144get(), (LongScreenshotData) this.assistantPresenceHandlerProvider.mo144get());
            default:
                return new KeyguardStateControllerImpl((Context) this.contextProvider.mo144get(), (KeyguardUpdateMonitor) this.uiEventLoggerProvider.mo144get(), (LockPatternUtils) this.assistUtilsProvider.mo144get(), DoubleCheck.lazy(this.phoneStateMonitorProvider), (DumpManager) this.assistantPresenceHandlerProvider.mo144get());
        }
    }
}
