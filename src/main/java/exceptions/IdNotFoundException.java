package exceptions;

import java.io.Serializable;

/**
 * 找不到数据
 */
public class IdNotFoundException extends Exception {

	private static final long serialVersionUID = -7477676262967808798L;

	public IdNotFoundException(String errorMsg, Serializable id) {
		super(String.format("%s. Id: %s not found!", errorMsg, String.valueOf(id)));
	}

}
