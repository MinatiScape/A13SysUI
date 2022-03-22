package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
/* loaded from: classes.dex */
class ChipTextInputComboView extends FrameLayout implements Checkable {
    public final Chip chip;
    public final EditText editText;
    public TextFormatter watcher;

    /* loaded from: classes.dex */
    public class TextFormatter extends TextWatcherAdapter {
        public TextFormatter() {
        }

        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public final void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable)) {
                ChipTextInputComboView chipTextInputComboView = ChipTextInputComboView.this;
                chipTextInputComboView.chip.setText(ChipTextInputComboView.access$100(chipTextInputComboView, "00"));
                return;
            }
            ChipTextInputComboView chipTextInputComboView2 = ChipTextInputComboView.this;
            chipTextInputComboView2.chip.setText(ChipTextInputComboView.access$100(chipTextInputComboView2, editable));
        }
    }

    public ChipTextInputComboView(Context context) {
        this(context, null);
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.Checkable
    public final boolean isChecked() {
        return this.chip.isChecked();
    }

    @Override // android.widget.Checkable
    public final void setChecked(boolean z) {
        int i;
        this.chip.setChecked(z);
        EditText editText = this.editText;
        int i2 = 0;
        if (z) {
            i = 0;
        } else {
            i = 4;
        }
        editText.setVisibility(i);
        Chip chip = this.chip;
        if (z) {
            i2 = 8;
        }
        chip.setVisibility(i2);
        if (isChecked()) {
            EditText editText2 = this.editText;
            editText2.requestFocus();
            editText2.post(new ViewUtils.AnonymousClass1(editText2));
            if (!TextUtils.isEmpty(this.editText.getText())) {
                EditText editText3 = this.editText;
                editText3.setSelection(editText3.getText().length());
            }
        }
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.chip.setOnClickListener(onClickListener);
    }

    @Override // android.view.View
    public final void setTag(int i, Object obj) {
        this.chip.setTag(i, obj);
    }

    @Override // android.widget.Checkable
    public final void toggle() {
        this.chip.toggle();
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater from = LayoutInflater.from(context);
        Chip chip = (Chip) from.inflate(2131624253, (ViewGroup) this, false);
        this.chip = chip;
        TextInputLayout textInputLayout = (TextInputLayout) from.inflate(2131624254, (ViewGroup) this, false);
        Objects.requireNonNull(textInputLayout);
        EditText editText = textInputLayout.editText;
        this.editText = editText;
        editText.setVisibility(4);
        TextFormatter textFormatter = new TextFormatter();
        this.watcher = textFormatter;
        editText.addTextChangedListener(textFormatter);
        editText.setImeHintLocales(getContext().getResources().getConfiguration().getLocales());
        addView(chip);
        addView(textInputLayout);
        TextView textView = (TextView) findViewById(2131428310);
        editText.setSaveEnabled(false);
    }

    public static String access$100(ChipTextInputComboView chipTextInputComboView, CharSequence charSequence) {
        Objects.requireNonNull(chipTextInputComboView);
        return String.format(chipTextInputComboView.getResources().getConfiguration().locale, "%02d", Integer.valueOf(Integer.parseInt(String.valueOf(charSequence))));
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.editText.setImeHintLocales(getContext().getResources().getConfiguration().getLocales());
    }
}
