package firzap.demo.crudh2cimb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog")
@Data
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;
    private String author;
}
