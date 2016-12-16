package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.SalesOrder;
import com.globe3.tno.g3_mobile.model.SalesOrderRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.model.TeamRepo;
import com.globe3.tno.g3_mobile.model.entities.erpsalesorder;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.constants.TagTableUsage.SALES_ORDER_TEAM;
import static com.globe3.tno.g3_mobile.constants.TagTableUsage.STAFF_TEAM;

public class SalesOrderFactory {
    SalesOrderRepo sales_order_repo;
    TeamRepo team_repo;
    TabledataRepo tabledata_repo;

    public SalesOrderFactory(Context context) {
        sales_order_repo = new SalesOrderRepo(context);
        team_repo = new TeamRepo(context);
        tabledata_repo = new TabledataRepo(context);
    }

    public void openRepo(){
        sales_order_repo.open();
        team_repo = new TeamRepo(sales_order_repo);
        tabledata_repo = new TabledataRepo(sales_order_repo);
    }

    public void closeRepo(){
        sales_order_repo.close();
        team_repo.close();
        tabledata_repo.close();
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
            erpsalesorder.date_lastupdate = DateUtility.getStringDate(saleOrderJson.getString("date_lastupdate"));
            erpsalesorder.sync_unique = logItem.getLogUnique();
            erpsalesorder.date_sync = logItem.getLogDate();
            erpsalesorder.salesorder_code = saleOrderJson.getString("salesorder_code");
            erpsalesorder.salesorder_name = saleOrderJson.getString("salesorder_desc");
            erpsalesorder.salesorder_unique = saleOrderJson.getString("salesorder_unique");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sales_order_repo.create_salesorder(erpsalesorder);
    }

    public void downloadSalesOrderTeam(JSONObject salesOrderTeamJson, LogItem logItem) {

        tabledata so_team = new tabledata();
        try {
            so_team.masterfn = salesOrderTeamJson.getString("masterfn");
            so_team.companyfn = salesOrderTeamJson.getString("companyfn");
            so_team.uniquenum_pri = salesOrderTeamJson.getString("uniquenum");

            so_team.tag_table_usage = SALES_ORDER_TEAM;

            so_team.nvar25_01 = salesOrderTeamJson.getString("sales_order_code");
            so_team.nvar25_02 = salesOrderTeamJson.getString("sales_order_unique");
            so_team.nvar100_01 = salesOrderTeamJson.getString("sales_order_desc");

            so_team.nvar25_03 = salesOrderTeamJson.getString("team_code");
            so_team.nvar25_04 = salesOrderTeamJson.getString("team_unique");
            so_team.nvar100_03 = salesOrderTeamJson.getString("team_name");

            so_team.date_post = DateUtility.getStringDate(salesOrderTeamJson.getString("date_post"));
            so_team.date01 = DateUtility.getStringDate(salesOrderTeamJson.getString("date_lastupdate"));

            so_team.sync_unique = logItem.getLogUnique();
            so_team.date_sync = logItem.getLogDate();

            so_team.tag_deleted_yn = "n";

            tabledata_repo.create_tabledata(so_team);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void deleteAllSalesOrderTeam(){
        tabledata_repo.open();
        tabledata_repo.delete_tabledata_all(SALES_ORDER_TEAM);
        tabledata_repo.close();
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

        return erpsalesorder;
    }
}
