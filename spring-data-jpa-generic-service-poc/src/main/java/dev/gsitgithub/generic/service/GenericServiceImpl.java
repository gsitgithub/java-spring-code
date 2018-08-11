package dev.gsitgithub.generic.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import dev.gsitgithub.generic.api.BaseEntity;
import dev.gsitgithub.generic.api.GenericRepository;
import dev.gsitgithub.generic.api.GenericService;

public abstract class GenericServiceImpl<T extends BaseEntity, ID extends Serializable> 
	implements GenericService<T, ID> {

	protected static final Logger log = LoggerFactory.getLogger(GenericServiceImpl.class);
	
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
	public List<T> findMany(final int firstResult, int maxResults) {
		Pageable pageSpecification = PageRequest.of(firstResult, maxResults > 0 ? maxResults:NUMBER_OF_PERSONS_PER_PAGE);
		return repository.findAll(pageSpecification).getContent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(Serializable id) {
		return repository.getOne((ID) id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isExist(Serializable id) {
		return repository.existsById((ID) id);
	}

	@Override
	public long getRowCount() {
		return repository.count();
	}

	@Override
	public List<T> executeAnyQuery(String query, Map<String, Object> args,	int firstResult, int maxResults, boolean sql) {
//		return parentDAO.executeAnyQuery(query, args, firstResult, maxResults, sql);
		// use entityManager
		return null;
	}

	@Override
	public T create(T newInstance) {
		return repository.save(newInstance);
	}

	@Override
	public void update(T transientObject) {
		repository.save(transientObject);
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
//		for (T t : repository.findAll((Iterable<ID>)idattribute))
//			ids.add((ID) t.getId());
		return ids;
	}

	@Override
	public void createMany(Collection<T> newInstances) {
		saveMany(newInstances);
	}

	@Override
	public void saveMany(Collection<T> instances) {
		repository.saveAll(instances);
	}

	@Override
	public void updateMany(Collection<T> instances) {
		saveMany(instances);
	}

}
