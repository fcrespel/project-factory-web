package com.cgi.projectfactory.api.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.ldap.repository.LdapRepository;

import com.cgi.projectfactory.api.model.User;

@RepositoryRestResource(exported = false)
public interface UserRepo extends LdapRepository<User> {

	User findOneByUserName(String userName);

}
