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
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.ui.RowClickListener;

import java.util.List;
import java.util.Map;

import top.defaults.drawabletoolbox.DrawableBuilder;


public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, ExcelCellModel>, BaseViewHolder> {
    private RowClickListener delegate;
    List<Map<Integer, ExcelCellModel>> data;
    private int isColumnClick;

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
                textView.setText(item.get(0).getValue() + "");
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setPadding(10, 10, 10, 10);
                textView.setBackground(getBackgroundDrawable(item.get(0).getColor()));
                view.addView(textView);
            } else {
                for (int i = 0; i < item.size(); i++) {
                    TextView textView = new TextView(mContext);
                    textView.setTextSize(13);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    textView.setText(item.get(i).getValue() + "");
                    textView.setLayoutParams(layoutParams);
                    textView.setPadding(10, 10, 10, 10);
                    if (item.get(i).getValue().toUpperCase().equals("IN COMMISION") || item.get(i).getValue().toUpperCase().equals("OK"))
                        textView.setBackground(getBackgroundDrawable("FF00FF00"));
                    else if (item.get(i).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(i).getValue().toUpperCase().equals("NOT OK"))
                        textView.setBackground(getBackgroundDrawable("FFFF0000"));
                    else
                        textView.setBackground(getBackgroundDrawable(item.get(i).getColor()));
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
                }
            }

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delegate.onRowClicked(item);
                }
            });
        } else {
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setText(item.get(isColumnClick).getValue() + "");
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            if (item.get(isColumnClick).getValue().toUpperCase().equals("IN COMMISION") || item.get(isColumnClick).getValue().toUpperCase().equals("OK"))
                textView.setBackground(getBackgroundDrawable("FF00FF00"));
            else if (item.get(isColumnClick).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(isColumnClick).getValue().toUpperCase().equals("NOT OK"))
                textView.setBackground(getBackgroundDrawable("FFFF0000"));
            else
                textView.setBackground(getBackgroundDrawable(item.get(isColumnClick).getColor()));
            view.addView(textView);
        }

    }

    private Drawable getBackgroundDrawable(String color) {
        Drawable drawable;
        if (color != null) {
            drawable = new DrawableBuilder()
                    .rectangle()
                    .hairlineBordered()
                    .solidColor(Color.parseColor("#" + color))
                    .strokeColor(ContextCompat.getColor(mContext, R.color.grey_level_1))
                    .build();
        } else {
            drawable = new DrawableBuilder()
                    .rectangle()
                    .hairlineBordered()
                    .solidColor(ContextCompat.getColor(mContext, R.color.white))
                    .strokeColor(ContextCompat.getColor(mContext, R.color.grey_level_1))
                    .build();
        }

        return drawable;
    }
}
