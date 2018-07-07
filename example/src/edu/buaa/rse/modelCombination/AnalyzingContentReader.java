package edu.buaa.rse.modelCombination;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class AnalyzingContentReader {
	public static ArrayList<Integer> read(String analyzingContentInfoPath) {
		String xmlPath = analyzingContentInfoPath;
		ArrayList<Integer> ansList = new ArrayList<Integer>();
		FileInputStream infoStream = null;
		File infoFile = new File(xmlPath);

		try {
			infoStream = new FileInputStream(infoFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(infoStream);
			org.dom4j.Element rootElement = doc.getRootElement();
			List<org.dom4j.Element> taskList = rootElement.elements();
			ansList.add(-1);
			ansList.add(-1);
			ansList.add(-1);
			ansList.add(-1);
			if(0 != taskList.size()) {
				for (org.dom4j.Element eachEle:taskList) {
					if("reliability".equals(eachEle.getTextTrim())) {
						ansList.set(0, 1);
					}else if("real-time performance".equals(eachEle.getTextTrim())) {
						ansList.set(1, 1);
					}else if("complex performance".equals(eachEle.getTextTrim())) {
						ansList.set(2, 1);
					}else if("schedulability performance".equals(eachEle.getTextTrim())) {
						ansList.set(3, 1);
					}
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}finally {
			if(null != infoStream) {
				try {
					infoStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ansList;
	}
}
