package ru.dankoy.coubtagssearcher.core.domain.coubcom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.coubtagssearcher.core.domain.Channel;

@EqualsAndHashCode
@Data
@ToString
@NoArgsConstructor
public class ChannelsWrapper {

    private List<Channel> channels = new ArrayList<>();

    private long page;

    @JsonProperty("per_page")
    private long perPage;

    @JsonProperty("total_pages")
    private long totalPages;
}
