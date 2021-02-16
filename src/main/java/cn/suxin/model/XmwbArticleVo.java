package cn.suxin.model;

import cn.suxin.constant.XmwbConstant;

import java.io.Serializable;

/**
 *
 */
public class XmwbArticleVo implements Serializable {

    private static final long serialVersionUID = 805929885311281398L;
    String chapterDesc;
    String desc;  //artTitle
    //https://paper.xinmin.cn/html/xmwb/2021-01-09/12/92381.html
    String articleUrl;
    String objectUrl;

    String artAhthor;
    String artContent;
    String[] artContentList;
    String pubDate;

    String collect;
    double sorce;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getObjectUrl() {
        return XmwbConstant.URL_CHAPTER_PRE + articleUrl;
    }

    public void setObjectUrl(String objectUrl) {
        this.objectUrl = objectUrl;
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
    public String[]  getArtContentList() {
        return  this.artContent.split(" ");
    }

    public String getPubDate() {
        String[] tmp = articleUrl.split("/");
        if(tmp != null && tmp.length > 4){
            pubDate = tmp[3];
        }
        return pubDate;
    }

    public String getChapterDesc() {
        return chapterDesc;
    }

    public void setChapterDesc(String chapterDesc) {
        this.chapterDesc = chapterDesc;
    }

    public double getSorce() {
        try{
            String[] tmp = articleUrl.split("/");
            StringBuffer sb = new StringBuffer();
            if(tmp != null && tmp.length > 5){
                sb.append(tmp[3]);
                sb.append(tmp[4]);
                sb.append(tmp[5]);
            }
            return Double.parseDouble(sb.toString().replaceAll("-",""));
        }catch (Exception r){
            return System.currentTimeMillis();
        }

    }
}
