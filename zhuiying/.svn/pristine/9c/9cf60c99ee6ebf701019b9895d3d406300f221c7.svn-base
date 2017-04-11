package com.utils.tools;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SrtManager {
	private final String EXPRESSION = "[0-9]+";
	private final String EXPRESSION1 = "[0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9] --> [0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9]";
	private List<String> endTimeList = new ArrayList<String>();
	private List<String> startTimeList = new ArrayList<String>();
	private List<String> textList = new ArrayList<String>();
	
	private int currNum = 0;
	
	public void loadSrtUrl(String url){
		
	}
	
	@SuppressWarnings("resource")
	public void loadSrtFile(String filepath) {
		String line;
		String startTime, endTime;
		String nowRow = null, oldRow = null;
		try {
			File fr = new File(filepath);
			FileInputStream iStream = new FileInputStream(fr);
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));

			while ((line = bfrd.readLine()) != null) {
				if (line.equals("")) {
					// 匹配为空行
				} else if (Pattern.matches(EXPRESSION, line)) {
					// 匹配为标号
					nowRow = line;
				} else if (Pattern.matches(EXPRESSION1, line)) {
					// 匹配为时间
					startTime = line.substring(0, 12);
					endTime = line.substring(17, 29);
					startTimeList.add(toSecond(startTime));
					endTimeList.add(toSecond(endTime));
				} else {
					// 其他为内容
					if (oldRow != null) {
						if (!oldRow.equals(nowRow)) {
							textList.add(line);
						} else {
							String oldText = textList.remove(textList.size() - 1);
							String newText = oldText + "\n" + line;
							textList.add(newText);
						}
					}else {
						if (!line.equals("﻿1")) {
							textList.add(line);
						}
						
					}
					
					oldRow = nowRow;
				}
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasSrt(){
		if (textList.size() > 0) {
			return true;
		}
		return false;
	}
	
	public String getCurrStrText(int currPosition){
		int startTime = Integer.parseInt(startTimeList.get(currNum));
		int endTime = Integer.parseInt(endTimeList.get(currNum));
		if (currPosition < startTime) {
			return "";
		}else if (currPosition <= endTime) {
			return textList.get(currNum);
		}else {
			currNum++;
			if (currPosition > Integer.parseInt(startTimeList.get(currNum)) && currPosition <= Integer.parseInt(endTimeList.get(currNum))) {
				return textList.get(currNum);
			}else if (currPosition > Integer.parseInt(endTimeList.get(currNum))){
				for (int i = 0; i < endTimeList.size(); i++) {
					if (currPosition < Integer.parseInt(endTimeList.get(i))) {
						currNum = i;
						return textList.get(currNum);
					}
				}
				return "";
			}else {
				return "";
			}
		}
		
	}
	
	public String toSecond(String time){
		int second = 0;
		String[] timeStrings = time.split(":");
		timeStrings[2] = timeStrings[2].substring(0, 2);
		second = Integer.parseInt(timeStrings[0]) * 3600 + Integer.parseInt(timeStrings[1]) * 60 + Integer.parseInt(timeStrings[2]);
		return second + "";
	}
}
