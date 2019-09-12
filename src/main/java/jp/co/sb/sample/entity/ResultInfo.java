package jp.co.sb.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class is annotated with {@code @Entity} but it's not really a persistent class.<br>
 * NativeJpaQuery in Spring Data JPA checks if the returning value
 * of the query methods is the given Entity class, so if we want to return
 * non-managed class in query methods of repositories,
 * we have to define the class with {@code @Entity}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResultInfo {
	@Id
    private String msn;
	private String api_key;
}