package firzap.demo.crudh2cimb.controller;

import firzap.demo.crudh2cimb.entity.Blog;
import firzap.demo.crudh2cimb.model.*;
import firzap.demo.crudh2cimb.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping(
            path = "/api/blogs",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BlogResponse> create(@RequestBody BlogRequest request){
        BlogResponse blogResponse = blogService.create(request);
        return WebResponse.<BlogResponse>builder()
                .data(blogResponse)
                .build();
    }

    @GetMapping(
            path = "/api/blogs/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BlogResponse> get(@PathVariable("id") Long id){
        BlogResponse blogResponse = blogService.get(id);
        return WebResponse.<BlogResponse>builder().data(blogResponse).build();
    }

    @GetMapping(
            path = "/api/blogs",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<BlogResponse>> getAll(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "body", required = false) String body,
                                                  @RequestParam(value = "author", required = false) String author,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "5") Integer size){

        AllBlogRequest request = AllBlogRequest.builder()
                .page(page)
                .size(size)
                .title(title)
                .body(body)
                .author(author)
                .build();

        Page<BlogResponse> blogResponses = blogService.getAll(request);
        return WebResponse.<List<BlogResponse>>builder()
                .data(blogResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(blogResponses.getNumber())
                        .totalPage(blogResponses.getTotalPages())
                        .size(blogResponses.getSize())
                        .build())
                .build();
    }

    @PutMapping(
            path = "/api/blogs/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BlogResponse> update(@RequestBody UpdateBlogRequest request,
                                            @PathVariable("id") Long id){
        request.setId(id);

        BlogResponse blogResponse = blogService.update(request);
        return WebResponse.<BlogResponse>builder()
                .data(blogResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/blogs/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("id") Long id){
        blogService.delete(id);
        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }

}
