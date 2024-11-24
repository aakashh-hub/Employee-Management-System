package replication;

import java.sql.Timestamp;
import java.util.List;

public interface PaginatedDataFetcher<T> {

    List<T> fetchPaginatedData(Timestamp lastSync, int offset, int limit);

    int getTotalRecordCount(Timestamp lastSync);
}