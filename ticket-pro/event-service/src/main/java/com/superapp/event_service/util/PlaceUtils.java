package com.superapp.event_service.util;

import java.util.*;
import com.superapp.event_service.domain.Segment;

public final class PlaceUtils {
    private PlaceUtils() {
    }

    /**
     * Flattens the segment tree and returns unique place IDs (in encounter order).
     */
    public static List<String> collectPlaceIdsFromSegments(List<Segment> roots) {
        if (roots == null || roots.isEmpty())
            return List.of();

        List<String> ordered = new ArrayList<>();
        Deque<Segment> stack = new ArrayDeque<>(roots);

        while (!stack.isEmpty()) {
            Segment s = stack.pop();

            // collect places in this segment
            var places = s.getPlaces();
            if (places != null) {
                for (Segment.Place p : places) {
                    if (p != null && p.getId() != null && !p.getId().isBlank()) {
                        ordered.add(p.getId());
                    }
                }
            }

            // traverse children
            var children = s.getSegments();
            if (children != null && !children.isEmpty()) {
                // push in reverse to keep original order
                for (int i = children.size() - 1; i >= 0; i--)
                    stack.push(children.get(i));
            }
        }

        // dedupe while preserving order
        return new ArrayList<>(new LinkedHashSet<>(ordered));
    }
}
