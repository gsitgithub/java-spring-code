package dev.gsitgithub.webapp.entity.abstracts;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.LazyInitializationException;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@EqualsAndHashCode(of = "uuid", callSuper = false)
@Setter
@Getter
@MappedSuperclass
public class IdUuidEntity extends AbstractPersistable<Long> {

    @Column(unique = true)
    @Setter(AccessLevel.PROTECTED)
    private String uuid = UUID.randomUUID().toString();

    @Override
    public String toString() {
        try {
            return ReflectionToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
        } catch (LazyInitializationException e) {
            return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                    .append("id", getId())
                    .append("uuid", getUuid())
                    .toString();
        }
    }

}
