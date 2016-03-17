package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.io.Serializable;

public interface IRequest extends Serializable {

	String toJson() throws Exception;

}
