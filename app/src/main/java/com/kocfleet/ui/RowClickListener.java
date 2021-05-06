package com.kocfleet.ui;

import com.kocfleet.model.ExcelCellModel;

import java.util.Map;

public interface RowClickListener {
    void onRowClicked(Map<Integer, ExcelCellModel> clickedRow);
    void onColumnCLicked(int position);
}
