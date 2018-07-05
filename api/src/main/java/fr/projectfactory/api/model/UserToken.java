package fr.projectfactory.api.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = { "token", "tokenHash" })
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_tokens")
public class UserToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "uid")
	private String uid;

	@JsonProperty(access = Access.READ_ONLY)
	@Transient
	private String token;

	@JsonIgnore
	@NotNull
	@Column(name = "token_hash", length = 96)
	private String tokenHash;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "comments")
	private String comments;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Calendar createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires_at")
	private Calendar expiresAt;

}
