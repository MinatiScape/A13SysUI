package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: PlayerSessionViewHolder.kt */
/* loaded from: classes.dex */
public final class PlayerSessionViewHolder extends MediaViewHolder {
    public static final Set<Integer> controlsIds = SetsKt__SetsKt.setOf(2131428102, 2131427505, 2131428086, 2131428080, 2131428355, 2131428353, 2131427409, 2131427408, 2131427410, 2131427411, 2131427406, 2131428102);
    public static final Set<Integer> gutsIds = SetsKt__SetsKt.setOf(2131428690, 2131427659, 2131427850, 2131428837);
    public final ImageButton actionEnd;
    public final ImageButton actionNext;
    public final ImageButton actionPlayPause;
    public final ImageButton actionPrev;
    public final ImageButton actionStart;

    public PlayerSessionViewHolder(View view) {
        super(view);
        ImageButton imageButton = (ImageButton) view.requireViewById(2131427409);
        this.actionPlayPause = imageButton;
        ImageButton imageButton2 = (ImageButton) view.requireViewById(2131427408);
        this.actionNext = imageButton2;
        ImageButton imageButton3 = (ImageButton) view.requireViewById(2131427410);
        this.actionPrev = imageButton3;
        ImageButton imageButton4 = (ImageButton) view.requireViewById(2131427411);
        this.actionStart = imageButton4;
        ImageButton imageButton5 = (ImageButton) view.requireViewById(2131427406);
        this.actionEnd = imageButton5;
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
        if (i == 2131427409) {
            return this.actionPlayPause;
        }
        if (i == 2131427408) {
            return this.actionNext;
        }
        if (i == 2131427410) {
            return this.actionPrev;
        }
        if (i == 2131427411) {
            return this.actionStart;
        }
        if (i == 2131427406) {
            return this.actionEnd;
        }
        throw new IllegalArgumentException();
    }
}
