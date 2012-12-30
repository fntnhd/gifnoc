package com.us.fountainhead.gifnoc;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Runs the story tests
 */
@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = false, stepsFactory = true)
@UsingSpring(resources = "classpath:context.xml")
@UsingSteps
public class Stories extends JUnitStories {

    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromPath("src/test/resources"), "stories/*.story", "");
    }


}
