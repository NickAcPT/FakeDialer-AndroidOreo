package me.nickac.fakedialer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import me.nickac.fakedialer.utils.DialerBidiFormatter;

@SuppressLint("AppCompatCustomView")
public final class BidiTextView extends TextView {

    public BidiTextView(Context context) {
        super(context);
    }

    public BidiTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(DialerBidiFormatter.format(text), type);
    }
}
