package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.BookmarkService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PutMapping("/bookmark/question/{id}")
    public ResponseDto createQuestionBookmark(@PathVariable Long id, Principal principal) {
        return ResponseDto.success(bookmarkService.makeQuestionBookmark(id, principal.getName()));
    }

    @DeleteMapping("/bookmark/question/{id}")
    public ResponseDto deleteQuestionBookmark(@PathVariable Long id, Principal principal) {
        bookmarkService.cancelQuestionBookmark(id, principal.getName());
        return ResponseDto.success();
    }

}
