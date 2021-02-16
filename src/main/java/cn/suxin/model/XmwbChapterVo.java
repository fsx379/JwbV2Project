package cn.suxin.model;

import cn.suxin.constant.XmwbConstant;

import java.io.Serializable;

/**
 *
 */
public class XmwbChapterVo implements Serializable {

    private static final long serialVersionUID = 805929885311289998L;

    String desc;
    String chapterUrl;
    String objectUrl;
    String collect;



    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getObjectUrl() {
        return XmwbConstant.URL_CHAPTER_PRE + this.chapterUrl;
    }

    public void setObjectUrl(String objectUrl) {
        this.objectUrl = objectUrl;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }
}
