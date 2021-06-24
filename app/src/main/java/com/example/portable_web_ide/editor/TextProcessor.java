package com.example.portable_web_ide.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Layout;
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

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor extends androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView {

    private static final String APP_MODULE = "TextProcessor";
    //Входные данные
    private Pattern keyWordsPHP = Pattern.compile("\\b((a(bstract|nd|rray|s))|(c(a(llable|se|tch)|l(ass|one)|on(st|tinue)))|(d(e(clare|fault)|ie|o))|(e(cho|lse(if)?|mpty|nd(declare|for(each)?|if|switch|while)|val|x(it|tends)))|(f(inal|or(each)?|unction))|(g(lobal|oto))|(i(f|mplements|n(clude(_once)?|st(anceof|eadof)|terface)|sset))|(n(amespace|ew))|(p(r(i(nt|vate)|otected)|ublic))|(re(quire(_once)?|turn))|(s(tatic|witch))|(t(hrow|r(ait|y)))|(u(nset|se))|(__halt_compiler|break|list|(x)?or|var|while))\\b");
    private Pattern keyWordsHTML = Pattern.compile("<.+?>");
    private Pattern keyWordsCSS = Pattern.compile("<style>.+?>");
    private CharSequence enteredText;


    // TODO: подсветка выбранного цвета
    // TODO: подстветка особых ключевых слов
    // TODO: дополнение текста

    private Paint paintText;

    private Paint paintIndent;

    //Фиксированный отступ
    private int indentPadding = 100;

    public TextProcessor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(textWatcher);
        PromptArrayAdapter promptArrayAdapter = new PromptArrayAdapter(MyApp.get(), R.id.textArea);
        this.setAdapter(promptArrayAdapter);
        this.setTokenizer(new TextTokenizer());
        this.setThreshold(1);
        setDropDownAnchor(R.id.app_bar_layout);

        init();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setDropDownWidth(w * 1 / 2);
        setDropDownHeight(h * 1 / 2);

        //update highlight
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.i(APP_MODULE,s.toString());


            if (s.length() > 0) {
                StringBuilder stringBuilder = new StringBuilder(s.toString());
                int cursorPosition = getSelectionStart();
                CharSequence enteredText = getText().toString();
                CharSequence startToCursor = enteredText.subSequence(0, cursorPosition);
                if (startToCursor.length() > 0) {

                    Character character = startToCursor.charAt(startToCursor.length() - 1);

                    Log.i(APP_MODULE, "Символ перед курсором" + character);


                    //Log.i(APP_MODULE,"Последний символ" + c.toString());

                    if ((character) == '>') {
                        Log.i(APP_MODULE, "Скобка найдена");
                        Matcher matcher = null;

                        //ищем html теги
                        String lastTag = null;
                        matcher = keyWordsHTML.matcher(getText());
                        matcher.region(0, cursorPosition);

                        while (matcher.find()) {
                            lastTag = matcher.group(matcher.groupCount());

                        }
                    /*
                    int tagPosition = stringBuilder.lastIndexOf(lastTag);
                    if (tagPosition + lastTag.length() == stringBuilder.length()) {
                        Log.i(APP_MODULE, "Последний тэг " + lastTag);
                    }*/
                        if (!lastTag.contains("/")) {

                            String closingTag = "<" + "/" + lastTag.substring(1);
                            Log.i(APP_MODULE, "Закрывающий тэг" + closingTag);
                            getText().insert(cursorPosition, closingTag);
                            setSelection(cursorPosition);
                        }


                        //Log.i(APP_MODULE,"Последний тэг " + lastTag);

                    } else {
                        Log.i(APP_MODULE, "Скобка НЕ найдена");

                    }
                    onPopupChangePosition();
                }
            }


        }


        @Override
        public void afterTextChanged(Editable s) {

            highlightSyntax(s);


        }
    };

    private void onPopupChangePosition() {

        try {

            int pos = this.getSelectionStart();
            Layout layout = this.getLayout();

            int line = layout.getLineForOffset(pos);
            int baseline = layout.getLineBaseline(line);
            int ascent = layout.getLineAscent(line);

            float x = layout.getPrimaryHorizontal(pos);

            float y = layout.getLineBottom(line);
            //float y = layout.getLineBaseline(line);
            //layout.getLineAscent(line);

            //Log.i(APP_MODULE,"Значение Y" + String.valueOf(y));

            setDropDownHorizontalOffset(((int) x));
            setDropDownVerticalOffset((int) y);
        } catch (Exception e) {

        }
    }

    void processText(CharSequence text) {
        //   Log.i(APP_MODULE,"processText");
        removeTextChangedListener(textWatcher);
        setText(text);
        addTextChangedListener(textWatcher);
    }

    private void highlightSyntax(Editable sequence) {

        //Log.i(APP_MODULE,getText().toString());
        Matcher matcher = null;

        //подсвечиваем теги HTML
        matcher = keyWordsHTML.matcher(getText());
        matcher.region(0, sequence.length());
        while (matcher.find()) {
            sequence.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#2E64FE")),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        //проверяем на наличие синтаксиса PHP
        matcher = keyWordsPHP.matcher(getText());
        matcher.region(0, sequence.length());
        while (matcher.find()) {
            sequence.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#7F0055")),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        //подсвечиваем теги CSS - не работает

        matcher = keyWordsCSS.matcher(getText());

        matcher.region(0, sequence.length());
        while (matcher.find()) {
            sequence.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#B40431")),
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

        this.setPadding(indentPadding, 3, getPaddingRight(), 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Рисуем отступ
        Rect field = new Rect(0, getScrollY(), indentPadding, getScrollY() + getHeight());
        canvas.drawRect(field, paintIndent);

        int lineHeight = getLineHeight();
        int lineCount = getLineCount();


        for (int i = 0; i < lineCount; i++) {
            float pad = paintText.measureText(String.valueOf(i + 1));
            canvas.drawText(String.valueOf(i + 1), indentPadding - pad - 10, i * lineHeight + lineHeight, paintText);
        }
        //canvas.drawLine(100,getScrollY(),indentPadding,getScrollY()+getHeight(),paint );
        super.onDraw(canvas);
    }

}
