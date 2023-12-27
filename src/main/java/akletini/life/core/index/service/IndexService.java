package akletini.life.core.index.service;

import java.io.IOException;

public interface IndexService {
    void reIndexAllIndexes() throws IOException;

    void reIndexSingleIndex(String indexName) throws IOException;
}
