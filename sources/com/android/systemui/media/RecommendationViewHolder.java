package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: RecommendationViewHolder.kt */
/* loaded from: classes.dex */
public final class RecommendationViewHolder {
    public static final Set<Integer> controlsIds = SetsKt__SetsKt.setOf(2131428672, 2131428673, 2131428336, 2131428338, 2131428340, 2131428342, 2131428344, 2131428346, 2131428337, 2131428339, 2131428341, 2131428343, 2131428345, 2131428347);
    public static final Set<Integer> gutsIds = SetsKt__SetsKt.setOf(2131428690, 2131427659, 2131427850, 2131428837);
    public final View cancel;
    public final ImageView cardIcon;
    public final TextView cardText;
    public final ViewGroup dismiss;
    public final View dismissLabel;
    public final TextView longPressText;
    public final List<ViewGroup> mediaCoverContainers;
    public final List<ImageView> mediaCoverItems;
    public final TransitionLayout recommendations;
    public final View settings;
    public final TextView settingsText;
    public final List<Integer> mediaCoverItemsResIds = SetsKt__SetsKt.listOf(2131428336, 2131428338, 2131428340, 2131428342, 2131428344, 2131428346);
    public final List<Integer> mediaCoverContainersResIds = SetsKt__SetsKt.listOf(2131428337, 2131428339, 2131428341, 2131428343, 2131428345, 2131428347);

    public RecommendationViewHolder(View view) {
        TransitionLayout transitionLayout = (TransitionLayout) view;
        this.recommendations = transitionLayout;
        this.cardIcon = (ImageView) view.requireViewById(2131428672);
        this.cardText = (TextView) view.requireViewById(2131428673);
        this.mediaCoverItems = SetsKt__SetsKt.listOf((ImageView) view.requireViewById(2131428336), (ImageView) view.requireViewById(2131428338), (ImageView) view.requireViewById(2131428340), (ImageView) view.requireViewById(2131428342), (ImageView) view.requireViewById(2131428344), (ImageView) view.requireViewById(2131428346));
        List<ViewGroup> listOf = SetsKt__SetsKt.listOf((ViewGroup) view.requireViewById(2131428337), (ViewGroup) view.requireViewById(2131428339), (ViewGroup) view.requireViewById(2131428341), (ViewGroup) view.requireViewById(2131428343), (ViewGroup) view.requireViewById(2131428345), (ViewGroup) view.requireViewById(2131428347));
        this.mediaCoverContainers = listOf;
        this.longPressText = (TextView) view.requireViewById(2131428690);
        this.cancel = view.requireViewById(2131427659);
        ViewGroup viewGroup = (ViewGroup) view.requireViewById(2131427850);
        this.dismiss = viewGroup;
        this.dismissLabel = viewGroup.getChildAt(0);
        this.settings = view.requireViewById(2131428837);
        this.settingsText = (TextView) view.requireViewById(2131428844);
        Drawable background = transitionLayout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
        IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
        for (ViewGroup viewGroup2 : listOf) {
            illuminationDrawable.registerLightSource(viewGroup2);
        }
        illuminationDrawable.registerLightSource(this.cancel);
        illuminationDrawable.registerLightSource(this.dismiss);
        illuminationDrawable.registerLightSource(this.dismissLabel);
        illuminationDrawable.registerLightSource(this.settings);
    }
}
