package dev.gsitgithub.generic.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import dev.gsitgithub.generic.api.BaseEntity;

public class DefaultRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {

	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 * @param entityManager
	 * @return
	 */
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new RepositoryFactory(entityManager);
	}

	public DefaultRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}
	
	private static class RepositoryFactory<T extends BaseEntity, I extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public RepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		/*@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new GenericRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
		}*/
		@Override
		protected Object getTargetRepository(RepositoryInformation information) {
			return new GenericRepositoryImpl<T, I>((Class<T>) information.getDomainType(), entityManager);
		}
		
		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			// The RepositoryMetadata can be safely ignored, it is used by the
			// JpaRepositoryFactory
			// to check for QueryDslJpaRepository's which is out of scope.
			return GenericRepositoryImpl.class;
		}
	}
}