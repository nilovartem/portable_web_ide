package com.example.portable_web_ide.editor;

import android.widget.MultiAutoCompleteTextView;

public class TextTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    private final String Token = "!@#$%^&*()_+-={}|[]:;'<>/<.? \r\n\t";

    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i>0 && !Token.contains(Character.toString(text.charAt(i-1))))
        {
            i--;
        }
        while (i<cursor && text.charAt(i) == ' ')
        {
            i++;
        }
        return i;

    }

    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        while (i<text.length())
        {
            if(Token.contains(Character.toString(text.charAt(i-1))))
            {
                return i;
            }
            else
            {
                i++;
            }
        }
        return text.length();
    }

    @Override
    public CharSequence terminateToken(CharSequence text) {
        return text;
    }
}
