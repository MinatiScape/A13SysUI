package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: PlayerViewHolder.kt */
/* loaded from: classes.dex */
public final class PlayerViewHolder extends MediaViewHolder {
    public static final Set<Integer> controlsIds = SetsKt__SetsKt.setOf(2131428102, 2131427505, 2131427468, 2131428086, 2131428080, 2131428355, 2131428517, 2131428353, 2131427401, 2131427402, 2131427403, 2131427404, 2131427405, 2131428102);
    public static final Set<Integer> gutsIds = SetsKt__SetsKt.setOf(2131428690, 2131427659, 2131427850, 2131428837);
    public final ImageButton action0;
    public final ImageButton action1;
    public final ImageButton action2;
    public final ImageButton action3;
    public final ImageButton action4;
    public final TextView elapsedTimeView;
    public final ViewGroup progressTimes;
    public final TextView totalTimeView;

    public PlayerViewHolder(View view) {
        super(view);
        this.progressTimes = (ViewGroup) view.requireViewById(2131428517);
        this.elapsedTimeView = (TextView) view.requireViewById(2131428348);
        this.totalTimeView = (TextView) view.requireViewById(2131428360);
        ImageButton imageButton = (ImageButton) view.requireViewById(2131427401);
        this.action0 = imageButton;
        ImageButton imageButton2 = (ImageButton) view.requireViewById(2131427402);
        this.action1 = imageButton2;
        ImageButton imageButton3 = (ImageButton) view.requireViewById(2131427403);
        this.action2 = imageButton3;
        ImageButton imageButton4 = (ImageButton) view.requireViewById(2131427404);
        this.action3 = imageButton4;
        ImageButton imageButton5 = (ImageButton) view.requireViewById(2131427405);
        this.action4 = imageButton5;
        Drawable background = this.player.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
        IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
        illuminationDrawable.registerLightSource(imageButton);
        illuminationDrawable.registerLightSource(imageButton2);
        illuminationDrawable.registerLightSource(imageButton3);
        illuminationDrawable.registerLightSource(imageButton4);
        illuminationDrawable.registerLightSource(imageButton5);
    }

    @Override // com.android.systemui.media.MediaViewHolder
    public final ImageButton getAction(int i) {
        if (i == 2131427401) {
            return this.action0;
        }
        if (i == 2131427402) {
            return this.action1;
        }
        if (i == 2131427403) {
            return this.action2;
        }
        if (i == 2131427404) {
            return this.action3;
        }
        if (i == 2131427405) {
            return this.action4;
        }
        throw new IllegalArgumentException();
    }

    @Override // com.android.systemui.media.MediaViewHolder
    public final TextView getElapsedTimeView() {
        return this.elapsedTimeView;
    }

    @Override // com.android.systemui.media.MediaViewHolder
    public final TextView getTotalTimeView() {
        return this.totalTimeView;
    }
}
