package fr.projectfactory.api.web.v1;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.security.Principal;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fr.projectfactory.api.model.Group;
import fr.projectfactory.api.model.User;
import fr.projectfactory.api.service.DirectoryService;

@RestController
@RequestMapping(value = "/v1/directory", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "directory", description = "User and group directory management")
public class DirectoryController implements ApplicationEventPublisherAware {

	@Autowired
	protected DirectoryService directory;

	protected ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiIgnore
	public ResourceSupport getResources() {
		ResourceSupport res = new ResourceSupport();
		res.add(linkTo(DirectoryController.class).withSelfRel());
		res.add(linkTo(methodOn(DirectoryController.class).getUsers()).withRel("users"));
		res.add(linkTo(methodOn(DirectoryController.class).getGroups()).withRel("groups"));
		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	@ApiOperation(value = "Get all users")
	public Collection<User> getUsers() {
		return directory.getUsers();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/{userName}")
	@ApiOperation(value = "Get a single user by its userName")
	public User getUser(@PathVariable String userName) {
		User u = directory.getUser(userName);
		u.add(linkTo(methodOn(DirectoryController.class).getUser(userName)).withSelfRel());
		return u;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/{userName}")
	@ApiOperation(value = "Update a single user by its userName")
	public User saveUser(Principal p, @PathVariable String userName, @Valid @RequestBody User user) {
		User u = directory.saveUser(userName, user);
		u.add(linkTo(methodOn(DirectoryController.class).saveUser(p, userName, user)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "DIRECTORY_SAVE_USER", user.toString()));
		return u;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/groups")
	@ApiOperation(value = "Get all groups")
	public Collection<Group> getGroups() {
		return directory.getGroups();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/groups/{name}")
	@ApiOperation(value = "Get a single group by name")
	public Group getGroup(@PathVariable String name) {
		Group g = directory.getGroup(name);
		g.add(linkTo(methodOn(DirectoryController.class).getGroup(name)).withSelfRel());
		return g;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/groups/{name}")
	@ApiOperation(value = "Update a single group by its name")
	public Group saveGroup(Principal p, @PathVariable String name, @Valid @RequestBody Group group) {
		Group g = directory.saveGroup(name, group);
		g.add(linkTo(methodOn(DirectoryController.class).saveGroup(p, name, group)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "DIRECTORY_SAVE_GROUP", group.toString()));
		return g;
	}

}
