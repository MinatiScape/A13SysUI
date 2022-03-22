package com.google.android.material.datepicker;

import androidx.fragment.app.Fragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.LinkedHashSet;
/* loaded from: classes.dex */
public abstract class PickerFragment<S> extends Fragment {
    public final LinkedHashSet<OnSelectionChangedListener<S>> onSelectionChangedListeners = new LinkedHashSet<>();

    public boolean addOnSelectionChangedListener(MaterialDatePicker.AnonymousClass3 r1) {
        return this.onSelectionChangedListeners.add(r1);
    }
}
