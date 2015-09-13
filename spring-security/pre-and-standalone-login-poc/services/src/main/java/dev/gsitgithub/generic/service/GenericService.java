package dev.gsitgithub.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.Type;

import dev.gsitgithub.generic.entity.BaseEntity;

public interface GenericService<T extends BaseEntity, ID extends Serializable> {
	int NUMBER_OF_PERSONS_PER_PAGE = 20;
	
	/** 
	 * Find previously persisted object by it's id
	 */
	public T findById(ID id);
	
	public boolean findIfObjectWithIdExists(ID id);

	/** 
	 * Find previously persisted objects by example instance
	 */

	public List<T> findByExample(T exampleInstance,
			Map<String, Object> exclusionProperties, int firstResult, int maxResults);

	/** 
	 * Search previously persisted objects by a sql query string and query arguments
	 * The result can be obtained in page manner by using firstResult and maxResults
	 */

	public List<T> findByQuery(String query, final Object[] queryArgs,
			int firstResult, int maxResults);

	public List<T> findBySqlQuery(String query, int firstResult, int maxResults);

	/** 
	 * Find all 
	 */
	public List<T> findAll();

	public List<ID> findAllIds(Iterable<ID> idattribute);
	
	public List<T> findMany(int firstResult, int maxResults);
	/** 
	 * Get row count of rows in table
	 */
	public long getRowCount();

	public long getRowCountByQuery(String query, final Object[] queryArgs);
	
	public long getRowCountByExample(T exampleInstance,	Map exclusionProperties);

	public List<HashMap> findScalarBySqlQuery(String query, final Map<String,Object> args, int firstResult, int maxResults);
	
	public List<HashMap> findAsScalar(String[] fieldNames, String whereClause, final Map<String,Object> args, int firstResult, int maxResults, boolean sql);

	public List<HashMap> executeSqlQuery(String query, List<String>fields, HashMap<String,Type> propTypes, final Map<String,Object> args, int firstResult, int maxResults);

	public List executeAnyQuery(String query, final Map<String,Object> args, int firstResult, int maxResults, boolean sql);
	
	public Map<String,String> findPropertyMap(String keyProperty, String[] otherProps, String whereClause, String delim);
	
	/** Persist the newInstance object into database */
	public abstract T create(T newInstance);

	/** Save changes made to a persistent object.  */
	public abstract T update(T instance);

	/** Remove an object from persistent storage in the database */
	public abstract void delete(T persistentObject);

	/** Remove an object from persistent storage in the database */
	public abstract void deleteAll();

	public abstract void createMany(Collection<T> newInstances);

	public abstract void saveMany(Collection<T> instances);

	/** Save changes made to a persistent object.  */
	public abstract void updateMany(Collection<T> instances);

	String[] findObjectStatusValues();

}
