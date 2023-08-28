package com.vaistra.services.impl;

import com.vaistra.dto.PostDTO;
import com.vaistra.entities.Category;
import com.vaistra.entities.Post;
import com.vaistra.entities.User;
import com.vaistra.exception.IsActiveExceptionHandler;
import com.vaistra.exception.IsDeleteExceptionHandler;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.*;
import com.vaistra.services.PostService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final AppUtils appUtilsComment;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository,
                           CommentRepository commentRepository, TagRepository tagRepository, AppUtils appUtilsComment)
    {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.appUtilsComment = appUtilsComment;
    }

    @Override
    public PostDTO addPost(PostDTO postDTO) {

        // USER VALIDATION
        User user = userRepository.findById(postDTO.getUser().getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User with id "+postDTO.getUser().getUserId()+" not found!" ));
        if(!user.isActive())
            throw new IsActiveExceptionHandler("User with id "+user.getUserId()+" is inactive!");
        if(user.isDeleted())
            throw new IsDeleteExceptionHandler("User with id "+user.getUserId()+" is deleted!");

        // CATEGORY VALIDATION
        Category category = categoryRepository.findById(postDTO.getCategory().getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category with id "+postDTO.getCategory().getCategoryId()+" not found!" ));
        if(!category.isActive())
            throw new IsActiveExceptionHandler("Category with id "+category.getCategoryId()+" is inactive!");
        if(category.isDeleted())
            throw new IsActiveExceptionHandler("Category with id "+category.getCategoryId()+" is deleted!");

        Post post = new Post();
        post.setPostContent(postDTO.getPostContent());
        post.setPostTitle(postDTO.getPostTitle());
        post.setUser(user);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setStatus(true);

        return appUtilsComment.postToDto(postRepository.save(post));
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        return appUtilsComment.postToDto(postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post with id "+postId+" not found")));
    }

    @Override
    public List<PostDTO> getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepository.findAll(pageable);
        return appUtilsComment.postsToDtos(pagePost.getContent());
    }

    @Override
    public List<PostDTO> getAllPostsByDeleted(boolean deleted, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepository.findAllByIsDeleted(deleted, pageable);
        return appUtilsComment.postsToDtos(pagePost.getContent());
    }

    @Override
    public List<PostDTO> getPostsByUserId(Integer userId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User with id "+userId+" not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepository.findAllByUser(user, pageable);
        return appUtilsComment.postsToDtos(pagePost.getContent());
    }

    @Override
    public List<PostDTO> getPostsByCommentId(Integer commentId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public List<PostDTO> getPostsByCategoryId(Integer categoryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category with id "+categoryId+" not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepository.findAllByCategory(category, pageable);
        return appUtilsComment.postsToDtos(pagePost.getContent());
    }

    @Override
    public PostDTO updatePost(Integer postId, PostDTO postDTO) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post with id "+postId+" not found!"));
        if(!post.isStatus())
            throw new IsActiveExceptionHandler("Post with id "+postId+" is inactive!");
        if(post.isDeleted())
            throw new IsDeleteExceptionHandler("Post with id "+postId+" is deleted!");

        post.setPostTitle(postDTO.getPostTitle());
        post.setPostContent(postDTO.getPostContent());
        if(postDTO.getCategory() != null)
        {
            Category category = categoryRepository.findById(postDTO.getCategory().getCategoryId())
                    .orElseThrow(()->new ResourceNotFoundException("Category with id "+postDTO.getCategory().getCategoryId()+" not found!"));
            System.out.println(category);
            post.setCategory(category);
        }
        return appUtilsComment.postToDto(postRepository.save(post));
    }

    @Override
    public String softDeletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post with id "+postId+" not found!"));
        if(post.isDeleted())
           return "Post with id "+postId+" is already deleted";

        post.setDeleted(true);
        postRepository.save(post);
        return "Post with id "+postId+" soft deleted!";
    }

    @Override
    public String hardDeletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post with id "+postId+" not found!"));
        postRepository.delete(post);
        return "Post with id "+postId+" hard deleted!";
    }

    @Override
    public String restorePost(Integer postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post with id "+postId+" not found!"));
        if(!post.isDeleted())
            return "Post with id "+postId+" is already active!";

        post.setDeleted(false);
        postRepository.save(post);
        return "Post with id "+postId+" restored!";
    }
}
