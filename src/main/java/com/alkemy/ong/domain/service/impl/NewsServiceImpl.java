package com.alkemy.ong.domain.service.impl;

import com.alkemy.ong.domain.util.Url;
import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.dto.PageDTO;
import com.alkemy.ong.exception.BadRequestException;
import com.alkemy.ong.exception.NotFoundException;
import com.alkemy.ong.mapper.NewsMapper;
import com.alkemy.ong.domain.model.Category;
import com.alkemy.ong.domain.model.News;
import com.alkemy.ong.domain.repository.CategoryRepository;
import com.alkemy.ong.domain.repository.NewsRepository;
import com.alkemy.ong.domain.service.INewsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NewsServiceImpl implements INewsService {

    private static final String NEWS = "news";
    private static final String ID_NOT_FOUND = "Id not found: ";
    private static final String URI = Url.URL + Url.NEWS_URI + Url.PAGE_URI;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NewsMapper newsMapper;

    @Transactional
    public NewsDTO save(NewsDTO dto){
        Category category = getCategoryNews();
        News news = newsMapper.newsDTO2Entity(dto);
        news.setCategory(category);
        News newsSaved = newsRepository.save(news);
        return newsMapper.newsEntity2DTO(newsSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public NewsDTO getById(Long id){
        Optional<News> entityResult = this.newsRepository.findById(id);
        if(entityResult.isEmpty()){
            throw new NotFoundException(ID_NOT_FOUND);
        }
        return this.newsMapper.newsEntity2DTO(entityResult.get());
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Optional<News> entity = this.newsRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("News with id provided not found");
        }
        entity.get().setDeleted(true);
        this.newsRepository.save(entity.get());
    }

    @Transactional
    @Override
    public NewsDTO updateNewsById(Long id, NewsDTO dto) {
        Category category = getCategoryNews();
        News newsEntity = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ID_NOT_FOUND + id));
        newsMapper.newsDTO2EntityWithId(newsEntity, dto);
        newsEntity.setCategory(category);
        return newsMapper.newsEntity2DTO(newsRepository.save(newsEntity));
    }

    @NotNull
    private Category getCategoryNews() {
        Optional<Category> category = categoryRepository.findByName(NEWS);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found: " + NEWS);
        }
        return category.get();
    }

    @Transactional(readOnly = true)
    @Override
    public PageDTO<NewsDTO> getAllNewsPageable(Integer page) {
        PageDTO<NewsDTO> newsDTOPageDTO = new PageDTO<>();
        try {
            Page<News> news = this.newsRepository.findAll(PageRequest.of(page - 1, Url.MAX_PAGE, Sort.by("name")));
            return Url.pagination(newsDTOPageDTO,news,page,this.newsMapper.newsEntityPage2Dto(news),URI);
        }catch (Exception e){
            throw new NotFoundException(e.getMessage());
        }
    }

}
