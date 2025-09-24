package com.superapp.event_service.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.superapp.event_service.domain.Segment;
import com.superapp.event_service.domain.Segment.Place;

public final class SeatUtils {
    private SeatUtils() {
    }

    private static final Pattern SUFFIX_NUMBER = Pattern.compile("^(.*?)(\\d+)$");
    private static final Pattern GROUPED = Pattern.compile("^(.+?)\\[(.*)]$");
    private static final Pattern RANGE = Pattern.compile("^(\\d+)\\s*-\\s*(\\d+)$");
    private static final Pattern DIGITS = Pattern.compile("^\\d+$");

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
            List<Place> places = s.getPlaces();
            if (places != null) {
                for (Segment.Place p : places) {
                    if (p != null && p.getId() != null && !p.getId().isBlank()) {
                        ordered.add(p.getId());
                    }
                }
            }

            // traverse children
            List<Segment> children = s.getSegments();
            if (children != null && !children.isEmpty()) {
                // push in reverse to keep original order
                for (int i = children.size() - 1; i >= 0; i--)
                    stack.push(children.get(i));
            }
        }

        // dedupe while preserving order
        return new ArrayList<>(new LinkedHashSet<>(ordered));
    }

    /**
     * Input: ["A1","A2","A3","A4","B10","B11","F1-A1","F1-A2","GA"]
     * Output: ["A[1,2,3,4]","B[10,11]","F1-A[1,2]","GA"]
     *
     * - Groups by the non-numeric prefix (e.g., "A", "B", "F1-A").
     * - Sorts numbers ascending within each group.
     * - Leaves IDs with no trailing number (e.g., "GA") as-is.
     * - Deduplicates numbers per prefix.
     */
    public static List<String> groupPlaceIds(Collection<String> ids) {
        Map<String, SortedSet<Integer>> groups = new LinkedHashMap<>();
        List<String> noNumber = new ArrayList<>();

        for (String id : ids) {
            Matcher m = SUFFIX_NUMBER.matcher(id);
            if (m.matches()) {
                String prefix = m.group(1);
                int num = Integer.parseInt(m.group(2));
                groups.computeIfAbsent(prefix, k -> new TreeSet<>()).add(num);
            } else {
                // No numeric suffix → keep as-is
                noNumber.add(id);
            }
        }

        List<String> result = new ArrayList<>();

        // Build "PREFIX[numbers]" strings
        for (Map.Entry<String, SortedSet<Integer>> e : groups.entrySet()) {
            String prefix = e.getKey();
            String nums = e.getValue().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            result.add(prefix + "[" + nums + "]");
        }

        // Append items without numeric suffix (preserve their appearance order)
        result.addAll(noNumber);

        return result;
    }

    public static List<String> groupPlaceIds(List<Segment> roots) {
        return groupPlaceIds(collectPlaceIdsFromSegments(roots));
    }

    // Optional: if you prefer "A[1-4,6,8-10]" style, swap this in place of the join
    // above.
    public static String compressNumbersToRanges(Collection<Integer> numbers) {
        List<Integer> ns = new ArrayList<>(new TreeSet<>(numbers));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ns.size();) {
            int start = ns.get(i), end = start;
            int j = i + 1;
            while (j < ns.size() && ns.get(j) == end + 1) {
                end = ns.get(j++);
            }
            if (sb.length() > 0)
                sb.append(",");
            sb.append(start == end ? String.valueOf(start) : (start + "-" + end));
            i = j;
        }
        return sb.toString();
    }

    /**
     * Expand grouped IDs:
     * ["A[1,2,3,4]", "F1-A[001-003,10]", "GA"] ->
     * ["A1","A2","A3","A4","F1-A001","F1-A002","F1-A003","F1-A10","GA"]
     *
     * - Supports comma lists and ranges.
     * - Preserves zero-padding width inside brackets (e.g., 001-003).
     * - Leaves items without brackets as-is.
     */
    public static List<String> expandGroupedPlaceIds(Collection<String> grouped) {
        List<String> out = new ArrayList<>();
        for (String item : grouped) {
            String s = item.trim();
            Matcher g = GROUPED.matcher(s);
            if (!g.matches()) {
                // No [ ... ] → pass through
                out.add(s);
                continue;
            }
            String prefix = g.group(1);
            String inside = g.group(2).trim();
            if (inside.isEmpty()) {
                out.add(s);
                continue;
            }

            for (String token : inside.split("\\s*,\\s*")) {
                if (token.isEmpty())
                    continue;

                Matcher r = RANGE.matcher(token);
                if (r.matches()) {
                    String startStr = r.group(1);
                    String endStr = r.group(2);
                    int start = Integer.parseInt(startStr);
                    int end = Integer.parseInt(endStr);
                    int width = Math.max(startStr.length(), endStr.length());

                    if (start <= end) {
                        for (int v = start; v <= end; v++) {
                            out.add(prefix + pad(v, width));
                        }
                    } else { // allow descending ranges like 10-1
                        for (int v = start; v >= end; v--) {
                            out.add(prefix + pad(v, width));
                        }
                    }
                } else if (DIGITS.matcher(token).matches()) {
                    int width = token.length(); // keep zero-padding
                    int v = Integer.parseInt(token);
                    out.add(prefix + pad(v, width));
                } else {
                    // Fallback: if someone put a non-numeric token inside brackets
                    out.add(prefix + token);
                }
            }
        }
        return out;
    }

    private static String pad(int value, int width) {
        return (width > 1) ? String.format("%0" + width + "d", value) : String.valueOf(value);
    }
}
