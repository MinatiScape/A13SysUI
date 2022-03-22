package com.android.systemui;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1;
import dagger.Lazy;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ActivityStarterDelegate implements ActivityStarter {
    public Lazy<Optional<StatusBar>> mActualStarterOptionalLazy;

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(final Intent intent, final int i) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).postStartActivityDismissingKeyguard(intent, i);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, final boolean z, final boolean z2, final int i) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startActivity(intent, z, z2, i);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarterOptionalLazy.get().ifPresent(new PipMediaController$$ExternalSyntheticLambda1(pendingIntent, 1));
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void dismissKeyguardThenExecute(final ActivityStarter.OnDismissAction onDismissAction, final Runnable runnable, final boolean z) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).dismissKeyguardThenExecute(ActivityStarter.OnDismissAction.this, runnable, z);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postQSRunnableDismissingKeyguard(Runnable runnable) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda0(runnable, 0));
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(final Intent intent, final int i, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).postStartActivityDismissingKeyguard(intent, i, controller);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, final boolean z) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startActivity(intent, z);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startPendingIntentDismissingKeyguard(pendingIntent, runnable);
            }
        });
    }

    public ActivityStarterDelegate(Lazy<Optional<StatusBar>> lazy) {
        this.mActualStarterOptionalLazy = lazy;
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarterOptionalLazy.get().ifPresent(new QSTileHost$$ExternalSyntheticLambda1(pendingIntent, 1));
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, final boolean z, final ActivityLaunchAnimator.Controller controller, final boolean z2) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startActivity(intent, z, controller, z2);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable, final View view) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startPendingIntentDismissingKeyguard(pendingIntent, runnable, view);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(final PendingIntent pendingIntent, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).postStartActivityDismissingKeyguard(pendingIntent, controller);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, final boolean z, final boolean z2) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startActivity(intent, z, z2);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startPendingIntentDismissingKeyguard(pendingIntent, runnable, controller);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, final boolean z, final ActivityStarter.Callback callback) {
        this.mActualStarterOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((StatusBar) obj).startActivity(intent, z, callback);
            }
        });
    }
}
