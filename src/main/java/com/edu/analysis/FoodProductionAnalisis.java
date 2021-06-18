package com.edu.analysis;

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
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.text.DecimalFormat;

//Food production index (2004-2006 = 100)
public class FoodProductionAnalisis extends AbstractAnlyzer{

    @Override
    /**
     * do analysis
     */
    public void analysis() {
    	//initial data
        data = "Food production index (2004-2006 = 100):\n";
        //get data from network
        JsonArray array = getResponse(MORTALITY_TYPE_STR_0);
        //create line chart
        createLineChart(array);
        //create scatter chart
        createScattChart(array);
        //create bar chart
        createBarChart(array);
    }

   

    /**
     * create scatter chart
     * @param jsonArray data
     */
    private void createScattChart(JsonArray jsonArray){
    	//create data collection
        XYSeriesCollection dataset = new XYSeriesCollection();
        //set series1 title
        XYSeries series1 = new XYSeries("Food production index (2004-2006 = 100)");
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        //for each json array and add data to data set
        for (int i = 0 ; i < array.size(); i++){
        	//year
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            series1.add(year,value);
            //append data string
            data+="in "+ year+": value is "+value+"%\n";
        }
        //add series to data set
        dataset.addSeries(series1);
        //create chart
        JFreeChart chart = ChartFactory.createScatterPlot("Food production","year","Food production index",dataset);
        ChartPanel panel = new ChartPanel(chart);
        //add chart panel to result set
        result.put("Scatter chart ",panel);
    }

    /**
     * create line chart
     * @param jsonArray data
     */
    private void createLineChart(JsonArray jsonArray){
    	//create data set
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        for (int i = 0 ; i < array.size(); i++){
        	//get date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //get value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            mDataset.addValue(value,"Food production",year+"");
        }
        //create line chart
        JFreeChart mChart = ChartFactory.createLineChart(
                "Food production ",
                "year",
                "Food production index (2004-2006 = 100)",
                mDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        //add chart panel to result set
        result.put("Line chart ",new ChartPanel(mChart));

    }

    /**
     * create bar chart
     * @param jsonArray data
     */
    private void createBarChart(JsonArray jsonArray){
    	//create data set
        DefaultCategoryDataset  mDataset = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = jsonArray.get(1).getAsJsonArray();
        for (int i = 0 ; i < array.size(); i++){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            mDataset.setValue(value,"Food production",year+"");
        }


        //create chart
        JFreeChart chart = ChartFactory.createBarChart("Food production ",
                "Food production index (2004-2006 = 100)",
                "Food production index (2004-2006 = 100)",
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
