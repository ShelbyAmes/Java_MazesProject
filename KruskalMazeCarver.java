package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        //throw new UnsupportedOperationException("Not implemented yet.");
        ArrayList<EdgeWithData<Room, Wall>> edges = new ArrayList<>();
        for (Wall wall: walls) {
            Room room1 = wall.getRoom1();
            Room room2 = wall.getRoom2();
            double weight= rand.nextDouble();
            edges.add(new EdgeWithData<>(room1, room2, weight, wall));
        }
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> mst =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Set<Wall> kept = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : mst.edges()) {
            kept.add(edge.data());  // Access the Wall object via edge.data()
        }
        return kept;
    }
}
