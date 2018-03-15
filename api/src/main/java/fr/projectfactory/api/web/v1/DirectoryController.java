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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.projectfactory.api.model.Group;
import fr.projectfactory.api.model.User;
import fr.projectfactory.api.service.DirectoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1/directory", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "directory", description = "User and group directory management")
public class DirectoryController implements ApplicationEventPublisherAware {

	@Autowired
	protected DirectoryService directory;

	protected ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@GetMapping
	@ApiIgnore
	public ResourceSupport getResources() {
		ResourceSupport res = new ResourceSupport();
		res.add(linkTo(DirectoryController.class).withSelfRel());
		res.add(linkTo(methodOn(DirectoryController.class).getUsers()).withRel("users"));
		res.add(linkTo(methodOn(DirectoryController.class).getGroups()).withRel("groups"));
		return res;
	}

	@GetMapping("/users")
	@ApiOperation("Get all users")
	public Collection<User> getUsers() {
		return directory.getUsers();
	}

	@GetMapping("/users/{userName}")
	@ApiOperation("Get a single user by its userName")
	public User getUser(@PathVariable String userName) {
		User u = directory.getUser(userName);
		u.add(linkTo(methodOn(DirectoryController.class).getUser(userName)).withSelfRel());
		return u;
	}

	@PutMapping("/users/{userName}")
	@ApiOperation("Update a single user by its userName")
	public User saveUser(Principal p, @PathVariable String userName, @Valid @RequestBody User user) {
		User u = directory.saveUser(userName, user);
		u.add(linkTo(methodOn(DirectoryController.class).saveUser(p, userName, user)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "DIRECTORY_SAVE_USER", user.toString()));
		return u;
	}

	@GetMapping("/groups")
	@ApiOperation("Get all groups")
	public Collection<Group> getGroups() {
		return directory.getGroups();
	}

	@GetMapping("/groups/{name}")
	@ApiOperation("Get a single group by name")
	public Group getGroup(@PathVariable String name) {
		Group g = directory.getGroup(name);
		g.add(linkTo(methodOn(DirectoryController.class).getGroup(name)).withSelfRel());
		return g;
	}

	@PutMapping("/groups/{name}")
	@ApiOperation("Update a single group by its name")
	public Group saveGroup(Principal p, @PathVariable String name, @Valid @RequestBody Group group) {
		Group g = directory.saveGroup(name, group);
		g.add(linkTo(methodOn(DirectoryController.class).saveGroup(p, name, group)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "DIRECTORY_SAVE_GROUP", group.toString()));
		return g;
	}

}
