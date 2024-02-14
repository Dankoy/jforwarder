package ru.dankoy.coubtagssearcher.core.domain.coubcom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Data
@ToString
@NoArgsConstructor
public class Meta {

    private long page;

    @JsonProperty("per_page")
    private long perPage;

    @JsonProperty("total_pages")
    private long totalPages;
}
