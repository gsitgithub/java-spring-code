package dev.gsitgithub.generic.repo;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class GenericRepositoryImpl <T extends BaseEntity, ID extends Serializable>
	extends SimpleJpaRepository<T, ID> 	implements GenericRepository<T, ID>, Serializable
{
	private static final long serialVersionUID = -3943656263780592037L;
	@PersistenceContext
	private EntityManager entityManager;
	
	// There are two constructors to choose from, either can be used.
	public GenericRepositoryImpl(Class<T> domainClass,
			EntityManager entityManager) {
		super(domainClass, entityManager);

		// This is the recommended method for accessing inherited class
		// dependencies.
		this.entityManager = entityManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
