package dev.gsitgithub.repo;

import dev.gsitgithub.generic.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import dev.gsitgithub.entity.AsyncLog;

@Repository
public interface AsyncLogRepository extends GenericRepository<AsyncLog, Long> {
}
