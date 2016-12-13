package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;
import android.util.Log;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.ProjectPhotoItem;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.model.ProjectPhotoRepo;
import com.globe3.tno.g3_mobile.model.ProjectRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.model.entities.entproject;
import com.globe3.tno.g3_mobile.model.entities.projectphoto;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class ProjectFactory {
    ProjectRepo project_repo;
    ProjectPhotoRepo projectphoto_repo;
    TabledataRepo tabledata_repo;

    public ProjectFactory(Context context) {
        project_repo = new ProjectRepo(context);
        tabledata_repo = new TabledataRepo(context);
        projectphoto_repo = new ProjectPhotoRepo(context);
    }

    public void openRepo(){
        project_repo.open();
        tabledata_repo = new TabledataRepo(project_repo);
        projectphoto_repo = new ProjectPhotoRepo(project_repo);
    }

    public void closeRepo(){
        project_repo.close();
        tabledata_repo.close();
        projectphoto_repo.close();
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

    public void downloadProject(JSONObject projectJson, LogItem logItem) {

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

        project_repo.create_project(entproject);
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

    public ArrayList<Project> searchProject(String searchTerm) {
        ArrayList<Project> projects = new ArrayList<Project>();
        project_repo.open();
        for(entproject entproject : project_repo.search_project(searchTerm)){
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

    public void saveProjectPhoto(ProjectPhotoItem projectPhotoItem, LogItem logItem){
        try {
            projectphoto projectphoto = new projectphoto();
            projectphoto.tag_table_usage = TagTableUsage.PROJECT_PHOTO_UPLOAD;
            projectphoto.sync_unique = logItem.getLogUnique();
            projectphoto.uniquenum_pri = projectPhotoItem.getUniquenumPri();
            projectphoto.uniquenum_sec = projectPhotoItem.getUniquenumSec();
            projectphoto.active_yn = "y";
            projectphoto.date_post = projectPhotoItem.getDatePost();
            projectphoto.date_submit = projectPhotoItem.getDatePost();
            projectphoto.date_lastupdate = projectPhotoItem.getDatePost();
            projectphoto.date_sync = logItem.getLogDate();
            projectphoto.userid_creator = USERLOGINID;
            projectphoto.masterfn = MASTERFN;
            projectphoto.companyfn = COMPANYFN;
            projectphoto.project_code = projectPhotoItem.getProject().getCode();
            projectphoto.project_name = projectPhotoItem.getProject().getDesc();
            projectphoto.project_unique = projectPhotoItem.getProject().getUniquenumPri();
            projectphoto.row_item_num = projectPhotoItem.getRowItemNum();
            projectphoto.reference_num = projectPhotoItem.getReferenceNum();
            projectphoto.remarks = projectPhotoItem.getRemarks();
            projectphoto.photo = projectPhotoItem.getPhoto();

            projectphoto_repo.create_projectphoto(projectphoto);
        }catch (Exception e){
            Log.i(APP_NAME, "saveProjectPhoto_Error");
            e.printStackTrace();
        }
    }

    private Project convertEntity(entproject entproject){
        Project project = new Project();
        project.setIdcode(entproject.idcode);
        project.setUniquenumPri(entproject.uniquenum_pri);
        project.setDesc(entproject.project_name);
        project.setCode(entproject.project_code);
        project.setActive(entproject.active_yn.equals("y"));
        return project;
    }

    private entproject convertToEntity(Project project){
        entproject entproject = new entproject();
        entproject.idcode = project.getIdcode();
        entproject.uniquenum_pri = project.getUniquenumPri();
        entproject.project_unique = project.getUniquenumPri();
        entproject.project_name = project.getDesc();
        entproject.project_code = project.getCode();
        entproject.active_yn = project.getActive() ? "y" : "n";
        return entproject;
    }
}
