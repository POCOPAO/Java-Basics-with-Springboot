package ph.com.bpi.training.dto;

import ph.com.bpi.training.controller.ResponseStatus;

public class ResponseDTO<T> {

	private ResponseStatus status;
	private String message;
	private T data;
	
	public ResponseStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	
	public T getData() {
		return this.data;
	}
	
	public void setData(Object data) {
		this.data = (T) data;
	}
	
	public void setMessage(String string) {
		this.message = string;
	}
	
	
}
