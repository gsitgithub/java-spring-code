package dev.gsitgithub.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import dev.gsitgithub.generic.entity.BaseEntity;

@Entity 
@Table(name="ASYNC_LOG")
public class AsyncLog implements BaseEntity {
     
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="EXCEPTION_TYPE")
    private String exceptionType;
    
    @Column(name="MESSAGE")
    private String message;
    
    @Column(name="STACK_TRACE", columnDefinition="TEXT")
    private String stackTrace;
    
    @Column(name="CLASS_NAME")
    private String className;
    
    @Column(name="METHOD_NAME")
    private String methodName;
    
    @Column(name="LINE_NUMBER")
    private int lineNumber;
    
    @Column(name="DATE_TIME", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    public Long getId() {
		return id;
	}
    public void setId(Long id) {
		this.id = id;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}
