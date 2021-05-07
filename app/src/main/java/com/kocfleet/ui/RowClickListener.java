package com.kocfleet.ui;

import com.kocfleet.model.ExcelCellModel;

import java.util.Map;

public interface RowClickListener {
    void onRowClicked(Map<Integer, String> clickedRow);
    void onColumnCLicked(int position);
}
