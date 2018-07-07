package edu.buaa.rse.modelCombination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import edu.buaa.rse.analysis.Graph;
import edu.buaa.rse.dotx.model.Base;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;

public class TaskSequence extends Base{
	public String number;
	public String[] taskSequence;
	public String timeRequest;
	public TaskSequence(){
		this.number = "";
		this.taskSequence = null;
		this.timeRequest = "";
	}
}
