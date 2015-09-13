package dev.gsitgithub.entity;

import java.math.BigInteger;
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
import javax.persistence.Transient;

import dev.gsitgithub.generic.entity.BaseEntity;

@Entity //(name = "User")
@Table(name="USER")
public class User implements BaseEntity {
     
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="EMAIL")
    private String email;
    
	private Boolean enabled;
	private String salt;
	
    @Column(name="USERNAME")
    private String username;
     
    @Column(name="PASSWORDHASH")
    private String passwordHash;
    
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name="USER_ROLE",
    	joinColumns={ @JoinColumn(name="USER_ID")},
    	inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    private Set<Role> roles = new HashSet<Role>();
    
    @Transient
    private final String PERMISSION_PREFIX = "ROLE_RIGHT_";
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
		return username;
	}
    public void setUsername(String username) {
		this.username = username;
	}
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPasswordHash() {
		return passwordHash;
	}
    public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public Boolean getEnabled() {
		return enabled;
	}
    public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
    public String getSalt() {
		return salt;
	}
    public void setSalt(String salt) {
		this.salt = salt;
	}
    
    /**
	 * encrypts the password before saving.
	 */
/*	@PrePersist
	@PreUpdate
	protected void encryptPassword() {
		if (password != null && (!password.matches("^[0-9a-fA-F]+$"))) {
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			// BCrypt bcrypt = new BCrypt();
			// String namak = bcrypt.gensalt(12);
			Random random = new SecureRandom();
			byte[] salt = new byte[SALT_BYTE_SIZE];
			random.nextBytes(salt);
			String salting = toHex(salt);
			this.salt = salting; // namak.getBytes();
			this.password = encoder.encodePassword(password, this.salt);
		}
	}*/

	/**
	 * Converts a byte array into a hexadecimal string.
	 *
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	public static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param hex
	 *            the hex string
	 * @return the hex string decoded into a byte array
	 */
	public static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(
					hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}
}