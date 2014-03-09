package com.hjwylde.uni.comp261.assignment01.geographicalMap;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

import com.hjwylde.commons.lang.graphs.DirectedGraph;
import com.hjwylde.commons.lang.graphs.DirectedGraph.Edge;
import com.hjwylde.commons.lang.graphs.SearchVertex;
import com.hjwylde.commons.lang.trees.QuadTree;
import com.hjwylde.uni.comp261.assignment01.geographicalMap.road.Node;
import com.hjwylde.uni.comp261.assignment01.geographicalMap.road.Road;
import com.hjwylde.uni.comp261.assignment01.geographicalMap.road.RoadRestriction;
import com.hjwylde.uni.comp261.assignment01.geographicalMap.road.RoadSegment;

/*
 * Code for Assignment 1, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public class GeographyMap {

    public static final double SCALE_LATITUDE = 111.0;
    public static final double SCALE_LONGITUDE = 88.649;

    public static final int SEARCH_ASTAR = 0;
    public static final int SEARCH_DIJKSTRA = 1;

    public static final int TRAVEL_CAR = 1;
    public static final int TRAVEL_PEDESTRIAN = 2;
    public static final int TRAVEL_BICYCLE = 4;
    public static final int TRAVEL_ANY = GeographyMap.TRAVEL_CAR | GeographyMap.TRAVEL_PEDESTRIAN
            | GeographyMap.TRAVEL_BICYCLE;

    private DirectedGraph roadMap;
    private QuadTree nodeMap;

    private Map<Integer, Node> mapNodes;
    private Map<Integer, Road> mapRoads;
    private Map<Integer, RoadSegment> mapRoadSegments;
    private Map<Integer, RoadRestriction> mapRoadRestrictions;

    private Bounds mapBounds;

    private boolean verbose = true;

    private int travelMethod = GeographyMap.TRAVEL_ANY;

    public GeographyMap() {}

    /**
     * @return the <code>verbose</code>.
     */
    public boolean doesVerbose() {
        return verbose;
    }

    public Image generateMapImage(int size, boolean drawNodes, boolean drawRoads) {
        return generateMapImage(size, drawNodes, drawRoads, null);
    }

    public Image generateMapImage(int size, boolean drawNodes, boolean drawRoads,
            List<Integer> shortestPath) {
        if (size <= 0)
            throw new IllegalArgumentException();

        Image map = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) map.getGraphics();

        boolean drawnMap = false;

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        if (drawRoads && (mapNodes != null) && (mapRoadSegments != null)) {
            drawnMap = true;

            Point2D pos1, pos2;
            Road r;
            for (RoadSegment rs : mapRoadSegments.values()) {
                r = mapRoads.get(rs.getRID());
                switch (travelMethod) {
                    case (GeographyMap.TRAVEL_CAR):
                        if (!r.isUseableByCar())
                            continue;
                        break;
                    case (GeographyMap.TRAVEL_PEDESTRIAN):
                        if (!r.isUseableByPedestrian())
                            continue;
                        break;
                    case (GeographyMap.TRAVEL_BICYCLE):
                        if (!r.isUseableByBicycle())
                            continue;
                }

                pos1 = scaleVectorTo(mapNodes.get(rs.getStartNode()).getPosition(), size);
                pos2 = scaleVectorTo(mapNodes.get(rs.getEndNode()).getPosition(), size);

                g.setColor(Color.LIGHT_GRAY);
                g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(),
                        (int) pos2.getY());
            }
        }

        if (drawNodes && (mapNodes != null)) {
            drawnMap = true;

            Point2D pos;
            for (Node n : mapNodes.values()) {
                pos = scaleVectorTo(n.getPosition(), size);

                g.setColor(Color.BLACK);
                g.fillRect((int) pos.getX(), (int) pos.getY(), 1, 1);
            }
        }

        if (shortestPath != null) {
            drawnMap = true;

            g.setColor(Color.BLUE);

            Point2D pos1, pos2;
            for (int rsID : shortestPath) {
                if (!mapRoadSegments.containsKey(rsID))
                    continue;

                RoadSegment rs = mapRoadSegments.get(rsID);

                pos1 = scaleVectorTo(mapNodes.get(rs.getStartNode()).getPosition(), size);
                pos2 = scaleVectorTo(mapNodes.get(rs.getEndNode()).getPosition(), size);

                g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(),
                        (int) pos2.getY());
            }
        }

        g.dispose();

        if (!drawnMap)
            return null;

        return map;
    }

    public int getIndexAt(Point2D v) {
        if (v == null)
            throw new NullPointerException();
        if (nodeMap == null)
            return -1;

        Set<Integer> indices =
                nodeMap.getIndices(new Rectangle((int) v.getX(), (int) v.getY(), 2, 2));

        if (indices.size() == 0)
            return -1;

        return indices.iterator().next();
    }

    /**
     * @return the <code>travelMethod</code>.
     */
    public int getTravelMethod() {
        return travelMethod;
    }

    public boolean loadNodes(File tabFile) {
        return loadNodes(tabFile, false);
    }

    public boolean loadNodes(File tabFile, boolean skipFirstLine) {
        if ((tabFile == null) || !tabFile.exists())
            return false;

        assert (tabFile.toString().toLowerCase(Locale.ENGLISH).endsWith(".tab"));

        Map<Integer, Node> mapNodes = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(tabFile))) {
            Node node;
            int nID;

            String line;
            StringTokenizer lineTokens;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (((count++ == 0) && skipFirstLine) || line.equals(""))
                    continue;

                try {
                    lineTokens = new StringTokenizer(line, "\t");

                    nID = Integer.parseInt(lineTokens.nextToken());
                    if (mapNodes.containsKey(nID)) {
                        System.err.println("Can not add: \"" + line
                                + "\" to nodes. Node ID already exists.");
                        continue;
                    }

                    double y =
                            Double.parseDouble(lineTokens.nextToken())
                                    * GeographyMap.SCALE_LATITUDE;
                    double x =
                            Double.parseDouble(lineTokens.nextToken())
                                    * GeographyMap.SCALE_LONGITUDE;

                    node = new Node(nID, new Point2D.Double(x, y));
                    mapNodes.put(nID, node);
                } catch (NoSuchElementException e) {
                    System.err.println("File format is incorrect. Expected 3 tokens for a node.");
                    return false;
                } catch (NumberFormatException e) {
                    System.err
                            .println("File format is incorrect. Could not parse an input to a number value.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.mapNodes = mapNodes;

        calculateBounds();

        initNodeMap();
        if (mapRoadSegments != null)
            initRoadMap();

        return true;
    }

    public boolean loadRoadRestrictions(File tabFile) {
        return loadRoadRestrictions(tabFile, true);
    }

    public boolean loadRoadRestrictions(File tabFile, boolean skipFirstLine) {
        if ((tabFile == null) || !tabFile.exists())
            return false;
        if ((mapNodes == null) || (mapRoads == null))
            return false;

        assert (tabFile.toString().toLowerCase(Locale.ENGLISH).endsWith(".tab"));

        Map<Integer, RoadRestriction> mapRoadRestrictions = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(tabFile))) {
            RoadRestriction rr;

            String line;
            StringTokenizer lineTokens;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (((count++ == 0) && skipFirstLine) || line.equals(""))
                    continue;

                try {
                    lineTokens = new StringTokenizer(line, "\t");

                    rr =
                            new RoadRestriction(Integer.parseInt(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()));
                    if (!mapNodes.containsKey(rr.getFromNID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Node ID does not exist.");
                        continue;
                    }
                    if (!mapNodes.containsKey(rr.getMidNID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Node ID does not exist.");
                        continue;
                    }
                    if (!mapNodes.containsKey(rr.getToNID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Node ID does not exist.");
                        continue;
                    }
                    if (!mapRoads.containsKey(rr.getFromRID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Road ID does not exist.");
                        continue;
                    }
                    if (!mapRoads.containsKey(rr.getToRID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Road ID does not exist.");
                        continue;
                    }

                    mapRoadRestrictions.put(rr.getMidNID(), rr);
                } catch (NoSuchElementException e) {
                    System.err
                            .println("File format is incorrect. Expected 5 tokens for a road segment.");
                    return false;
                } catch (NumberFormatException e) {
                    System.err
                            .println("File format is incorrect. Could not parse an input to a number value.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.mapRoadRestrictions = mapRoadRestrictions;

        return true;
    }

    public boolean loadRoads(File tabFile) {
        return loadRoads(tabFile, true);
    }

    public boolean loadRoads(File tabFile, boolean skipFirstLine) {
        if ((tabFile == null) || !tabFile.exists())
            return false;

        assert (tabFile.toString().toLowerCase(Locale.ENGLISH).endsWith(".tab"));

        Map<Integer, Road> mapRoads = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(tabFile))) {
            Road road;
            int rID;

            String line;
            StringTokenizer lineTokens;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (((count++ == 0) && skipFirstLine) || line.equals(""))
                    continue;

                try {
                    lineTokens = new StringTokenizer(line, "\t");

                    rID = Integer.parseInt(lineTokens.nextToken());
                    if (mapRoads.containsKey(rID)) {
                        System.err.println("Can not add: \"" + line
                                + "\" to roads. Road ID already exists.");
                        continue;
                    }

                    road = new Road(rID);
                    lineTokens.nextToken(); // Skip type
                    road.setLabel(Main.toTitleCase(lineTokens.nextToken()));
                    road.setCity(Main.toTitleCase(lineTokens.nextToken()));
                    road.setOneWay(Integer.parseInt(lineTokens.nextToken()) == 1 ? true : false);
                    road.setSpeedZone(Integer.parseInt(lineTokens.nextToken()));
                    road.setRoadClass(Integer.parseInt(lineTokens.nextToken()));
                    road.setUseableByCar(Integer.parseInt(lineTokens.nextToken()) == 0 ? true
                            : false);
                    road.setUseableByPedestrian(Integer.parseInt(lineTokens.nextToken()) == 0 ? true
                            : false);
                    road.setUseableByBicycle(Integer.parseInt(lineTokens.nextToken()) == 0 ? true
                            : false);
                    mapRoads.put(rID, road);
                } catch (NoSuchElementException e) {
                    System.err.println("File format is incorrect. Expected 10 tokens for a road.");
                    return false;
                } catch (NumberFormatException e) {
                    System.err
                            .println("File format is incorrect. Could not parse an input to a number value.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.mapRoads = mapRoads;

        return true;
    }

    public boolean loadRoadSegments(File tabFile) {
        return loadRoadSegments(tabFile, true);
    }

    public boolean loadRoadSegments(File tabFile, boolean skipFirstLine) {
        if ((tabFile == null) || !tabFile.exists())
            return false;
        if ((mapNodes == null) || (mapRoads == null))
            return false;

        assert (tabFile.toString().toLowerCase(Locale.ENGLISH).endsWith(".tab"));

        Map<Integer, RoadSegment> mapRoadSegments = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(tabFile))) {
            RoadSegment rs;

            String line;
            StringTokenizer lineTokens;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (((count++ == 0) && skipFirstLine) || line.equals(""))
                    continue;

                try {
                    lineTokens = new StringTokenizer(line, "\t");

                    rs =
                            new RoadSegment(Integer.parseInt(lineTokens.nextToken()),
                                    Double.parseDouble(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()),
                                    Integer.parseInt(lineTokens.nextToken()));
                    if (!mapRoads.containsKey(rs.getRID())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Road ID does not exist.");
                        continue;
                    }
                    if (!mapNodes.containsKey(rs.getStartNode())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Node ID does not exist.");
                        continue;
                    }
                    if (!mapNodes.containsKey(rs.getEndNode())) {
                        System.err.println("Can not add: \"" + line
                                + "\" to road segments. Node ID does not exist.");
                        continue;
                    }

                    mapRoadSegments.put(rs.getRSID(), rs);

                    if (!mapRoads.get(rs.getRID()).isOneWay()) {
                        RoadSegment oppositeRS =
                                new RoadSegment(rs.getRID(), rs.getLength(), rs.getEndNode(),
                                        rs.getStartNode());
                        mapRoadSegments.put(oppositeRS.getRSID(), oppositeRS);
                    }
                } catch (NoSuchElementException e) {
                    System.err
                            .println("File format is incorrect. Expected 4 tokens for a road segment.");
                    return false;
                } catch (NumberFormatException e) {
                    System.err
                            .println("File format is incorrect. Could not parse an input to a number value.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.mapRoadSegments = mapRoadSegments;

        initRoadMap();

        return true;
    }

    public String pathToString(List<Integer> path) {
        if ((path == null) || (path.size() == 0))
            throw new IllegalArgumentException();
        if ((mapRoads == null) || (mapRoadSegments == null))
            return "Could not convert \"" + path
                    + "\" to string at this current time.\nOne or more maps are null.";

        String str = "Shortest path:\n";

        double cost = 0.0;
        double collectiveCost = 0.0;
        Road r, lastR = null;
        RoadSegment rs;
        for (int rsID : path) {
            rs = mapRoadSegments.get(rsID);
            r = mapRoads.get(rs.getRID());

            double addedCost = rs.getLength();
            if ((travelMethod & GeographyMap.TRAVEL_CAR) == GeographyMap.TRAVEL_CAR)
                addedCost /= ((r.getSpeedZone() == 7) ? 160.0 : Road.SPEED_ZONES[r.getSpeedZone()]);
            else
                addedCost /= 10.0;

            cost += addedCost;
            collectiveCost += addedCost;

            if ((lastR == null)
                    || lastR.equals(r)
                    || (lastR.getLabel().equals(r.getLabel()) && lastR.getCity()
                            .equals(r.getCity()))) {
                lastR = r;
                continue;
            }
            str +=
                    "Travel along " + lastR.getLabel() + ", " + lastR.getCity() + " for "
                            + Main.round(collectiveCost, 6) + "h.\n";

            collectiveCost = 0.0;

            lastR = r;
        }

        if ((collectiveCost > 0) && (lastR != null))
            str +=
                    "Travel along " + lastR.getLabel() + ", " + lastR.getCity() + " for "
                            + Main.round(collectiveCost, 6) + "h.\n";

        str += "Total time travelling: " + Main.round(cost, 4) + "h.";

        return str;
    }

    public Point2D scaleVectorFrom(Point2D v, double size) {
        if (v == null)
            throw new NullPointerException();
        if (size <= 0)
            throw new IllegalArgumentException();

        Point2D scaledV = new Point2D.Double(v.getX(), v.getY());

        scaledV.setLocation(mapBounds.x + ((mapBounds.width * v.getX()) / size),
                (mapBounds.y + mapBounds.height) - ((mapBounds.height * v.getY()) / size));

        return scaledV;
    }

    public Point2D scaleVectorTo(Point2D v, double size) {
        if (v == null)
            throw new NullPointerException();
        if (size <= 0)
            throw new IllegalArgumentException();

        Point2D scaledV = new Point2D.Double(v.getX(), v.getY());

        scaledV.setLocation((size * (v.getX() - mapBounds.x)) / mapBounds.width,
                size - ((size * (v.getY() - mapBounds.y)) / mapBounds.height));

        return scaledV;
    }

    public List<Integer> searchGraph(int start, int end, int searchMethod) {
        if (roadMap == null)
            return null;

        GraphSearch gs = new GraphSearch();

        switch (searchMethod) {
            case (GeographyMap.SEARCH_DIJKSTRA):
                return gs.searchDijkstra(start, end);
            case (GeographyMap.SEARCH_ASTAR):
            default:
                return gs.searchAstar(start, end);
        }
    }

    /**
     * @param travelMethod the <code>travelMethod</code> to set.
     */
    public void setTravelMethod(int travelMethod) {
        this.travelMethod = travelMethod;
    }

    /**
     * @param verbose the <code>verbose</code> to set.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    private void calculateBounds() {
        mapBounds = new Bounds(Double.MAX_VALUE, Double.MAX_VALUE, 0, 0);
        Point2D bottomCorner = new Point2D.Double(-Double.MAX_VALUE, -Double.MAX_VALUE);

        Point2D pos;
        for (Node n : mapNodes.values()) {
            pos = n.getPosition();

            mapBounds.x = Math.min(mapBounds.x, pos.getX());
            mapBounds.y = Math.min(mapBounds.y, pos.getY());

            bottomCorner.setLocation(Math.max(bottomCorner.getX(), pos.getX()),
                    Math.max(bottomCorner.getY(), pos.getY()));
        }

        mapBounds.width = bottomCorner.getX() - mapBounds.x;
        mapBounds.height = bottomCorner.getY() - mapBounds.y;
    }

    private void initNodeMap() {
        if (mapNodes == null)
            return;

        nodeMap =
                new QuadTree(new Rectangle((int) mapBounds.x, (int) mapBounds.y,
                        (int) mapBounds.width, (int) mapBounds.height));

        for (Node n : mapNodes.values())
            nodeMap.insert(n.getNID(), new Rectangle((int) n.getPosition().getX(), (int) n
                    .getPosition().getY(), 1, 1));
    }

    private void initRoadMap() {
        if ((mapNodes == null) || (mapRoadSegments == null))
            return;

        roadMap = new DirectedGraph();

        for (Node n : mapNodes.values())
            if (!roadMap.addVertex(n.getNID()))
                System.err.println("Could not add node: " + n);

        for (RoadSegment rs : mapRoadSegments.values())
            if (!roadMap.addEdge(rs.getRSID(), rs.getStartNode(), rs.getEndNode()))
                System.err.println("Could not add edge: " + rs);
    }

    private void verbose(String str) {
        if (verbose)
            System.out.println(str);
    }

    public class Bounds {

        public double x, y;
        public double width, height;

        public Bounds(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;

            this.width = width;
            this.height = height;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
        }
    }

    private class GraphSearch {

        public List<Integer> searchAstar(int start, int end) {
            if (!mapNodes.containsKey(start) || !mapNodes.containsKey(end))
                throw new NoSuchElementException();

            verbose("Searching graph from (" + start + ") to (" + end + ") with astar.");

            Map<Integer, Boolean> visitedNodes = new HashMap<>();

            PriorityQueue<SearchVertex> fringe = new PriorityQueue<>();
            fringe.add(new SearchVertex(start, 0, calculateHeuristicCost(distance(start, end))));
            while (!fringe.isEmpty()) {
                SearchVertex sv = fringe.poll();

                verbose("Dequeueing: " + sv);

                int iD = sv.getVID();

                if (iD == end) {
                    verbose("Path found: " + sv.getPathEID());

                    return sv.getPathEID();
                }

                if (visitedNodes.containsKey(iD))
                    continue;

                visitedNodes.put(iD, true);

                RoadRestriction rr =
                        (mapRoadRestrictions == null ? null : mapRoadRestrictions.get(iD));
                if (rr != null)
                    if (sv.getSrc() == null)
                        rr = null;
                    else if (rr.getFromNID() != sv.getSrc().getVID())
                        rr = null;
                    else if (rr.getFromRID() != mapRoadSegments.get(sv.getEID()).getRID())
                        rr = null;

                for (Edge edge : roadMap.getOutgoingEdges(iD)) {
                    Node neighbour = mapNodes.get(edge.getEnd());
                    RoadSegment rs = mapRoadSegments.get(edge.getEID());
                    Road r = mapRoads.get(rs.getRID());

                    if (visitedNodes.containsKey(neighbour.getNID()))
                        continue;

                    switch (travelMethod) {
                        case (GeographyMap.TRAVEL_CAR):
                            if (!r.isUseableByCar())
                                continue;
                            break;
                        case (GeographyMap.TRAVEL_PEDESTRIAN):
                            if (!r.isUseableByPedestrian())
                                continue;
                            break;
                        case (GeographyMap.TRAVEL_BICYCLE):
                            if (!r.isUseableByBicycle())
                                continue;
                    }
                    if (rr != null)
                        if ((rr.getToNID() == edge.getEnd()) && (rr.getToRID() == r.getRID()))
                            continue;

                    double cost =
                            calculateCost(
                                    rs.getLength(),
                                    (((travelMethod & GeographyMap.TRAVEL_CAR) == GeographyMap.TRAVEL_CAR) ? Road.SPEED_ZONES[r
                                            .getSpeedZone()] : 10.0), r.getRoadClass());
                    double heuristicCost =
                            calculateHeuristicCost(distance(neighbour.getNID(), end));

                    SearchVertex next =
                            new SearchVertex(neighbour.getNID(), edge.getEID(), sv, cost,
                                    heuristicCost);
                    fringe.offer(next);

                    verbose("Enqueueing: " + next);
                }
            }

            verbose("Path not found.");

            return null;
        }

        public List<Integer> searchDijkstra(int start, int end) {
            if (!mapNodes.containsKey(start) || !mapNodes.containsKey(end))
                throw new NoSuchElementException();

            verbose("Searching graph from (" + start + ") to (" + end + ") with dijkstras.");

            Map<Integer, Boolean> visitedNodes = new HashMap<>();

            PriorityQueue<SearchVertex> fringe = new PriorityQueue<>();
            fringe.add(new SearchVertex(start, 0));
            while (!fringe.isEmpty()) {
                SearchVertex sv = fringe.poll();

                verbose("Dequeueing: " + sv);

                int iD = sv.getVID();

                if (iD == end) {
                    verbose("Path found: " + sv.getPathEID());

                    return sv.getPathEID();
                }

                if (visitedNodes.containsKey(iD))
                    continue;

                visitedNodes.put(iD, true);

                RoadRestriction rr =
                        (mapRoadRestrictions == null ? null : mapRoadRestrictions.get(iD));
                if (rr != null)
                    if (sv.getSrc() == null)
                        rr = null;
                    else if (rr.getFromNID() != sv.getSrc().getVID())
                        rr = null;
                    else if (rr.getFromRID() != mapRoadSegments.get(sv.getEID()).getRID())
                        rr = null;

                for (Edge edge : roadMap.getOutgoingEdges(iD)) {
                    Node neighbour = mapNodes.get(edge.getEnd());
                    RoadSegment rs = mapRoadSegments.get(edge.getEID());
                    Road r = mapRoads.get(rs.getRID());

                    if (visitedNodes.containsKey(neighbour.getNID()))
                        continue;

                    switch (travelMethod) {
                        case (GeographyMap.TRAVEL_CAR):
                            if (!r.isUseableByCar())
                                continue;
                            break;
                        case (GeographyMap.TRAVEL_PEDESTRIAN):
                            if (!r.isUseableByPedestrian())
                                continue;
                            break;
                        case (GeographyMap.TRAVEL_BICYCLE):
                            if (!r.isUseableByBicycle())
                                continue;
                    }
                    if (rr != null)
                        if ((rr.getToNID() == edge.getEnd()) && (rr.getToRID() == r.getRID()))
                            continue;

                    double cost =
                            calculateCost(
                                    rs.getLength(),
                                    (((travelMethod & GeographyMap.TRAVEL_CAR) == GeographyMap.TRAVEL_CAR) ? Road.SPEED_ZONES[r
                                            .getSpeedZone()] : 10.0), r.getRoadClass());

                    SearchVertex next =
                            new SearchVertex(neighbour.getNID(), edge.getEID(), sv, cost);
                    fringe.offer(next);

                    verbose("Enqueueing: " + next);
                }
            }

            verbose("Path not found.");

            return null;
        }

        private double calculateCost(double cost, double speedLimit, double roadClass) {
            if (speedLimit == 0.0)
                speedLimit = 160.0;

            cost /= speedLimit;
            cost /= ((roadClass == 0) ? 0.1 : (roadClass / 4.0));

            return cost;
        }

        private double calculateHeuristicCost(double heuristicCost) {
            return calculateCost(
                    heuristicCost,
                    (((travelMethod & GeographyMap.TRAVEL_CAR) == GeographyMap.TRAVEL_CAR) ? Road.SPEED_ZONES[7]
                            : 10.0), 4);
        }

        private double distance(int src, int end) {
            if (!mapNodes.containsKey(src) || !mapNodes.containsKey(end))
                throw new IllegalArgumentException();

            Point2D length = new Point2D.Double();
            length.setLocation(mapNodes.get(end).getPosition());

            return length.distance(mapNodes.get(src).getPosition());
        }
    }
}
