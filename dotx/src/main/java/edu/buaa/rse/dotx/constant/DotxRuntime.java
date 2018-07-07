package edu.buaa.rse.dotx.constant;

public class DotxRuntime {
	public static class Worker{
		public static String WORKSPACE = "workspace_worker";
		public static String TASKPATH = WORKSPACE+"/"+"task";
		public static String JOBPATH = WORKSPACE+"/"+"job";
	}
	public static class Agent{
		public static String WORKSPACE = "workspace_agent";
		public static String TASKPATH = WORKSPACE+"/"+"task";
		public static String JOBPATH = WORKSPACE+"/"+"job";
	}
	public static class Ftp{
		public static String TASKPATH = "/task";
		public static String JOBPATH = "/job";
		public static String STAGEPATH = "/stage";
	}
	public static class Zk{
		public static String TASKCOUNTOERPATH = "/taskcounter";
		public static String JOBCOUNTOERPATH = "/jobcounter";
		public static String TASKPATH = "/task";
		public static String JOBPATH = "/job";
		public static String JOBINIT = "0";
		public static String JOBRUNNING = "1";
		public static String JOBFINISHED = "2";
	}
	
}
