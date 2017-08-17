package com.zes.squad.gmh.web.view;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import lombok.Cleanup;

public class ExcelView extends AbstractXlsxStreamingView {

    @Override
    public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                   HttpServletResponse response)
            throws Exception {
        String fileName = "record.xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        @Cleanup
        OutputStream output = response.getOutputStream();
        workbook.write(output);
    }

}
