package com.jobbox.Project_Jobbox.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class HiringPolicy {
	private boolean allowReapply;
	private int reapplyMonths;
	public boolean isAllowReapply() {
		return allowReapply;
	}
	public void setAllowReapply(boolean allowReapply) {
		this.allowReapply = allowReapply;
	}
	public int getReapplyMonths() {
		return reapplyMonths;
	}
	public void setReapplyMonths(int reapplyMonths) {
		this.reapplyMonths = reapplyMonths;
	}
	@Override
	public String toString() {
		return "HiringPolicy [allowReapply=" + allowReapply + ", reapplyMonths=" + reapplyMonths + "]";
	}
	
	
}
