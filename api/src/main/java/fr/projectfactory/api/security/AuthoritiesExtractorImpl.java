package fr.projectfactory.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthoritiesExtractorImpl extends AbstractAttributeExtractor implements AuthoritiesExtractor {

	private static final Logger logger = LoggerFactory.getLogger(AuthoritiesExtractorImpl.class);

	private String userAttribute;
	private GrantedAuthoritiesMapper userAuthorityMapper;
	private String groupAttribute;
	private GrantedAuthoritiesMapper groupAuthorityMapper;
	private Set<String> defaultAuthorities;
	private Map<String, List<String>> authorityMapping;

	@Override
	public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		// Add default authorities
		if (defaultAuthorities != null) {
			for (String authority : defaultAuthorities) {
				logger.debug("Adding default authority={}", authority);
				authorities.add(new SimpleGrantedAuthority(authority));
			}
		}

		// Add user authority
		String user = getAttributeValue(map, userAttribute);
		if (user != null && !user.isEmpty()) {
			Collection<? extends GrantedAuthority> userAuthorities = AuthorityUtils.createAuthorityList(user);
			if (getUserAuthorityMapper() != null) {
				userAuthorities = getUserAuthorityMapper().mapAuthorities(userAuthorities);
			}
			logger.debug("Adding user authorities={}", userAuthorities);
			authorities.addAll(userAuthorities);
		}

		// Add group authorities
		String groups = getAttributeValue(map, groupAttribute);
		if (groups != null && !groups.isEmpty()) {
			Collection<? extends GrantedAuthority> groupAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(groups);
			if (getGroupAuthorityMapper() != null) {
				groupAuthorities = getGroupAuthorityMapper().mapAuthorities(groupAuthorities);
			}
			logger.debug("Adding group authorities={}", groupAuthorities);
			authorities.addAll(groupAuthorities);
		}

		// Add mapped authorities
		if (authorityMapping != null) {
			Set<String> currentAuthorities = AuthorityUtils.authorityListToSet(authorities);
			for (Entry<String, List<String>> mapping : authorityMapping.entrySet()) {
				for (String authority : mapping.getValue()) {
					if (currentAuthorities.contains(authority)) {
						logger.debug("Adding mapped authority={} from existing authority={}", mapping.getKey(), authority);
						authorities.add(new SimpleGrantedAuthority(mapping.getKey()));
						break;
					}
				}
			}
		}

		logger.debug("Extracted authorities={}", authorities);
		return new ArrayList<GrantedAuthority>(authorities);
	}

}
