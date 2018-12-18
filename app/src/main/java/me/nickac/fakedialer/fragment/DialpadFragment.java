package me.nickac.fakedialer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import me.nickac.fakedialer.R;
import me.nickac.fakedialer.views.dialpad.DialpadView;
import me.nickac.fakedialer.views.dialpad.DtmfKeyListener;

public class DialpadFragment extends Fragment {

    private DialpadView dialpadView;
    private EditText dtmfDialerField;
    private DtmfKeyListener dtmfKeyListener;
    private OnFragmentEventListener mHandler;

    public DialpadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Dialer_ThemeBase);
        LayoutInflater layoutInflater = inflater.cloneInContext(contextThemeWrapper);
        final View parent = layoutInflater.inflate(R.layout.incall_dialpad_fragment, container, false);
        dialpadView = parent.findViewById(R.id.dialpad_view);
        dialpadView.setCanDigitsBeEdited(false);
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
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this::onDialpadBackButtonClick);

        return parent;
    }

    private void configureKeypadListeners() {

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
