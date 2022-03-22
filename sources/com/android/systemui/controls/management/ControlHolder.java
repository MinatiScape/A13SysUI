package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.RenderInfo;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlHolder extends Holder {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ControlHolderAccessibilityDelegate accessibilityDelegate;
    public final CheckBox favorite;
    public final Function2<String, Boolean, Unit> favoriteCallback;
    public final String favoriteStateDescription;
    public final ImageView icon;
    public final ControlsModel.MoveHelper moveHelper;
    public final String notFavoriteStateDescription;
    public final TextView removed;
    public final TextView subtitle;
    public final TextView title;

    @Override // com.android.systemui.controls.management.Holder
    public final void bindData(final ElementWrapper elementWrapper) {
        CharSequence charSequence;
        ControlInterface controlInterface = (ControlInterface) elementWrapper;
        ComponentName component = controlInterface.getComponent();
        int deviceType = controlInterface.getDeviceType();
        SparseArray<Drawable> sparseArray = RenderInfo.iconMap;
        RenderInfo lookup = RenderInfo.Companion.lookup(this.itemView.getContext(), component, deviceType, 0);
        this.title.setText(controlInterface.getTitle());
        this.subtitle.setText(controlInterface.getSubtitle());
        updateFavorite(controlInterface.getFavorite());
        TextView textView = this.removed;
        if (controlInterface.getRemoved()) {
            charSequence = this.itemView.getContext().getText(2131952203);
        } else {
            charSequence = "";
        }
        textView.setText(charSequence);
        this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlHolder$bindData$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlHolder controlHolder = ControlHolder.this;
                controlHolder.updateFavorite(!controlHolder.favorite.isChecked());
                ControlHolder controlHolder2 = ControlHolder.this;
                Objects.requireNonNull(controlHolder2);
                controlHolder2.favoriteCallback.invoke(((ControlInterface) elementWrapper).getControlId(), Boolean.valueOf(ControlHolder.this.favorite.isChecked()));
            }
        });
        Context context = this.itemView.getContext();
        ColorStateList colorStateList = context.getResources().getColorStateList(lookup.foreground, context.getTheme());
        Unit unit = null;
        this.icon.setImageTintList(null);
        Icon customIcon = controlInterface.getCustomIcon();
        if (customIcon != null) {
            this.icon.setImageIcon(customIcon);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            this.icon.setImageDrawable(lookup.icon);
            if (controlInterface.getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
    }

    public final String stateDescription(boolean z) {
        if (!z) {
            return this.notFavoriteStateDescription;
        }
        if (this.moveHelper == null) {
            return this.favoriteStateDescription;
        }
        return this.itemView.getContext().getString(2131951693, Integer.valueOf(getLayoutPosition() + 1));
    }

    @Override // com.android.systemui.controls.management.Holder
    public final void updateFavorite(boolean z) {
        this.favorite.setChecked(z);
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = this.accessibilityDelegate;
        Objects.requireNonNull(controlHolderAccessibilityDelegate);
        controlHolderAccessibilityDelegate.isFavorite = z;
        this.itemView.setStateDescription(stateDescription(z));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ControlHolder(View view, ControlsModel.MoveHelper moveHelper, Function2<? super String, ? super Boolean, Unit> function2) {
        super(view);
        this.moveHelper = moveHelper;
        this.favoriteCallback = function2;
        this.favoriteStateDescription = view.getContext().getString(2131951692);
        this.notFavoriteStateDescription = view.getContext().getString(2131951699);
        this.icon = (ImageView) view.requireViewById(2131428102);
        this.title = (TextView) view.requireViewById(2131429057);
        this.subtitle = (TextView) view.requireViewById(2131428947);
        this.removed = (TextView) view.requireViewById(2131428921);
        CheckBox checkBox = (CheckBox) view.requireViewById(2131427958);
        checkBox.setVisibility(0);
        this.favorite = checkBox;
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = new ControlHolderAccessibilityDelegate(new ControlHolder$accessibilityDelegate$1(this), new ControlHolder$accessibilityDelegate$2(this), moveHelper);
        this.accessibilityDelegate = controlHolderAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(view, controlHolderAccessibilityDelegate);
    }
}
