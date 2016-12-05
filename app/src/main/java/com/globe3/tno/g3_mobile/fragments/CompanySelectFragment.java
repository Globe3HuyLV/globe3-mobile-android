package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.adapters.CompanyListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Company;
import com.globe3.tno.g3_mobile.view_objects.RowCompany;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANY_NAME;

public class CompanySelectFragment extends DialogFragment {
    Context parent_context;

    RecyclerView recycler_company_list;
    RelativeLayout layout_base_loader;

    RecyclerView.Adapter recycler_view_adapter;
    RecyclerView.LayoutManager recycler_view_layout_manager;

    ArrayList<RowCompany> company_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View companySelectFragment = inflater.inflate(R.layout.fragment_company_select, viewGroup, false);
        parent_context = companySelectFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        recycler_company_list = (RecyclerView) companySelectFragment.findViewById(R.id.recycler_company_list);
        layout_base_loader = (RelativeLayout) companySelectFragment.findViewById(R.id.layout_base_loader);

        layout_base_loader.setVisibility(View.VISIBLE);

        new LoadCompanies().execute();

        return companySelectFragment;
    }

    private class LoadCompanies extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params){
            try {
                company_list = new ArrayList<>();
                for(final Company company : (ArrayList<Company>) getArguments().getSerializable("company_list")){
                    RowCompany rowCompany = new RowCompany();
                    rowCompany.setCompanyName(company.getName());
                    rowCompany.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            COMPANYFN = company.getUniquenumPri();
                            COMPANY_NAME = company.getName();
                            ((DashboardActivity) getActivity()).resetCompanyName();
                            dismiss();
                        }
                    });
                    company_list.add(rowCompany);
                }

                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                recycler_company_list.setHasFixedSize(true);

                recycler_view_layout_manager = new LinearLayoutManager(parent_context);
                recycler_company_list.setLayoutManager(recycler_view_layout_manager);

                recycler_view_adapter = new CompanyListAdapter(company_list, parent_context);
                recycler_company_list.setAdapter(recycler_view_adapter);

                layout_base_loader.setVisibility(View.GONE);
                recycler_company_list.setVisibility(View.VISIBLE);
            }else{
                ((DashboardActivity) getActivity()).onActivityError();
            }
        }
    }
}
