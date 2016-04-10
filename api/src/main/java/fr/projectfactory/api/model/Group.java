package fr.projectfactory.api.model;

import java.util.HashSet;
import java.util.Set;

import javax.naming.Name;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@EqualsAndHashCode(callSuper = true)
@Entry(objectClasses = { "groupOfNames", "top" }, base = "ou=groups")
public class Group extends ResourceSupport {

	@Id
	@JsonIgnore
	private Name dn;

	@NotNull
	@Attribute(name = "cn")
	@DnAttribute(value = "cn", index = 1)
	private String name;

	@Attribute(name = "description")
	private String description;

	@JsonIgnore
	@Attribute(name = "member")
	private Set<Name> members = new HashSet<Name>();

	public void addMember(Name newMember) {
		members.add(newMember);
	}

	public void removeMember(Name member) {
		members.remove(member);
	}
}
