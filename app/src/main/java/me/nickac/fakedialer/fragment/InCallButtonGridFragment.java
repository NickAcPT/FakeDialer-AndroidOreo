package me.nickac.fakedialer.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.nickac.fakedialer.R;
import me.nickac.fakedialer.model.CallScreenButton;
import me.nickac.fakedialer.views.CheckableLabeledButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class InCallButtonGridFragment extends Fragment {

    private CheckableLabeledButton[] buttons = new CheckableLabeledButton[6];
    private InCallVoiceFragment mEventHandler;

    public InCallButtonGridFragment() {
        // Required empty public constructor
    }

    public CheckableLabeledButton getButton(int number) {
        return buttons[number];
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.incall_button_grid, container, false);

        buttons[0] = view.findViewById(R.id.incall_first_button);
        buttons[1] = view.findViewById(R.id.incall_second_button);
        buttons[2] = view.findViewById(R.id.incall_third_button);
        buttons[3] = view.findViewById(R.id.incall_fourth_button);
        buttons[4] = view.findViewById(R.id.incall_fifth_button);

        prepareButtons();

        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return view;
    }

    private void prepareButtons() {
        CallScreenButton[] values = CallScreenButton.values();
        for (int i = 0; i < values.length; i++) {
            CallScreenButton callButton = values[i];

            CheckableLabeledButton btn = getButton(i);
            updateButton(btn, callButton);
            if (mEventHandler != null) {
                btn.setOnCheckedChangeListener((v, isChecked) -> handleButtonClick(v, callButton));
            }
        }
    }

    private void handleButtonClick(CheckableLabeledButton btn, CallScreenButton callButton) {
        mEventHandler.onButtonPress(btn, callButton);
        updateButton(btn, callButton);
    }

    private void updateButton(CheckableLabeledButton btn, CallScreenButton callButton) {
        btn.setIconDrawable(callButton.getIcon());
        btn.setVisibility(View.VISIBLE);
        btn.setLabelText(callButton.getLabel());
        updateContentDescription(btn, callButton);
    }

    private void updateContentDescription(CheckableLabeledButton btn, CallScreenButton callButton) {
        int resId = btn.isChecked() ? callButton.getContentDescriptionChecked() : callButton.getContentDescriptionUnchecked();
        if (resId != 0)
            btn.setContentDescription(getString(resId));
    }

    public InCallButtonGridFragment withCallVoiceFragment(InCallVoiceFragment fragment) {
        mEventHandler = fragment;
        return this;
    }
}
