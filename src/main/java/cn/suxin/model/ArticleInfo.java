package cn.suxin.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;
import cn.suxin.constant.IniBean;

public class ArticleInfo implements Serializable {

    private static final long serialVersionUID = 5970681189939507297L;
    
    String artDate;
    String artUrl;
    
    String artId;
    String artTitle;
    String artAhthor;
    String artContent;
  
    
    String fkDesc;
    String fkUrl;
    
    public ArticleInfo() {}
    
    public ArticleInfo(FKInfo fk ) {
        this.artId = IniBean.generId();
        this.artDate = fk.getPageDate();
        this.fkUrl = fk.getPageUrl();
        this.fkDesc = fk.getPageDesc();
        
    }
    
    public String getArtDate() {
        return artDate;
    }
    public void setArtDate(String artDate) {
        this.artDate = artDate;
    }
    public String getArtUrl() {
        return artUrl;
    }
    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }
    public String getArtId() {
        return artId;
    }
    public void setArtId(String artId) {
        this.artId = artId;
    }
    public String getArtTitle() {
        return artTitle;
    }
    public void setArtTitle(String artTitle) {
        this.artTitle = artTitle;
    }
    public String getArtAhthor() {
        return artAhthor;
    }
    public void setArtAhthor(String artAhthor) {
        this.artAhthor = artAhthor;
    }
    public String getArtContent() {
        return artContent;
    }
    public void setArtContent(String artContent) {
        this.artContent = artContent;
    }

    public String getFkDesc() {
		return fkDesc;
	}


	public boolean articleHasContent() {
        if(StringUtils.isEmpty(this.artAhthor) || StringUtils.isEmpty(this.artContent)) {
            return false;
        }
        return true;
    }
    

}
