package com.vaistra.controller;

import com.vaistra.dto.CategoryDTO;
import com.vaistra.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("add")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO c = categoryService.addCategory(categoryDTO);
        if (c != null)
            return new ResponseEntity<>("Category created...", HttpStatus.OK);
        else
            return new ResponseEntity<>("Category not created...!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories()
    {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.FOUND);
    }
}
