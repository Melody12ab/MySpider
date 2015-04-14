package com.caidongyu.application.io.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.helper.StringUtil;

import com.caidongyu.application.log.LogUtil;
import com.caidongyu.application.text.NumberUtil;


public class ExcelUtil8POI {
	
	
	public static void generate(){
		try {
			XSSFWorkbook book = new XSSFWorkbook("1.xlsx");
			XSSFSheet sheet = book.getSheetAt(0);
			
			ArrayList<XSSFCell> cell1List = new ArrayList<XSSFCell>();
			ArrayList<XSSFCell> cell2List = new ArrayList<XSSFCell>();
			for(int rowId=0;rowId<100;rowId++){
				XSSFRow row = sheet.getRow(rowId);
				if(row != null){
					for(int colId=0;colId<100;colId++){
						XSSFCell cell = row.getCell(colId);
						if(cell != null){
							switch(cell.getCellType()){
								case XSSFCell.CELL_TYPE_BLANK:break;
								case XSSFCell.CELL_TYPE_BOOLEAN:break;
								case XSSFCell.CELL_TYPE_ERROR:break;
								case XSSFCell.CELL_TYPE_FORMULA:break;
								case XSSFCell.CELL_TYPE_NUMERIC:break;
								case XSSFCell.CELL_TYPE_STRING:
									String content = cell.getStringCellValue();
									content = StringUtil.isBlank(content)?"":content.trim();
									
									Pattern pattern1 = Pattern.compile("^\\[([S|M|B])?(\\d+)(\\.(\\d+))?]$");
									Matcher match1 = pattern1.matcher(content);
									if(match1.find()){
										cell1List.add(cell);break;
									}
									Pattern pattern2 = Pattern.compile("^\\[([S|M|B])?(\\d+)(\\.(\\d+))?](-([A-Z])(\\d+))$");
									Matcher match2 = pattern2.matcher(content);
									if(match2.find()){
										cell2List.add(cell);break;
									}
									
									LogUtil.print("原始数据:"+content);
									
									/*if(content.startsWith("V")){
										String command = cell.getStringCellValue().substring(1);
										int dotIndex = command.indexOf(".");
										String pre = command.substring(0,dotIndex);
										String last = command.substring(dotIndex+1);
										pre = NumberUtil.getRandom(Integer.parseInt(pre));
										last = NumberUtil.getRandom(Integer.parseInt(last));
										cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
										cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
										cell.setCellStyle(cell.getCellStyle());
										cell.setCellValue(pre+"."+last);
									}*/
									break;
								default:break;
							}
						}
					}
				}
			}
			//普通自动填充
			for(XSSFCell cell:cell1List){
				String content = cell.getStringCellValue();
				System.out.println(content);
				content = StringUtil.isBlank(content)?"":content.trim();
				Pattern pattern = Pattern.compile("^\\[([S|M|B])?(\\d+)(\\.(\\d+))?]$");
				Matcher match = pattern.matcher(content);
				match.find();
				String smb = match.group(1);
				String lenPre = match.group(2);
				String lenLas = match.group(4);
				
				int small = 1;
				int big = 9;
				if("S".equals(smb)){
					small = 1;
					big = 3;
				}
				if("M".equals(smb)){
					small = 4;
					big = 6;
				}
				if("B".equals(smb)){
					small = 7;
					big = 9;
				}
				StringBuffer value = new StringBuffer();
				if(!StringUtil.isBlank(lenPre)){
					String pre = NumberUtil.getRandomBetween(Integer.parseInt(lenPre),small,big);
					value.append(pre);
				}
				if(!StringUtil.isBlank(lenLas)){
					String las = NumberUtil.getRandomBetween(Integer.parseInt(lenLas),small,big);
					value.append(".").append(las);
				}
				
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
				cell.setCellStyle(cell.getCellStyle());
				cell.setCellValue(value.toString());
			}
			//需要依赖值的特殊填充
			for(XSSFCell cell:cell2List){
				String content = cell.getStringCellValue();
				System.out.println(content);
				content = StringUtil.isBlank(content)?"":content.trim();
				Pattern pattern = Pattern.compile("^\\[([S|M|B])?(\\d+)(\\.(\\d+))?](-([A-Z])(\\d+))$");
				Matcher match = pattern.matcher(content);
				match.find();
				String smb = match.group(1);
				String lenPre = match.group(2);
				String lenLas = match.group(4);
				
				String col = match.group(6);
				String row = match.group(7);
				
				int small = 1;
				int big = 9;
				if("S".equals(smb)){
					small = 1;
					big = 3;
				}
				if("M".equals(smb)){
					small = 4;
					big = 6;
				}
				if("B".equals(smb)){
					small = 7;
					big = 9;
				}
				StringBuffer value = new StringBuffer();
				if(!StringUtil.isBlank(lenPre)){
					String pre = NumberUtil.getRandomBetween(Integer.parseInt(lenPre),small,big);
					value.append(pre);
				}
				if(!StringUtil.isBlank(lenLas)){
					String las = NumberUtil.getRandomBetween(Integer.parseInt(lenLas),small,big);
					value.append(".").append(las);
				}
				
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
				cell.setCellStyle(cell.getCellStyle());
				cell.setCellValue(value.toString());
			}
			
			book.getCreationHelper().createFormulaEvaluator().evaluateAll();
			book.write(new FileOutputStream(new File("tmp.xlsx")));
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		ExcelUtil8POI.generate();
	
//		BigDecimal sum = new BigDecimal(10.4);
//		MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
//		BigDecimal result = sum.subtract(new BigDecimal(2.1),mc);
//		System.out.println(result.toPlainString());
		
		Pattern pattern2 = Pattern.compile("^\\[([S|M|B])?(\\d+)(\\.(\\d+))?](-([A-Z])(\\d+))$");
		Matcher match2 = pattern2.matcher("[M7.2]-D23");
		match2.find();
		System.out.println(match2.group(7));
		
	}
	
	
	
	
	
}
