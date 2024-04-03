package com.barco.common.utility.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SheetFiled {

	private String sheetName;
	private List<String> mainTitle;
	private List<String> colTitle;

	public SheetFiled() {
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<String> getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(List<String> mainTitle) {
		this.mainTitle = mainTitle;
	}

	public List<String> getColTitle() {
		return colTitle;
	}

	public void setColTitle(List<String> colTitle) {
		this.colTitle = colTitle;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
