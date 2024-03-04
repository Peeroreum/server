package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.BookmarkService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/bookmark/question/{id}")
    public ResponseDto createQuestionBookmark(@PathVariable Long id, Principal principal) {
        bookmarkService.makeQuestionBookmark(id, principal.getName());
        return ResponseDto.success();
    }

    @DeleteMapping("/bookmark/question/{id}")
    public ResponseDto deleteQuestionBookmark(@PathVariable Long id, Principal principal) {
        bookmarkService.cancelQuestionBookmark(id, principal.getName());
        return ResponseDto.success();
    }

    @GetMapping("/bookmark/question/my")
    public ResponseDto getBookmarkQuestions(Principal principal, @RequestParam(defaultValue = "0") int page) {
        return ResponseDto.success(bookmarkService.getMyQuestions(principal.getName(), page));
    }

}
