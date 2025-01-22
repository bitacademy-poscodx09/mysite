package mysite.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardVo {
	private Long Id;
	private String title;
	private String contents;
	private String regDate;
	private Integer hit;
	private Integer groupNo;
	private Integer orderNo;
	private Integer depth;
	private Long userId;
	private String userName;
}
