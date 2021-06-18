package com.edu.analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.edu.constants.Contants;
import com.google.gson.JsonArray;
/**
 * 
 * Ratio of CO2 emissions (metric tons per capita) and GDP per capita (current US$) Analysis
 *
 */
public class CO2vsGDP extends AbstractAnlyzer{
	
	@Override
	/**
	 * do analysis
	 */
	public void analysis() {
		data = " Ratio of CO2 emissions (metric tons per capita) and GDP per capita (current US$)\n";
		//get data from network
		JsonArray emissions = getResponse(Contants.CO2_Emissions_STR_1);
		JsonArray GDP = getResponse(Contants.GDP_STR_1);
		//create line chart
		createLineChart(emissions, GDP);
		createBarChart(emissions, GDP);
		createScattChart(emissions, GDP);
	}
	
	/**
	 * create line chart
	 * @param emissions array
	 * @param GDP array
	 */
	private void createLineChart(JsonArray emissionsArr,JsonArray GDPArr){
		//create a x_y data set 
        XYSeriesCollection mDataset1 =  new XYSeriesCollection();
        XYSeriesCollection mDataset2 =  new XYSeriesCollection();

        JsonArray array = emissionsArr.get(1).getAsJsonArray();
        JsonArray array1 = GDPArr.get(1).getAsJsonArray();
        //set series name
        XYSeries series1 = new XYSeries("CO2 Emissions");
        XYSeries series2 = new XYSeries("GPD");
        //add data to data set and append data string
        for (int i = 0 ; i < array.size(); i++){
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
            series1.add(year,value);
            series2.add(year,value1);
            data+= year+":";
            data+="  CO2 Emissions is "+value +", GDP is "+value1+"\n";
        }
        //add series to data set
        mDataset1.addSeries(series1);
        mDataset2.addSeries(series2);

        //create jfreechart instance
        JFreeChart chart = ChartFactory.createXYLineChart("Ratio of CO2 Emission and GDP ",
                "year",
                "CO2 Emission",
                mDataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        
        //get plot from jfreechart
        XYPlot plot = chart.getXYPlot();
        NumberAxis axis2 = new NumberAxis("GDP");
        axis2.setAxisLinePaint(Color.BLUE);
        axis2.setLabelPaint(Color.BLUE);
        axis2.setTickLabelPaint(Color.BLUE);
        
        //set second series 
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, mDataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer render2 =  new XYLineAndShapeRenderer();
        render2.setSeriesPaint(0, Color.BLUE);
        //render plot
        plot.setRenderer(1, render2);
        //add chart panel to result set
        result.put("Line chart",new ChartPanel(chart));
    }

    /**
     * create bar chart
     */
    private void createBarChart(JsonArray emiArray,JsonArray gdpArray) {
    	//create data set
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = emiArray.get(1).getAsJsonArray();
        JsonArray array2 = gdpArray.get(1).getAsJsonArray();
        for (int i = array.size()-1 ; i >= 0; i--){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value,"CO2 Emissions",year+"");
            dataset2.setValue(value1,"GDP",year+"");
        }
        
        //create bar chart
		CategoryPlot plot = new CategoryPlot();
		BarRenderer barrenderer1 = new BarRenderer();
		BarRenderer barrenderer2 = new BarRenderer();

		plot.setDataset(0, dataset1);
		plot.setRenderer(0, barrenderer1);
		CategoryAxis domainAxis = new CategoryAxis("Year");
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis(" metric tons per capita"));

		plot.setDataset(1, dataset2);
		plot.setRenderer(1, barrenderer2);
		plot.setRangeAxis(1, new NumberAxis("US$"));

		//set the max bar width for CO2 emission
		plot.setRenderer(barrenderer1);
		barrenderer1.setMaximumBarWidth(0.2);
		
		plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); // 2nd dataset to 2nd y-axis

		JFreeChart barChart = new JFreeChart("CO2 Emissions vs GDP (US$)",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
		
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		
		result.put("Bar chart",new ChartPanel(barChart));
    	
    }
	
    /**
     * create scatter chart
     */
    private void createScattChart(JsonArray emArray,JsonArray gdpArray){
    	//create data set 
    	XYSeriesCollection mDataset1 =  new XYSeriesCollection();
    	XYSeriesCollection mDataset2 =  new XYSeriesCollection();
    	
    	//get data and for each json array
    	JsonArray array = emArray.get(1).getAsJsonArray();
    	JsonArray array1 = gdpArray.get(1).getAsJsonArray();
    	//set series name
        XYSeries series1 = new XYSeries("CO2 Emissions");
        XYSeries series2 = new XYSeries("GDP");
    	for (int i = 0 ; i < array.size(); i++){
    		//get date
    		int year = array.get(i).getAsJsonObject().get("date").getAsInt();
    		double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
    		double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
    		series1.add(year,value);
    		series2.add(year,value1);
    	}
    	mDataset1.addSeries(series1);
    	mDataset2.addSeries(series2);
    	//create Scatter chart
		XYPlot plot = new XYPlot();
		XYItemRenderer itemrenderer1 = new XYLineAndShapeRenderer(false, true);
		XYItemRenderer itemrenderer2 = new XYLineAndShapeRenderer(false, true);

		plot.setDataset(0, mDataset1);
		plot.setRenderer(0, itemrenderer1);
		DateAxis domainAxis = new DateAxis("Year");
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis(" metric tons per capita"));

		plot.setDataset(1, mDataset2);
		plot.setRenderer(1, itemrenderer2);
		plot.setRangeAxis(1, new NumberAxis("US$"));

		plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); // 2nd dataset to 2nd y-axis

		JFreeChart scatterChart = new JFreeChart("CO2 Emission vs GDP",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

		ChartPanel chartPanel = new ChartPanel(scatterChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		
		result.put("Scatter",new ChartPanel(scatterChart));
    }

}
