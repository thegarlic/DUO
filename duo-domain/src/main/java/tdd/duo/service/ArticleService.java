package tdd.duo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tdd.duo.domain.Article;
import tdd.duo.domain.User;
import tdd.duo.dto.ArticlePage;
import tdd.duo.exception.ArticleCreationException;
import tdd.duo.exception.ArticleModificationException;
import tdd.duo.exception.ArticleNotFoundException;
import tdd.duo.repository.ArticleRepository;

import javax.naming.AuthenticationException;
import java.util.List;

/**
 * Created by yoon on 15. 4. 14..
 */
@Service
public class ArticleService {

    public static final String VALIDATION_EXCEPTION_MESSAGE = "입력데이터를 다시확인해 주시기 바랍니다.";
    public static final String INVALID_REQUEST_EXCEPTION_MESSAGE = "잘못된 요청입니다";
    public static final int PAGE_PER_ARTICLE_NUMBER = 5;
    public static final int PAGENATION_INTERVAL_FROM_CURRENT_PAGENUMBER = 5;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SessionService sessionService;

    public void create(Article article) throws ArticleCreationException {

        User user = sessionService.getCurrentUser();
        article.setAuthor(user);

        //새로 업로드요청한 데이터의 정합성 체크
        if (article.isRegistable())
            articleRepository.save(article);
        else
            throw new ArticleCreationException(VALIDATION_EXCEPTION_MESSAGE);
    }

    public List<Article> findsByQueryString(String query) {

        if (StringUtils.isEmpty(query))
            return null;

        return articleRepository.findsByQueryStringFromTitleAndContent(query);
    }

    public Article modify(Article requestArticle) throws ArticleModificationException {

        User user = sessionService.getCurrentUser();
        requestArticle.setAuthor(user);

        //수정요청 데이터의 정합성 확인
        if (!requestArticle.isRegistable()) {
            throw new ArticleModificationException(VALIDATION_EXCEPTION_MESSAGE);
        }

        Article article = articleRepository.findOne(requestArticle.getId());

        //수정요청에 해당하는 기존의 article이 있는지 확인
        if (article == null) {
            throw new ArticleModificationException(INVALID_REQUEST_EXCEPTION_MESSAGE);
        }

        //기존의 article author와 현재로그인한 user가 같은지 확인
        if (article.getAuthor().getId() != user.getId()) {
            throw new ArticleModificationException(INVALID_REQUEST_EXCEPTION_MESSAGE);
        }

        Article modifiedArticle = articleRepository.save(requestArticle);
        if (modifiedArticle == null)
            throw new ArticleModificationException("예기치못한 에러발생");

        return modifiedArticle;
    }

    //TODO authenticationException을 DuoAuthenticationException으로 바꾸고, 다른곳에도 적용
    //TODO 실제 현업에서는 글을 삭제하지 않는다. 글 상태값을 변경하는 형태로 리팩토링 하자
    public void delete(Long articleId) throws ArticleNotFoundException, AuthenticationException {

        if (articleId == null || articleId <= 0)
            throw new ArticleNotFoundException();

        Article article = articleRepository.findOne(articleId);
        if (article == null || article.getAuthor() == null)
            throw new ArticleNotFoundException();

        User currentUser = sessionService.getCurrentUser();
        User author = article.getAuthor();
        if (currentUser.getId() != author.getId())
            throw new AuthenticationException();

        articleRepository.delete(article);
    }

    public Article findById(Long articleId) {
        if (articleId <= 0)
            return null;

        return articleRepository.findOne(articleId);
    }

    //TODO Throw IllegalException, if pageNumber is exceeding limit
    public ArticlePage findsByPageNumber(int pageNumber) {
        return new ArticlePage(articleRepository.findAll(getPageRequest(pageNumber)));
    }

    public PageRequest getPageRequest(int pageNumber) {
        return new PageRequest(pageNumber - 1, PAGE_PER_ARTICLE_NUMBER, Sort.Direction.DESC, "id");
    }
}
