package dev.gsitgithub.generic.entity;

import java.io.Serializable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public interface BaseEntity {
	Serializable getId();
}
