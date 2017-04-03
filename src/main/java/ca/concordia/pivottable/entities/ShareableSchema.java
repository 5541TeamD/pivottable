package ca.concordia.pivottable.entities;

import com.google.gson.Gson;

/**
 * POJO representing a shareable pivot table schema.
 */
public class ShareableSchema 
{
	private long schemaID;
	private final String schemaName;
	private final String ownerUsername;
	private final PivotTableSchema pvtTblSchema;
	private final String dbURL;
	private final String dbUsername;
	private final String dbPassword;
	
	/**
	 * Constructor with all parameters provided. 
	 * @param	schemaID		ID for uniquely identifying a shareable schema. Generated in the database.
	 * @param 	schemaName		Name of the shareable schema
	 * @param	ownerUsername	Username of the owner user
	 * @param 	pvtTblSchema	Details of the pivot table schema (selections) defined by the user
	 * @param 	dbURL			URL of the database for which the schema has been defined
	 * @param 	dbUsername		Username for connecting to the database
	 * @param 	dbPassword		Password for connecting to the database
	 */
	public ShareableSchema(long schemaID, String schemaName, String ownerUsername, PivotTableSchema pvtTblSchema, String dbURL, String dbUsername, String dbPassword)
	{
		this.schemaID = schemaID;
		this.schemaName = schemaName;
		this.ownerUsername = ownerUsername;
		this.pvtTblSchema = pvtTblSchema;
		this.dbURL = dbURL;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}
	
	/**
	 * Accessor method for data member schemaID.
	 * @return	schemaID of this shareable schema
	 */
	public long getSchemaID() 
	{
		return this.schemaID;
	}

	/**
	 * Mutator method for data member schemaID.
	 * @param 	schemaID	Schema ID to be assigned to the data member
	 */
	public void setschemaID(long schemaID) 
	{
		this.schemaID = schemaID;
	}
	
	/**
	 * Accessor method for data member schemaName.
	 * @return	schemaName of this shareable schema
	 */
	public String getSchemaName()
	{
		return this.schemaName;
	}
	
	/**
	 * Accessor method for data member ownerUsername.
	 * @return	ownerUsername of this shareable schema
	 */
	public String getOwnerUsername()
	{
		return this.ownerUsername;
	}
	
	/**
	 * Accessor method for data member pvtTblSchema.
	 * @return	pvtTblSchema of this shareable schema
	 */
	public PivotTableSchema getPvtTblSchema() 
	{
		return this.pvtTblSchema;
	}

	/**
	 * Accessor method for data member dbURL.
	 * @return	dbURL of this shareable schema
	 */
	public String getDbURL() 
	{
		return this.dbURL;
	}

	/**
	 * Accessor method for data member dbUsername.
	 * @return	dbUsername of this shareable schema
	 */
	public String getDbUsername() 
	{
		return this.dbUsername;
	}

	/**
	 * Accessor method for data member dbPassword.
	 * @return	dbPassword of this shareable schema
	 */
	public String getDbPassword() 
	{
		return this.dbPassword;
	}

	/**
	 * Creates a JSON string from an instance of ShareableSchema.
	 * @return	A JSON string
	 */
	public String toJSON() 
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	/**
     * Creates an instance of ShareableSchema from a JSON string.
     * @param	json	The string containing the JSON
     * @return	A ShareableSchema object
     */
	static public ShareableSchema fromJSon(String json)
	{
		Gson gson = new Gson();
        return gson.fromJson(json, ShareableSchema.class);
	}	
}
