package org.robert.study.service;

import java.io.IOException;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.iisi.rl.table.discrepancy.DiscrepancyReport;

public interface CompareDBTableAnd64TableDisplayService {

    public void exportReport3(final DiscrepancyReport report, final String fileName) throws ParsePropertyException,
            InvalidFormatException, IOException;

    public void display(DiscrepancyReport report);
    public Map<String,String>  summaryData(DiscrepancyReport report);
    public  Map<String, String[]> sumaryComparison(final Map<String, String>... sumarys) ;

}
