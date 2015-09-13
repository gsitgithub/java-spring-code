package dev.gsitgithub.services;

import dev.gsitgithub.entity.AsyncLog;
import dev.gsitgithub.generic.service.GenericService;

public interface AsyncService extends GenericService<AsyncLog, Long> {
	//void logMessageScheduled();

	AsyncLog logExceptionAsync(String message, Exception e);
}
