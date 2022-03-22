package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.Objects;
/* compiled from: MediaViewHolder.kt */
/* loaded from: classes.dex */
public abstract class MediaViewHolder {
    public final ImageView albumView;
    public final ImageView appIcon;
    public final TextView artistText;
    public final View cancel;
    public final TextView cancelText;
    public final ViewGroup dismiss;
    public final TextView dismissText;
    public final TextView longPressText;
    public final TransitionLayout player;
    public final ViewGroup seamless;
    public final View seamlessButton;
    public final ImageView seamlessIcon;
    public final TextView seamlessText;
    public final SeekBar seekBar;
    public final View settings;
    public final TextView settingsText;
    public final TextView titleText;

    public abstract ImageButton getAction(int i);

    public TextView getElapsedTimeView() {
        return null;
    }

    public TextView getTotalTimeView() {
        return null;
    }

    public final void marquee(final boolean z) {
        Handler handler = this.longPressText.getHandler();
        if (handler == null) {
            Log.d("MediaViewHolder", "marquee while longPressText.getHandler() is null", new Exception());
        } else {
            handler.postDelayed(new Runnable() { // from class: com.android.systemui.media.MediaViewHolder$marquee$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaViewHolder mediaViewHolder = MediaViewHolder.this;
                    Objects.requireNonNull(mediaViewHolder);
                    mediaViewHolder.longPressText.setSelected(z);
                }
            }, 500L);
        }
    }

    public MediaViewHolder(View view) {
        TransitionLayout transitionLayout = (TransitionLayout) view;
        this.player = transitionLayout;
        this.albumView = (ImageView) view.requireViewById(2131427468);
        this.appIcon = (ImageView) view.requireViewById(2131428102);
        this.titleText = (TextView) view.requireViewById(2131428086);
        this.artistText = (TextView) view.requireViewById(2131428080);
        ViewGroup viewGroup = (ViewGroup) view.requireViewById(2131428355);
        this.seamless = viewGroup;
        this.seamlessIcon = (ImageView) view.requireViewById(2131428357);
        this.seamlessText = (TextView) view.requireViewById(2131428358);
        this.seamlessButton = view.requireViewById(2131428356);
        this.seekBar = (SeekBar) view.requireViewById(2131428353);
        this.longPressText = (TextView) view.requireViewById(2131428690);
        View requireViewById = view.requireViewById(2131427659);
        this.cancel = requireViewById;
        this.cancelText = (TextView) view.requireViewById(2131427662);
        ViewGroup viewGroup2 = (ViewGroup) view.requireViewById(2131427850);
        this.dismiss = viewGroup2;
        this.dismissText = (TextView) view.requireViewById(2131427853);
        View requireViewById2 = view.requireViewById(2131428837);
        this.settings = requireViewById2;
        this.settingsText = (TextView) view.requireViewById(2131428844);
        Drawable background = transitionLayout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
        IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
        illuminationDrawable.registerLightSource(viewGroup);
        illuminationDrawable.registerLightSource(requireViewById);
        illuminationDrawable.registerLightSource(viewGroup2);
        illuminationDrawable.registerLightSource(requireViewById2);
    }
}
