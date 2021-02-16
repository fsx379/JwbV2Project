package cn.suxin.threadpool;

import cn.suxin.model.XmwbArticleVo;
import cn.suxin.sevice.PrintWordUtil;
import cn.suxin.sevice.XmwbService;
import cn.suxin.util.SpringContextHolder;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XmwbPrintWordThread extends Thread{
    protected static Logger log = LoggerFactory.getLogger(XmwbPrintWordThread.class);
    String path;
    XmwbService xmwbService ;

    public XmwbPrintWordThread(String path) {
        this.path = path;
        xmwbService = SpringContextHolder.getBean("xmwbService");
    }

    @Override
    public void run()  {
        // 生成Word2007版本
        XWPFDocument doc2007 = new XWPFDocument();
        // 创建Word输出流
        FileOutputStream fos = null;
        try {
            List<XmwbArticleVo> list = xmwbService.queryXmwbCollectList();
            if (list != null && list.size() > 0) {

                fos = new FileOutputStream(new File(path));
                for (XmwbArticleVo vo : list) {
                    try {
                        PrintWordUtil.createAricle2Word(doc2007 , vo.getDesc(),vo.getArtAhthor(),vo.getArtContentList(),vo.getPubDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                doc2007.write(fos);
                log.info("[XmwbPrintWordThread] export end , path={}" , path);
            }
        } catch (Exception e) {
            log.error("[XmwbPrintWordThread] export.." , e);
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
