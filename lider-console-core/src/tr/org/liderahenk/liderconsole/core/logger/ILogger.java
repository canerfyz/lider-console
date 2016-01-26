package tr.org.liderahenk.liderconsole.core.logger;

public interface ILogger {
	
//	public void info(String text, Connection connection);
//
//	public void warning(String text, Connection connection, Exception ex);
//
//	public void warning(String text, Connection connection);
//
//	public void error(String text, Connection connection, Exception ex);
//
//	public void error(String text, Connection connection);
//
//	public void debug(String text, Connection connection);

	/**
	 * Sets the logger ID.
	 * 
	 * @param id
	 *            the new logger ID
	 */
	public void setId(String id);

	/**
	 * Gets the logger ID.
	 * 
	 * @return the logger ID
	 */
	public String getId();

	/**
	 * Sets the logger name.
	 * 
	 * @param name
	 *            the new logger name
	 */
	public void setName(String name);

	/**
	 * Gets the logger name.
	 * 
	 * @return the logger name
	 */
	public String getName();

	/**
	 * Sets the logger description.
	 * 
	 * @param description
	 *            the new logger description
	 */
	public void setDescription(String description);

	/**
	 * Gets the logger description.
	 * 
	 * @return the logger description
	 */
	public String getDescription();

}
