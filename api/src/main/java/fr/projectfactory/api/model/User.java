package fr.projectfactory.api.model;

import java.nio.charset.Charset;

import javax.naming.Name;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@EqualsAndHashCode(callSuper = true)
@Entry(objectClasses = { "inetOrgPerson", "organizationalPerson", "person", "top" }, base = "ou=users")
public class User extends ResourceSupport {

	@Id
	@JsonIgnore
	private Name dn;

	@NotNull
	@Attribute(name = "uid")
	@DnAttribute(value = "uid", index = 1)
	private String userName;

	@JsonIgnore
	@Attribute(name = "userPassword", type = Type.BINARY)
	private byte[] userPassword;

	@Attribute(name = "cn")
	private String commonName;

	@NotNull
	@Attribute(name = "givenName")
	private String firstName;

	@NotNull
	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "displayName")
	private String displayName;

	@NotNull
	@Attribute(name = "mail")
	private String mail;

	@Attribute(name = "o")
	private String organization;

	public User setUserPassword(byte[] userPassword) {
		this.userPassword = userPassword;
		return this;
	}

	public User setUserPassword(String userPassword) {
		this.userPassword = (userPassword != null) ? userPassword.getBytes(Charset.forName("UTF-8")) : null;
		return this;
	}

	@JsonProperty
	public boolean hasPassword() {
		return this.userPassword != null;
	}

}
