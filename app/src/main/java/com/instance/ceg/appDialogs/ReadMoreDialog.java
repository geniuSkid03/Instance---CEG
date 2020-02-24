package com.instance.ceg.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instance.ceg.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class ReadMoreDialog extends Dialog {

    private TextView readMoreTv;

    public ReadMoreDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.circular_read_more_item, null);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view, layoutParams);

        readMoreTv = view.findViewById(R.id.read_more_txt);
        ImageView closeBtn = view.findViewById(R.id.read_more_close);

        closeBtn.setOnClickListener(view1 -> dismiss());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);

        setCancelable(false);
    }

    public void setAndShow(String description) {
        readMoreTv.setText(description);
        show();
    }
}
