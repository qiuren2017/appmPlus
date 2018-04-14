package com.cdfortis.utils.file;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: PoiExcelUtil
 * @Description: POI操作Excel工具类
 * @author chenhx
 * @date 2017年4月26日 下午2:55:53
 * @version 0.0.1
 */
@SuppressWarnings("deprecation")
public class PoiExcelUtil {
	
	/**
	 * 
	 * @MethodName: WriteExcel
	 * @Description: 写入Excel
	 * @param fileName		文件名
	 * @param titleArray	表头数组
	 * @param keyArray		表头key值数组
	 * @param DataList		数据列表
	 * @return
	 * @throws IOException HSSFWorkbook
	 */
	public static HSSFWorkbook WriteExcel(String fileName,String[] titleArray,String keyArray[],List<JSONObject> DataList) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();			// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet("sheet1");		// 建立新的sheet对象
		
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,titleArray.length-1)); 	 //合并单元格  标题
		
        //标题字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setColor(Font.COLOR_RED);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeight((short) 300);

		//表头字体
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(Font.BOLDWEIGHT_NORMAL);
		
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		printSetup.setFitHeight((short) 100);
		printSetup.setFitWidth((short) 180);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		sheet.createFreezePane(0, 2);
		sheet.setAutobreaks(true);
		sheet.setDefaultColumnWidth((short) 18);
		
		//标题样式
		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFont(titleFont);
		titlestyle.setAlignment(CellStyle.ALIGN_CENTER);
//		titlestyle.setFillForegroundColor(HSSFColor.WHITE.index);
//		titlestyle.setFillPattern(HSSFCellStyle.SQUARES);
		titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
		titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
		titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
		// titlestyle.setFillPattern((short)300);
		titlestyle.setWrapText(true);
		
		//表头样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(headerFont);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setHidden(true);
		
		// 内容样式
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	
		//写入标题
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(30.120f);
		HSSFCell titlecell = titleRow.createCell((short) 0);// 标题
		//titlecell.setEncoding(HSSFCell.ENCODING_UTF_16);
		titlecell.setCellStyle(titlestyle);
		titlecell.setCellValue(fileName);

		//写入表头
		HSSFRow headRow = sheet.createRow((short) 1);
		headRow.setHeightInPoints(20.120f);
		for (int i = 0; i < titleArray.length; i++) {
			HSSFCell cell = headRow.createCell(i);
			cell.setCellStyle(style);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);	//设置单元格为字符型
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);	// 设置cell编码解决中文高位字节截断
			cell.setCellValue(titleArray[i]);
		}
		
		//写入内容
		for (int i = 0; i < DataList.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);			// 建立新行
			JSONObject job = DataList.get(i);
			for (int j = 0; j < job.size(); j++) {
				// 新建一列
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);	// 定义单元格为字符串类型
				Object t = job.getString(keyArray[j]);
				if(t!=null){
					cell.setCellValue(t.toString());
				}else{
					cell.setCellValue("");
				}
				
			}
		}
		return wb;
	}
	
	
}
