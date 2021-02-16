package cn.suxin.sevice;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import cn.suxin.model.ArticleInfo;
import cn.suxin.util.DateUtil;

public class PrintWordUtil {
    
    public static void createAricle2Word(ArticleInfo artInfo ,  XWPFDocument doc2007) {
        
        //1. 创建标题  
        XWPFParagraph title = doc2007.createParagraph();  
        // 设置样式,此时样式为一个正方形包围文字  
        title.setAlignment(ParagraphAlignment.CENTER);  
  
        // 创建1段文字,通过段落创建  
        XWPFRun r1 = title.createRun();  
        // 设置是否粗体  
        r1.setBold(true);  
        r1.setText(artInfo.getArtTitle());  
        r1.setFontFamily("黑体");
        r1.setFontSize(14);
       //r1.addBreak();
        
        
        XWPFParagraph author = doc2007.createParagraph();  
        author.setAlignment(ParagraphAlignment.CENTER);
  
        XWPFRun r2 = author.createRun();  
        r2.setText(artInfo.getArtAhthor());  
        r2.setFontFamily("宋体");
        r2.setFontSize(10);
        r2.addBreak();
        
        XWPFParagraph content = null;
        XWPFRun r3 = null;
        
        String[] oriContent = artInfo.getArtContent().split(" ");
        for(String textTmp : oriContent){
            content = doc2007.createParagraph();  
            content.setAlignment(ParagraphAlignment.LEFT);
            content.setIndentationFirstLine(2);
            
            r3 = content.createRun();  
            r3.setText(textTmp);  
            r3.setFontFamily("宋体");
            r3.setFontSize(10);
        }
        r3.addBreak();
        
        
        XWPFParagraph dateStr = doc2007.createParagraph();  
        dateStr.setAlignment(ParagraphAlignment.RIGHT);
        dateStr.setIndentationFirstLine(2);
        
        XWPFRun r4= dateStr.createRun();  
        r4.setText(DateUtil.formatDate(DateUtil.formatToDate(artInfo.getArtDate(), DateUtil.DEFAULT_DATE_FORMAT), DateUtil.DATE_FORMAT_CHINA));  
        r4.setFontFamily("黑体");
        r4.setFontSize(10);
        r4.setBold(true); 
        r4.addBreak();
        r4.addBreak();
        
    }


    public static void createAricle2Word( XWPFDocument doc2007 , String artTitle ,
                                          String artAuthor , String[] oriContent,
                                          String artDate) {

        //1. 创建标题
        XWPFParagraph title = doc2007.createParagraph();
        // 设置样式,此时样式为一个正方形包围文字
        title.setAlignment(ParagraphAlignment.CENTER);

        // 创建1段文字,通过段落创建
        XWPFRun r1 = title.createRun();
        // 设置是否粗体
        r1.setBold(true);
        r1.setText(artTitle);
        r1.setFontFamily("黑体");
        r1.setFontSize(14);
        //r1.addBreak();


        XWPFParagraph author = doc2007.createParagraph();
        author.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun r2 = author.createRun();
        r2.setText(artAuthor);
        r2.setFontFamily("宋体");
        r2.setFontSize(10);
        r2.addBreak();

        XWPFParagraph content = null;
        XWPFRun r3 = null;

        for(String textTmp : oriContent){
            content = doc2007.createParagraph();
            content.setAlignment(ParagraphAlignment.LEFT);
            content.setIndentationFirstLine(2);

            r3 = content.createRun();
            r3.setText(textTmp);
            r3.setFontFamily("宋体");
            r3.setFontSize(10);
        }
        r3.addBreak();


        XWPFParagraph dateStr = doc2007.createParagraph();
        dateStr.setAlignment(ParagraphAlignment.RIGHT);
        dateStr.setIndentationFirstLine(2);

        XWPFRun r4= dateStr.createRun();
        r4.setText(DateUtil.formatDate(DateUtil.formatToDate(artDate, DateUtil.DEFAULT_DATE_FORMAT), DateUtil.DATE_FORMAT_CHINA));
        r4.setFontFamily("黑体");
        r4.setFontSize(10);
        r4.setBold(true);
        r4.addBreak();
        r4.addBreak();

    }
        
    
}
