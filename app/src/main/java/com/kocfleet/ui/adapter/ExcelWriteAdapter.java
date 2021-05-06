package com.kocfleet.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kocfleet.R;
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.utils.ExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class ExcelWriteAdapter extends RecyclerView.Adapter<ExcelWriteAdapter.ViewHolder> {
    List<EditText> editTexts = new ArrayList<>();
    List<Map<Integer, ExcelCellModel>> data;
    int etSize = 0;
    int dataSize = 0;
    Context mContext;

    public ExcelWriteAdapter(@Nullable List<Map<Integer, ExcelCellModel>> data, Context context) {
        this.data = data;
        mContext = context;
    }

    private Drawable getBackgroundDrawable(String color) {
        Drawable drawable;
        if (color != null) {
            //color = color.substring(2);
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

    public void saveCodeHere() {
        int et_count = 0;
        List<Map<Integer, Object>> exportExcel = new ArrayList<>();
        for (int i = 2; i < data.size(); i++) {
            Map<Integer, Object> value = new HashMap<>();
            for (int j = 0; j < data.get(0).size(); j++) {
                value.put(j, editTexts.get(et_count).getText().toString());
                et_count++;
            }
            exportExcel.add(value);
        }
        ExcelUtil.writeExcelNew(mContext, exportExcel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.template1_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<Integer, ExcelCellModel> item = data.get(position);
        LinearLayout view = (LinearLayout) holder.itemView;
        view.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if(position == 0 || position == 1) {
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
                editTexts.add(new EditText(mContext));
                editTexts.get(etSize).setTextSize(13);
                editTexts.get(etSize).setGravity(Gravity.CENTER);
                editTexts.get(etSize).setTextColor(ContextCompat.getColor(mContext, R.color.black));
                editTexts.get(etSize).setText(item.get(i).getValue() + "");
                editTexts.get(etSize).setLayoutParams(layoutParams);
                editTexts.get(etSize).setPadding(10, 10, 10, 10);
                if(item.get(i).getValue().toUpperCase().equals("IN COMMISION") || item.get(i).getValue().toUpperCase().equals("OK"))
                    editTexts.get(etSize).setBackground(getBackgroundDrawable("FF00FF00"));
                else if(item.get(i).getValue().toUpperCase().equals("OUT OF COMMISION") || item.get(i).getValue().toUpperCase().equals("NOT OK"))
                    editTexts.get(etSize).setBackground(getBackgroundDrawable("FFFF0000"));
                else
                    editTexts.get(etSize).setBackground(getBackgroundDrawable(item.get(i).getColor()));
                view.addView(editTexts.get(etSize));
                etSize += 1;
            }
        }
        if(position == (data.size()-1))
            Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
