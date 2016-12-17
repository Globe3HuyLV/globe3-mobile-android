package com.globe3.tno.g3_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.RowSalesOrder;

import java.util.ArrayList;

public class LogTimeSalesOrderListAdapter extends RecyclerView.Adapter<LogTimeSalesOrderListAdapter.ViewHolder> {
    Context parent_context;

    private ArrayList<RowSalesOrder> sales_order_list;

    public LogTimeSalesOrderListAdapter(ArrayList<RowSalesOrder> sales_orderList, Context parentContext) {
        this.parent_context = parentContext;
        this.sales_order_list = sales_orderList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_row_sales_order;
        public TextView tv_sales_order_code;
        public TextView tv_sales_order_name;

        public ViewHolder(View view) {
            super(view);
            rl_row_sales_order = (RelativeLayout) view.findViewById(R.id.rl_row_sales_order);
            tv_sales_order_code = (TextView) view.findViewById(R.id.tv_sales_order_code);
            tv_sales_order_name = (TextView) view.findViewById(R.id.tv_sales_order_name);
        }
    }

    public void add(int position, RowSalesOrder sales_order) {
        sales_order_list.add(position, sales_order);
        notifyItemInserted(position);
    }

    public void remove(RowSalesOrder sales_order) {
        int position = sales_order_list.indexOf(sales_order);
        sales_order_list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public LogTimeSalesOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_log_time_sales_order, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RowSalesOrder rowSalesOrder = sales_order_list.get(position);
        viewHolder.rl_row_sales_order.setOnClickListener(rowSalesOrder.getOnClickListener());
        viewHolder.tv_sales_order_code.setText(rowSalesOrder.getSalesOrderCode());
        viewHolder.tv_sales_order_name.setText(rowSalesOrder.getSalesOrderDesc());

    }

    @Override
    public int getItemCount() {
        return sales_order_list.size();
    }
}
