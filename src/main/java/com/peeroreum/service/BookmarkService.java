package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.bookmark.QuestionBookMark;
import com.peeroreum.dto.member.MemberProfileDto;
import com.peeroreum.dto.mypage.MyQuestionReadDto;
import com.peeroreum.dto.question.QuestionListReadDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.repository.QuestionBookmarkRepository;
import com.peeroreum.repository.QuestionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookmarkService {

    private final QuestionBookmarkRepository questionBookmarkRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerService answerService;
    private final LikeService likeService;

    public BookmarkService(QuestionBookmarkRepository questionBookmarkRepository, QuestionRepository questionRepository, MemberRepository memberRepository, AnswerService answerService, LikeService likeService) {
        this.questionBookmarkRepository = questionBookmarkRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.answerService = answerService;
        this.likeService = likeService;
    }

    public void makeQuestionBookmark(Long id, String name) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(isBookmarkedQuestion(question, member)) {
            throw new CustomException(ExceptionType.ALREADY_BOOKMARKED);
        }

        QuestionBookMark questionBookMark = new QuestionBookMark(member, question);
        questionBookmarkRepository.save(questionBookMark);
    }

    public void cancelQuestionBookmark(Long id, String name) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(!isBookmarkedQuestion(question, member)) {
            throw new CustomException(ExceptionType.BOOKMARK_NOT_FOUND);
        }

        questionBookmarkRepository.deleteByQuestionAndMember(question, member);
    }

    public void deleteAllByQuestion(Question question) {
        questionBookmarkRepository.deleteAllByQuestion(question);
    }

    public void deleteAllByMember(Member member) {
        questionBookmarkRepository.deleteAllByMember(member);
    }

    public boolean isBookmarkedQuestion(Question question, Member member) {
        return questionBookmarkRepository.existsByQuestionAndMember(question, member);
    }

    public MyQuestionReadDto getMyQuestions(String name, int page) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        List<Question> questions = questionBookmarkRepository.findAllByMemberOrderByIdDesc(member, PageRequest.of(page, 15))
                .stream().map(QuestionBookMark::getQuestion).toList();

        List<QuestionListReadDto> questionListReadDtos = new ArrayList<>();
        for(Question question : questions) {
            Member writer = question.getMember();
            QuestionListReadDto questionListReadDto = new QuestionListReadDto(
                    question.getId(),
                    new MemberProfileDto(writer.getGrade(), writer.getImage() != null? writer.getImage().getImagePath() : null, writer.getNickname()),
                    question.getTitle(), question.getContent(), answerService.checkIfSelected(question), likeService.countByQuestion(question), answerService.countByQuestion(question), question.getCreatedTime()
            );
            questionListReadDtos.add(questionListReadDto);
        }

        return new MyQuestionReadDto(questionBookmarkRepository.countAllByMember(member), questionListReadDtos);
    }
}
