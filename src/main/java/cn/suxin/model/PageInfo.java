package cn.suxin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageInfo  implements Serializable  {

    private static final long serialVersionUID = 1134379589798215807L;
    
    String pageDate;
    
    List<FKInfo> list ;

    public PageInfo() {}
    
    public PageInfo(String pageDate ) {
        this.pageDate = pageDate;
        list = new ArrayList<>();
    }
    
    public String getPageDate() {
        return pageDate;
    }


    public List<FKInfo> getList() {
        return list;
    }

    public void addFKInfo(FKInfo fk) {
        list.add(fk);
    }
}
