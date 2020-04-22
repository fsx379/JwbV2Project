package cn.suxin.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.suxin.constant.Constant;
import cn.suxin.constant.IniBean;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.ArticleInfoVo;
import cn.suxin.model.ArticleModelVO;
import cn.suxin.sevice.JwbService;
import cn.suxin.util.DateUtil;
import cn.suxin.util.JsonUtils;

@Controller
@RequestMapping("/jwb")
public class JwbWebController {
	private static Logger logger = LoggerFactory.getLogger(JwbWebController.class);
	
	@Autowired
	JwbService jwbService;
	
	
	/**jwbService
	 * 文章列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/lists"})
    public String getArticleLists(HttpServletRequest request, HttpServletResponse response) {
		String queryDate = request.getParameter("queryDate");
		
		Date startDate = null;
		Date endDate = null;
		
		if(StringUtils.isNotBlank(queryDate)) {
			queryDate = queryDate.trim();
			startDate = DateUtil.getToday(DateUtil.formatString(queryDate,  "yyyy-MM-dd"));
			endDate = startDate;
		}
		
		if(startDate == null) {
			Timestamp hour10 = DateUtil.get10ClockOfToday();
			Timestamp currentTime = DateUtil.getCurrentTimestamp();
			if(currentTime.compareTo(hour10) > 0) {
				startDate = DateUtil.getToday(currentTime);
			}else {
				startDate = DateUtil.getIntervalBeginOfDay(currentTime, 1);
			}
			
			
			endDate = startDate;
			queryDate = DateUtil.formatDate(startDate, "yyyy-MM-dd");
		}
		
		logger.info("[getArticleLists] queryDate = {}" , queryDate);
		
		request.setAttribute("title", "文章列表");
		request.setAttribute("queryDate", queryDate);
		
		try {
			
			List<ArticleModelVO>  articleLists = new ArrayList<ArticleModelVO>();
			
			int queryNum  = 0;
			int maxNum = 3;
			Set<String> setList = jwbService.getPrintList();
			while(queryNum < maxNum) {
				logger.info("[getArticleLists] queryNum = {}" , queryNum);
				List<ArticleInfoVo> articleVoLists = jwbService.queryArticleList(startDate, endDate);
				
				if(articleVoLists != null && articleVoLists.size() > 0) {
					for(ArticleInfoVo vo : articleVoLists) {
						if(vo.getArtList() != null && vo.getArtList().size() > 0) {
							for(ArticleInfo info : vo.getArtList()) {
								ArticleModelVO articlevo = new ArticleModelVO();
								BeanUtils.copyProperties(info, articlevo);
								
								if(setList.contains(articlevo.getArtId())) {
									articlevo.setCollect(1);
								}
								
								articleLists.add(articlevo);
								queryNum = maxNum;
							}
						}
					}
				} 
				
				
				if(queryNum ==0 ) {
					logger.info("[getArticleLists] queryNum = {} , taskStart " , queryNum);
					jwbService.taskStart( "任务" + IniBean.generId(), startDate, endDate);
				}
				if(queryNum < maxNum) {
					Thread.sleep(2000);
				}
				queryNum++;
				
				
			} 
			
			request.setAttribute("articleLists",articleLists);
			
		} catch (Exception e) {
			logger.error("[JwbWebController] getArticleLists error!" , e);
		} 
		
		return "/index";
	}
	
	
	@RequestMapping(value = {"/addPrintList"})
	@ResponseBody
    public Map<String, Object> add2PrintList(HttpServletRequest request, HttpServletResponse response) {
		 Map<String, Object> ret = new HashMap<>();
		String artId = request.getParameter("artId");
		
		if(StringUtils.isBlank(artId)) {
			ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
            return ret;
		}
		
		try {
			jwbService.addPrintList(artId);
			ret.put(Constant.RET_CODE, 200);
            ret.put(Constant.RET_DESC, "成功！");
		} catch (Exception e) {
			logger.error("[add2PrintList] add2PrintList error!" , e);
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
			jwbService.delFromPrintList(artId);
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
			boolean delRet =  jwbService.delAllPrintList();
			if(delRet) {
				ret.put(Constant.RET_CODE, 200);
	            ret.put(Constant.RET_DESC, "成功！");
			}else {
				ret.put(Constant.RET_CODE, 500);
	            ret.put(Constant.RET_DESC, "服务异常！");
			}
		} catch (Exception e) {
			logger.error("[add2PrintList] add2PrintList error!" , e);
			ret.put(Constant.RET_CODE, 500);
            ret.put(Constant.RET_DESC, "服务异常！");
		} 
		
		return ret;
	}
	
	
	@RequestMapping(value = {"/getPrintLists"})
    public String getPrintLists(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("title", "收藏列表");
		try {
			List<ArticleModelVO> articleLists = new ArrayList<ArticleModelVO>();
			List<ArticleInfo> artList = jwbService.getPrintArticleList();
			if(artList != null && artList.size() >0) {
				for(ArticleInfo info : artList) {
					if(info != null) {
						ArticleModelVO vo = new ArticleModelVO();
						BeanUtils.copyProperties(info, vo);
						articleLists.add(vo);
					}else {
						logger.error("[getPrintLists]  info = {}" , JsonUtils.toJson(info));
					}
					
				}
			}
			request.setAttribute("articleLists",articleLists);
		} catch (Exception e){
			logger.error("[getPrintLists]  error!" , e);
		} 
		
		return "/printIndex";
	}
	
	@RequestMapping(value = {"/articleDetail"})
    public String articleDetail(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("title", "文章详情");
		
		String artId = request.getParameter("artId");
		
		try {
			ArticleInfo info  = jwbService.queryArticleDetail(artId);
			ArticleModelVO vo = new ArticleModelVO();
			if(info != null) {
				BeanUtils.copyProperties(info, vo);
			}
			logger.info("[articleDetail] {} " , JsonUtils.toJson(vo));
			request.setAttribute("articleInfo",vo);
		} catch (Exception e){
			logger.error("[articleDetail]  error!" , e);
		} 
		
		return "/artDetail";
	}
	
	
}
