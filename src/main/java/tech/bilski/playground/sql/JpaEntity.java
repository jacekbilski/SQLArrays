package tech.bilski.playground.sql;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "jpa_entity")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class JpaEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Type(type = "list-array")
    @Column(name = "list", columnDefinition = "TEXT[]")
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
