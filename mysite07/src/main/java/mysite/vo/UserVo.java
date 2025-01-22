package mysite.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserVo {
	private Long id;
	
	@NotEmpty
	@Size(min=2, max=8)
	private String name;
	
	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	@Size(min=2, max=8)
	private String password;

	private String gender;
	private String joinDate;
	private String role;
}
