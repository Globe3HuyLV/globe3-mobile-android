package com.globe3.tno.g3_mobile.fragments;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.adapters.RegisterFingerStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.ProjectListAdapter;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class TimesheetProjectFragment extends Fragment {
    ProjectFactory projectFactory;

    RecyclerView recycler_project_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    LogTimeStaffFragment logTimeStaffFragment;

    ArrayList<RowProject> project_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectFactory = new ProjectFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectFragment = inflater.inflate(R.layout.fragment_project_list, viewGroup, false);

        recycler_project_list = (RecyclerView) projectFragment.findViewById(R.id.recycler_project_list);
        rl_search_loader = (RelativeLayout) projectFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) projectFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) projectFragment.findViewById(R.id.rl_no_record);

        project_list = new ArrayList<>();
        for(Project project : projectFactory.getActiveProjects()){
            RowProject rowProject = new RowProject();
            rowProject.setProjectCode(project.getCode());
            rowProject.setProjectName(project.getDesc());
            rowProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*FragmentManager fragmentManager = getActivity().getFragmentManager();
                    Bundle projectBundle = new Bundle();

                    Project project = new Project();
                    project.setCode("PRJ"+String.valueOf(n));
                    project.setDesc("Project Desc");

                    projectBundle.putSerializable("entproject", project);

                    logTimeStaffFragment = new LogTimeStaffFragment();
                    logTimeStaffFragment.setCancelable(false);
                    logTimeStaffFragment.setArguments(projectBundle);
                    logTimeStaffFragment.show(fragmentManager, getString(R.string.label_log_time_staff));*/
                }
            });
            project_list.add(rowProject);
        }

        recycler_project_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new ProjectListAdapter(project_list, viewGroup.getContext());
        recycler_project_list.setAdapter(recyclerViewAdapter);

        return projectFragment;
    }

    public void searchProject(String searchTerm){
        new SearchProject(searchTerm).execute();
    }

    public class SearchProject extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchProject(String searchTerm){
            this.searchTerm = searchTerm;
            project_list.clear();
        }

        @Override
        protected  void onPreExecute()
        {
            recycler_project_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Project project : (searchTerm.equals("")?projectFactory.getActiveProjects():projectFactory.searchProject(searchTerm))){
                RowProject rowProject = new RowProject();
                rowProject.setProjectCode(project.getCode());
                rowProject.setProjectName(project.getDesc());
                rowProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*FragmentManager fragmentManager = getActivity().getFragmentManager();
                    Bundle projectBundle = new Bundle();

                    Project project = new Project();
                    project.setCode("PRJ"+String.valueOf(n));
                    project.setDesc("Project Desc");

                    projectBundle.putSerializable("entproject", project);

                    logTimeStaffFragment = new LogTimeStaffFragment();
                    logTimeStaffFragment.setCancelable(false);
                    logTimeStaffFragment.setArguments(projectBundle);
                    logTimeStaffFragment.show(fragmentManager, getString(R.string.label_log_time_staff));*/
                    }
                });
                project_list.add(rowProject);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_project_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
            recycler_project_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new ProjectListAdapter(project_list, getActivity());
            recycler_project_list.setAdapter(recyclerViewAdapter);

            recycler_project_list.setVisibility(project_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(project_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
