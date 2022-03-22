package com.android.systemui.dreams.complication;

import android.content.Context;
import android.database.ContentObserver;
import android.os.UserHandle;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ComplicationTypesUpdater extends CoreStartable {
    public final DreamBackend mDreamBackend;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final Executor mExecutor;
    public final SecureSettings mSecureSettings;

    /* renamed from: com.android.systemui.dreams.complication.ComplicationTypesUpdater$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ContentObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            ComplicationTypesUpdater.this.mExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda19(this, 1));
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        AnonymousClass1 r0 = new AnonymousClass1();
        this.mSecureSettings.registerContentObserverForUser("screensaver_enabled_complications", r0, UserHandle.myUserId());
        r0.onChange(false);
    }

    public ComplicationTypesUpdater(Context context, DreamBackend dreamBackend, Executor executor, SecureSettings secureSettings, DreamOverlayStateController dreamOverlayStateController) {
        super(context);
        this.mDreamBackend = dreamBackend;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mDreamOverlayStateController = dreamOverlayStateController;
    }
}
