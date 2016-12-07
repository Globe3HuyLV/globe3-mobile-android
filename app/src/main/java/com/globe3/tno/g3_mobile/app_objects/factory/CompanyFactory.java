package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.app_objects.Company;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.EntityRepo;
import com.globe3.tno.g3_mobile.model.entities.entity;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyFactory {
    EntityRepo entity_repo;

    public CompanyFactory(Context context){
        entity_repo = new EntityRepo(context);
    }

    public void openRepo(){
        entity_repo.open();
    }

    public void closeRepo(){
        entity_repo.close();
    }

    public Company getCompany(String pUniquenum){
        entity entity;
        entity_repo.open();
        entity = entity_repo.get_entity(pUniquenum);
        entity_repo.close();

        return entity != null ? convertEntity(entity) : null;
    }

    public ArrayList<Company> getActiveCompanys() {
        entity_repo.open();

        ArrayList<Company> companys = new ArrayList<Company>();

        for(entity entity : entity_repo.get_active_entitys()){
            companys.add(convertEntity(entity));
        }

        entity_repo.close();
        return companys;
    }

    public ArrayList<Company> getUserCompanys(String userCompanies) {
        entity_repo.open();

        ArrayList<Company> companys = new ArrayList<Company>();

        if(Globals.USERLOGINID.equals("m8")){
            for(entity entity : entity_repo.get_active_entitys()){
                companys.add(convertEntity(entity));
            }
        }else{
            for(entity entity : entity_repo.get_user_entitys(userCompanies)){
                companys.add(convertEntity(entity));
            }
        }

        entity_repo.close();
        return companys;
    }

    public void deleteAll(){
        entity_repo.open();
        entity_repo.delete_entity_all();
        entity_repo.close();
    }

    public void createCompany(JSONObject entityJson, LogItem logItem){
        entity entity = new entity();
        try {
            entity.uniquenum_pri = entityJson.getString("uniquenum_pri");
            entity.uniquenum_sec = entityJson.getString("uniquenum_sec");
            entity.tag_table_usage = entityJson.getString("tag_table_usage");
            entity.co_code = entityJson.getString("co_code");
            entity.co_name = entityJson.getString("co_name");
            entity.companyfn = entityJson.getString("companyfn");
            entity.masterfn = entityJson.getString("masterfn");
            entity.active_yn = "y";
            entity.date_post = DateUtility.getStringDate(entityJson.getString("date_post"));
            entity.sync_unique = logItem.getLogUnique();
            entity.date_sync = logItem.getLogDate();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        entity_repo.open();
        entity_repo.create_entity(entity);
        entity_repo.close();
    }

    public void downloadCompany(JSONObject entityJson, LogItem logItem){
        entity entity = new entity();
        try {
            entity.uniquenum_pri = entityJson.getString("uniquenum_pri");
            entity.uniquenum_sec = entityJson.getString("uniquenum_sec");
            entity.tag_table_usage = entityJson.getString("tag_table_usage");
            entity.co_code = entityJson.getString("co_code");
            entity.co_name = entityJson.getString("co_name");
            entity.companyfn = entityJson.getString("companyfn");
            entity.masterfn = entityJson.getString("masterfn");
            entity.active_yn = "y";
            entity.date_post = DateUtility.getStringDate(entityJson.getString("date_post"));
            entity.sync_unique = logItem.getLogUnique();
            entity.date_sync = logItem.getLogDate();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        entity_repo.create_entity(entity);
    }

    private Company convertEntity(entity entity){
        Company company = new Company();
        company.setIdcode(entity.idcode);
        company.setUniquenumPri(entity.uniquenum_pri);
        company.setName(entity.co_name);
        company.setCode(entity.co_code);
        company.setActive(entity.active_yn=="y");
        return company;
    }
}
