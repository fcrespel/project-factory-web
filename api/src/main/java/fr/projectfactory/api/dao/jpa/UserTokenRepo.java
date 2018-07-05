package fr.projectfactory.api.dao.jpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import fr.projectfactory.api.model.UserToken;

public interface UserTokenRepo extends JpaRepository<UserToken, Long> {

	Collection<UserToken> findAllByUid(String uid);

	@Transactional
	void deleteByIdAndUid(Long id, String uid);

}
