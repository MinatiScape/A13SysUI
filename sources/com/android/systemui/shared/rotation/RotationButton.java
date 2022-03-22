package com.android.systemui.shared.rotation;

import android.graphics.drawable.Drawable;
import android.view.View;
/* loaded from: classes.dex */
public interface RotationButton {

    /* loaded from: classes.dex */
    public interface RotationButtonUpdatesCallback {
    }

    default View getCurrentView() {
        return null;
    }

    default Drawable getImageDrawable() {
        return null;
    }

    default boolean hide() {
        return false;
    }

    default boolean isVisible() {
        return false;
    }

    default void setCanShowRotationButton(boolean z) {
    }

    default void setDarkIntensity(float f) {
    }

    default void setOnClickListener(View.OnClickListener onClickListener) {
    }

    default void setOnHoverListener(RotationButtonController$$ExternalSyntheticLambda0 rotationButtonController$$ExternalSyntheticLambda0) {
    }

    default void setRotationButtonController(RotationButtonController rotationButtonController) {
    }

    default void setUpdatesCallback(RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
    }

    default boolean show() {
        return false;
    }

    default void updateIcon(int i, int i2) {
    }

    default boolean acceptRotationProposal() {
        if (getCurrentView() != null) {
            return true;
        }
        return false;
    }
}
