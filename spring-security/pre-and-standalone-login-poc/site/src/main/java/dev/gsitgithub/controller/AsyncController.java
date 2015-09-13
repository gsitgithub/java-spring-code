package dev.gsitgithub.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.gsitgithub.services.AsyncService;
import dev.gsitgithub.util.AsyncLogUtil;

@RestController
public class AsyncController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@Autowired
	private AsyncService asyncService;

	@RequestMapping("/async")
	public @ResponseBody String greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		String result = "Hello " + name;
		asyncService.create(AsyncLogUtil.getAsyncLog(result));
		try {
			throw new RuntimeException("my messages");
		} catch (RuntimeException e) {
			asyncService.logExceptionAsync(result, e);
		}
		return result;
	}
}
