package br.com.irweb.ajshf.View;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;

/**
 * Created by Igor on 22/07/2017.
 */

public class HtmlTextView extends android.support.v7.widget.AppCompatTextView {

    public HtmlTextView(Context context) {
        super(context);
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setText(Html.fromHtml(getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            setText(Html.fromHtml(getText().toString()));
        }
    }
}
