package com.kocfleet.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
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
import com.kocfleet.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import top.defaults.drawabletoolbox.DrawableBuilder;


public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, ExcelCellModel>, BaseViewHolder> {
    private final RowClickListener delegate;
    List<Map<Integer, ExcelCellModel>> data;
    private final int isColumnClick;
    private final String regDate = "[0-9]{2}-[0-9]{2}-[0-9]{2}";
    private int selectedColor = 0;
    private final String[] colors = new String[]{"#eefdec", "#c7c7c7", "#f0b099", "#afb3e9"};
    private final String filename;

    public ExcelAdapter(@Nullable List<Map<Integer, ExcelCellModel>> data, RowClickListener delegate, int isColumnClick, String filename) {
        super(R.layout.template1_item, data);
        this.delegate = delegate;
        this.data = data;
        this.isColumnClick = isColumnClick;
        this.filename = filename;
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
                textView.setText(Objects.requireNonNull(item.get(0)).getValue());
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
                    textView.setText(Objects.requireNonNull(item.get(i)).getValue());
                    textView.setLayoutParams(layoutParams);
                    textView.setPadding(10, 10, 10, 10);
                    if (Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("in commission")
                            || Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("OK"))
                        textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                    else if (Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("out of commission")
                            || Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("not ok")
                            || Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("empty" ))
                        textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                    else
                        textView.setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(i)).getColor()));

                    if(filename != null && filename.equals(Constants.CERTIFICATES)) {
                        if(helper.getLayoutPosition() == 1) {
                            int finalI = i;
                            textView.setOnClickListener(view1 -> delegate.onColumnCLicked(finalI));
                            textView.setBackground(getBackgroundDrawable(setColor(i)));
                        }
                        if(i == 2 && helper.getLayoutPosition() != 1)
                            textView.setBackground(getBackgroundDrawable("#FFFFFF"));
                    } else {
                        if (helper.getLayoutPosition() == 2) {
                            int finalI = i;
                            textView.setOnClickListener(view12 -> delegate.onColumnCLicked(finalI));
                        }
                    }


                    helper.itemView.setOnClickListener(view13 -> delegate.onRowClicked(item));

                    if (Objects.requireNonNull(item.get(i)).getValue().matches(regDate)) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(item.get(i)).getValue());
                        } catch (ParseException ignored) {
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
                if(filename.equals(Constants.CERTIFICATES) && i == 2)
                    break;
                TextView textView = new TextView(mContext);
                textView.setTextSize(13);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                textView.setText(Objects.requireNonNull(item.get(i)).getValue());
                textView.setLayoutParams(layoutParams);
                textView.setPadding(10, 10, 10, 10);
                if (Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("IN COMMISSION")
                        || Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("OK"))
                    textView.setBackground(getBackgroundDrawable("#FF00FF00"));
                else if (Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("OUT OF COMMISSION")
                        || Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("NOT OK")
                        || Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("empty" ))
                    textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                else
                    textView.setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(i)).getColor()));

                view.addView(textView);
            }
            if(filename.equals(Constants.CONDITION)) {
                setAdditionalRow(item, view);
            }
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setText(Objects.requireNonNull(item.get(isColumnClick)).getValue());
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            if (Objects.requireNonNull(item.get(isColumnClick)).getValue().toUpperCase().equals("IN COMMISSION")
                    || Objects.requireNonNull(item.get(isColumnClick)).getValue().toUpperCase().equals("OK"))
                textView.setBackground(getBackgroundDrawable("#FF00FF00"));
            else if (Objects.requireNonNull(item.get(isColumnClick)).getValue().toUpperCase().equals("OUT OF COMMISSION")
                    || Objects.requireNonNull(item.get(isColumnClick)).getValue().toUpperCase().equals("NOT OK")
                    || Objects.requireNonNull(item.get(isColumnClick)).getValue().toLowerCase().equals("empty" ))
                textView.setBackground(getBackgroundDrawable("#FFFF0000"));
            else
                textView.setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(isColumnClick)).getColor()));
            if (Objects.requireNonNull(item.get(isColumnClick)).getValue().matches(regDate)) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                Date date = null;
                try {
                    date = formatter.parse(Objects.requireNonNull(Objects.requireNonNull(item.get(isColumnClick)).getValue()));
                } catch (ParseException ignored) {
                }
                if (matchDates(date) < 1) {
                    textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                }
            }
            view.addView(textView);
        }

    }

    private void setAdditionalRow(Map<Integer, ExcelCellModel> item, LinearLayout view) {
        int additionalRow;
        if(isColumnClick == 4 || isColumnClick == 5) {
            if(isColumnClick == 4)
                additionalRow = 5;
            else
                additionalRow = 4;
            addAdditionalRow(additionalRow, item, view);
        }
    }

    private void addAdditionalRow(int additionalRow, Map<Integer, ExcelCellModel> item, LinearLayout view) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(mContext);
        textView.setTextSize(13);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        textView.setText(Objects.requireNonNull(item.get(additionalRow)).getValue());
        textView.setLayoutParams(layoutParams);
        textView.setPadding(10, 10, 10, 10);
        if (Objects.requireNonNull(item.get(additionalRow)).getValue().toUpperCase().equals("IN COMMISSION")
                || Objects.requireNonNull(item.get(additionalRow)).getValue().toUpperCase().equals("OK"))
            textView.setBackground(getBackgroundDrawable("#FF00FF00"));
        else if (Objects.requireNonNull(item.get(additionalRow)).getValue().toUpperCase().equals("OUT OF COMMISSION")
                || Objects.requireNonNull(item.get(additionalRow)).getValue().toUpperCase().equals("NOT OK")
                || Objects.requireNonNull(item.get(additionalRow)).getValue().toLowerCase().equals("empty" ))
            textView.setBackground(getBackgroundDrawable("#FFFF0000"));
        else
            textView.setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(additionalRow)).getColor()));
        if (Objects.requireNonNull(item.get(additionalRow)).getValue().matches(regDate)) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            Date date = null;
            try {
                date = formatter.parse(Objects.requireNonNull(Objects.requireNonNull(item.get(additionalRow)).getValue()));
            } catch (ParseException ignored) {
            }
            if (matchDates(date) < 1) {
                textView.setBackground(getBackgroundDrawable("#FFFF0000"));
            }
        }
        view.addView(textView);
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

    private String setColor(int j) {
        if(j%2 == 0)
            selectedColor += 1;
        return colors[selectedColor % 4];
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
