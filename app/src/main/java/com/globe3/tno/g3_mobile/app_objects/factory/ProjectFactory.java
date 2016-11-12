package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.ProjectPhoto;
import com.globe3.tno.g3_mobile.model.ProjectRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.model.entities.entproject;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class ProjectFactory {
    ProjectRepo project_repo;
    TabledataRepo tabledata_repo;

    public ProjectFactory(Context context) {
        project_repo = new ProjectRepo(context);
        tabledata_repo = new TabledataRepo(context);
    }

    public void createProject(Project project) {
        project_repo.open();
        project_repo.create_project(convertToEntity(project));
        project_repo.close();
    }

    public void createProject(JSONObject projectJson, LogItem logItem) {

        entproject entproject = new entproject();
        try {
            entproject.masterfn = projectJson.getString("masterfn");
            entproject.companyfn = projectJson.getString("companyfn");
            entproject.uniquenum_pri = projectJson.getString("uniquenum");
            entproject.tag_table_usage = projectJson.getString("tag_table_usage");
            entproject.uniquenum_sec = projectJson.getString("uniquenum_sec");
            entproject.active_yn = projectJson.getString("tag_active_yn");
            entproject.date_post = DateUtility.getStringDate(projectJson.getString("date_post"));
            entproject.date_submit = DateUtility.getStringDate(projectJson.getString("date_submit"));
            entproject.date_lastupdate = DateUtility.getStringDate(projectJson.getString("date_lastupdate"));
            entproject.sync_unique = logItem.getLogUnique();
            entproject.date_sync = logItem.getLogDate();
            entproject.project_code = projectJson.getString("projcode_code");
            entproject.project_name = projectJson.getString("desc_english");
            entproject.project_unique = projectJson.getString("projcode_unique");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        project_repo.open();
        project_repo.create_project(entproject);
        project_repo.close();
    }

    public void deleteProject(Project project) {
        project_repo.open();
        project_repo.delete_project(convertToEntity(project));
        project_repo.close();
    }

    public void deleteAll() {
        project_repo.open();
        project_repo.delete_project_all();
        project_repo.close();
    }

    public void updateProject(Project project) {
        project_repo.open();
        project_repo.update_project(convertToEntity(project));
        project_repo.close();
    }

    public ArrayList<Project> getActiveProjects() {
        ArrayList<Project> projects = new ArrayList<Project>();
        project_repo.open();
        for(entproject entproject : project_repo.get_active_projects()){
            projects.add(convertEntity(entproject));
        }
        project_repo.close();
        return projects;
    }

    public ArrayList<Project> getStaffProjects(String staffUnique) {
        ArrayList<Project> projects = new ArrayList<Project>();
        tabledata_repo.open();
        project_repo.open();
        for(tabledata tabledata : tabledata_repo.get_tabledatas("nvar25_02 = '" + staffUnique + "' AND DATE('" +  DateUtility.getDateString(Calendar.getInstance().getTime(),"yyyy-MM-dd") + "') BETWEEN DATE(date01) AND DATE(date02) ")){
            projects.add(convertEntity(project_repo.get_project(tabledata.nvar25_04)));
        }
        project_repo.close();
        tabledata_repo.close();
        return projects;
    }

    public Project getProject(String pUniquenum){
        project_repo.open();
        Project project = convertEntity(project_repo.get_project(pUniquenum));
        project_repo.close();
        return project;
    }

    public void saveProjectPhotos(ProjectPhoto projectPhoto){
        tabledata_repo.open();

        tabledata tabledata = new tabledata();

        tabledata.uniquenum_pri = projectPhoto.getUnique();
        tabledata.uniquenum_sec = projectPhoto.getProject().getUniquenum();
        tabledata.tag_void_yn = "n";
        tabledata.tag_deleted_yn = "n";
        tabledata.date_post = new Date();
        tabledata.userid_creator = USERLOGINID;
        tabledata.masterfn = MASTERFN;
        tabledata.companyfn = COMPANYFN;
        tabledata.nvar25_01 = projectPhoto.getReference();
        tabledata.nvar100_01 = projectPhoto.getRemarks();

        tabledata_repo.create_tabledata(tabledata);

        project_repo.close();
    }

    public ArrayList<ProjectPhoto> getProjectPhotos(String projectUnique, String searchTerm){
        tabledata_repo.open();

        Project project = this.getProject(projectUnique);
        ArrayList<ProjectPhoto> projectPhotos = new ArrayList<ProjectPhoto>();

        for(tabledata tabledata : tabledata_repo.get_tabledatas("uniquenum_sec = '" + projectUnique + "' AND (nvar25_01 LIKE '%" + searchTerm + "%' OR nvar100_01 LIKE '%" + searchTerm + "%')")){
            ProjectPhoto projectPhoto = new ProjectPhoto();
            projectPhoto.setProject(project);
            projectPhoto.setUnique(tabledata.uniquenum_pri);
            projectPhoto.setReference(tabledata.nvar25_01);
            projectPhoto.setRemarks(tabledata.nvar100_01);

            projectPhotos.add(projectPhoto);
        }

        tabledata_repo.close();

        return projectPhotos;
    }

    private Project convertEntity(entproject entproject){
        Project project = new Project();
        project.setIdcode(entproject.idcode);
        project.setUniquenum(entproject.uniquenum_pri);
        project.setDesc(entproject.project_name);
        project.setCode(entproject.project_code);
        project.setActive(entproject.active_yn.equals("y"));
        return project;
    }

    private entproject convertToEntity(Project project){
        entproject entproject = new entproject();
        entproject.idcode = project.getIdcode();
        entproject.uniquenum_pri = project.getUniquenum();
        entproject.project_unique = project.getUniquenum();
        entproject.project_name = project.getDesc();
        entproject.project_code = project.getCode();
        entproject.active_yn = project.getActive() ? "y" : "n";
        return entproject;
    }
}
