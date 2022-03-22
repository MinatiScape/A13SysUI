package com.android.systemui.statusbar.notification;

import android.util.FloatProperty;
import android.util.Property;
import android.view.View;
import java.util.function.BiConsumer;
import java.util.function.Function;
/* loaded from: classes.dex */
public abstract class AnimatableProperty {
    public static final AnonymousClass7 Y = new AnonymousClass7(2131429286, 2131429285, 2131429284, View.Y);
    public static final AnonymousClass7 TRANSLATION_X = new AnonymousClass7(2131429283, 2131429282, 2131429281, View.TRANSLATION_X);
    public static final AnonymousClass7 SCALE_X = new AnonymousClass7(2131428734, 2131428733, 2131428735, View.SCALE_X);
    public static final AnonymousClass7 SCALE_Y = new AnonymousClass7(2131428738, 2131428737, 2131428739, View.SCALE_Y);

    /* renamed from: com.android.systemui.statusbar.notification.AnimatableProperty$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 extends FloatProperty<Object> {
        public final /* synthetic */ Function val$getter;
        public final /* synthetic */ BiConsumer val$setter;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass5(String str, Function function, BiConsumer biConsumer) {
            super(str);
            this.val$getter = function;
            this.val$setter = biConsumer;
        }

        @Override // android.util.Property
        public final Float get(Object obj) {
            return (Float) this.val$getter.apply((View) obj);
        }

        @Override // android.util.FloatProperty
        public final void setValue(Object obj, float f) {
            this.val$setter.accept((View) obj, Float.valueOf(f));
        }
    }

    /* renamed from: com.android.systemui.statusbar.notification.AnimatableProperty$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass6 extends AnimatableProperty {
        public final /* synthetic */ int val$animatorTag;
        public final /* synthetic */ int val$endValueTag;
        public final /* synthetic */ Property val$property;
        public final /* synthetic */ int val$startValueTag;

        public AnonymousClass6(int i, int i2, int i3, AnonymousClass5 r4) {
            this.val$startValueTag = i;
            this.val$endValueTag = i2;
            this.val$animatorTag = i3;
            this.val$property = r4;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimationEndTag() {
            return this.val$endValueTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimationStartTag() {
            return this.val$startValueTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimatorTag() {
            return this.val$animatorTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final Property getProperty() {
            return this.val$property;
        }
    }

    /* renamed from: com.android.systemui.statusbar.notification.AnimatableProperty$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass7 extends AnimatableProperty {
        public final /* synthetic */ int val$animatorTag;
        public final /* synthetic */ int val$endValueTag;
        public final /* synthetic */ Property val$property;
        public final /* synthetic */ int val$startValueTag;

        public AnonymousClass7(int i, int i2, int i3, Property property) {
            this.val$startValueTag = i;
            this.val$endValueTag = i2;
            this.val$animatorTag = i3;
            this.val$property = property;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimationEndTag() {
            return this.val$endValueTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimationStartTag() {
            return this.val$startValueTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final int getAnimatorTag() {
            return this.val$animatorTag;
        }

        @Override // com.android.systemui.statusbar.notification.AnimatableProperty
        public final Property getProperty() {
            return this.val$property;
        }
    }

    public abstract int getAnimationEndTag();

    public abstract int getAnimationStartTag();

    public abstract int getAnimatorTag();

    public abstract Property getProperty();

    static {
        Property property = View.X;
        new FloatProperty<View>() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.1
            @Override // android.util.Property
            public final Float get(Object obj) {
                View view = (View) obj;
                Object tag = view.getTag(2131427351);
                if (tag instanceof Float) {
                    return (Float) tag;
                }
                return (Float) View.X.get(view);
            }

            @Override // android.util.FloatProperty
            public final void setValue(View view, float f) {
                View view2 = view;
                view2.setTag(2131427351, Float.valueOf(f));
                View.X.set(view2, Float.valueOf(f));
            }
        };
        new FloatProperty<View>() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.2
            @Override // android.util.Property
            public final Float get(Object obj) {
                View view = (View) obj;
                Object tag = view.getTag(2131427355);
                if (tag instanceof Float) {
                    return (Float) tag;
                }
                return (Float) View.Y.get(view);
            }

            @Override // android.util.FloatProperty
            public final void setValue(View view, float f) {
                View view2 = view;
                view2.setTag(2131427355, Float.valueOf(f));
                View.Y.set(view2, Float.valueOf(f));
            }
        };
        new FloatProperty<View>() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.3
            @Override // android.util.Property
            public final Float get(Object obj) {
                View view = (View) obj;
                Object tag = view.getTag(2131429200);
                if (tag instanceof Float) {
                    return (Float) tag;
                }
                return Float.valueOf(view.getWidth());
            }

            @Override // android.util.FloatProperty
            public final void setValue(View view, float f) {
                View view2 = view;
                view2.setTag(2131429200, Float.valueOf(f));
                view2.setRight((int) (view2.getLeft() + f));
            }
        };
        new FloatProperty<View>() { // from class: com.android.systemui.statusbar.notification.AnimatableProperty.4
            @Override // android.util.Property
            public final Float get(Object obj) {
                View view = (View) obj;
                Object tag = view.getTag(2131429192);
                if (tag instanceof Float) {
                    return (Float) tag;
                }
                return Float.valueOf(view.getHeight());
            }

            @Override // android.util.FloatProperty
            public final void setValue(View view, float f) {
                View view2 = view;
                view2.setTag(2131429192, Float.valueOf(f));
                view2.setBottom((int) (view2.getTop() + f));
            }
        };
    }
}
