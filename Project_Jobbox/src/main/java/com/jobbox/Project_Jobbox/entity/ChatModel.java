package com.jobbox.Project_Jobbox.entity;

public class ChatModel {

	private String senderName;
	private String receiverName;
	private String message;
	private String date;
	private Status status;
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ChatModel [senderName=" + senderName + ", receiverName=" + receiverName + ", message=" + message
				+ ", date=" + date + ", status=" + status + "]";
	}
	
	
}
