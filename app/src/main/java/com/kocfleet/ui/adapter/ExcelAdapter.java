package com.kocfleet.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kocfleet.R;
import com.kocfleet.ui.RowClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import top.defaults.drawabletoolbox.DrawableBuilder;


public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, String>, BaseViewHolder> {
    private RowClickListener delegate;
    List<Map<Integer, String>> data;
    private int isColumnClick;
    private String regDate = "[a-zA-Z]*-[0-9]{2}";

    private int selectedColor = 0;
    private String[] colors = new String[]{ "#eefdec", "#c7c7c7", "#f0b099", "#afb3e9" };
    private String[] hColors = new String[]{ "#f0b099", "#afb3e9" };
    private String mColor = colors[selectedColor];
    private String mHColor = hColors[selectedColor];

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
                textView.setBackground(getBackgroundDrawable("#FFFFFF"));
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
                        textView.setBackground(getBackgroundDrawable("#cbf7c7"));
                        helper.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delegate.onRowClicked(item);
                            }
                        });
                    }
                    if(helper.getLayoutPosition() == 2) {
                        if(i != 0) {
                            textView.setBackground(getBackgroundDrawable("#4169E1"));
                        }
                    }
                    if(i == 1 || i == 2) {
                        if(!item.get(2).equals("") && i != 2) {
                            selectedColor = selectedColor + 1;
                        }
                        mColor = colors[selectedColor%4];
                        textView.setBackground(getBackgroundDrawable(mColor));
                    }
                    if(Objects.requireNonNull(item.get(i)).matches(regDate)) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd");
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(item.get(i)));
                        } catch (ParseException e) {
                        }
                        if(matchDates(date) < 1) {
                            textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                        }
                    }
                    view.addView(textView);
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

                if (i == 0) {
                    textView.setBackground(getBackgroundDrawable("#cbf7c7"));
                }
                if(helper.getLayoutPosition() == 2) {
                    if(i != 0) {
                        textView.setBackground(getBackgroundDrawable("#4169E1"));
                    }
                }
                if(i == 1 || i == 2) {
                    if(!item.get(2).equals("") && i != 2) {
                        selectedColor = selectedColor + 1;
                    }
                    mColor = colors[selectedColor%4];
                    textView.setBackground(getBackgroundDrawable(mColor));
                }
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

    private int matchDates(Date date) {
        Date currentDate = new Date();
        Calendar calDate = Calendar.getInstance();
        Calendar calCurrDate = Calendar.getInstance();
        calDate.setTime(date);
        calCurrDate.setTime(currentDate);

        int m1 = calDate.get(Calendar.MONTH) - calCurrDate.get(Calendar.MONTH);

        return m1;
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
