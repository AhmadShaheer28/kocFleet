package com.kocfleet.ui.adapter;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kocfleet.R;
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.ui.RowClickListener;
import com.kocfleet.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class ExcelWriteAdapter extends RecyclerView.Adapter<ExcelWriteAdapter.ViewHolder> {
    List<EditText> editTexts = new ArrayList<>();
    List<Map<Integer, ExcelCellModel>> data;
    private final RowClickListener delegate;
    int etSize = 0;
    Context mContext;
    boolean isEditable;
    private final String filename;
    private final int isColumnClicked;
    private final int loopForSheetCol;
    private final int sheetColPosition;

    public ExcelWriteAdapter(@Nullable List<Map<Integer, ExcelCellModel>> data, Context context,
                             RowClickListener delegate, boolean isEditable, String filename, int isColumnClicked) {
        this.data = data;
        mContext = context;
        this.delegate = delegate;
        this.isEditable = isEditable;
        this.filename = filename;
        this.isColumnClicked = isColumnClicked;
        loopForSheetCol = filename.equals(Constants.CERTIFICATES) ? 2 : 3;
        sheetColPosition = filename.equals(Constants.CERTIFICATES) ? 0 : 2;
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

    public List<Map<String, String>> saveRowDataHere() {
        int et_count = 0;
        List<Map<String, String>> exportExcel = new ArrayList<>();
        for (int i = 0; i < (editTexts.size() / data.get(0).size()); i++) {
            Map<String, String> value = new HashMap<>();
            for (int j = 0; j < data.get(0).size(); j++) {
                value.put("cell" + j, editTexts.get(et_count).getText().toString());
                et_count++;
            }

            exportExcel.add(value);
        }
        return exportExcel;
    }

    public List<Map<String, String>> saveColDataHere() {
        int et_count = 0;
        int editedCol = filename.equals(Constants.CERTIFICATES) ? 2 : 3;
        int loopCol = filename.equals(Constants.CERTIFICATES) ? 3 : 4;
        List<Map<String, String>> exportExcel = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> value = new HashMap<>();
            for (int j = 0; j < loopCol; j++) {
                if (j == editedCol) {
                    if (et_count <= etSize) {
                        value.put("cell" + j, editTexts.get(et_count).getText().toString());
                    } else {
                        value.put("cell" + j, Objects.requireNonNull(data.get(i).get(j)).getValue());
                    }
                }

                et_count++;
            }

            exportExcel.add(value);
        }
        return exportExcel;
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (
                        300,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
        if (isColumnClicked != -1) {
            String regDate = "[0-9]{2}-[0-9]{2}-[0-9]{2}";
            for (int i = 0; i< loopForSheetCol; i++) {
                editTexts.add(new EditText(mContext));
                editTexts.get(etSize).setTextSize(13);
                editTexts.get(etSize).setGravity(Gravity.CENTER);
                editTexts.get(etSize).setTextColor(ContextCompat.getColor(mContext, R.color.black));
                editTexts.get(etSize).setText(Objects.requireNonNull(item.get(i)).getValue(), TextView.BufferType.EDITABLE);
                editTexts.get(etSize).setLayoutParams(layoutParams);
                editTexts.get(etSize).setPadding(10, 10, 10, 10);
                editTexts.get(etSize).setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(i)).getColor()));
                view.addView(editTexts.get(etSize));
                etSize += 1;
            }
            editTexts.add(new EditText(mContext));
            editTexts.get(etSize).setTextSize(13);
            editTexts.get(etSize).setGravity(Gravity.CENTER);
            editTexts.get(etSize).setTextColor(ContextCompat.getColor(mContext, R.color.black));
            editTexts.get(etSize).setText(Objects.requireNonNull(item.get(isColumnClicked)).getValue(), TextView.BufferType.EDITABLE);
            editTexts.get(etSize).setLayoutParams(layoutParams);
            editTexts.get(etSize).setPadding(10, 10, 10, 10);
            if (Objects.requireNonNull(item.get(isColumnClicked)).getValue().toUpperCase().equals("IN COMMISSION")
                    || Objects.requireNonNull(item.get(isColumnClicked)).getValue().toUpperCase().equals("OK"))
                editTexts.get(etSize).setBackground(getBackgroundDrawable("#FF00FF00"));
            else if (Objects.requireNonNull(item.get(isColumnClicked)).getValue().toUpperCase().equals("OUT OF COMMISSION")
                    || Objects.requireNonNull(item.get(isColumnClicked)).getValue().toUpperCase().equals("NOT OK")
                    || Objects.requireNonNull(item.get(isColumnClicked)).getValue().toLowerCase().equals("empty"))
                editTexts.get(etSize).setBackground(getBackgroundDrawable("#FFFF0000"));
            else
                editTexts.get(etSize).setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(isColumnClicked)).getColor()));

            if (Objects.requireNonNull(item.get(isColumnClicked)).getValue().matches(regDate)) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                Date date = null;
                try {
                    date = formatter.parse(Objects.requireNonNull(item.get(isColumnClicked)).getValue());
                } catch (ParseException ignored) { }
                if (matchDates(date) < 1) {
                    editTexts.get(etSize).setBackground(getBackgroundDrawable("#FFFF0000"));
                }
            }

            view.addView(editTexts.get(etSize));
            etSize += 1;
        } else if (position == 0 && !filename.equals(Constants.CERTIFICATES)) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setText(Objects.requireNonNull(item.get(0)).getValue());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            textView.setPadding(10, 10, 10, 10);
            textView.setBackground(getBackgroundDrawable("#FFFFFF"));
            view.addView(textView);
        } else {
            for (int i = 0; i < item.size(); i++) {
                String regDate = "[0-9]{2}-[0-9]{2}-[0-9]{2}";
                if(!isEditable) {
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
                            || Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("empty"))
                        textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                    else
                        textView.setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(i)).getColor()));

                    if (Objects.requireNonNull(item.get(i)).getValue().matches(regDate)) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(Objects.requireNonNull(item.get(i)).getValue()));
                        } catch (ParseException ignored) { }
                        if (matchDates(date) < 1) {
                            textView.setBackground(getBackgroundDrawable("#FFFF0000"));
                        }
                    }

                    if (position == sheetColPosition) {
                        int finalI = i;
                        textView.setOnClickListener( view4 -> delegate.onWriteColumnClicked(finalI));
                    }

                    view.addView(textView);
                } else {
                    editTexts.add(new EditText(mContext));
                    editTexts.get(etSize).setTextSize(13);
                    editTexts.get(etSize).setGravity(Gravity.CENTER);
                    editTexts.get(etSize).setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    editTexts.get(etSize).setText(Objects.requireNonNull(item.get(i)).getValue(), TextView.BufferType.EDITABLE);
                    editTexts.get(etSize).setLayoutParams(layoutParams);
                    editTexts.get(etSize).setPadding(10, 10, 10, 10);
                    if (Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("IN COMMISSION")
                            || Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("OK"))
                        editTexts.get(etSize).setBackground(getBackgroundDrawable("#FF00FF00"));
                    else if (Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("OUT OF COMMISSION")
                            || Objects.requireNonNull(item.get(i)).getValue().toUpperCase().equals("NOT OK")
                            || Objects.requireNonNull(item.get(i)).getValue().toLowerCase().equals("empty"))
                        editTexts.get(etSize).setBackground(getBackgroundDrawable("#FFFF0000"));
                    else
                        editTexts.get(etSize).setBackground(getBackgroundDrawable(Objects.requireNonNull(item.get(i)).getColor()));

                    if (Objects.requireNonNull(item.get(i)).getValue().matches(regDate)) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(item.get(i)).getValue());
                        } catch (ParseException ignored) { }
                        if (matchDates(date) < 1) {
                            editTexts.get(etSize).setBackground(getBackgroundDrawable("#FFFF0000"));
                        }
                    }

                    view.addView(editTexts.get(etSize));
                    etSize += 1;
                }
            }
            holder.itemView.setOnClickListener(view1 -> delegate.onWriteRowClicked(item, position));
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
