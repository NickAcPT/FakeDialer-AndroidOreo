/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.nickac.fakedialer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import me.nickac.fakedialer.R;

/**
 * EditText which resizes dynamically with respect to text length.
 */
@SuppressLint("AppCompatCustomView")
public class ResizingTextEditText extends EditText {

    private final int originalTextSize;
    private final int minTextSize;

    public ResizingTextEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        originalTextSize = (int) getTextSize();
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ResizingText);
        minTextSize =
                (int) a.getDimension(R.styleable.ResizingText_resizing_text_min_size, originalTextSize);
        a.recycle();
    }

    public void resizeText(int originalTextSize, int minTextSize) {
        final Paint paint = this.getPaint();
        final int width = this.getWidth();
        if (width == 0) {
            return;
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
        float ratio = width / paint.measureText(this.getText().toString());
        if (ratio <= 1.0f) {
            this.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, Math.max(minTextSize, originalTextSize * ratio));
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        resizeText(originalTextSize, minTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resizeText(originalTextSize, minTextSize);
    }

}
