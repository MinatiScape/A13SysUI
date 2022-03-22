package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ActionButtonsPreference extends Preference {
    public static final boolean mIsAtLeastS = BuildCompatUtils.isAtLeastS();
    public final ArrayList mBtnBackgroundStyle1;
    public final ArrayList mBtnBackgroundStyle2;
    public final ArrayList mBtnBackgroundStyle3;
    public final ArrayList mBtnBackgroundStyle4;
    public View mDivider1;
    public View mDivider2;
    public View mDivider3;
    public final ButtonInfo mButton1Info = new ButtonInfo();
    public final ButtonInfo mButton2Info = new ButtonInfo();
    public final ButtonInfo mButton3Info = new ButtonInfo();
    public final ButtonInfo mButton4Info = new ButtonInfo();
    public final ArrayList mVisibleButtonInfos = new ArrayList(4);

    /* loaded from: classes.dex */
    public static class ButtonInfo {
        public Button mButton;

        public final boolean isVisible() {
            if (this.mButton.getVisibility() == 0) {
                return true;
            }
            return false;
        }

        public final void setUpButton() {
            this.mButton.setText((CharSequence) null);
            this.mButton.setOnClickListener(null);
            this.mButton.setEnabled(true);
            this.mButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            if (!TextUtils.isEmpty(null)) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }
    }

    public static void setupBackgrounds(List list, List list2) {
        for (int i = 0; i < list2.size(); i++) {
            ((ButtonInfo) list.get(i)).mButton.setBackground((Drawable) list2.get(i));
        }
    }

    public final void fetchDrawableArray(List<Drawable> list, TypedArray typedArray) {
        for (int i = 0; i < typedArray.length(); i++) {
            list.add(this.mContext.getDrawable(typedArray.getResourceId(i, 0)));
        }
    }

    public final void updateLayout() {
        boolean z;
        if (this.mButton1Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton1Info);
        }
        if (this.mButton2Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton2Info);
        }
        if (this.mButton3Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton3Info);
        }
        if (this.mButton4Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton4Info);
        }
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            z = true;
        } else {
            z = false;
        }
        int size = this.mVisibleButtonInfos.size();
        if (size != 1) {
            if (size != 2) {
                if (size != 3) {
                    if (size != 4) {
                        Log.e("ActionButtonPreference", "No visible buttons info, skip background settings.");
                    } else if (z) {
                        setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    } else {
                        setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    }
                } else if (z) {
                    setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                } else {
                    setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                }
            } else if (z) {
                setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            } else {
                setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            }
        } else if (z) {
            setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        } else {
            setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        }
        if (this.mDivider1 != null && this.mButton1Info.isVisible() && this.mButton2Info.isVisible()) {
            this.mDivider1.setVisibility(0);
        }
        if (this.mDivider2 != null && this.mButton3Info.isVisible() && (this.mButton1Info.isVisible() || this.mButton2Info.isVisible())) {
            this.mDivider2.setVisibility(0);
        }
        if (this.mDivider3 != null && this.mVisibleButtonInfos.size() > 1 && this.mButton4Info.isVisible()) {
            this.mDivider3.setVisibility(0);
        }
    }

    public ActionButtonsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ArrayList arrayList = new ArrayList(1);
        this.mBtnBackgroundStyle1 = arrayList;
        ArrayList arrayList2 = new ArrayList(2);
        this.mBtnBackgroundStyle2 = arrayList2;
        ArrayList arrayList3 = new ArrayList(3);
        this.mBtnBackgroundStyle3 = arrayList3;
        ArrayList arrayList4 = new ArrayList(4);
        this.mBtnBackgroundStyle4 = arrayList4;
        this.mLayoutResId = 2131624478;
        if (this.mSelectable) {
            this.mSelectable = false;
            notifyChanged();
        }
        Resources resources = this.mContext.getResources();
        fetchDrawableArray(arrayList, resources.obtainTypedArray(2130903046));
        fetchDrawableArray(arrayList2, resources.obtainTypedArray(2130903047));
        fetchDrawableArray(arrayList3, resources.obtainTypedArray(2130903048));
        fetchDrawableArray(arrayList4, resources.obtainTypedArray(2130903049));
    }

    public static void setupRtlBackgrounds(List list, List list2) {
        int size = list2.size();
        while (true) {
            size--;
            if (size >= 0) {
                ((ButtonInfo) list.get((list2.size() - 1) - size)).mButton.setBackground((Drawable) list2.get(size));
            } else {
                return;
            }
        }
    }

    @Override // androidx.preference.Preference
    public final void notifyChanged() {
        super.notifyChanged();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
            updateLayout();
        }
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        boolean z = !mIsAtLeastS;
        preferenceViewHolder.mDividerAllowedAbove = z;
        preferenceViewHolder.mDividerAllowedBelow = z;
        this.mButton1Info.mButton = (Button) preferenceViewHolder.findViewById(2131427638);
        this.mButton2Info.mButton = (Button) preferenceViewHolder.findViewById(2131427639);
        this.mButton3Info.mButton = (Button) preferenceViewHolder.findViewById(2131427640);
        this.mButton4Info.mButton = (Button) preferenceViewHolder.findViewById(2131427641);
        this.mDivider1 = preferenceViewHolder.findViewById(2131427856);
        this.mDivider2 = preferenceViewHolder.findViewById(2131427857);
        this.mDivider3 = preferenceViewHolder.findViewById(2131427858);
        this.mButton1Info.setUpButton();
        this.mButton2Info.setUpButton();
        this.mButton3Info.setUpButton();
        this.mButton4Info.setUpButton();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
        }
        updateLayout();
    }
}
