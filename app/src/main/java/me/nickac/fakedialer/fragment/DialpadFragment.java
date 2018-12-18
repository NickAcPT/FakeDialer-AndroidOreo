package me.nickac.fakedialer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.Map;
import java.util.logging.Logger;

import me.nickac.fakedialer.R;
import me.nickac.fakedialer.utils.DTMFUtils;
import me.nickac.fakedialer.views.dialpad.DialpadKeyButton;
import me.nickac.fakedialer.views.dialpad.DialpadView;
import me.nickac.fakedialer.views.dialpad.DtmfKeyListener;

public class DialpadFragment extends Fragment implements View.OnKeyListener, DialpadKeyButton.OnPressedListener {

    /**
     * Hash Map to map a view id to a character
     */
    private static final Map<Integer, Character> displayMap = new ArrayMap<>();

    /** Set up the static maps */
    static {
        // Map the buttons to the display characters
        displayMap.put(R.id.one, '1');
        displayMap.put(R.id.two, '2');
        displayMap.put(R.id.three, '3');
        displayMap.put(R.id.four, '4');
        displayMap.put(R.id.five, '5');
        displayMap.put(R.id.six, '6');
        displayMap.put(R.id.seven, '7');
        displayMap.put(R.id.eight, '8');
        displayMap.put(R.id.nine, '9');
        displayMap.put(R.id.zero, '0');
        displayMap.put(R.id.pound, '#');
        displayMap.put(R.id.star, '*');
    }

    private final int[] buttonIds =
            new int[]{
                    R.id.zero,
                    R.id.one,
                    R.id.two,
                    R.id.three,
                    R.id.four,
                    R.id.five,
                    R.id.six,
                    R.id.seven,
                    R.id.eight,
                    R.id.nine,
                    R.id.star,
                    R.id.pound
            };

    private DialpadView dialpadView;
    private EditText dtmfDialerField;
    private DtmfKeyListener dtmfKeyListener;
    private OnFragmentEventListener mHandler;
    private boolean digitsCanBeEdited;

    public DialpadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Dialer_ThemeBase);
        LayoutInflater layoutInflater = inflater.cloneInContext(contextThemeWrapper);
        final View parent = layoutInflater.inflate(R.layout.incall_dialpad_fragment, container, false);
        dialpadView = parent.findViewById(R.id.dialpad_view);
        dialpadView.setCanDigitsBeEdited(digitsCanBeEdited);
        dialpadView.setBackgroundResource(R.color.incall_dialpad_background);
        dtmfDialerField = parent.findViewById(R.id.digits);
        if (dtmfDialerField != null) {
            dtmfKeyListener = new DtmfKeyListener();
            dtmfDialerField.setKeyListener(dtmfKeyListener);
            // remove the long-press context menus that support
            // the edit (copy / paste / select) functions.
            dtmfDialerField.setLongClickable(false);
            dtmfDialerField.setElegantTextHeight(false);
            configureKeypadListeners();
        }
        View backButton = dialpadView.findViewById(R.id.dialpad_back);
        backButton.setVisibility(digitsCanBeEdited ? View.GONE : View.VISIBLE);
        backButton.setOnClickListener(this::onDialpadBackButtonClick);

        return parent;
    }

    private void configureKeypadListeners() {
        DialpadKeyButton dialpadKey;
        for (int buttonId : buttonIds) {
            dialpadKey = dialpadView.findViewById(buttonId);
            dialpadKey.setOnKeyListener(this);
            dialpadKey.setOnPressedListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentEventListener) {
            mHandler = (OnFragmentEventListener) context;
        } else {
            throw new IllegalStateException(context.getClass().getName() + " doesn't implement the listener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler = null;
    }

    public void animateShowDialpad() {
        View view = getView();
        if (view == null) return;

        final DialpadView dialpadView = view.findViewById(R.id.dialpad_view);
        dialpadView.animateShow();
    }

    private void onDialpadBackButtonClick(View v) {
        if (mHandler != null) {
            mHandler.closeDialpad(v);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d("DialpadFragment", "onKey:  keyCode " + keyCode + ", view " + v);

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            int viewId = v.getId();
            if (displayMap.containsKey(viewId)) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        if (event.getRepeatCount() == 0) {
                            processDtmf(displayMap.get(viewId));
                        }
                        break;
                    case KeyEvent.ACTION_UP:
                        stopDtmf();
                        break;
                    default: // fall out
                }
                // do not return true [handled] here, since we want the
                // press / click animation to be handled by the framework.
            }
        }
        return false;
    }

    @Override
    public void onPressed(View view, boolean pressed) {
        if (pressed && displayMap.containsKey(view.getId())) {
            Log.d("DialpadFragment", "onPressed: " + pressed + " " + displayMap.get(view.getId()));
            processDtmf(displayMap.get(view.getId()));
        }
        if (!pressed) {
            Log.d("DialpadFragment", "onPressed: " + pressed);
            stopDtmf();
        }
    }

    private void stopDtmf() {
        DTMFUtils.INSTANCE.stopTone();
    }

    private void processDtmf(Character c) {
        appendDigitsToField(c);
        DTMFUtils.INSTANCE.playTone(c);
    }

    public void appendDigitsToField(char digit) {
        if (dtmfDialerField != null) {
            dtmfDialerField.getText().append(digit);
        }
    }

    public void setCanDigitsBeEdited(boolean b) {
        digitsCanBeEdited = b;
    }


    /**
     * LinearLayout with getter and setter methods for the translationY property using floats, for
     * animation purposes.
     */
    public static class DialpadSlidingRelativeLayout extends RelativeLayout {

        public DialpadSlidingRelativeLayout(Context context) {
            super(context);
        }

        public DialpadSlidingRelativeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public DialpadSlidingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public float getYFraction() {
            final int height = getHeight();
            if (height == 0) {
                return 0;
            }
            return getTranslationY() / height;
        }

        public void setYFraction(float yFraction) {
            setTranslationY(yFraction * getHeight());
        }
    }

    public interface OnFragmentEventListener {

        void closeDialpad(View v);

    }

}
