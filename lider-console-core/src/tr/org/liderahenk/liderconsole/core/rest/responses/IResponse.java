package tr.org.liderahenk.liderconsole.core.rest.responses;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;

public interface IResponse extends Serializable {

	Object toObject(String json) throws Exception;

	RestResponseStatus getStatus();

	List<String> getMessages();

	Map<String, Object> getResultMap();

}
