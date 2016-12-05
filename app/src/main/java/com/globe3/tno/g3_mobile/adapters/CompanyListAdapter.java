package com.globe3.tno.g3_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.view_objects.RowCompany;

import java.util.ArrayList;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
    Context parent_context;

    private ArrayList<RowCompany> company_list;

    public CompanyListAdapter(ArrayList<RowCompany> companyList, Context parentContext) {
        this.parent_context = parentContext;
        this.company_list = companyList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_row_company;
        public TextView tv_company_name;

        public ViewHolder(View view) {
            super(view);
            rl_row_company = (RelativeLayout) view.findViewById(R.id.rl_row_company);
            tv_company_name = (TextView) view.findViewById(R.id.tv_company_name);
        }
    }

    public void add(int position, RowCompany company) {
        company_list.add(position, company);
        notifyItemInserted(position);
    }

    public void remove(RowCompany company) {
        int position = company_list.indexOf(company);
        company_list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public CompanyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View rowView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.row_company, parentView, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RowCompany rowCompany = company_list.get(position);
        viewHolder.rl_row_company.setOnClickListener(rowCompany.getOnClickListener());
        viewHolder.tv_company_name.setText(rowCompany.getCompanyName());

    }

    @Override
    public int getItemCount() {
        return company_list.size();
    }
}
