package com.globe3.tno.g3_mobile.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.activities.ProjectPhotoActivity;
import com.globe3.tno.g3_mobile.adapters.ProjectListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.view_objects.RowProject;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.PhotosProjectListAdapter;

import java.util.ArrayList;

public class PhotosProjectFragment extends Fragment {
    ProjectFactory project_factory;

    RecyclerView recycler_project_list;
    RecyclerView.Adapter recycler_view_adapter;
    RecyclerView.LayoutManager recycler_view_layout_manager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ArrayList<RowProject> project_list;

    SearchProject search_project;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project_factory = new ProjectFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View projectFragment = inflater.inflate(R.layout.fragment_project_list, viewGroup, false);

        recycler_project_list = (RecyclerView) projectFragment.findViewById(R.id.recycler_project_list);
        rl_search_loader = (RelativeLayout) projectFragment.findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) projectFragment.findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) projectFragment.findViewById(R.id.rl_no_record);

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_rotate_clockwise));

        project_list = new ArrayList<>();
        for(Project project : project_factory.getActiveProjects()){
            project_list.add(createRowProject(project));
        }

        recycler_project_list.setHasFixedSize(true);

        recycler_view_layout_manager = new LinearLayoutManager(viewGroup.getContext());
        recycler_project_list.setLayoutManager(recycler_view_layout_manager);

        recycler_view_adapter = new PhotosProjectListAdapter(project_list, viewGroup.getContext());
        recycler_project_list.setAdapter(recycler_view_adapter);

        return projectFragment;
    }

    private RowProject createRowProject(final Project project){
        RowProject rowProject = new RowProject();
        rowProject.setProjectCode(project.getCode());
        rowProject.setProjectName(project.getDesc());
        rowProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), ProjectPhotoSelectActivity.class));
                startActivity(new Intent(getActivity(), ProjectPhotoActivity.class));
            }
        });

        return rowProject;
    }
    public void searchProject(String searchTerm){
        if(search_project !=null){
            search_project.cancel(true);
        }
        search_project = new SearchProject(searchTerm);
        search_project.execute();
    }

    public class SearchProject extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchProject(String searchTerm){
            this.searchTerm = searchTerm;
            project_list.clear();
        }

        @Override
        protected  void onPreExecute() {
            recycler_project_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
            rl_no_record.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Project project : (searchTerm.equals("")? project_factory.getActiveProjects(): project_factory.searchProject(searchTerm))){
                project_list.add(createRowProject(project));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_project_list.setHasFixedSize(true);

            recycler_view_layout_manager = new LinearLayoutManager(getActivity());
            recycler_project_list.setLayoutManager(recycler_view_layout_manager);

            recycler_view_adapter = new ProjectListAdapter(project_list, getActivity());
            recycler_project_list.setAdapter(recycler_view_adapter);

            recycler_project_list.setVisibility(project_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(project_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
