package org.galatea.pgbi.positions.client.td.retriever;

import java.util.Collection;
import org.galatea.pgbi.core.domain.Pair;
import org.galatea.pgbi.positions.client.td.domain.PositionKey;
import org.galatea.pgbi.positions.client.td.domain.PositionValue;

/**
 * Interface to be implemented by all position retrievers in this module
 */
public interface Retriever {

  Collection<Pair<PositionKey, PositionValue>> getPositions() throws Exception;

}
