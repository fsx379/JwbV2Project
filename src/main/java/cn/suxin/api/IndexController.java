package cn.suxin.api;


import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.suxin.util.IPUtil;
import cn.suxin.util.QrCodeCreateUtil;

@Controller
public class IndexController {
	@RequestMapping(value = {"/"})
    public void getArticleLists(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/jwb/lists");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return;
	}
	
	@RequestMapping(value = {"/qr"})
    public void getQr(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			String content = "http://" + IPUtil.loadServerIp();
			int qrCodeSize = 900;
			String imageFormat = "PNG";
			
			response.setContentType("image/png"); //设置返回的文件类型     
	        OutputStream os = response.getOutputStream();    
	        QrCodeCreateUtil.createQrCode(os, content, qrCodeSize, imageFormat);
	        os.flush();    
	        os.close();   
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return;
	}
	
	
	@RequestMapping(value = {"/qrIndex"})
    public String qrIndex(HttpServletRequest request, HttpServletResponse response) {
		
		return "/qrIndex";
	}
	
	
}
