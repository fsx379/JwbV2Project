package cn.suxin.model;

import java.io.Serializable;

public class ArticleModelVO implements Serializable {

	private static final long serialVersionUID = 3867721758965273959L;
	String artDate;
    String artUrl;
    
    String artId;
    String artTitle;
    String artAhthor;
    String artContent;
    
    String fkDesc;
    String fkUrl;
    
    int collect;
    String[] artContentList;
    public ArticleModelVO() {}
    
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

	public void setFkDesc(String fkDesc) {
		this.fkDesc = fkDesc;
	}

	public String getFkUrl() {
		return fkUrl;
	}

	public void setFkUrl(String fkUrl) {
		this.fkUrl = fkUrl;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}
	public String[]  getArtContentList() {
		return  this.artContent.split(" ");
	}


}
