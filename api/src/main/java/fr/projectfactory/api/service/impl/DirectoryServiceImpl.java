package fr.projectfactory.api.service.impl;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;

import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import fr.projectfactory.api.dao.ldap.GroupRepo;
import fr.projectfactory.api.dao.ldap.UserRepo;
import fr.projectfactory.api.exception.BusinessException;
import fr.projectfactory.api.model.Group;
import fr.projectfactory.api.model.User;
import fr.projectfactory.api.service.DirectoryService;

@Service
public class DirectoryServiceImpl implements DirectoryService, BaseLdapNameAware {

	@Autowired
	protected LdapTemplate ldapTemplate;

	@Autowired
	protected UserRepo userRepo;

	@Autowired
	protected GroupRepo groupRepo;

	@Autowired
	protected PasswordValidator passwordValidator;

	protected LdapName baseLdapPath;
	protected LdapShaPasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();

	private static final Random random;

	static {
		Random randomLocal = null;
		try {
			randomLocal = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			randomLocal = new Random();
		}
		random = randomLocal;
	}

	@Override
	public void setBaseLdapPath(LdapName baseLdapPath) {
		this.baseLdapPath = baseLdapPath;
	}

	public LdapName toAbsoluteDn(Name relativeName) {
		return LdapNameBuilder.newInstance(baseLdapPath).add(relativeName).build();
	}

	@Override
	public Set<User> getUsers() {
		return Sets.newHashSet(userRepo.findAll());
	}

	@Override
	public User getUser(String userName) {
		return userRepo.findOneByUserName(userName);
	}

	@Override
	public User saveUser(String userName, User user) {
		User existingUser = userRepo.findOneByUserName(userName);
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setDisplayName(user.getFirstName() + " " + user.getLastName());
		existingUser.setMail(user.getMail());
		return userRepo.save(existingUser);
	}

	@Override
	public User saveUserPassword(String userName, String oldPassword, String newPassword) {
		// Check password policy
		RuleResult ruleResult = passwordValidator.validate(PasswordData.newInstance(newPassword, userName, null));
		if (!ruleResult.isValid()) {
			StringBuilder sb = new StringBuilder("Failed to change password: new password does not match the policy.");
			for (String msg : passwordValidator.getMessages(ruleResult)) {
				sb.append('\n').append(msg);
			}
			throw new BusinessException(sb.toString());
		}

		// Check old password
		try {
			ldapTemplate.authenticate(query().where("uid").is(userName), oldPassword);
		} catch (AuthenticationException e) {
			throw new BusinessException("Failed to change user password: old password is invalid.", e);
		}

		// Encode new password (with 32-bits salt)
		byte[] salt = new byte[4];
		random.nextBytes(salt);
		String newPasswordEncoded = passwordEncoder.encodePassword(newPassword, salt);

		// Update user
		User user = userRepo.findOneByUserName(userName);
		user.setUserPassword(newPasswordEncoded);
		return userRepo.save(user);
	}

	@Override
	public Set<Group> getGroups() {
		return Sets.newHashSet(groupRepo.findAll());
	}

	@Override
	public Group getGroup(String name) {
		return groupRepo.findOneByName(name);
	}

	@Override
	public Group saveGroup(String name, Group group) {
		Group existingGroup = groupRepo.findOneByName(name);
		existingGroup.setDescription(group.getDescription());
		existingGroup.setMembers(group.getMembers());
		return groupRepo.save(existingGroup);
	}

}
