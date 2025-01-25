package mysite.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SiteVo {
	private Long id;
	private String title;
	private String welcome;
	private String profile;
	private String description;
}
