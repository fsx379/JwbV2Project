package cn.suxin.sevice;

import cn.suxin.model.XmwbArticleVo;
import cn.suxin.model.XmwbChapterVo;

import java.util.Date;
import java.util.List;

public interface XmwbService {

    List<XmwbChapterVo> getChapterLists(Date queryDate);

    List<XmwbArticleVo> queryXmwbArticleListByChapter(String chapterUrl,String chapterDesc);

    XmwbArticleVo queryXmwbArticleDetail(String articleUrl , String chapterDesc);

    //收藏章节
    boolean collectChapter(String chapterUrl,String chapterDesc);

    boolean collectArticle(String articleUrl,String chapterDesc);

    List<XmwbArticleVo> queryXmwbCollectList();

    boolean delAllPrintList();

    long delFromPrintList(String artId);

    void startTaskForPrintWord(String path);



}
