package dev.gsitgithub.util;

import java.util.Date;

import dev.gsitgithub.entity.AsyncLog;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncLogUtil {
	public final static boolean DEBUG = true;
	private static Logger log = LoggerFactory.getLogger(AsyncLogUtil.class);
	private static final String NONE = "NONE";
	
	public static AsyncLog logException(String message, Class clazz, Exception t) {
		StackTraceElement l = t.getStackTrace()[0];
		Date date = new Date(System.currentTimeMillis());
/*		if (DEBUG) {
			log.info(DateFormat.getDateTimeInstance().format(date)+" "+
				clazz.getName() + " : " + t.getMessage()+ " at "+
				l.getClassName()+"."+l.getMethodName()+":"+l.getLineNumber());
		}*/
	
		AsyncLog asyncLog = new AsyncLog();
		asyncLog.setMessage(message != null ? message:t.getMessage());
		asyncLog.setStackTrace(ExceptionUtils.getStackTrace(t));
		asyncLog.setClassName(l.getClassName());
		asyncLog.setMethodName(l.getMethodName());
		asyncLog.setLineNumber(l.getLineNumber());
		asyncLog.setExceptionType(clazz.getName());
		asyncLog.setDateTime(date);
		return asyncLog;
	}
	
	public static AsyncLog getAsyncLog(String message) {
		Date date = new Date(System.currentTimeMillis());
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String fullClassName = ste.getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = ste.getMethodName();
		int lineNumber = ste.getLineNumber();
		
/*		log.info(DateFormat.getDateTimeInstance().format(date)+ " " +
				className + "." + methodName + "():" + lineNumber + " " + 
				message);*/
		AsyncLog asyncLog = new AsyncLog();
		asyncLog.setClassName(fullClassName);
		asyncLog.setMethodName(methodName);
		asyncLog.setMessage(message);
		asyncLog.setLineNumber(lineNumber);
		asyncLog.setExceptionType(NONE);
		asyncLog.setDateTime(date);
		return asyncLog;
	}
	
	/**
	 * Returns <i>class.method:linenumber</i> of the caller (or, more accurately
	 * the caller of the caller).
	 * </p>
	 *
	 * <p>
	 * Returns unknown if stacktrace is mucked up. Uses reflection and string
	 * concatenation, so don't overuse this for trivial matters. For exception
	 * handling and for logging, on the other hand, it is useful.
	 *
	 * @return method name of caller's caller and line number (String)
	 */
	public String returnCaller( Class ignoreMe )
	{

	    String              ignoreClass = ignoreMe.getName();
	    StackTraceElement[] steArr      = new Throwable().getStackTrace();

	    if (steArr != null)
	    {
	        // subscript 1 is returnCaller().
	        // subscript 2 is the caller of returnCaller()
	        // subscript 3 is the caller of the caller of returnCaller()...
	        for( int i = 0; i < steArr.length; i++)
	        {
	            if (steArr[i] == null)
	                break;

	            String myclass = steArr[i].getClassName();

	            if (myclass.equals(AsyncLogUtil.class.getName()))
	                continue;

	            if (ignoreClass.equals(myclass))
	                continue;

	            return steArr[i].getClassName()
	                    + "."
	                    + steArr[i].getMethodName()
	                    + ":"
	                    + steArr[i].getLineNumber();
	        }
	    }

	    return "unknown";
	}
}
