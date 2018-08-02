package cn.suxin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArticleInfoVo implements Serializable{

    private static final long serialVersionUID = 5489229067800757625L;

    String queryDate;
    List<ArticleInfo> artList ;
    
    public ArticleInfoVo() {}
    public ArticleInfoVo(String queryDate) {
        this.queryDate = queryDate;
        this.artList = new ArrayList<>();
    }
    
    public String getQueryDate() {
        return queryDate;
    }
    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }
    public List<ArticleInfo> getArtList() {
        return artList;
    }
    public void addList(ArticleInfo a) {
        this.artList.add(a);
    }
    
    public void addList(List<ArticleInfo> al) {
        this.artList.addAll(al);
    }
    
}
