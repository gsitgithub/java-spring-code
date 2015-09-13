package dev.gsitgithub.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity //(name = "Role")
@Table(name="ROLE")
public class Role   {
     
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(name="NAME")
    private String name;
    
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name="ROLE_RIGHT",
		joinColumns={ @JoinColumn(name="ROLE_ID")},
		inverseJoinColumns={@JoinColumn(name="RIGHT_ID")})
    private Set<UserRight> rights = new HashSet<UserRight>();
    
    
    public Integer getId() {
		return id;
	}
    public void setId(Integer id) {
		this.id = id;
	}
    public String getName() {
		return name;
	}
    public void setName(String name) {
		this.name = name;
	}
    public Set<UserRight> getRights() {
		return rights;
	}
    public void setRights(Set<UserRight> rights) {
		this.rights = rights;
	}
}