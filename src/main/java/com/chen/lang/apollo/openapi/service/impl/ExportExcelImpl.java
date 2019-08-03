package com.chen.lang.apollo.openapi.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.chen.lang.apollo.openapi.service.ExportExcel;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.chen.lang.apollo.openapi.util.ApolloPropertiesUtil;
import com.chen.lang.apollo.openapi.util.CollectionUtil;
import com.chen.lang.apollo.openapi.util.FileUtil;
import com.chen.lang.apollo.openapi.util.ScopeUtil;
import com.chen.lang.apollo.openapi.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-29 11:02
 */
@Slf4j
@Service
public class ExportExcelImpl implements ExportExcel {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    private static Integer currentRow = 0;
    private static Workbook workbook;

    private static CellStyle menuStyle;

    private static CellStyle cellStyle;

    private static CellStyle linkStyle;

    //app信息
    private static List<OpenAppDTO> appList;

    //导出的环境
    private String env;

    /**
     * 导出配置
     *
     * @throws Exception
     */
    @Override
    public void generateConfigExcel(String env) throws Exception {
        this.env = env;
        FileOutputStream xlsStream = null;
        try {
            workbook = new XSSFWorkbook();
            menuStyle = formatMenu();
            cellStyle = formatCell();
            linkStyle = formatLinkCell();
            //模拟数据
            //mockData();
            //获取接口信息
            appList = apolloConfigFileService.listApps();
            appList = ScopeUtil.filterApps(appList);
            // appList = appList.subList(0, 2);

            // String appid = appList.get(0)
            //     .getAppId();
            // String clusterName = "default";
            // String env = "DEV";
            // List<OpenNamespaceDTO> namespaceDTOList = apolloConfigFileService.queryOpenNamespaceDTOList(appid,
            //     clusterName, env);
            //2、生成excel
            exportExcel();

            String path = ApolloPropertiesUtil.getExportExcelPath();
            FileUtil.createDirectories(path);
            String fileName = ApolloPropertiesUtil.getExportExcelName(env);
            String outputPath = String.join(File.separator, path, fileName);
            File xlsFile = new File(outputPath);
            if (!xlsFile.exists()) {
                xlsFile.createNewFile();
            }
            //输出文件
            xlsStream = new FileOutputStream(xlsFile);
            workbook.write(xlsStream);
            log.info("文档生成成功！,生成路径：{}", outputPath);
        } catch (Exception e) {
            log.error("生成接口文档出错：{}", e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (xlsStream != null) {
                xlsStream.close();
            }
        }
    }

    /**
     * 导出excel
     */
    private void exportExcel() throws Exception {
        //创建目录
        createCatalogSheet();
        //创建公共报文sheet
        // createCommonParamSheet();
        // //创建接口sheet
        createDocSheet();
    }

    /**
     * 创建目录
     */
    private static void createCatalogSheet() throws Exception {
        if (CollectionUtil.isEmpty(appList)) {
            throw new Exception("app 数据为空");
        }
        Collections.sort(appList, (o1, o2) -> {
            int res = o1.getAppId()
                .compareTo(o2.getAppId());
            return res;
        });

        Sheet catalogSheet = workbook.createSheet("目录");
        catalogSheet.setDisplayGridlines(false);
        Row catalogRow = catalogSheet.createRow(0);
        String[] menus = new String[] {"序号", "应用APPID", "应用名称", "所属组织ID", "所属组织", "链接详情"};
        for (int i = 0; i < menus.length; i++) {
            Cell cell = catalogRow.createCell(i);
            cell.setCellStyle(menuStyle);
            cell.setCellValue(menus[i]);
        }
        //创建目录列表
        int currentRow = 1;
        int i = 1;
        for (OpenAppDTO openAppDTO : appList) {
            Row tableRow = catalogSheet.createRow(currentRow);
            //序号
            Cell facadeNumCell = tableRow.createCell(0);
            //接口类名
            Cell appIdCell = tableRow.createCell(1);
            //接口描述
            Cell appNameCell = tableRow.createCell(2);
            //请求方式
            Cell appOrgIdCell = tableRow.createCell(3);
            //接口路径
            Cell appOrgNameCell = tableRow.createCell(4);

            Cell linkCell = tableRow.createCell(5);

            facadeNumCell.setCellValue(i);
            appIdCell.setCellValue(openAppDTO.getAppId());
            appNameCell.setCellValue(openAppDTO.getName());
            appOrgIdCell.setCellValue(openAppDTO.getOrgId());
            appOrgNameCell.setCellValue(openAppDTO.getOrgName());
            linkCell.setCellValue(openAppDTO.getName());
            facadeNumCell.setCellStyle(cellStyle);
            appIdCell.setCellStyle(cellStyle);
            appNameCell.setCellStyle(cellStyle);
            appOrgIdCell.setCellStyle(cellStyle);
            appOrgNameCell.setCellStyle(cellStyle);
            linkCell.setCellStyle(linkStyle);

            appIdCell.setCellType(HSSFCellStyle.ALIGN_LEFT);
            appNameCell.setCellType(HSSFCellStyle.ALIGN_LEFT);
            appOrgIdCell.setCellType(HSSFCellStyle.ALIGN_LEFT);
            appOrgNameCell.setCellType(HSSFCellStyle.ALIGN_LEFT);
            linkCell.setCellType(HSSFCellStyle.ALIGN_LEFT);

            Hyperlink hyperlink = workbook.getCreationHelper()
                .createHyperlink(Hyperlink.LINK_DOCUMENT);
            String sheetName = openAppDTO.getAppId();
            if (sheetName.length() > 31) {
                sheetName = sheetName.substring(0, 31);
            }
            hyperlink.setAddress("#'" + sheetName + "'!D1");
            linkCell.setHyperlink(hyperlink);
            currentRow = currentRow + 1;
            i++;
        }
        //自适应宽度
        for (int j = 0; j < menus.length; j++) {
            catalogSheet.autoSizeColumn(i);
            catalogSheet.setColumnWidth(i, 5000);
            catalogSheet.autoSizeColumn(j, true);
        }
    }

    /**
     * 格式化菜单
     *
     * @return HSSFCellStyle
     */
    private static CellStyle formatMenu() {
        //设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);
        font.setBoldweight((short) 1);
        //粗体
        //font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //设置背景颜色
        style.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        return style;
    }

    /**
     * 格式化菜单
     *
     * @return HSSFCellStyle
     */
    private static CellStyle formatTag() {
        //设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight((short) 1);
        //粗体
        //font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //设置背景颜色
        style.setFillBackgroundColor((byte) 64);
        style.setFillForegroundColor((byte) 22);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        return style;
    }

    /**
     * 格式化单元格
     *
     * @return HSSFCellStyle
     */
    private static CellStyle formatCell() {

        //设置字体
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);

        CellStyle style = workbook.createCellStyle();

        style.setFont(font);

        //居中
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        //自动换行
        //style.setWrapText(true);
        return style;
    }

