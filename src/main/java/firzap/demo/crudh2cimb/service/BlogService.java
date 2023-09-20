package firzap.demo.crudh2cimb.service;

import firzap.demo.crudh2cimb.entity.Blog;
import firzap.demo.crudh2cimb.model.AllBlogRequest;
import firzap.demo.crudh2cimb.model.BlogRequest;
import firzap.demo.crudh2cimb.model.BlogResponse;
import firzap.demo.crudh2cimb.model.UpdateBlogRequest;
import firzap.demo.crudh2cimb.repository.BlogRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    public BlogResponse create(BlogRequest request){

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setBody(request.getBody());
        blog.setAuthor(request.getAuthor());
        blogRepository.save(blog);

        return toBlogResponse(blog);
    }

    @Transactional(readOnly = true)
    public BlogResponse get(Long id){
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toBlogResponse(blog);
    }

    @Transactional
    public BlogResponse update(UpdateBlogRequest request){
        //find ke db
        Blog blog = blogRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with ID : " + request.getId() + " not found"));

        blog.setTitle(request.getTitle());
        blog.setBody(request.getBody());
        blog.setAuthor(request.getAuthor());
        blogRepository.save(blog);

        return toBlogResponse(blog);
    }

    @Transactional
    public void delete(Long id){
        //find ke db
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with ID : " + id + " not found"));

        blogRepository.delete(blog);
    }

    @Transactional(readOnly = true)
    public Page<BlogResponse> getAll(AllBlogRequest request){

        //Spesifikasi
        Specification<Blog> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getTitle())){
                predicates.add(builder.like(root.get("title"), "%"+request.getTitle()+"%"));
            }

            if (Objects.nonNull(request.getBody())){
                predicates.add(builder.like(root.get("body"), "%"+request.getBody()+"%"));
            }

            if (Objects.nonNull(request.getAuthor())){
                predicates.add(builder.like(root.get("author"), "%"+request.getAuthor()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Blog> blogs = blogRepository.findAll(specification, pageable);

        //buat response
        List<BlogResponse> contactResponses = blogs.getContent().stream()
                .map(this::toBlogResponse)
                .toList();

        return new PageImpl<>(contactResponses, pageable, blogs.getTotalElements());
    }

    private BlogResponse toBlogResponse(Blog blog){
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .body(blog.getBody())
                .author(blog.getAuthor())
                .build();
    }
}
