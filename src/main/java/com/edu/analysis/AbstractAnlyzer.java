package com.edu.analysis;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartPanel;

import com.edu.utils.HttpClientUtils;
import com.google.gson.JsonArray;
/**
 * The realization of abstraction to analyzer 
 *
 */
public abstract class AbstractAnlyzer implements Analyzer{
	
	//begin year default 1962
	protected Integer fromDate= 1962;
	//end year default 1965
	protected Integer toDate=1965;
	//country default us
	protected String country = "us";
	//chart panels list
	protected Map<String, ChartPanel> result = new HashMap<>();
	//data
	protected String data;
	/**
	 * get data from network
	 * @param perfix data subfix example :SH.XPD.CHEX.PC.CD
	 * @return
	 */
	protected JsonArray getResponse(String perfix){
        String urlString =
                String.format(BASE_STR, this.country,perfix,fromDate,toDate);
        //get data from network and format string to json array
        return HttpClientUtils.getJSONObject(urlString);
    }

	@Override
    public Map<String, ChartPanel> getResult() {
        return result;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void setFromDate(Integer fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public void setToDate(Integer toDate) {
        this.toDate = toDate;
    }

    @Override
    public String getData() {
        return data;
    }

}
