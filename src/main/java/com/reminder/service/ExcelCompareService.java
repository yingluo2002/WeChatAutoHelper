package com.reminder.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcelCompareService {
    private final String referenceFile;

    public ExcelCompareService(String referenceFile) {
        this.referenceFile = referenceFile;
    }

    public List<String> findMissingNames(String newFile) {
        Set<String> referenceNames = new HashSet<>();
        Set<String> newFileNames = new HashSet<>();
        List<String> missingNames = new ArrayList<>();

        try {
            // 读取参考表
            FileInputStream refFis = new FileInputStream(referenceFile);
            Workbook refWorkbook = new XSSFWorkbook(refFis);
            Sheet refSheet = refWorkbook.getSheetAt(0);

            // 读取新表
            FileInputStream newFis = new FileInputStream(newFile);
            Workbook newWorkbook = new XSSFWorkbook(newFis);
            Sheet newSheet = newWorkbook.getSheetAt(0);

            // 获取姓名列索引（假设姓名列的标题是"姓名"）
            int refNameColIndex = findColumnIndex(refSheet);
            int newNameColIndex = findColumnIndex(newSheet);

            // 读取参考表中的所有姓名
            for (Row row : refSheet) {
                if (row.getRowNum() == 0) continue; // 跳过表头
                Cell cell = row.getCell(refNameColIndex);
                if (cell != null) {
                    referenceNames.add(getCellValueAsString(cell));
                }
            }

            // 读取新表中的所有姓名
            for (Row row : newSheet) {
                if (row.getRowNum() == 0) continue; // 跳过表头
                Cell cell = row.getCell(newNameColIndex);
                if (cell != null) {
                    newFileNames.add(getCellValueAsString(cell));
                }
            }

            // 找出缺少的姓名
            for (String name : referenceNames) {
                if (!newFileNames.contains(name)) {
                    missingNames.add(name);
                }
            }

            // 关闭资源
            refWorkbook.close();
            newWorkbook.close();
            refFis.close();
            newFis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return missingNames;
    }

    private int findColumnIndex(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (getCellValueAsString(cell).equals("姓名")) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> "";
        };
    }
}