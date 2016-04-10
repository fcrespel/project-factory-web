package fr.projectfactory.api.dao;

import java.util.Collection;

import javax.naming.Name;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.ldap.repository.LdapRepository;
import org.springframework.ldap.repository.Query;

import fr.projectfactory.api.model.Group;

@RepositoryRestResource(exported = false)
public interface GroupRepo extends LdapRepository<Group> {

	Group findOneByName(String name);

	@Query("(member={0})")
	Collection<Group> findByMember(Name member);

}
