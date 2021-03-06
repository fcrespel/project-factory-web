package fr.projectfactory.api.dao.ldap;

import org.springframework.data.ldap.repository.LdapRepository;

import fr.projectfactory.api.model.User;

public interface UserRepo extends LdapRepository<User> {

	User findOneByUserName(String userName);

}
