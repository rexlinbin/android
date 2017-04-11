package com.bccv.boxcomic.tool;

import java.io.File;
import java.util.Comparator;

public class MyComparator implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		// TODO Auto-generated method stub
		
		return lhs.getName().compareTo(rhs.getName());
	}

}
