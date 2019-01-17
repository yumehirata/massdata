package jp.co.rakus.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.service.DownloadXlxsService;

/**
 * Excelファイルをダウンロードするコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/dl")
public class DownloadXlxsController {
	
	@Autowired
	private DownloadXlxsService downloadXlxsService;
	
	@RequestMapping("/itemlist")
	public void downloadXlxs(Integer pageNumber,HttpServletResponse response) throws FileNotFoundException,IOException{
		
		Date date = new Date();
		String nowDate = new SimpleDateFormat("yyyyMMdd").format(date);
		String filename = nowDate + "itemList.xlsx";
		
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("content-disposition", String.format("attachment; filename=\"%s\"", filename));
        response.setCharacterEncoding("UTF-8");
        
        downloadXlxsService.setXlsx(downloadXlxsService.searchItem(pageNumber),response);
	}
}
