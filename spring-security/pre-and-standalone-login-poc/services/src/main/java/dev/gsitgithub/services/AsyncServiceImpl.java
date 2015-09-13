package dev.gsitgithub.services;

import dev.gsitgithub.generic.service.GenericServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.gsitgithub.entity.AsyncLog;
import dev.gsitgithub.repo.AsyncLogRepository;
import dev.gsitgithub.util.AsyncLogUtil;

@Service
@Transactional
public class AsyncServiceImpl extends GenericServiceImpl<AsyncLog, Long> implements AsyncService {
	private static final String NONE = "NONE";
	private AsyncLogRepository asyncLogRepository;
	@Autowired
	public AsyncServiceImpl(AsyncLogRepository asyncLogRepository) {
		super(asyncLogRepository);
		this.asyncLogRepository = asyncLogRepository;
	}

	private final Logger logger = LoggerFactory.getLogger(AsyncService.class);
	
	//Below are other possible configurations
	//@Scheduled(fixedRate = 50000, , initialDelay=10*60*1000)
	//@Scheduled(cron="*/5 * * * * ?")
	// @Scheduled(cron = "${cron.expression}")----this can be set in async.properties and referred here
	//@Scheduled(cron="*/2 * * * * MON-FRI")
	
	@Override
	public AsyncLog logExceptionAsync(String message, Exception e){
		return create(AsyncLogUtil.logException(message, e.getClass(), e));
	}
}
