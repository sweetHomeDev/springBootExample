package jp.co.sb.sample.bean;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MsnJson {
	
	@NotBlank
	public String msn;

}
