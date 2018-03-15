package fr.projectfactory.api.service;

import java.util.Set;

import fr.projectfactory.api.model.Group;
import fr.projectfactory.api.model.User;

public interface DirectoryService {

	public Set<User> getUsers();

	public User getUser(String userName);

	public User saveUser(String userName, User user);

	public User saveUserPassword(String userName, String oldPassword, String newPassword);

	public Set<Group> getGroups();

	public Group getGroup(String name);

	public Group saveGroup(String name, Group group);

}