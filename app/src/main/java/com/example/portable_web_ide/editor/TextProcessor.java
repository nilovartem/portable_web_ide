package com.example.portable_web_ide.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.portable_web_ide.R;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor extends androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView {

    private static final String APP_MODULE = "TextProcessor";
    //Входные данные
    private Pattern keyWords = Pattern.compile("\\b((a(bstract|nd|rray|s))|(c(a(llable|se|tch)|l(ass|one)|on(st|tinue)))|(d(e(clare|fault)|ie|o))|(e(cho|lse(if)?|mpty|nd(declare|for(each)?|if|switch|while)|val|x(it|tends)))|(f(inal|or(each)?|unction))|(g(lobal|oto))|(i(f|mplements|n(clude(_once)?|st(anceof|eadof)|terface)|sset))|(n(amespace|ew))|(p(r(i(nt|vate)|otected)|ublic))|(re(quire(_once)?|turn))|(s(tatic|witch))|(t(hrow|r(ait|y)))|(u(nset|se))|(__halt_compiler|break|list|(x)?or|var|while))\\b");

    private Paint paintText;


    private Paint paintIndent;

    //Фиксированный отступ
    private int indentPadding = 100;

    public TextProcessor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(textWatcher);
        init();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

           highlightSyntax(s);
        }
    };
    void processText(CharSequence text)
    {
        Log.i(APP_MODULE,"processText");
        removeTextChangedListener(textWatcher);
        setText(text);
        addTextChangedListener(textWatcher);
    }
    private void highlightSyntax(Editable sequence)
    {
       Log.i(APP_MODULE,getText().toString());
       Matcher matcher = keyWords.matcher(getText());

        matcher.region(0,sequence.length());
        while (matcher.find()) {
            sequence.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#7F0055")),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

    }
    public void init() {
        paintText = new Paint();
        paintText.setColor(Color.GRAY);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(35);

        paintIndent = new Paint();
        paintIndent.setColor(getResources().getColor(R.color.indentColor));
        paintText.setStyle(Paint.Style.FILL);

        this.setPadding(indentPadding,3,getPaddingRight(),0);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //Рисуем отступ
        Rect field = new Rect(0,getScrollY(),indentPadding,getScrollY()+getHeight());
        canvas.drawRect(field,paintIndent);

        int lineHeight = getLineHeight();
        int lineCount = getLineCount();


        for (int i = 0; i < lineCount; i++) {
            float pad = paintText.measureText(String.valueOf(i+1));
            canvas.drawText(String.valueOf(i + 1), indentPadding-pad-10, i * lineHeight + lineHeight, paintText);
        }


        //canvas.drawLine(100,getScrollY(),indentPadding,getScrollY()+getHeight(),paint );
        super.onDraw(canvas);
    }

}
