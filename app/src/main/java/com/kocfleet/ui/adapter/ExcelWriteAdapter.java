package com.kocfleet.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kocfleet.R;
import com.kocfleet.model.ExcelCellModel;

import java.util.List;
import java.util.Map;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class ExcelWriteAdapter extends BaseQuickAdapter<Map<Integer, ExcelCellModel>, BaseViewHolder> {
    public ExcelWriteAdapter(@Nullable List<Map<Integer, ExcelCellModel>> data) {
        super(R.layout.template1_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Map<Integer, ExcelCellModel> item) {
        LinearLayout view = (LinearLayout) helper.itemView;
        view.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < item.size(); i++) {
            EditText textView = new EditText(mContext);
            textView.setTextSize(13);
            textView.setText(item.get(i).getValue() + "");
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            if(item.get(i).getValue().toUpperCase().equals("IN COMMISION") || item.get(i).getValue().toUpperCase().equals("OK"))
                textView.setBackground(getBackgroundDrawable("FF00FF00"));
            else if(item.get(i).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(i).getValue().toUpperCase().equals("NOT OK"))
                textView.setBackground(getBackgroundDrawable("FFFF0000"));
            else
                textView.setBackground(getBackgroundDrawable(item.get(i).getColor()));
            view.addView(textView);
        }

    }

    private Drawable getBackgroundDrawable(String color) {
        Drawable drawable;
        if(color != null) {
            //color = color.substring(2);
            drawable = new DrawableBuilder()
                    .rectangle()
                    .solidColor(Color.parseColor("#" + color))
                    .strokeColor(ContextCompat.getColor(mContext, R.color.grey_level_1))
                    .build();
        } else {
            drawable = new DrawableBuilder()
                    .rectangle()
                    .solidColor(ContextCompat.getColor(mContext, R.color.white))
                    .strokeColor(ContextCompat.getColor(mContext, R.color.grey_level_1))
                    .build();
        }

        return drawable;
    }
}
