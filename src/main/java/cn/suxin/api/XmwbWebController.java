package cn.suxin.api;

import cn.suxin.constant.Constant;
import cn.suxin.model.*;
import cn.suxin.sevice.XmwbService;
import cn.suxin.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/xmwb")
public class XmwbWebController {
	private static Logger logger = LoggerFactory.getLogger(XmwbWebController.class);

	@Autowired
	XmwbService xmwbService;

	/**jwbService
	 * 文章列表detailAction
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/index"})
    public String index(HttpServletRequest request, HttpServletResponse response) {
		String queryDateStr = request.getParameter("queryDate");
		
		Date queryDate = null;

		if(StringUtils.isNotBlank(queryDateStr)) {
			queryDateStr = queryDateStr.trim();
			queryDate = DateUtil.getToday(DateUtil.formatString(queryDateStr,  "yyyy-MM-dd"));
		}
		
		if(queryDate == null) {
			Timestamp hour10 = DateUtil.get10ClockOfToday();
			Timestamp currentTime = DateUtil.getCurrentTimestamp();
			if(currentTime.compareTo(hour10) > 0) {
				queryDate = DateUtil.getToday(currentTime);
			}else {
				queryDate = DateUtil.getIntervalBeginOfDay(currentTime, 1);
			}
			queryDateStr = DateUtil.formatDate(queryDate, "yyyy-MM-dd");
		}

		request.setAttribute("title", "文章列表");
		request.setAttribute("queryDate", queryDateStr);

		List<XmwbChapterVo>  chapterVoList = new ArrayList<XmwbChapterVo>();
		try {
			chapterVoList = xmwbService.getChapterLists(queryDate);

		} catch (Exception e) {
			logger.error("[XmwbWebController] index error!" , e);
		}
		request.setAttribute("chapterVoList",chapterVoList);
        logger.info("[XmwbWebController] index queryDate = {}" , queryDate);
		return "/xmwb/index";
	}

	@RequestMapping(value = {"/articleList"})
	public String articleList(HttpServletRequest request, HttpServletResponse response) {
		String chapterUrl = request.getParameter("chapterUrl");
		String chapterDesc = request.getParameter("chapterDesc");
		String queryDate = request.getParameter("queryDate");

		List<XmwbArticleVo>  list = new ArrayList<>();
		try {
			list = xmwbService.queryXmwbArticleListByChapter(chapterUrl , chapterDesc );

		} catch (Exception e) {
			logger.error("[XmwbWebController] articleList error!" , e);
		}
		request.setAttribute("title", "文章列表");
		request.setAttribute("chapterDesc", chapterDesc);
		request.setAttribute("queryDate", queryDate);
		request.setAttribute("chapterUrl", chapterUrl);
		request.setAttribute("xmwbArticleVoList",list);
		return "/xmwb/articleList";
	}

	@RequestMapping(value = {"/articlePage"})
	public String articlePage(HttpServletRequest request, HttpServletResponse response) {
		String articleUrl = request.getParameter("articleUrl");
		String chapterDesc = request.getParameter("chapterDesc");

		XmwbArticleVo articleVo = new XmwbArticleVo();
		try {
			articleVo = xmwbService.queryXmwbArticleDetail(articleUrl,chapterDesc);

		} catch (Exception e) {
			logger.error("[XmwbWebController] articlePage error!" , e);
		}
		request.setAttribute("title", "文章");
		request.setAttribute("articleInfo",articleVo);
		return "/xmwb/articlePage";
	}


    @RequestMapping(value = {"/queryCollectList"})
    public String queryCollectList(HttpServletRequest request, HttpServletResponse response) {

        List<XmwbArticleVo>  list = new ArrayList<>();
        try {
            list = xmwbService.queryXmwbCollectList();

        } catch (Exception e) {
            logger.error("[XmwbWebController] articleList error!" , e);
        }
        request.setAttribute("title", "收藏列表");
        request.setAttribute("xmwbArticleVoList",list);
        return "/xmwb/printIndex";
    }

	@RequestMapping(value = {"/collectChapter"})
	@ResponseBody
	public Map<String, Object> collectChapter(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<>();
		String chapterUrl = request.getParameter("chapterUrl");
		String chapterDesc = request.getParameter("chapterDesc");


		if(StringUtils.isBlank(chapterUrl)) {
			ret.put(Constant.RET_CODE, 401);
			ret.put(Constant.RET_DESC, "参数有误！");
			return ret;
		}

		try {
			xmwbService.collectChapter(chapterUrl,chapterDesc);
			ret.put(Constant.RET_CODE, 200);
			ret.put(Constant.RET_DESC, "成功！");
		} catch (Exception e) {
			logger.error("[add2PrintList] add2PrintList error!" , e);
			ret.put(Constant.RET_CODE, 500);
			ret.put(Constant.RET_DESC, "服务异常！");
		}

		return ret;
	}

	@RequestMapping(value = {"/collectArticle"})
	@ResponseBody
	public Map<String, Object> collectArticle(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<>();
		String articleUrl = request.getParameter("articleUrl");
		String chapterDesc = request.getParameter("chapterDesc");


		if(StringUtils.isBlank(articleUrl)) {
			ret.put(Constant.RET_CODE, 401);
			ret.put(Constant.RET_DESC, "参数有误！");
			return ret;
		}

		try {
			xmwbService.collectArticle(articleUrl,chapterDesc);
			ret.put(Constant.RET_CODE, 200);
			ret.put(Constant.RET_DESC, "成功！");
		} catch (Exception e) {
			logger.error("[add2PrintList] add2PrintList error!" , e);
			ret.put(Constant.RET_CODE, 500);
			ret.put(Constant.RET_DESC, "服务异常！");
		}

		return ret;
	}

	@RequestMapping(value = {"/delAllPringtList"})
	@ResponseBody
	public Map<String, Object> delAllPringtList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<>();

		try {
			boolean delRet =  xmwbService.delAllPrintList();
			if(delRet) {
				ret.put(Constant.RET_CODE, 200);
				ret.put(Constant.RET_DESC, "成功！");
			}else {
				ret.put(Constant.RET_CODE, 500);
				ret.put(Constant.RET_DESC, "服务异常！");
			}
		} catch (Exception e) {
			logger.error("[delAllPringtList]  error!" , e);
			ret.put(Constant.RET_CODE, 500);
			ret.put(Constant.RET_DESC, "服务异常！");
		}

		return ret;
	}

	@RequestMapping(value = {"/delPrintList"})
	@ResponseBody
	public Map<String, Object> delFromPrintList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<>();
		String artId = request.getParameter("artId");

		if(StringUtils.isBlank(artId)) {
			ret.put(Constant.RET_CODE, 401);
			ret.put(Constant.RET_DESC, "参数有误！");
			return ret;
		}

		try {
			xmwbService.delFromPrintList(artId);
			ret.put(Constant.RET_CODE, 200);
			ret.put(Constant.RET_DESC, "成功！");
		} catch (Exception e) {
			logger.error("[add2PrintList] add2PrintList error!" , e);
			ret.put(Constant.RET_CODE, 500);
			ret.put(Constant.RET_DESC, "服务异常！");
		}

		return ret;
	}


	@RequestMapping(value = "/startTaskForPrintWord")
	@ResponseBody
	public Map<String, Object> startTaskForPrintWord(ServletRequest request) {

		Map<String, Object> ret = new HashMap<>();

		try {

			String path = "D:\\xmwb_" + DateUtil.formatDate(new Date(), DateUtil.FMT_YYYYMMDDHHMMSS)+".doc";

			xmwbService.startTaskForPrintWord(path);
			ret.put(Constant.RET_DETAIL, path);
			ret.put(Constant.RET_CODE, 200);
			ret.put(Constant.RET_DESC, "已经提交！文件地址："+ path);
		} catch (Exception e) {
			ret.put(Constant.RET_CODE, 500);
			ret.put(Constant.RET_DESC, "提交失败！");
			e.printStackTrace();
		}

		return ret;
	}
	
}
