package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @Test
    void solve() throws IOException {
        Solution sol = new Solution();
        var casesPath = "/src/test/resources/cases/";
        var answerPath = "/src/test/resources/caseAnswers/";
        var folder = new File(new File("").getAbsolutePath().concat(casesPath));
        var i = 1;
        for (var file : Objects.requireNonNull(folder.listFiles())) {

            sol.loadData(casesPath + file.getName());
            sol.solve();

            var current = sol.getResultText();
            var expected = Files.readString(Path.of(new File("").getAbsolutePath() + answerPath + "answer" + i));
            current = i + current.replaceAll("\n", "").replaceAll("\r", "");
            expected = i + expected.replaceAll("\n", "").replaceAll("\r", "");

            assertEquals(current, expected);
            sol.clear();
            i++;
        }

    }
}