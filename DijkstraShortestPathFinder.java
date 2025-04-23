package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> known = new HashSet<>();
        DoubleMapMinPQ<V> perimeter = new DoubleMapMinPQ<>();
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> edgeTo = new HashMap<>();

        if (Objects.equals(start, end)) {
            return edgeTo;
        }

        perimeter.add(start, 0.0);
        distTo.put(start, 0.0);

        while (!perimeter.isEmpty() && !known.contains(end)) {
            V u = perimeter.removeMin();
            known.add(u);

            for (E edge : graph.outgoingEdgesFrom(u)) {
                V v = edge.to();
                double newDist = distTo.get(u) + edge.weight();

                if (!distTo.containsKey(v)) {
                    distTo.put(v, Double.POSITIVE_INFINITY);
                }

                if (newDist < distTo.get(v)) {
                    distTo.put(v, newDist);
                    edgeTo.put(v, edge);

                    if (perimeter.contains(v)) {
                        perimeter.changePriority(v, newDist);
                    } else {
                        perimeter.add(v, newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        // if there's only one vertex
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        // if no path to end
        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }

        // otherwise go backwards through spt to get path from end to start
        Stack<E> backwards = new Stack<>();
        V curr = end;

        while (!Objects.equals(curr, start)) {
            E edge = spt.get(curr);
            backwards.add(edge);
            curr = edge.from();
        }
        List<E> path = new ArrayList<>();
        while (!backwards.isEmpty()) {
            path.add(backwards.pop());
        }
        return new ShortestPath.Success<>(path);
    }
}
