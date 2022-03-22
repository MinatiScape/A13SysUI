package com.android.systemui.dreams;

import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda1;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.qs.tiles.InternetTile$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda5;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class DreamOverlayStateController implements CallbackController<Callback> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Executor mExecutor;
    public int mState;
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public int mAvailableComplicationTypes = 0;
    public boolean mShouldShowComplications = true;
    public final HashSet mComplications = new HashSet();

    /* loaded from: classes.dex */
    public interface Callback {
        default void onAvailableComplicationTypesChanged() {
        }

        default void onComplicationsChanged() {
        }

        default void onStateChanged() {
        }
    }

    public final void setOverlayActive(boolean z) {
        boolean z2;
        if (z) {
            z2 = true;
        } else {
            z2 = true;
        }
        int i = this.mState;
        if (z2) {
            this.mState = i & (-2);
        } else if (z2) {
            this.mState = i | 1;
        }
        if (i != this.mState) {
            this.mExecutor.execute(new Runnable(this) { // from class: com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda3
                public final /* synthetic */ DreamOverlayStateController f$0;
                public final /* synthetic */ Consumer f$1;

                {
                    DreamOverlayStateController$$ExternalSyntheticLambda8 dreamOverlayStateController$$ExternalSyntheticLambda8 = DreamOverlayStateController$$ExternalSyntheticLambda8.INSTANCE;
                    this.f$0 = this;
                    this.f$1 = dreamOverlayStateController$$ExternalSyntheticLambda8;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    DreamOverlayStateController dreamOverlayStateController = this.f$0;
                    Consumer consumer = this.f$1;
                    Objects.requireNonNull(dreamOverlayStateController);
                    Iterator<DreamOverlayStateController.Callback> it = dreamOverlayStateController.mCallbacks.iterator();
                    while (it.hasNext()) {
                        consumer.accept(it.next());
                    }
                }
            });
        }
    }

    static {
        Log.isLoggable("DreamOverlayStateCtlr", 3);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(Callback callback) {
        this.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda20(this, callback, 2));
    }

    public final void addComplication(Complication complication) {
        this.mExecutor.execute(new InternetTile$$ExternalSyntheticLambda0(this, complication, 1));
    }

    public final Collection<Complication> getComplications() {
        return Collections.unmodifiableCollection((Collection) this.mComplications.stream().filter(new BubbleData$$ExternalSyntheticLambda5(this, 2)).collect(Collectors.toCollection(DreamOverlayStateController$$ExternalSyntheticLambda9.INSTANCE)));
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(Callback callback) {
        this.mExecutor.execute(new SystemUIApplication$$ExternalSyntheticLambda1(this, callback, 1));
    }

    @VisibleForTesting
    public DreamOverlayStateController(Executor executor) {
        this.mExecutor = executor;
    }
}
