package com.edu.analysis;

import com.edu.exception.MyException;
import com.google.gson.JsonArray;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;

//Access to clean fuels and technologies for cooking (% of population)
public class CleanFuelsAnalisis extends AbstractAnlyzer{

    

    @Override
    /**
	 * do analysis
	 */
    public void analysis() {
    	//from date and to date must equals
        if (!toDate.equals(fromDate)){
            throw new MyException("must have a selected years ");
        }
        //initial data
        data = "Access to clean fuels and technologies for cooking (% of population):\n";
        //get data
        JsonArray jsonArray = getResponse(FOREST_TYPE_STR_0);
        //create pie chart
        createPieChart(jsonArray);
        //create bar chart
        createBarChart(jsonArray);
    }

    /**
     * create pie chart
     * @param jsonArray data 
     */
    private void createPieChart(JsonArray jsonArray){
    	//year
        int date = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("date").getAsInt();
        //get value
        double persent = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble();
        //instance data set
        DefaultPieDataset<String> mDataset = new DefaultPieDataset<>();
        mDataset.setValue("have clean fuels "+persent+"%",persent);
        mDataset.setValue("can't have " +(100-persent)+"%" ,100-persent);
        //set value to data set
        data+="Access to clean fuels and technologies for cooking (% of population) for " + date+":"+persent+"%";
//        create pie chart
        JFreeChart chart = ChartFactory.createPieChart("Access to clean fuels and technologies for cooking (% of population) for selected year",mDataset,true,true,false);
        //set title
        chart.setTitle(new TextTitle("Access to clean fuels and technologies for cooking (% of population) for " + fromDate, new Font("Times New Roman",Font.CENTER_BASELINE, 10)));
        //set font style
        chart.getLegend().setItemFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 5));
        //add chart panel to result set
        result.put("Pie chart",new ChartPanel(chart));

    }

    /**
     * create bar chart
     * @param jsonArray data
     */
    private void createBarChart(JsonArray jsonArray){
    	//get date
        int date = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("date").getAsInt();
        //get value
        double persent = jsonArray.get(1).getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble();
        //create data set
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        //set value to data set
        mDataset.setValue(persent,"have clean fuels",""+date);
        //set value to data set
        mDataset.setValue(100-persent,"other",""+date);
        //create bar chart 
        JFreeChart chart = ChartFactory.createBarChart("forest ",
                "have clean fuels (% of land area)",
                "%",
                mDataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        //get plot to chart
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        //get x axis 
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        //set auto tick false
        numberAxis.setAutoTickUnitSelection(false);
        //set tick unit 10
        numberAxis.setTickUnit(new NumberTickUnit(10));
        //set auto range true
        numberAxis.setAutoRangeStickyZero(true);
        //set range type is positive
        numberAxis.setRangeType(RangeType.POSITIVE);
        //set number format is 0.00
        numberAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        //get bar render
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        //set label
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00")));
      //add chart panel to result set
        result.put("Bar chart",new ChartPanel(chart));
    }
}
