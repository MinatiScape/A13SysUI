package com.android.systemui.accessibility.floatingmenu;

import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda1;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda3;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class AnnotationLinkSpan extends ClickableSpan {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Optional<View.OnClickListener> mClickListener;

    /* loaded from: classes.dex */
    public static class LinkInfo {
        public final Optional<String> mAnnotation = Optional.of("link");
        public final Optional<View.OnClickListener> mListener;

        public LinkInfo(View.OnClickListener onClickListener) {
            this.mListener = Optional.ofNullable(onClickListener);
        }
    }

    public static SpannableStringBuilder linkify(CharSequence charSequence, final LinkInfo... linkInfoArr) {
        final SpannableString spannableString = new SpannableString(charSequence);
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
        Arrays.asList((Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)).forEach(new Consumer() { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AnnotationLinkSpan.LinkInfo[] linkInfoArr2 = linkInfoArr;
                final SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
                final SpannableString spannableString2 = spannableString;
                final Annotation annotation = (Annotation) obj;
                final String value = annotation.getValue();
                Arrays.asList(linkInfoArr2).stream().filter(new Predicate() { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj2) {
                        String str = value;
                        AnnotationLinkSpan.LinkInfo linkInfo = (AnnotationLinkSpan.LinkInfo) obj2;
                        if (!linkInfo.mAnnotation.isPresent() || !linkInfo.mAnnotation.get().equals(str)) {
                            return false;
                        }
                        return true;
                    }
                }).findFirst().flatMap(WMShellBaseModule$$ExternalSyntheticLambda3.INSTANCE$1).ifPresent(new Consumer() { // from class: com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj2) {
                        SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder2;
                        SpannableString spannableString3 = spannableString2;
                        Annotation annotation2 = annotation;
                        AnnotationLinkSpan annotationLinkSpan = new AnnotationLinkSpan((View.OnClickListener) obj2);
                        spannableStringBuilder3.setSpan(annotationLinkSpan, spannableString3.getSpanStart(annotation2), spannableString3.getSpanEnd(annotation2), spannableString3.getSpanFlags(annotationLinkSpan));
                    }
                });
            }
        });
        return spannableStringBuilder;
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        this.mClickListener.ifPresent(new QSTileHost$$ExternalSyntheticLambda1(view, 2));
    }

    public AnnotationLinkSpan(View.OnClickListener onClickListener) {
        this.mClickListener = Optional.ofNullable(onClickListener);
    }
}
