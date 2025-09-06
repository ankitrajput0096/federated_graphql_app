package com.subgraph.university.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class UniversitySource {

    private static final List<University> universityList = List.of(
            new University(1L, "Saturn V", "The Original Super Heavy-Lift Rocket!"),
            new University(2L, "Lunar Module"),
            new University(3L, "Space Shuttle"),
            new University(4L, "Falcon 9", "Reusable Medium-Lift Rocket"),
            new University(5L, "Dragon", "Reusable Medium-Lift Rocket"),
            new University(6L, "Starship", "Super Heavy-Lift Reusable Launch Vehicle")
    );

    private static final Map<Long, University> universityMap =
            universityList.stream().collect(Collectors.toMap(University::id, university -> university));

    public static University getUniversity(Long id) {
        return universityMap.get(id);
    }

    public static List<University> getUniversities() {
        return universityList;
    }
}