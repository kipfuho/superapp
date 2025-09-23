package com.superapp.event_service.util;

import java.text.Normalizer;
import java.util.*;
import com.superapp.event_service.domain.Segment;
import com.superapp.event_service.domain.Venue;

public final class SegmentIdBuilder {
    private SegmentIdBuilder() {
    }

    /**
     * Assigns IDs to all segments that don't have one, based on parent path + name.
     */
    public static void ensureSegmentIds(Venue venue) {
        if (venue == null || venue.getSegments() == null)
            return;

        Set<String> used = new HashSet<>();
        for (Segment root : venue.getSegments()) {
            assignRec(root, null, used);
        }
    }

    private static void assignRec(Segment seg, String parentId, Set<String> used) {
        String base = slug(seg.getId() != null ? seg.getId() : seg.getName());
        String desired = (parentId == null || parentId.isBlank()) ? base : parentId + ":" + base;
        seg.setId(unique(desired, used));
        used.add(seg.getId());

        if (seg.getSegments() != null) {
            for (Segment child : seg.getSegments()) {
                assignRec(child, seg.getId(), used);
            }
        }
    }

    private static String unique(String candidate, Set<String> used) {
        if (!used.contains(candidate))
            return candidate;
        int i = 2;
        while (used.contains(candidate + "-" + i))
            i++;
        return candidate + "-" + i;
    }

    /** slug like: "Main Hall" -> "main-hall", "Tầng 1" -> "tang-1" */
    private static String slug(String s) {
        if (s == null || s.isBlank())
            return "segment";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", ""); // strip accents
        n = n.replaceAll("[^\\p{Alnum}]+", "-") // non-alnum -> dash
                .replaceAll("(^-|-$)", "") // trim dashes
                .toLowerCase(Locale.ROOT);
        return n.isBlank() ? "segment" : n;
    }
}
