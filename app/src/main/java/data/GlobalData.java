package data;

import android.app.Application;

import java.util.List;

public class GlobalData extends Application {
    public String Server = "";
    public String AIMS_SERVER_HOST = "";

    private String LoginUserID,LoginUserName;
    private List<User> users;

    public List<Drugstore> Drugstores;
    private List<Druginfo> Druginfos;
    private List<Inventory> Inventorys;

    public void setLoginUserID(String LoginUserID){this.LoginUserID = LoginUserID;}
    public void setLoginUserName(String LoginUserName){this.LoginUserName = LoginUserName;}
    public void setDrugstores(List<Drugstore> Drugstores){this.Drugstores = Drugstores;}

    public String getLoginUserID(){ return LoginUserID;}
    public String getLoginUserName(){return LoginUserName;}
    public List<Drugstore> getDrugstores(){return Drugstores;}



}




