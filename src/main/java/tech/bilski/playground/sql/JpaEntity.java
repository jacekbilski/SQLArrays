package tech.bilski.playground.sql;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "jpa_entity")
public class JpaEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(name = "list", columnDefinition = "TEXT ARRAY[5]")
    private List<String> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
