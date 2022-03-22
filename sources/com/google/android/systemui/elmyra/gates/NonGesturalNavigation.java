package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import androidx.leanback.R$color;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationModeController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NonGesturalNavigation extends Gate {
    public boolean mCurrentModeIsGestural;
    public final AnonymousClass1 mModeListener = new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.elmyra.gates.NonGesturalNavigation.1
        @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
        public final void onNavigationModeChanged(int i) {
            NonGesturalNavigation.this.mCurrentModeIsGestural = R$color.isGesturalMode(i);
            NonGesturalNavigation.this.notifyListener();
        }
    };
    public final NavigationModeController mModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        return !this.mCurrentModeIsGestural;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        this.mCurrentModeIsGestural = R$color.isGesturalMode(this.mModeController.addListener(this.mModeListener));
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        NavigationModeController navigationModeController = this.mModeController;
        AnonymousClass1 r1 = this.mModeListener;
        Objects.requireNonNull(navigationModeController);
        navigationModeController.mListeners.remove(r1);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.elmyra.gates.NonGesturalNavigation$1] */
    public NonGesturalNavigation(Context context) {
        super(context);
    }
}
