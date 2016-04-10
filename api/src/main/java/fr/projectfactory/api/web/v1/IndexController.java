package fr.projectfactory.api.web.v1;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiIgnore
public class IndexController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResourceSupport getResources() {
		ResourceSupport res = new ResourceSupport();
		res.add(linkTo(IndexController.class).withSelfRel());
		res.add(linkTo(AccountController.class).withRel("account"));
		res.add(linkTo(DirectoryController.class).withRel("directory"));
		return res;
	}

}
