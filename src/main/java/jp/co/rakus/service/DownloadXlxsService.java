package jp.co.rakus.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.Item;

/**
 * Excelファイルでアイテムリストを出力する.
 * 
 * @author yume.hirata
 *
 */
@Service
public class DownloadXlxsService {

	@Autowired
	private HttpSession session;
	@Autowired
	private SearchItemService searchItemService;

	/**
	 * セッションの情報からダウンロードするアイテムリストを探してくる.
	 * 
	 * @param pageNumber
	 *            現在位置
	 * @return アイテムリスト
	 */
	public List<Item> searchItem(Integer pageNumber) {

		String name = null;
		String brand = null;
		Integer largeCategory = null;
		Integer middleCategory = null;
		Integer smallCategory = null;
		boolean isBrandSearch = false;

		if (pageNumber == null || pageNumber <= 0) {
			pageNumber = 1;
		}
		if (session.getAttribute("name") != null) {
			name = session.getAttribute("name").toString();
		}
		if (session.getAttribute("brand") != null) {
			brand = session.getAttribute("brand").toString();
		}
		if (session.getAttribute("largeCategory") != null) {
			largeCategory = Integer.parseInt(session.getAttribute("largeCategory").toString());
		}
		if (session.getAttribute("middleCategory") != null) {
			middleCategory = Integer.parseInt(session.getAttribute("middleCategory").toString());
		}
		if (session.getAttribute("smallCategory") != null) {
			smallCategory = Integer.parseInt(session.getAttribute("smallCategory").toString());
		}
		if (session.getAttribute("isBrandSearch") != null) {
			isBrandSearch = Boolean.valueOf(session.getAttribute("isBrandSearch").toString());
		}

		List<Item> itemList = searchItemService.searchItem(name, smallCategory, middleCategory, largeCategory, brand,
				pageNumber, isBrandSearch);

		return itemList;
	}

	/**
	 * DLするExcelファイルの内容を設定する.
	 * 
	 * @param itemList	ファイルに書き出す内容
	 * @param response	response
	 */
	public void setXlsx(List<Item> itemList, HttpServletResponse response) {

		Workbook workbook = null;
		OutputStream outputStream = null;

		try {
			workbook = new SXSSFWorkbook();
			outputStream = response.getOutputStream();

			Sheet sheet = workbook.createSheet();
			if (sheet instanceof SXSSFSheet) {
				((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
			}
			
			Row row;
			Cell cell;
			int rowNumber = 0;
			int colNumber = 0;

			// ヘッダの設定
			row = sheet.createRow(rowNumber);
			cell = row.createCell(colNumber++);
			cell.setCellValue("ID");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Name");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Price");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Category");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Brand");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Condition");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Shipping");
			cell = row.createCell(colNumber++);
			cell.setCellValue("Description");

			// ヘッダ行にオートフィルタを設定
			sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, colNumber));

			// 列幅の自動調整
			for (int i = 0; i <= colNumber; i++) {
				sheet.autoSizeColumn(i, true);
			}

			// データ生成
			for (Item item : itemList) {
				rowNumber++;
				colNumber = 0;

				row = sheet.createRow(rowNumber);
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getId());
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getName());
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getPrice());
				cell = row.createCell(colNumber++);
				if (item.getSmallCategory().getName() != null ) {
					cell.setCellValue(item.getLargeCategory().getName() + "/" + item.getMiddleCategory().getName() + "/"
							+ item.getSmallCategory().getName());
				} 
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getBrand());
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getCondition());
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getShipping());
				cell = row.createCell(colNumber++);
				cell.setCellValue(item.getDescription());

				for (int j = 0; j <= colNumber; j++) {
					sheet.autoSizeColumn(j, true);
				}
			}

			workbook.write(outputStream);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
