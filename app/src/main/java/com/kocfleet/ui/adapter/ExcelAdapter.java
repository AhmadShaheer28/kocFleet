package com.kocfleet.ui.adapter;

import android.annotation.SuppressLint;
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
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.ui.RowClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import top.defaults.drawabletoolbox.DrawableBuilder;


public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, ExcelCellModel>, BaseViewHolder> {
    private RowClickListener delegate;
    List<Map<Integer, ExcelCellModel>> data;
    private int isColumnClick;
    private String regDate = "[0-9]{2}-[0-9]{2}-[0-9]{2}";

    private int selectedColor = 0;
    private String[] colors = new String[]{"#eefdec", "#c7c7c7", "#f0b099", "#afb3e9"};
    private String[] hColors = new String[]{"#f0b099", "#afb3e9"};
    private String mColor = colors[selectedColor];
    private String mHColor = hColors[selectedColor];

    public ExcelAdapter(@Nullable List<Map<Integer, ExcelCellModel>> data, RowClickListener delegate, int isColumnClick) {
        super(R.layout.template1_item, data);
        this.delegate = delegate;
        this.data = data;
        this.isColumnClick = isColumnClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Map<Integer, ExcelCellModel> item) {
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
                textView.setText(item.get(0).getValue());
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setPadding(10, 10, 10, 10);
                textView.setBackground(getBackgroundDrawable("#ffffff"));
                view.addView(textView);
            } else {
                for (int i = 0; i < item.size(); i++) {
                    TextView textView = new TextView(mContext);
                    textView.setTextSize(13);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    textView.setText(item.get(i).getValue());
                    textView.setLayoutParams(layoutParams);
                    textView.setPadding(10, 10, 10, 10);
                    if (item.get(i).getValue().toLowerCase().equals("in commision") || item.get(i).getValue().equals("Ok") || item.get(i).getValue().equals("OK"))
                        textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                    else if (item.get(i).getValue().toLowerCase().equals("out of commision") || item.get(i).getValue().equals("Not Ok"))
                        textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                    else
                        textView.setBackground(getBackgroundDrawable(item.get(i).getColor()));

                    if (helper.getLayoutPosition() == 2) {
                        int finalI = i;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delegate.onColumnCLicked(finalI);
                            }
                        });
                    }
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delegate.onRowClicked(item);
                        }
                    });

                    if (Objects.requireNonNull(item.get(i)).getValue().matches(regDate)) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(item.get(i).getValue()));
                        } catch (ParseException e) {
                        }
                        if (matchDates(date) < 1) {
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
                textView.setText(item.get(i).getValue());
                textView.setLayoutParams(layoutParams);
                textView.setPadding(10, 10, 10, 10);
                if (item.get(i).getValue().toUpperCase().equals("IN COMMISION") || item.get(i).getValue().toUpperCase().equals("OK"))
                    textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                else if (item.get(i).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(i).getValue().toUpperCase().equals("NOT OK"))
                    textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                else
                    textView.setBackground(getBackgroundDrawable(item.get(i).getColor()));

                view.addView(textView);
            }
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setText(item.get(isColumnClick) + "");
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            if (item.get(isColumnClick).getValue().toUpperCase().equals("IN COMMISION") || item.get(isColumnClick).getValue().toUpperCase().equals("OK"))
                textView.setBackground(getBackgroundDrawable("#FF00FF00"));
            else if (item.get(isColumnClick).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(isColumnClick).getValue().toUpperCase().equals("NOT OK"))
                textView.setBackground(getBackgroundDrawable("#FFFF0000"));
            else
                textView.setBackground(getBackgroundDrawable("#FFFFFF"));
            if (Objects.requireNonNull(item.get(isColumnClick)).getValue().matches(regDate)) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                Date date = null;
                try {
                    date = formatter.parse(Objects.requireNonNull(item.get(isColumnClick).getValue()));
                } catch (ParseException e) {
                }
                if (matchDates(date) < 1) {
                    textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                }
            }
            view.addView(textView);
        }

    }

    private int matchDates(Date date) {
        Date currentDate = new Date();
        Calendar calDate = Calendar.getInstance();
        Calendar calCurrDate = Calendar.getInstance();
        calDate.setTime(date);
        calCurrDate.setTime(currentDate);

        int m1 = calCurrDate.get(Calendar.YEAR) * 12 + calCurrDate.get(Calendar.MONTH);
        int m2 = calDate.get(Calendar.YEAR) * 12 + calDate.get(Calendar.MONTH);

        return m2 - m1;
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
