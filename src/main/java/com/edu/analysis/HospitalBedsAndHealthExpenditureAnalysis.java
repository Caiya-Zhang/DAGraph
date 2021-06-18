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
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import com.edu.constants.Contants;
import com.google.gson.JsonArray;
/**
 * 
 * Ratio of Hospital beds (per 1,000 people) and Current health expenditure (per 1,000 people) Analysis
 *
 */
public class HospitalBedsAndHealthExpenditureAnalysis extends AbstractAnlyzer{
	

    

	@Override
	/**
	 * do analysis
	 */
	public void analysis() {
		data = "Ratio of Hospital beds (per 1,000 people) and Current health expenditure (per 1,000 people)\n";
		//get data from network
		JsonArray hospital = getResponse(Contants.HOSITAL_BEDS_STR);
		//get data from network
		JsonArray heath = getResponse(Contants.HEALTH_EXPENDITURE_PER_STR);
		//create line chart
		createLineChart(hospital, heath);
		createBarChart(hospital, heath);
		createScattChart(hospital, heath);
		createMixChart(hospital, heath);
		
	}
	
	/**
	 * create line chart
	 * @param hospitalArray hospital array
	 * @param heathArray health array
	 */
	private void createLineChart(JsonArray hospitalArray,JsonArray heathArray){
		//create a x_y data set hospital array
        XYSeriesCollection mDataset1 =  new XYSeriesCollection();
        //create a x_y data set to health array
        XYSeriesCollection mDataset2 =  new XYSeriesCollection();

        JsonArray array = hospitalArray.get(1).getAsJsonArray();
        JsonArray array1 = heathArray.get(1).getAsJsonArray();
        //set series name
        XYSeries series1 = new XYSeries("Hospital beds");
        //set series name
        XYSeries series2 = new XYSeries("health expenditure");
        //add data to data set and append data string
        for (int i = 0 ; i < array.size(); i++){
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
            series1.add(year,value);
            series2.add(year,value1);
            data+="  int year "+ year+":";
            data+="  hospital per is "+value +", health expenditure is "+value1+"\n";
        }
        //add series to data set
        mDataset1.addSeries(series1);
        //add series to data set
        mDataset2.addSeries(series2);

        //create jfreechart instance
        JFreeChart chart = ChartFactory.createXYLineChart("Ratio of Hospital beds  and Current health expenditure ",
                "year",
                "hospital bed per ",
                mDataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        
        //get plot from jfreechart
        XYPlot plot = chart.getXYPlot();
        NumberAxis axis2 = new NumberAxis("health expenditure");
        // -- modify second series show style
        axis2.setAxisLinePaint(Color.BLUE);
        axis2.setLabelPaint(Color.BLUE);
        axis2.setTickLabelPaint(Color.BLUE);
        
        //set second series range
        plot.setRangeAxis(1, axis2);
        //set second series data set
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
     * create mixed chart
     * @param hospitalArray  Hospital beds data
     * @param healthArray health expenditure data
     */
    private void createMixChart(JsonArray hospitalArray,JsonArray healthArray) {
    	//create data set to hospital bed
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	//create data set to health expenditure
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = hospitalArray.get(1).getAsJsonArray();
        JsonArray array2 = healthArray.get(1).getAsJsonArray();
        for (int i = array.size()-1 ; i >= 0; i--){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            int year1 = array2.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value,"Hospital beds",year+"");
            dataset2.setValue(value1,"health expenditure",year1+"");
        }
        //create mix chart 
        JFreeChart chart = ChartFactory.createBarChart("Ratio of Hospital beds  and Current health expenditure ", "", "Hospital beds", dataset1);
        //get plot
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        //set mortality data set
        categoryplot.setDataset(1, dataset2);
        //set mortality axis
		categoryplot.mapDatasetToRangeAxis(1, 1);
		// x 
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// right for y axis
		NumberAxis numberaxis = new NumberAxis("health expenditure($)");
		categoryplot.setRangeAxis(1, numberaxis);
		// hide y axis
		numberaxis.setAxisLineVisible(false);
		numberaxis.setTickMarksVisible(false);

		// set line chat style
		LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
		lineRenderer.setSeriesPaint(0, new Color(255, 185, 1));
		categoryplot.setRenderer(1, lineRenderer);
		//set z-index for set line chart in top
		categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
		
		result.put("Mixed chart",new ChartPanel(chart));
    	
    }
	
    /**
     * create scatter chart
     * @param hospitalArray hospital array
	 * @param heathArray health array
     */
    private void createScattChart(JsonArray hospitalArray,JsonArray mortalityArray){
    	//create data set to health
    	XYSeriesCollection mDataset1 =  new XYSeriesCollection();
    	//create data set to mortality
    	XYSeriesCollection mDataset2 =  new XYSeriesCollection();
    	
    	//get data and for each json array
    	JsonArray array = hospitalArray.get(1).getAsJsonArray();
    	JsonArray array1 = mortalityArray.get(1).getAsJsonArray();
    	//set series name
        XYSeries series1 = new XYSeries("hospotal bed per");
        XYSeries series2 = new XYSeries("health expenditure");
        
    	for (int i = 0 ; i < array.size(); i++){
    		//get date
    		int year = array.get(i).getAsJsonObject().get("date").getAsInt();
    		//get health data
    		double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
    		//get mortality data
    		double value1 = array1.get(i).getAsJsonObject().get("value").getAsDouble();
    		//add health value to series1
    		series1.add(year,value);
    		//add mortality value to series2
    		series2.add(year,value1);
    	}
    	//add health series to data set1
    	mDataset1.addSeries(series1);
    	//add mortality series to data set2
    	mDataset2.addSeries(series2);
    	//create Scatter chart
    	//get plot
		XYPlot plot = new XYPlot();
		//create y axis
		XYItemRenderer itemrenderer1 = new XYLineAndShapeRenderer(false, true);
		XYItemRenderer itemrenderer2 = new XYLineAndShapeRenderer(false, true);
		//set axis data set
		plot.setDataset(0, mDataset1);
		//add render to plot
		plot.setRenderer(0, itemrenderer1);
		DateAxis domainAxis = new DateAxis("Year");
		plot.setDomainAxis(domainAxis);
		//add axis name
		plot.setRangeAxis(new NumberAxis("Hsopital beds"));
		//set axis data set
		plot.setDataset(1, mDataset2);
		//add render to plot
		plot.setRenderer(1, itemrenderer2);
		//add axis name
		plot.setRangeAxis(1, new NumberAxis("health expenditure($)"));
		//set 1st data set to 1st y-axis
		plot.mapDatasetToRangeAxis(0, 0);
		//set 2nd data set to 2nd y-axis
		plot.mapDatasetToRangeAxis(1, 1);
		//create scatter chart
		JFreeChart chart = new JFreeChart("Ratio of Hospital beds and Current health expenditure",
				new Font("Times New Roman", java.awt.Font.BOLD, 24), plot, true);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
		chartPanel.setBackground(Color.white);
		//add chart panel to result set
		result.put("Scatter",new ChartPanel(chart));
    }
    
    /**
     * create bar chart
     * @param hospitalArray  Hospital beds data
     * @param healthArray health expenditure data
     */
    private void createBarChart(JsonArray hospitalArray,JsonArray healthArray) {
    	//create data set to hospital bed
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	//create data set to health expenditure
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        //for each json array
        JsonArray array = hospitalArray.get(1).getAsJsonArray();
        JsonArray array2 = healthArray.get(1).getAsJsonArray();
        for (int i = array.size()-1 ; i >= 0; i--){
        	//date
            int year = array.get(i).getAsJsonObject().get("date").getAsInt();
            //value
            double value = array.get(i).getAsJsonObject().get("value").getAsDouble();
            double value1 = array2.get(i).getAsJsonObject().get("value").getAsDouble();
            //add value to data set
            dataset1.setValue(value,"Hospital beds",year+"");
            dataset2.setValue(value1,"health expenditure",year+"");
        }
        
        //create bar chart
        //get plot
		CategoryPlot plot = new CategoryPlot();
		BarRenderer barrenderer1 = new BarRenderer();
		BarRenderer barrenderer2 = new BarRenderer();
		//set beds data set
		plot.setDataset(0, dataset1);
		plot.setRenderer(0, barrenderer1);
		//set x axis
		CategoryAxis domainAxis = new CategoryAxis("Year");
		plot.setDomainAxis(domainAxis);
		// left for y axis
		plot.setRangeAxis(new NumberAxis("Hospital beds"));
		//set mortality data set
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, barrenderer2);
		// right for y axis
		plot.setRangeAxis(1, new NumberAxis("health expenditure($)"));

		plot.setRenderer(barrenderer1);
		barrenderer1.setMaximumBarWidth(0.2);
		//set 1st data set to 1st y-axis
		plot.mapDatasetToRangeAxis(0, 0);
		//set 2nd data set to 2nd y-axis
		plot.mapDatasetToRangeAxis(1, 1);
		//create bar chart
		JFreeChart chart = new JFreeChart("Ratio of Hospital beds  and Current health expenditure ",
				new Font("Times New Roman", java.awt.Font.BOLD, 24), plot, true);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		//add chart panel to result set
		result.put("Bar chart",new ChartPanel(chart));
    }

}
