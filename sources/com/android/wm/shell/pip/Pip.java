package com.android.wm.shell.pip;

import android.content.res.Configuration;
import android.graphics.Rect;
import java.io.PrintWriter;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface Pip {
    default void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    default IPip createExternalInterface() {
        return null;
    }

    default void dump(PrintWriter printWriter) {
    }

    default void hidePipMenu() {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onDensityOrFontScaleChanged() {
    }

    default void onOverlayChanged() {
    }

    default void onSystemUiStateChanged(boolean z, int i) {
    }

    default void registerSessionListenerForCurrentUser() {
    }

    default void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    default void setPinnedStackAnimationType() {
    }

    default void showPictureInPictureMenu() {
    }
}
