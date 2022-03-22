package com.google.android.material.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class MaterialDatePicker<S> extends DialogFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public MaterialShapeDrawable background;
    public MaterialCalendar<S> calendar;
    public CalendarConstraints calendarConstraints;
    public Button confirmButton;
    public DateSelector<S> dateSelector;
    public boolean fullscreen;
    public TextView headerSelectionText;
    public CheckableImageButton headerToggleButton;
    public int inputMode;
    public int overrideThemeResId;
    public PickerFragment<S> pickerFragment;
    public CharSequence titleText;
    public int titleTextResId;
    public final LinkedHashSet<MaterialPickerOnPositiveButtonClickListener<? super S>> onPositiveButtonClickListeners = new LinkedHashSet<>();
    public final LinkedHashSet<View.OnClickListener> onNegativeButtonClickListeners = new LinkedHashSet<>();
    public final LinkedHashSet<DialogInterface.OnCancelListener> onCancelListeners = new LinkedHashSet<>();
    public final LinkedHashSet<DialogInterface.OnDismissListener> onDismissListeners = new LinkedHashSet<>();

    public static boolean readMaterialCalendarStyleBoolean(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(MaterialAttributes.resolveOrThrow(context, 2130969428, MaterialCalendar.class.getCanonicalName()), new int[]{i});
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z;
    }

    public final DateSelector<S> getDateSelector() {
        if (this.dateSelector == null) {
            this.dateSelector = (DateSelector) this.mArguments.getParcelable("DATE_SELECTOR_KEY");
        }
        return this.dateSelector;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnCancelListener> it = this.onCancelListeners.iterator();
        while (it.hasNext()) {
            it.next().onCancel(dialogInterface);
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public final Dialog onCreateDialog() {
        Context requireContext = requireContext();
        Context requireContext2 = requireContext();
        int i = this.overrideThemeResId;
        if (i == 0) {
            i = getDateSelector().getDefaultThemeResId(requireContext2);
        }
        Dialog dialog = new Dialog(requireContext, i);
        Context context = dialog.getContext();
        this.fullscreen = isFullscreen(context);
        int resolveOrThrow = MaterialAttributes.resolveOrThrow(context, 2130968847, MaterialDatePicker.class.getCanonicalName());
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, 2130969428, 2132018666);
        this.background = materialShapeDrawable;
        materialShapeDrawable.initializeElevationOverlay(context);
        this.background.setFillColor(ColorStateList.valueOf(resolveOrThrow));
        MaterialShapeDrawable materialShapeDrawable2 = this.background;
        View decorView = dialog.getWindow().getDecorView();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        materialShapeDrawable2.setElevation(ViewCompat.Api21Impl.getElevation(decorView));
        return dialog;
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i;
        boolean z;
        if (this.fullscreen) {
            i = 2131624307;
        } else {
            i = 2131624306;
        }
        View inflate = layoutInflater.inflate(i, viewGroup);
        Context context = inflate.getContext();
        if (this.fullscreen) {
            inflate.findViewById(2131428452).setLayoutParams(new LinearLayout.LayoutParams(getPaddedPickerWidth(context), -2));
        } else {
            inflate.findViewById(2131428453).setLayoutParams(new LinearLayout.LayoutParams(getPaddedPickerWidth(context), -1));
        }
        TextView textView = (TextView) inflate.findViewById(2131428464);
        this.headerSelectionText = textView;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api19Impl.setAccessibilityLiveRegion(textView, 1);
        this.headerToggleButton = (CheckableImageButton) inflate.findViewById(2131428466);
        TextView textView2 = (TextView) inflate.findViewById(2131428470);
        CharSequence charSequence = this.titleText;
        if (charSequence != null) {
            textView2.setText(charSequence);
        } else {
            textView2.setText(this.titleTextResId);
        }
        this.headerToggleButton.setTag("TOGGLE_BUTTON_TAG");
        CheckableImageButton checkableImageButton = this.headerToggleButton;
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842912}, AppCompatResources.getDrawable(context, 2131232433));
        stateListDrawable.addState(new int[0], AppCompatResources.getDrawable(context, 2131232435));
        checkableImageButton.setImageDrawable(stateListDrawable);
        CheckableImageButton checkableImageButton2 = this.headerToggleButton;
        if (this.inputMode != 0) {
            z = true;
        } else {
            z = false;
        }
        checkableImageButton2.setChecked(z);
        ViewCompat.setAccessibilityDelegate(this.headerToggleButton, null);
        updateToggleContentDescription(this.headerToggleButton);
        this.headerToggleButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialDatePicker.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
                materialDatePicker.confirmButton.setEnabled(materialDatePicker.getDateSelector().isSelectionComplete());
                MaterialDatePicker.this.headerToggleButton.toggle();
                MaterialDatePicker materialDatePicker2 = MaterialDatePicker.this;
                materialDatePicker2.updateToggleContentDescription(materialDatePicker2.headerToggleButton);
                MaterialDatePicker.this.startPickerFragment();
            }
        });
        this.confirmButton = (Button) inflate.findViewById(2131427730);
        if (getDateSelector().isSelectionComplete()) {
            this.confirmButton.setEnabled(true);
        } else {
            this.confirmButton.setEnabled(false);
        }
        this.confirmButton.setTag("CONFIRM_BUTTON_TAG");
        this.confirmButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialDatePicker.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Iterator<MaterialPickerOnPositiveButtonClickListener<? super S>> it = MaterialDatePicker.this.onPositiveButtonClickListeners.iterator();
                while (it.hasNext()) {
                    MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
                    Objects.requireNonNull(materialDatePicker);
                    materialDatePicker.getDateSelector().getSelection();
                    it.next().onPositiveButtonClick();
                }
                MaterialDatePicker materialDatePicker2 = MaterialDatePicker.this;
                Objects.requireNonNull(materialDatePicker2);
                materialDatePicker2.dismissInternal(false, false);
            }
        });
        Button button = (Button) inflate.findViewById(2131427661);
        button.setTag("CANCEL_BUTTON_TAG");
        button.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialDatePicker.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Iterator<View.OnClickListener> it = MaterialDatePicker.this.onNegativeButtonClickListeners.iterator();
                while (it.hasNext()) {
                    it.next().onClick(view);
                }
                MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
                Objects.requireNonNull(materialDatePicker);
                materialDatePicker.dismissInternal(false, false);
            }
        });
        return inflate;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnDismissListener> it = this.onDismissListeners.iterator();
        while (it.hasNext()) {
            it.next().onDismiss(dialogInterface);
        }
        ViewGroup viewGroup = (ViewGroup) this.mView;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        super.onDismiss(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onStop() {
        PickerFragment<S> pickerFragment = this.pickerFragment;
        Objects.requireNonNull(pickerFragment);
        pickerFragment.onSelectionChangedListeners.clear();
        super.onStop();
    }

    public final void updateToggleContentDescription(CheckableImageButton checkableImageButton) {
        String str;
        if (this.headerToggleButton.isChecked()) {
            str = checkableImageButton.getContext().getString(2131952856);
        } else {
            str = checkableImageButton.getContext().getString(2131952858);
        }
        this.headerToggleButton.setContentDescription(str);
    }

    public static int getPaddedPickerWidth(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(2131166432);
        int i = new Month(UtcDates.getTodayCalendar()).daysInWeek;
        int dimensionPixelSize = resources.getDimensionPixelSize(2131166438) * i;
        return ((i - 1) * resources.getDimensionPixelOffset(2131166452)) + dimensionPixelSize + (dimensionPixelOffset * 2);
    }

    public static boolean isFullscreen(Context context) {
        return readMaterialCalendarStyleBoolean(context, 16843277);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = this.mArguments;
        }
        this.overrideThemeResId = bundle.getInt("OVERRIDE_THEME_RES_ID");
        this.dateSelector = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.calendarConstraints = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.titleTextResId = bundle.getInt("TITLE_TEXT_RES_ID_KEY");
        this.titleText = bundle.getCharSequence("TITLE_TEXT_KEY");
        this.inputMode = bundle.getInt("INPUT_MODE_KEY");
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        Month month;
        super.onSaveInstanceState(bundle);
        bundle.putInt("OVERRIDE_THEME_RES_ID", this.overrideThemeResId);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
        CalendarConstraints.Builder builder = new CalendarConstraints.Builder(this.calendarConstraints);
        MaterialCalendar<S> materialCalendar = this.calendar;
        Objects.requireNonNull(materialCalendar);
        if (materialCalendar.current != null) {
            MaterialCalendar<S> materialCalendar2 = this.calendar;
            Objects.requireNonNull(materialCalendar2);
            builder.openAt = Long.valueOf(materialCalendar2.current.timeInMillis);
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("DEEP_COPY_VALIDATOR_KEY", builder.validator);
        Month create = Month.create(builder.start);
        Month create2 = Month.create(builder.end);
        CalendarConstraints.DateValidator dateValidator = (CalendarConstraints.DateValidator) bundle2.getParcelable("DEEP_COPY_VALIDATOR_KEY");
        Long l = builder.openAt;
        if (l == null) {
            month = null;
        } else {
            month = Month.create(l.longValue());
        }
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", new CalendarConstraints(create, create2, dateValidator, month));
        bundle.putInt("TITLE_TEXT_RES_ID_KEY", this.titleTextResId);
        bundle.putCharSequence("TITLE_TEXT_KEY", this.titleText);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onStart() {
        super.onStart();
        Window window = requireDialog().getWindow();
        if (this.fullscreen) {
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(this.background);
        } else {
            window.setLayout(-2, -2);
            int dimensionPixelOffset = requireContext().getResources().getDimensionPixelOffset(2131166440);
            Rect rect = new Rect(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            window.setBackgroundDrawable(new InsetDrawable((Drawable) this.background, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset));
            window.getDecorView().setOnTouchListener(new InsetDialogOnTouchListener(requireDialog(), rect));
        }
        startPickerFragment();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.material.datepicker.MaterialDatePicker$3] */
    public final void startPickerFragment() {
        PickerFragment<S> pickerFragment;
        Context requireContext = requireContext();
        int i = this.overrideThemeResId;
        if (i == 0) {
            i = getDateSelector().getDefaultThemeResId(requireContext);
        }
        DateSelector<S> dateSelector = getDateSelector();
        CalendarConstraints calendarConstraints = this.calendarConstraints;
        MaterialCalendar<S> materialCalendar = new MaterialCalendar<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i);
        bundle.putParcelable("GRID_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        Objects.requireNonNull(calendarConstraints);
        bundle.putParcelable("CURRENT_MONTH_KEY", calendarConstraints.openAt);
        materialCalendar.setArguments(bundle);
        this.calendar = materialCalendar;
        if (this.headerToggleButton.isChecked()) {
            DateSelector<S> dateSelector2 = getDateSelector();
            CalendarConstraints calendarConstraints2 = this.calendarConstraints;
            pickerFragment = new MaterialTextInputPicker<>();
            Bundle bundle2 = new Bundle();
            bundle2.putInt("THEME_RES_ID_KEY", i);
            bundle2.putParcelable("DATE_SELECTOR_KEY", dateSelector2);
            bundle2.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints2);
            pickerFragment.setArguments(bundle2);
        } else {
            pickerFragment = this.calendar;
        }
        this.pickerFragment = pickerFragment;
        updateHeader();
        FragmentManager childFragmentManager = getChildFragmentManager();
        Objects.requireNonNull(childFragmentManager);
        BackStackRecord backStackRecord = new BackStackRecord(childFragmentManager);
        backStackRecord.doAddOp(2131428452, this.pickerFragment, null, 2);
        if (!backStackRecord.mAddToBackStack) {
            backStackRecord.mManager.execSingleAction(backStackRecord, false);
            this.pickerFragment.addOnSelectionChangedListener(new OnSelectionChangedListener<S>() { // from class: com.google.android.material.datepicker.MaterialDatePicker.3
                @Override // com.google.android.material.datepicker.OnSelectionChangedListener
                public final void onIncompleteSelectionChanged() {
                    MaterialDatePicker.this.confirmButton.setEnabled(false);
                }

                @Override // com.google.android.material.datepicker.OnSelectionChangedListener
                public final void onSelectionChanged(S s) {
                    MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
                    int i2 = MaterialDatePicker.$r8$clinit;
                    materialDatePicker.updateHeader();
                    MaterialDatePicker materialDatePicker2 = MaterialDatePicker.this;
                    materialDatePicker2.confirmButton.setEnabled(materialDatePicker2.getDateSelector().isSelectionComplete());
                }
            });
            return;
        }
        throw new IllegalStateException("This transaction is already being added to the back stack");
    }

    public final void updateHeader() {
        String selectionDisplayString = getDateSelector().getSelectionDisplayString(getContext());
        this.headerSelectionText.setContentDescription(String.format(requireContext().getResources().getString(2131952831), selectionDisplayString));
        this.headerSelectionText.setText(selectionDisplayString);
    }
}
