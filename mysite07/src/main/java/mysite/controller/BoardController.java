package mysite.controller;

import mysite.service.BoardService;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;
import mysite.web.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@RequestMapping("")
	public String index(
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="kwd", defaultValue="") String keyword,
		Authentication authentication,
		Model model) {
		
		// model.addAllAttributes(map);
		model.addAttribute("map", boardService.getContentsList(page, keyword));
		model.addAttribute("keyword", keyword);
		model.addAttribute("principal", authentication != null ? authentication.getPrincipal() : null);
		
		return "board/index";
	}
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") Long id, Model model) {
		BoardVo boardVo = boardService.getContents(id);
		model.addAttribute("boardVo", boardVo);
		return "board/view";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(
		Authentication authentication,
		@PathVariable("id") Long boardId,
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="kwd", defaultValue="") String keyword) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		boardService.deleteContents(boardId, authUser.getId());

		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@RequestMapping("/modify/{id}")
	public String modify(Authentication authentication, @PathVariable("id") Long id, Model model) {
		UserVo authUser = (UserVo)authentication.getPrincipal();

		BoardVo boardVo = boardService.getContents(id, authUser.getId());
		model.addAttribute("boardVo", boardVo);

		return "board/modify";
	}

	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
		Authentication authentication,
		BoardVo boardVo,
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="kwd", defaultValue="") String keyword) {
		UserVo authUser = (UserVo)authentication.getPrincipal();

		boardVo.setUserId(authUser.getId());
		boardService.modifyContents(boardVo);

		return "redirect:/board/view/" + boardVo.getId() + "?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}

	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}

	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(
		Authentication authentication,
		@ModelAttribute BoardVo boardVo,
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="kwd", defaultValue="") String keyword) {
		UserVo authUser = (UserVo)authentication.getPrincipal();

		boardVo.setUserId(authUser.getId());
		boardService.addContents(boardVo);
		return	"redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}

	@RequestMapping(value="/reply/{id}")
	public String reply(@PathVariable("id") Long id, Model model) {
		BoardVo boardVo = boardService.getContents(id);
		boardVo.setOrderNo(boardVo.getOrderNo() + 1);
		boardVo.setDepth(boardVo.getDepth() + 1);
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/reply";
	}	
}