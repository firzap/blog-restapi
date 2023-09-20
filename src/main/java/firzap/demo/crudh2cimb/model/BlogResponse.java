package firzap.demo.crudh2cimb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponse {

    private Long id;
    private String title;
    private String body;
    private String author;
}
