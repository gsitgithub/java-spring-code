package dev.gsitgithub.generic.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import dev.gsitgithub.generic.entity.BaseEntity;

@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity, ID extends Serializable> extends
		JpaSpecificationExecutor<T>,
		JpaRepository<T, ID> {

}
