package cn.suxin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FKInfo implements Serializable  {
    private static final long serialVersionUID = -3113032524721407045L;
    
    String pageDate;
    
    String pageDesc;
    
    String pageUrl;
    
    List<ArticleInfo> artList;

    public FKInfo() {}
    
    public FKInfo(String pageDate) {
        this.pageDate = pageDate;
        this.artList = new ArrayList<>();
    }
    
    public String getPageDate() {
        return pageDate;
    }

    public void setPageDate(String pageDate) {
        this.pageDate = pageDate;
    }

    public String getPageDesc() {
        return pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<ArticleInfo> getArtList() {
        return artList;
    }

    public void addArtList(ArticleInfo art) {
        this.artList.add(art);
    }
    
    
}
