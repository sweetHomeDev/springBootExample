package jp.co.sb.sample.bean;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Payload1Json {
	
	@Valid
	public @NotNull MsnJson payload1;
	
}
