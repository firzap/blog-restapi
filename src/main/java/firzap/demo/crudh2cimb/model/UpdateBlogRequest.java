package firzap.demo.crudh2cimb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBlogRequest {

    @JsonIgnore
    private Long id;
    private String title;
    private String body;
    private String author;
}
