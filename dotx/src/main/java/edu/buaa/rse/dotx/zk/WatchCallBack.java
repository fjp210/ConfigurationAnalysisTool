package edu.buaa.rse.dotx.zk;

public interface WatchCallBack {
	
	    /**
	     * @param listenningPath
	     *
	     */
	    public void run(String listenningPath);

	    /**
	     * override your own function to deal with exception of this changed value of the zookeeper node
	     *
	     */
	    public void fail();
	    
        /**
         * 
         * @param path :  changed path
         * @param value : changed value
         */
		public void run(String path, String value);
}