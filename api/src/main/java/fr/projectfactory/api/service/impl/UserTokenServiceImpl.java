package fr.projectfactory.api.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;

import fr.projectfactory.api.dao.jpa.UserTokenRepo;
import fr.projectfactory.api.model.UserToken;
import fr.projectfactory.api.service.UserTokenService;

@Service
public class UserTokenServiceImpl implements UserTokenService {

	@Autowired
	protected UserTokenRepo userTokenRepo;

	protected StringKeyGenerator keyGenerator = KeyGenerators.string();

	@Override
	public Set<UserToken> findAllByUid(String uid) {
		return Sets.newLinkedHashSet(userTokenRepo.findAllByUid(uid));
	}

	public UserToken save(UserToken userToken) {
		if (Strings.isNullOrEmpty(userToken.getTokenHash())) {
			if (Strings.isNullOrEmpty(userToken.getToken())) {
				userToken.setToken(keyGenerator.generateKey());
			}
			String tokenHash = Hashing.sha384().hashString(userToken.getToken(), StandardCharsets.UTF_8).toString();
			userToken.setTokenHash(tokenHash);
		}
		return userTokenRepo.save(userToken);
	}

	@Override
	public void delete(Long id, String uid) {
		userTokenRepo.deleteByIdAndUid(id, uid);
	}

}
