package mysite.dto;

import lombok.Getter;

@Getter
public class JsonResult {
	private final String result;	// "success"  or "fail"
	private final Object data;		// if success, set
	private final String message;	// if fail, set

	public static JsonResult success(Object data) {
		return new JsonResult(data);
	}

	public static JsonResult fail(String message) {
		return new JsonResult(message);
	}
	
	private JsonResult(Object data) {
		this.result = "success";
		this.data = data;
		this.message = null;
	}

	private JsonResult(String message) {
		this.result = "fail";
		this.data = null;
		this.message = message;
	}
}
