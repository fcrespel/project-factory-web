package fr.projectfactory.api.dao;

import java.util.Collection;

import javax.naming.Name;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.ldap.repository.Query;

import fr.projectfactory.api.model.Group;

public interface GroupRepo extends LdapRepository<Group> {

	Group findOneByName(String name);

	@Query("(member={0})")
	Collection<Group> findByMember(Name member);

}
