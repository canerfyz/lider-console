package tr.org.liderahenk.liderconsole.core.policy;

public interface IPolicy {
	
	/**
	 * Sets the logger label.
	 * 
	 * @param label
	 *            the new logger label
	 */
	public void setLabel(String label);

	/**
	 * Gets the logger label.
	 * 
	 * @return the logger label
	 */
	public String getLabel();

	/**
	 * Sets the logger type.
	 * 
	 * @param type
	 *            the new logger type
	 */
	public void setType(String type);

	/**
	 * Gets the logger type.
	 * 
	 * @return the logger type
	 */
	public String getType();

}