    /**
     * 链接
     *
     * @return
     */
    private static CellStyle formatLinkCell() {

        //设置字体
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        font.setColor(HSSFColor.BLUE.index);
        font.setUnderline((byte) 1);

        CellStyle style = workbook.createCellStyle();

        style.setFont(font);

        //居中
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        //自动换行
        //style.setWrapText(true);
        return style;
    }

    /**
     * 创建接口sheet
     */
    private void createDocSheet() throws Exception {
        if (CollectionUtil.isEmpty(appList)) {
            throw new Exception("app 数据为空");
        }
        Collections.sort(appList, (o1, o2) -> {
            int res = o1.getAppId()
                .compareTo(o2.getAppId());
            return res;
        });
        int ii = 1;
        for (OpenAppDTO openAppDTO : appList) {

            log.info("开始生成应用 {}的文档信息", openAppDTO.getName());
            Sheet sheet = null;
            try {
                String sheetName = openAppDTO.getAppId();
                if (sheetName.length() > 31) {
                    sheetName = sheetName.substring(0, 31);
                }
                sheet = workbook.createSheet(sheetName.toString());
                sheet.setDisplayGridlines(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //接口信息
            createFacadeSheetRow(openAppDTO, sheet, ii);
            // 自适应宽度
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, 5000);
                //sheet.setDefaultColumnStyle(i, cellStyle);
                //自适应宽度
                sheet.autoSizeColumn(i, true);
            }
            ii++;
        }
    }

