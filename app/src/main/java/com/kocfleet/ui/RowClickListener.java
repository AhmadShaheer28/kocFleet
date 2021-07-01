package com.kocfleet.ui;

import com.kocfleet.model.ExcelCellModel;

import java.util.Map;

public interface RowClickListener {
    void onRowClicked(Map<Integer, ExcelCellModel> clickedRow);
    void onWriteRowClicked(Map<Integer, ExcelCellModel> clickedRow, int pos);
    void onColumnCLicked(int position);
    void onWriteColumnClicked(int position);
}
