package com.globe3.tno.g3_mobile.view_objects;

import android.view.View;

public class RowSalesOrder {
    private String salesOrderCode;
    private String salesOrderDesc;
    private int salesOrderPhotosCount;
    private View.OnClickListener onClickListener;

    public String getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public String getSalesOrderDesc() {
        return salesOrderDesc;
    }

    public void setSalesOrderDesc(String salesOrderDesc) {
        this.salesOrderDesc = salesOrderDesc;
    }

    public int getSalesOrderPhotosCount() {
        return salesOrderPhotosCount;
    }

    public void setSalesOrderPhotosCount(int salesOrderPhotosCount) {
        this.salesOrderPhotosCount = salesOrderPhotosCount;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
