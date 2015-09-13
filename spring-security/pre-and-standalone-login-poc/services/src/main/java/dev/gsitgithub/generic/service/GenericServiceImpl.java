package dev.gsitgithub.generic.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import dev.gsitgithub.generic.entity.BaseEntity;
import dev.gsitgithub.generic.repo.GenericRepository;


public abstract class GenericServiceImpl<T extends BaseEntity, ID extends Serializable> 
	implements GenericService<T, ID> {

	protected static final Logger log = LoggerFactory.getLogger("dev.gsitgithub.lab.service");
	protected static String[] ALL_STATUSES ;//= new String[]{ObjectStatusValues.STATUS_ACTIVE,ObjectStatusValues.STATUS_APPROVED,ObjectStatusValues.STATUS_CANCELLED,ObjectStatusValues.STATUS_COMPLETED,ObjectStatusValues.STATUS_EXCEPTION,ObjectStatusValues.STATUS_INELIGIBLE,ObjectStatusValues.STATUS_INVALID,ObjectStatusValues.STATUS_PROPOSED,ObjectStatusValues.STATUS_REJECTED,ObjectStatusValues.STATUS_SUSPENDED,ObjectStatusValues.STATUS_SUBMITTED,ObjectStatusValues.STATUS_TERMINATED,ObjectStatusValues.STATUS_VERIFIED}; 
	private GenericRepository<T,ID> repository;
	
	public GenericServiceImpl(GenericRepository<T,ID> repository) {
		this.repository = repository;
	}
	
	public static Map<String, List<Object>> scalarListToMap(String[] fields, List<Object[]> result) {
		Map<String, List<Object>> retval = null;
    	if (result != null && !result.isEmpty()) {
    		retval = new HashMap<String, List<Object>>();
    		for (int r = 0; r < result.size(); r++) {
    			for (int f = 0; f < fields.length; f++) {
    				if (r == 0) {
    					retval.put(fields[f], new ArrayList<Object>());
    				}
    				List<Object> l = retval.get(fields[f]); 
    				if (fields.length > 1) {
	    				Object[] vals = (Object[]) result.get(r);
	    				l.add(vals[f]);
    				} else {
	    				l.add(result.get(r));
    				}
	    		}
	    	}
    	}
    	return retval;
	}
	
	@Override
	public String[] findObjectStatusValues() {
		return ALL_STATUSES;
	}
	
	@Override
	public List<T> findAll() {
		return repository.findAll();
	}

	@Override
	public List<T> findMany(final int firstResult, int maxResults) {
		Pageable pageSpecification = new PageRequest(firstResult, maxResults > 0 ? maxResults:NUMBER_OF_PERSONS_PER_PAGE);
		return repository.findAll(pageSpecification).getContent();
	}

	@Override
	public List<T> findByExample(T exampleInstance, Map<String, Object> exclusionProperties, int firstResult, int maxResults) {
//		return parentDAO.findByExample(exampleInstance, exclusionProperties, firstResult, maxResults);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T findById(Serializable id) {
		return repository.getOne((ID) id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean findIfObjectWithIdExists(Serializable id) {
		return repository.exists((ID) id);
	}

	@Override
	public List<T> findByQuery(String query, Object[] queryArgs, int firstResult, int maxResults) {
		Pageable pageSpecification = new PageRequest(firstResult, maxResults > 0 ? maxResults:NUMBER_OF_PERSONS_PER_PAGE);
		return repository.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> q, CriteriaBuilder cb) {
            	q.distinct(true);
                root.fetch("permissions", JoinType.LEFT);
                
//            	EntityType<T> metamodel= root.getModel();
//            	Predicate start = cb.equal(root.get("id"), (long)firstResult);
//            	Predicate start = cb.greaterThan(root.get("id").as(Long.class), (long)firstResult);
            	
                return cb.equal(root.get("id"), 1l);
            }
        }, pageSpecification).getContent();
	
//		return null;
	}

	@Override
	public long getRowCount() {
		return repository.count();
	}

    public long getRowCountByQuery(String query, final Object[] queryArgs) {
//    	return parentDAO.count(query, queryArgs);
    	return 0;
    }

	@Override
	public long getRowCountByExample(T exampleInstance, Map exclusionProperties) {
//		return parentDAO.getRowCountByExample(exampleInstance, exclusionProperties);
		return 0;
	}

	@Override
	public List<T> findBySqlQuery(String query, int firstResult, int maxResults) {
//		return parentDAO.findBySqlQuery(query, firstResult, maxResults);
		return null;
	}

	@Override
	public List<HashMap> findScalarBySqlQuery(String query, final Map<String,Object> args, int firstResult, int maxResults) {
//		return parentDAO.findScalarBySqlQuery(query, args, firstResult, maxResults);
		return null;
	}
	
	@Override
	public List<HashMap> findAsScalar(String[] fieldNames, String whereClause, final Map<String,Object> args, int firstResult, int maxResults, boolean sql) {
//		return parentDAO.findAsScalar(fieldNames, whereClause, args, firstResult, maxResults, sql);
		return null;
	}

	public List<HashMap> executeSqlQuery(String query, List<String>fields, HashMap<String,Type> propTypes, final Map<String,Object> args, int firstResult, int maxResults) {
//		return parentDAO.executeSqlQuery(query, fields, propTypes, args, firstResult, maxResults);
		return null;
	}

	@Override
	public List executeAnyQuery(String query, Map<String, Object> args,
			int firstResult, int maxResults, boolean sql) {
//		return parentDAO.executeAnyQuery(query, args, firstResult, maxResults, sql);
		return null;
	}

	@Override
	public T create(T newInstance) {
		return repository.save(newInstance);
	}

	@Override
	public T update(T transientObject) {
		return repository.save(transientObject);
	}

	@Override
	public void delete(T persistentObject) {
		repository.delete(persistentObject);
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

	@Override
	public List<ID> findAllIds(Iterable<ID> idattribute) {
		List<ID> ids = new ArrayList<ID>();
		for (T t : repository.findAll((Iterable<ID>)idattribute))
			ids.add((ID) t.getId());
		return ids;
	}

	@Override
	public Map<String, String> findPropertyMap(String keyProperty,
			String[] otherProps, String whereClause, String delim) {
//		return parentDAO.findPropertyMap(keyProperty, otherProps, whereClause, delim);
		return null;
	}

	@Override
	public void createMany(Collection<T> newInstances) {
		repository.save(newInstances);
	}

	@Override
	public void saveMany(Collection<T> instances) {
		repository.save(instances);
	}

	@Override
	public void updateMany(Collection<T> instances) {
		repository.save(instances);
	}

}
