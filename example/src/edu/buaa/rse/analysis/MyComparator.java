package edu.buaa.rse.analysis;
import java.util.*;

public class MyComparator implements Comparator<ArrayList<Integer>>{

	@Override
	public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
		// TODO Auto-generated method stub
		return l1.get(3).compareTo(l2.get(3));
	}
}
