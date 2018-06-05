package fr.projectfactory.api.web.v1;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.security.Principal;
import java.util.Calendar;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.projectfactory.api.model.User;
import fr.projectfactory.api.model.UserToken;
import fr.projectfactory.api.model.request.PasswordChangeRequest;
import fr.projectfactory.api.model.request.UserTokenRequest;
import fr.projectfactory.api.service.DirectoryService;
import fr.projectfactory.api.service.UserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "account", description = "Account management")
public class AccountController implements ApplicationEventPublisherAware {

	@Autowired
	protected DirectoryService directoryService;

	@Autowired
	protected UserTokenService userTokenService;

	protected ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@GetMapping
	@ApiIgnore
	public ResourceSupport getResources() {
		ResourceSupport res = new ResourceSupport();
		res.add(linkTo(AccountController.class).withSelfRel());
		res.add(linkTo(methodOn(AccountController.class).getPrincipal(null)).withRel("principal"));
		res.add(linkTo(methodOn(AccountController.class).getUser(null)).withRel("user"));
		res.add(linkTo(methodOn(AccountController.class).getTokens(null)).withRel("tokens"));
		return res;
	}

	@GetMapping("/principal")
	@ApiOperation("Get the authenticated principal")
	public Authentication getPrincipal(Principal p) {
		if (p instanceof OAuth2Authentication) {
			return ((OAuth2Authentication) p).getUserAuthentication();
		} else {
			return null;
		}
	}

	@GetMapping("/user")
	@ApiOperation("Get the authenticated user")
	public User getUser(Principal p) {
		User u = directoryService.getUser(p.getName());
		u.add(linkTo(methodOn(AccountController.class).getUser(p)).withSelfRel());
		return u;
	}

	@PostMapping("/user")
	@ApiOperation("Update the authenticated user")
	public User saveUser(Principal p, @Valid @RequestBody User user) {
		User u = directoryService.saveUser(p.getName(), user);
		u.add(linkTo(methodOn(AccountController.class).saveUser(p, user)).withSelfRel());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_SAVE_USER", user.toString()));
		return u;
	}

	@PostMapping("/password")
	@ApiOperation("Change the authenticated user password")
	public void changePassword(Principal p, @Valid @RequestBody PasswordChangeRequest passwordChange) {
		directoryService.saveUserPassword(p.getName(), passwordChange.getOldPassword(), passwordChange.getNewPassword());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_CHANGE_PASSWORD"));
	}

	@GetMapping("/tokens")
	@ApiOperation("Get tokens associated with the authenticated user")
	public Set<UserToken> getTokens(Principal p) {
		return userTokenService.findAllByUid(p.getName());
	}

	@PostMapping("/tokens")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Generate a new token and associate it with the authenticated user")
	public UserToken generateToken(Principal p, @RequestBody UserTokenRequest userTokenRequest) {
		UserToken userToken = new UserToken();
		userToken.setUid(p.getName());
		userToken.setComments(userTokenRequest.getComments());
		if (userTokenRequest.getDuration() != null) {
			Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.SECOND, userTokenRequest.getDuration());
			userToken.setExpiresAt(expiresAt);
		}
		userToken = userTokenService.save(userToken);
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_GENERATE_TOKEN", userToken.toString()));
		return userToken;
	}

	@DeleteMapping("/tokens/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Revoke a token associated with the authenticated user by its id")
	public void revokeToken(Principal p, @PathVariable Long id) {
		userTokenService.delete(id, p.getName());
		applicationEventPublisher.publishEvent(new AuditApplicationEvent(p.getName(), "ACCOUNT_REVOKE_TOKEN", Long.toString(id)));
	}

}
