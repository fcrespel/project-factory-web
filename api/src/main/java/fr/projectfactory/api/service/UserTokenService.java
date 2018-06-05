package fr.projectfactory.api.service;

import java.util.Set;

import fr.projectfactory.api.model.UserToken;

public interface UserTokenService {

	public Set<UserToken> findAllByUid(String uid);

	public UserToken save(UserToken userToken);

	public void delete(Long id, String uid);

}
