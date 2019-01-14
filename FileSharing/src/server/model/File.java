package server.model;

import common.FileDTO;

public class File implements FileDTO {
	private static final long serialVersionUID = 2564664235695229561L;
	private String name;
	private String size;
	private String owner;
	private String permission;
	
	/**
	 * Create a new File instance
	 * 
	 * @param name The name of the file
	 * @param owner The owner of the file
	 * @param size The size of the file
	 * @param permission The permission of the file
	 */
	public File(String name, String owner, String size, String permission) {
		this.name = name;
		this.size = size;
		this.owner = owner; 
		this.permission = permission;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getPermission() {
		return permission;
	}
	
	@Override
	public String getOwner() {
		return owner;
	}
	
	@Override
	public String getSize() {
		return size;
	}
}
