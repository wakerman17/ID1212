package common;

import java.io.Serializable;

public interface FileDTO extends Serializable {
	
	public String getName();
	
	public String getPermission();
	
	public String getOwner();
	
	public String getSize();
}