    private void createFacadeSheetRow(OpenAppDTO openAppDTO, Sheet sheet, Integer sheetNum) {

        currentRow = 0;

        String title = openAppDTO.getAppId() + "  desc:" + openAppDTO.getName();
        Row row = sheet.createRow(currentRow);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 1, 5));
        String[] menus = null;
        Cell cell = row.createCell(1);
        cell.setCellStyle(formatMenu());
        cell.setCellValue(title);
        // Cell cell1 = row.createCell(1);
        // cell1.setCellStyle(formatMenu());
        // cell1.setCellValue("");
        // Cell cell2 = row.createCell(2);
        // cell2.setCellStyle(formatMenu());
        // cell2.setCellValue("");
        // Cell cell3 = row.createCell(3);
        // cell3.setCellStyle(formatMenu());
        // cell3.setCellValue("");
        // Cell cell4 = row.createCell(4);
        // cell4.setCellStyle(formatMenu());
        // cell4.setCellValue("");

        Cell cell5 = row.createCell(0);
        Hyperlink hyperlink = workbook.getCreationHelper()
            .createHyperlink(Hyperlink.LINK_DOCUMENT);
        hyperlink.setAddress("#目录!C" + (sheetNum + 1));
        cell5.setCellStyle(linkStyle);
        cell5.setHyperlink(hyperlink);
        cell5.setCellValue("返回目录");

        String appid = openAppDTO.getAppId();
        String clusterName = "default";
        // String env = "DEV";
        List<OpenNamespaceDTO> namespaceDTOList = apolloConfigFileService.queryOpenNamespaceDTOList(appid, clusterName,
            env);

        for (OpenNamespaceDTO openNamespaceDTO : namespaceDTOList) {
            // system-default,system-custom
            if (!openNamespaceDTO.getNamespaceName()
                .contains("system-default") && !openNamespaceDTO.getNamespaceName()
                .contains("system-custom")) {

                // 000 application  *.system 三种 namesapce
                if (openNamespaceDTO.getNamespaceName()
                    .contains("000") || openNamespaceDTO.getNamespaceName()
                    .contains("system") || openNamespaceDTO.getNamespaceName()
                    .contains("application")) {
                    createEnumRow(sheet, openNamespaceDTO, false);
                }

            }

        }

    }

    /**
     * 创建枚举信息
     *
     * @param sheet
     */
    private static void createEnumRow(Sheet sheet, OpenNamespaceDTO openNamespaceDTO, boolean first) {
        if (openNamespaceDTO != null) {
            if (!first) {
                currentRow += 1;
            }
            Row row = sheet.createRow(currentRow);
            sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
            String[] menus = null;
            Cell cell = row.createCell(0);
            cell.setCellStyle(formatMenu());
            cell.setCellValue(
                openNamespaceDTO.getNamespaceName() + " isPublic:" + openNamespaceDTO.isPublic() + " comment:"
                    + openNamespaceDTO.getComment());
            Cell cell1 = row.createCell(1);
            cell1.setCellStyle(formatMenu());
            cell1.setCellValue("");
            Cell cell2 = row.createCell(2);
            cell2.setCellStyle(formatMenu());
            cell2.setCellValue("");
            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(formatMenu());
            cell3.setCellValue("");
            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(formatMenu());
            cell4.setCellValue("");
            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(formatMenu());
            cell5.setCellValue("");
            menus = new String[] {"clusterName", "namespaceName", "key", "value", "备注", "是否公共属性"};
            currentRow += 1;
            Row columnMethodRow = sheet.createRow(currentRow);
            for (int i = 0; i < menus.length; i++) {
                cell = columnMethodRow.createCell(i);
                cell.setCellStyle(formatMenu());
                cell.setCellValue(menus[i]);
            }
            createEnumFieldRow(sheet, openNamespaceDTO, menus);
        }
    }

    /**
     * 创建枚举详细信息
     *
     * @param sheet
     * @param menus
     */
    private static void createEnumFieldRow(Sheet sheet, OpenNamespaceDTO openNamespaceDTO, String[] menus) {

        int keyAmount = 0;
        for (OpenItemDTO openItemDTO : openNamespaceDTO.getItems()) {

            if (StringUtil.isBlank(openItemDTO.getKey())) {

                continue;
            } else {
                keyAmount += 1;
                currentRow += 1;
            }

            Row row = sheet.createRow(currentRow);
            for (int i = 0; i < menus.length; i++) {

                Cell cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellType(HSSFCellStyle.ALIGN_LEFT);

                // {"clusterName", "namespaceName", "key", "value", "备注", "是否公共属性"};
                switch (i) {
                    case 0:
                        cell.setCellValue(openNamespaceDTO.getClusterName());
                        break;
                    case 1:
                        cell.setCellValue(openNamespaceDTO.getNamespaceName());
                        break;
                    case 2:
                        cell.setCellValue(openItemDTO.getKey());
                        break;
                    case 3:
                        cell.setCellValue(openItemDTO.getValue());
                        break;
                    case 4:
                        cell.setCellValue(openItemDTO.getComment());
                        break;
                    case 5:
                        cell.setCellValue(openNamespaceDTO.isPublic());
                        break;
                }
            }
        }
        if (keyAmount > 1) {
            sheet.addMergedRegion(new CellRangeAddress(currentRow - keyAmount + 1, currentRow, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(currentRow - keyAmount + 1, currentRow, 1, 1));
        } else {
            log.info("{}没有查询到配置信息---------------------" + JSON.toJSONString(openNamespaceDTO));
        }

    }
}
