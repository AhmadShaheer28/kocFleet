package com.kocfleet.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kocfleet.R;
import com.kocfleet.ui.RowClickListener;

import java.util.List;
import java.util.Map;

import top.defaults.drawabletoolbox.DrawableBuilder;


public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, String>, BaseViewHolder> {
    private RowClickListener delegate;
    List<Map<Integer, String>> data;
    private int isColumnClick;

    public ExcelAdapter(@Nullable List<Map<Integer, String>> data, RowClickListener delegate, int isColumnClick) {
        super(R.layout.template1_item, data);
        this.delegate = delegate;
        this.data = data;
        this.isColumnClick = isColumnClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Map<Integer, String> item) {
        LinearLayout view = (LinearLayout) helper.itemView;
        view.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.MATCH_PARENT);

        if (isColumnClick == -1) {
            if (helper.getLayoutPosition() == 0) {
                TextView textView = new TextView(mContext);
                textView.setTextSize(13);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setText(item.get(0) + "");
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setPadding(10, 10, 10, 10);
                //textView.setBackground(getBackgroundDrawable(item.get(0).getColor()));
                view.addView(textView);
            } else {
                for (int i = 0; i < item.size(); i++) {
                    TextView textView = new TextView(mContext);
                    textView.setTextSize(13);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    textView.setText(item.get(i) + "");
                    textView.setLayoutParams(layoutParams);
                    textView.setPadding(10, 10, 10, 10);
                    if (item.get(i).toLowerCase().equals("in commision") || item.get(i).equals("Ok") || item.get(i).equals("OK"))
                        textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                    else if (item.get(i).toLowerCase().equals("out of commision") || item.get(i).equals("Not Ok"))
                        textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                    else
                        textView.setBackground(getBackgroundDrawable("#FFFFFF"));
                    view.addView(textView);
                    if (helper.getLayoutPosition() == 2) {
                        int finalI = i;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delegate.onColumnCLicked(finalI);
                            }
                        });
                    }
                    if (i == 0) {
                        helper.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delegate.onRowClicked(item);
                            }
                        });
                    }
                }
            }


        } else {

            for (int i = 0; i < 3; i++) {
                TextView textView = new TextView(mContext);
                textView.setTextSize(13);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                textView.setText(item.get(i) + "");
                textView.setLayoutParams(layoutParams);
                textView.setPadding(10, 10, 10, 10);
                if (item.get(i).toUpperCase().equals("IN COMMISION") || item.get(i).toUpperCase().equals("OK"))
                    textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                else if (item.get(i).toUpperCase().equals("OUT OF COMMISION") || item.get(i).toUpperCase().equals("NOT OK"))
                    textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                else
                    textView.setBackground(getBackgroundDrawable("#FFFFFF"));
                view.addView(textView);
            }
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setText(item.get(isColumnClick) + "");
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            if (item.get(isColumnClick).toUpperCase().equals("IN COMMISION") || item.get(isColumnClick).toUpperCase().equals("OK"))
                textView.setBackground(getBackgroundDrawable("#FF00FF00"));
            else if (item.get(isColumnClick).toUpperCase().equals("OUT OF COMMISION") || item.get(isColumnClick).toUpperCase().equals("NOT OK"))
                textView.setBackground(getBackgroundDrawable("#FFFF0000"));
            else
                textView.setBackground(getBackgroundDrawable("#FFFFFF"));
                view.addView(textView);
        }

    }

    private Drawable getBackgroundDrawable(String color) {
        Drawable drawable;
        drawable = new DrawableBuilder()
                .rectangle()
                .hairlineBordered()
                .solidColor(Color.parseColor(color))
                .strokeColor(ContextCompat.getColor(mContext, R.color.grey_level_1))
                .build();

        return drawable;
    }
}
