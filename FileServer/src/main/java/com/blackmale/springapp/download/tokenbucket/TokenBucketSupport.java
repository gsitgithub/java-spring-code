package com.blackmale.springapp.download.tokenbucket;

import javax.servlet.ServletRequest;

/**
 *
 * @author kuldeep
 */

public abstract class TokenBucketSupport implements TokenBucket {

	@Override
	public void takeBlocking(ServletRequest req) throws InterruptedException {
		takeBlocking(req, 1);
	}

	@Override
	public boolean tryTake(ServletRequest req) {
		return tryTake(req, 1);
	}

}
