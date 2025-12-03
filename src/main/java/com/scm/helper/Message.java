package com.scm.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	private String content;
	
	@Builder.Default
	private MessageType type=MessageType.red;
	
	
	
}
