package fr.projectfactory.api.service;

import java.util.Set;

import fr.projectfactory.api.model.Group;
import fr.projectfactory.api.model.User;

public interface DirectoryService {

	public abstract Set<User> getUsers();

	public abstract User getUser(String userName);

	public abstract User saveUser(String userName, User user);

	public abstract User saveUserPassword(String userName, String oldPassword, String newPassword);

	public abstract Set<Group> getGroups();

	public abstract Group getGroup(String name);

	public abstract Group saveGroup(String name, Group group);

}