package fr.projectfactory.api.web.v1;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.projectfactory.api.model.PasswordChange;
import fr.projectfactory.api.model.User;
import fr.projectfactory.api.service.DirectoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "account", description = "Account management")
public class AccountController implements ApplicationEventPublisherAware {

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
		res.add(linkTo(AccountController.class).withSelfRel());
		res.add(linkTo(methodOn(AccountController.class).getPrincipal(null)).withRel("principal"));
		res.add(linkTo(methodOn(AccountController.class).getUser(null)).withRel("user"));
		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/principal")
	@ApiOperation(value = "Get the authenticated principal")
	public Authentication getPrincipal(Principal p) {
		if (p instanceof OAuth2Authentication) {
			return ((OAuth2Authentication) p).getUserAuthentication();
		} else {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user")
	@ApiOperation(value = "Get the authenticated user")
	public User getUser(Principal p) {
		User u = directory.getUser(p.getName());
		u.add(linkTo(methodOn(AccountController.class).getUser(p)).withSelfRel());
		return u;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user")
	@ApiOperation(value = "Update the authenticated user")
	public User saveUser(Principal p, @Valid @RequestBody User user) {
		User u = directory.saveUser(p.getName(), user);
		u.add(linkTo(methodOn(AccountController.class).saveUser(p, user)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_SAVE_USER", user.toString()));
		return u;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/password")
	@ApiOperation(value = "Change the authenticated user password")
	public void changePassword(Principal p, @Valid @RequestBody PasswordChange passwordChange) {
		directory.saveUserPassword(p.getName(), passwordChange.getOldPassword(), passwordChange.getNewPassword());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_CHANGE_PASSWORD"));
	}

}
