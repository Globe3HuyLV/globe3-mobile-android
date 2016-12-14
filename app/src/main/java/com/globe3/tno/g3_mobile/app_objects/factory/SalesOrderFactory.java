package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;
import android.util.Log;

import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.ProjectPhotoItem;
import com.globe3.tno.g3_mobile.app_objects.SalesOrder;
import com.globe3.tno.g3_mobile.app_objects.StaffTeam;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.model.ProjectPhotoRepo;
import com.globe3.tno.g3_mobile.model.ProjectRepo;
import com.globe3.tno.g3_mobile.model.SalesOrderRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.model.TeamRepo;
import com.globe3.tno.g3_mobile.model.entities.entproject;
import com.globe3.tno.g3_mobile.model.entities.erpsalesorder;
import com.globe3.tno.g3_mobile.model.entities.projectphoto;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.model.entities.team;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class SalesOrderFactory {
    SalesOrderRepo sales_order_repo;
    TeamRepo team_repo;

    public SalesOrderFactory(Context context) {
        sales_order_repo = new SalesOrderRepo(context);
        team_repo = new TeamRepo(context);
    }

    public void openRepo(){
        sales_order_repo.open();
        team_repo = new TeamRepo(sales_order_repo);
    }

    public void closeRepo(){
        sales_order_repo.close();
        team_repo.close();
    }

    public void createSalesOrder(SalesOrder salesOrder) {
        sales_order_repo.open();
        sales_order_repo.create_salesorder(convertToEntity(salesOrder));
        sales_order_repo.close();
    }

    public void downloadSalesOrder(JSONObject saleOrderJson, LogItem logItem) {

        erpsalesorder erpsalesorder = new erpsalesorder();
        try {
            erpsalesorder.masterfn = saleOrderJson.getString("masterfn");
            erpsalesorder.companyfn = saleOrderJson.getString("companyfn");
            erpsalesorder.uniquenum_pri = saleOrderJson.getString("uniquenum");
            erpsalesorder.tag_table_usage = saleOrderJson.getString("tag_table_usage");
            erpsalesorder.uniquenum_sec = saleOrderJson.getString("uniquenum_sec");
            erpsalesorder.active_yn = saleOrderJson.getString("tag_active_yn");
            erpsalesorder.date_post = DateUtility.getStringDate(saleOrderJson.getString("date_post"));
            erpsalesorder.date_submit = DateUtility.getStringDate(saleOrderJson.getString("date_submit"));
            erpsalesorder.date_lastupdate = DateUtility.getStringDate(saleOrderJson.getString("date_lastupdate"));
            erpsalesorder.sync_unique = logItem.getLogUnique();
            erpsalesorder.date_sync = logItem.getLogDate();
            erpsalesorder.salesorder_code = saleOrderJson.getString("salesorder_code");
            erpsalesorder.salesorder_name = saleOrderJson.getString("salesorder_name");
            erpsalesorder.salesorder_unique = saleOrderJson.getString("salesorder_unique");
            erpsalesorder.team_unique = saleOrderJson.getString("team_unique");
            erpsalesorder.team_name = saleOrderJson.getString("team_name");
            erpsalesorder.team_code = saleOrderJson.getString("team_code");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sales_order_repo.create_salesorder(erpsalesorder);
    }

    public void deleteSalesOrder(SalesOrder salesOrder) {
        sales_order_repo.open();
        sales_order_repo.delete_salesorder(convertToEntity(salesOrder));
        sales_order_repo.close();
    }

    public void deleteAll() {
        sales_order_repo.open();
        sales_order_repo.delete_salesorder_all();
        sales_order_repo.close();
    }

    public ArrayList<SalesOrder> searchSalesOrder(String searchTerm) {
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        sales_order_repo.open();
        for(erpsalesorder erpsalesorder : sales_order_repo.search_salesorder(searchTerm)){
            salesOrders.add(convertEntity(erpsalesorder));
        }
        sales_order_repo.close();
        return salesOrders;
    }

    public ArrayList<SalesOrder> getTeamSalesOrder(String teamUnique) {
        ArrayList<SalesOrder> salesOrders = new ArrayList<>();
        sales_order_repo.open();
        for(erpsalesorder erpsalesorder : sales_order_repo.get_erpsalesorders("team_unique = '" + teamUnique + "'")){
            salesOrders.add(convertEntity(erpsalesorder));
        }
        sales_order_repo.close();
        return salesOrders;
    }

    public SalesOrder getSalesOrder(String pUniquenum){
        sales_order_repo.open();
        SalesOrder salesOrder = convertEntity(sales_order_repo.get_salesorder(pUniquenum));
        sales_order_repo.close();
        return salesOrder;
    }

    private SalesOrder convertEntity(erpsalesorder erpsalesorder){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setIdcode(erpsalesorder.idcode);
        salesOrder.setUniquenumPri(erpsalesorder.uniquenum_pri);
        salesOrder.setDesc(erpsalesorder.salesorder_name);
        salesOrder.setCode(erpsalesorder.salesorder_code);
        salesOrder.setActive(erpsalesorder.active_yn.equals("y"));

        if(!erpsalesorder.team_unique.equals("")){
            team_repo.open();
            team team = team_repo.get_team(erpsalesorder.team_unique);
            StaffTeam staffTeam = new StaffTeam();
            staffTeam.setIdcode(team.idcode);
            staffTeam.setUniquenumPri(team.uniquenum_pri);
            staffTeam.setCode(team.team_code);
            staffTeam.setDesc(team.team_name);
            staffTeam.setActive(team.active_yn.equals("y"));

            salesOrder.setStaffTeam(staffTeam);
            team_repo.close();
        }

        return salesOrder;
    }

    private erpsalesorder convertToEntity(SalesOrder salesOrder){
        erpsalesorder erpsalesorder = new erpsalesorder();
        erpsalesorder.idcode = salesOrder.getIdcode();
        erpsalesorder.uniquenum_pri = salesOrder.getUniquenumPri();
        erpsalesorder.salesorder_unique = salesOrder.getUniquenumPri();
        erpsalesorder.salesorder_name = salesOrder.getDesc();
        erpsalesorder.salesorder_code = salesOrder.getCode();
        erpsalesorder.active_yn = salesOrder.isActive() ? "y" : "n";
        erpsalesorder.team_unique = salesOrder.getStaffTeam().getUniquenumPri();
        erpsalesorder.team_code = salesOrder.getStaffTeam().getUniquenumPri();
        erpsalesorder.team_name = salesOrder.getStaffTeam().getDesc();

        return erpsalesorder;
    }
}
