package edu.buaa.rse.dotx.model.deploy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("appToPartion")
public class AppToPartion {
	long begin;
	long end;
	long maxresult;
	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public long getMaxresult() {
		return maxresult;
	}
	public void setMaxresult(long maxresult) {
		this.maxresult = maxresult;
	}
}
