package com.vaistra.services.impl;

import com.vaistra.dto.CategoryDTO;
import com.vaistra.entities.Category;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.repositories.CategoryRepository;
import com.vaistra.services.CategoryService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AppUtils appUtilsComment;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository, AppUtils appUtilsComment) {
        this.categoryRepository = repository;
        this.appUtilsComment = appUtilsComment;
    }

    @Override
    public CategoryDTO getCategoryById(int id) {
        return null;
    }

    @Override
    public CategoryDTO getTrashedCategoryById(int id) {
        return null;
    }

    @Override
    public CategoryDTO getInActiveCategoryById(int id) {
        return null;
    }

    @Override
    public List<CategoryDTO> getAllCategories(Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return appUtilsComment.categoriesToDtos(categoryRepository.findAll());
    }

    @Override
    public List<CategoryDTO> getAllCategoriesSortByField(String field) {
        return null;
    }

    @Override
    public List<CategoryDTO> getAllTrashedCategories() {
        return null;
    }

    @Override
    public List<CategoryDTO> getAllInActiveCategories() {
        return null;
    }

    @Override
    public CategoryDTO updateCategoryById(int id, CategoryDTO categoryDTO) {
        return null;
    }

    @Override
    public CategoryDTO updateCategoryStatusById(int id, boolean status) {
        return null;
    }

//    @Override
//    public CategoryDTO updateCategoryStatusById(int id, Boolean status) {
//        return null;
//    }

    @Override
    public int deleteCategoryById(int id) {
        return 0;
    }

    @Override
    public int trashCategoryById(int id) {
        return 0;
    }

    @Override
    public int restoreCategoryById(int id) {
        return 0;
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        Category c = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

        if (c != null)
            throw new DuplicateEntryException("Category name: " + c.getCategoryName() + " already exist..!");

        categoryDTO.setCategoryName(categoryDTO.getCategoryName().toUpperCase());
        categoryDTO.setCategoryDescription(categoryDTO.getCategoryDescription());
        categoryDTO.setCreatedAt(LocalDateTime.now());

        return appUtilsComment.categoryToDto(categoryRepository.save(appUtilsComment.dtoToCategory(categoryDTO)));


    }


}
