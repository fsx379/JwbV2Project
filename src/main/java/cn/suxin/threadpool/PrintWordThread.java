package cn.suxin.threadpool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import cn.suxin.constant.Constant;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.TaskModel;
import cn.suxin.sevice.JwbSpiderUtil;
import cn.suxin.sevice.PrintWordUtil;
import cn.suxin.util.JsonUtils;

public class PrintWordThread extends TaskThread {


    public PrintWordThread(TaskModel task) {
        super(task);
    }

    @Override
    public void runTaskJob() throws InterruptedException, FileNotFoundException, IOException {
        String[] articleIds = task.getArcticleIds();
        if (articleIds != null && articleIds.length > 0) {

            // 生成Word2007版本
            XWPFDocument doc2007 = new XWPFDocument();
            // 创建Word输出流
            FileOutputStream fos = new FileOutputStream(new File(this.task.getPath()));


            for (String artId : articleIds) {

                ArticleInfo artInfo = (ArticleInfo) redisService.hmGet(Constant.CACHE_ARCTICLE_HASH,
                                artId);

                if (!artInfo.articleHasContent()) {
                    JwbSpiderUtil.queryArticleDetal(artInfo);
                    redisService.hmSet(Constant.CACHE_ARCTICLE_HASH, artInfo.getArtId(), artInfo);
                }

                PrintWordUtil.createAricle2Word(artInfo, doc2007);
                Thread.sleep(10);
            }

            doc2007.write(fos);

            if (fos != null) {
                fos.close();
            }

        }

        log.info("[PrintWordThread] task : " + JsonUtils.toJson(task));

    }
}
